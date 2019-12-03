package com.spinevox.app.screens.profile

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.spinevox.app.R
import com.spinevox.app.common.convertBitmapToBase64
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutProfileSettingsBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.screens.base.LazyFragment
import com.spinevox.app.screens.login.HostLoginActivity
import kotlinx.coroutines.launch

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class ProfileSettingsFragment : LazyFragment<LayoutProfileSettingsBinding>() {

    override lateinit var binding: LayoutProfileSettingsBinding

    private lateinit var serverToken: String

    override fun initController(view: View) {
        launch {
            try {
                serverToken = sharedPreferences.getString("serverToken", "") ?: ""
                val userInfo = SpineVoxService.getService(context!!, true).me("JWT $serverToken").await()
                binding.tvInputName.text = userInfo.data.first_name
                binding.tvInputEmail.text = userInfo.data.email ?: ""

                Glide.with(context!!)
                    .load(userInfo.data.profile.photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfile)
            } catch (e: Throwable) {
                val error = e.message
            }
        }

        binding.ivProfile.setOnClickListener {
            if (checkPermission()) {
                selectImage(context!!)
            } else {
                requestPermission()
            }
        }

        binding.tvInputEmail.setOnClickListener { showChangeEmailDialog() }
        binding.tvInputPassword.setOnClickListener { showChangePasswordDialog() }

        binding.btnExit.setOnClickListener {
            sharedPreferences.edit { putString("serverToken", "") }
            startActivity(Intent(context, HostLoginActivity::class.java))
            activity?.finish()
        }
    }


    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>("Камера", "Галерея")
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Оберіть фото")
            setItems(options) { _, item ->
                if (options[item] == "Камера") {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                } else if (options[item] == "Галерея") {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                }
            }

            show()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity!!, arrayOf(permission.CAMERA), 102)
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val bitmap = data.extras!!.get("data") as Bitmap?
                    if (bitmap != null) {
                        doOnPickImage(bitmap)
                    } else {
                        showToast(context!!, "Помилка")
                    }
                }

                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)

                    if (bitmap != null) {
                        doOnPickImage(bitmap)
                    } else {
                        showToast(context!!, "Помилка")
                    }
                }
            }
        }
    }

    private fun doOnPickImage(bitmap: Bitmap) {
        val base64 = convertBitmapToBase64(bitmap)

        launch {
            try {
                val result = SpineVoxService.getService(context!!, true).changePhoto("JWT $serverToken", base64).await()
                Glide.with(context!!)
                    .load(result.data.photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfile)
            } catch (e: Throwable) {
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showChangeEmailDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
            .setTitle("Змінити Email")

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        input.hint = "Email"
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Ок") { dialog, _ ->
            val email = input.text.toString()
            if (!email.contains('.') || !email.contains('@')) {
                showToast(context!!, "Не вірний Email")
                return@setPositiveButton
            }

            launch {
                try {
                    val result = SpineVoxService.getService(context!!, true).changeEmail("JWT $serverToken", email).await()
                    dialog.cancel()
                    binding.tvInputEmail.text = email
                } catch (e: Throwable) {
                    dialog.cancel()
                    showToast(context!!, "Помилка")
                }
            }
        }

        alertDialog.setNegativeButton("Закрити") { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    private fun showChangePasswordDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
        alertDialog.setTitle("Змінити пароль")
        val oldPass = EditText(context!!)
        val newPass = EditText(context!!)
        val confirmPass = EditText(context!!)


        oldPass.transformationMethod = PasswordTransformationMethod.getInstance()
        newPass.transformationMethod = PasswordTransformationMethod.getInstance()
        confirmPass.transformationMethod = PasswordTransformationMethod.getInstance()

        oldPass.hint = "Старий пароль"
        newPass.hint = "Новий пароль"
        confirmPass.hint = "Підтвердіть пароль"
        val ll = LinearLayout(context!!)
        ll.orientation = LinearLayout.VERTICAL

        ll.addView(oldPass)

        ll.addView(newPass)
        ll.addView(confirmPass)
        alertDialog.setView(ll)
        alertDialog.setPositiveButton("Ок") { dialog, _ ->
            if (newPass.text.toString() != confirmPass.text.toString()) {
                showToast(context!!, "Помилка")
                return@setPositiveButton
            }

            launch {
                try {
                    SpineVoxService.getService(context!!, true).setPassword("JWT $serverToken", newPass.text.toString(), oldPass.text.toString()).await()
                    showToast(context!!, "Пароль змінено")
                    dialog.cancel()
                } catch (e: Throwable) {
                    showToast(context!!, "Помилка")
                }
            }
        }
        alertDialog.setNegativeButton("Закрити") { dialog, _ -> dialog.cancel() }

        val alert11 = alertDialog.create()
        alert11.show()
    }

    override fun getLayoutId(): Int = R.layout.layout_profile_settings

}