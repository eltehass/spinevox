package com.spinevox.app.screens.login

import android.annotation.SuppressLint
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.common.URL_TERMS_RULES
import com.spinevox.app.common.openWebPage
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutRegisterBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.serverErrorMessage
import com.spinevox.app.screens.base.LazyFragment
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterFragment : LazyFragment<LayoutRegisterBinding>() {

    override lateinit var binding: LayoutRegisterBinding

    override fun initController(view: View) {
        binding.llBottom.setOnClickListener { activity?.openWebPage(URL_TERMS_RULES) }
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.etPhone.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.etPhone.text.toString().contains('+') && !s.toString().contains('+') && s.toString().isNotEmpty()) {
                    binding.etPhone.setText("+" + binding.etPhone.text.toString())
                    binding.etPhone.setSelection(binding.etPhone.text.toString().length)
                }
            }
        })

        binding.tvEnter.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnStart.setOnClickListener {
            //TODO finish checking rules
            if (binding.etName.text.toString().isEmpty() || binding.etPhone.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            launch {
                showProgress()
                try {
                    SpineVoxService.getService(context!!).registration(phone = binding.etPhone.text.toString().replace("\\s".toRegex(), ""), firstName = binding.etName.text.toString(), password = binding.etPassword.text.toString()).await()
                    showConfirmSmsCodeDialog()
                } catch (e: HttpException) {
                    showToast(context!!, e.serverErrorMessage())
                } catch (e: Throwable) {
                } finally {
                    hideProgress()
                }
            }
        }

    }

    @SuppressLint("ShowToast")
    private fun showConfirmSmsCodeDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
            .setTitle("СМС код")
            .setMessage("Підтвердіть номер телефону за допомогою СМС коду")

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.inputType = InputType.TYPE_CLASS_PHONE
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Ок") { dialog, _ ->
            val confirmCode = input.text.toString()
            launch {
                showProgress()
                try {
                    val result = SpineVoxService.getService(context!!).registrationConfirm(binding.etPhone.text.toString().replace("\\s".toRegex(), ""), confirmCode).await()
                    dialog.cancel()
                    showToast(context!!, "Реєстрація успішна. Увійдіть у ваш аккаунт.")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }  catch (e: HttpException) {
                    showToast(context!!, e.serverErrorMessage())
                }
                catch (e: Throwable) {
                } finally {
                    hideProgress()
                }
            }
        }

        alertDialog.setNegativeButton("Закрити") { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    private fun showProgress() {
        binding.btnStart.isClickable = false
        binding.pbLoader.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.btnStart.isClickable = true
        binding.pbLoader.visibility = View.GONE
    }

    override fun getLayoutId(): Int = R.layout.layout_register

}