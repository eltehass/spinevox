package com.spinevox.app.screens.login

import android.annotation.SuppressLint
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutRegisterBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.screens.base.LazyFragment
import kotlinx.coroutines.launch

class RegisterFragment : LazyFragment<LayoutRegisterBinding>() {

    override lateinit var binding: LayoutRegisterBinding

    override fun initController(view: View) {
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.tvEnter.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnStart.setOnClickListener {
            //TODO finish checking rules
            if (binding.etName.text.toString().isEmpty() || binding.etPhone.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            launch {
                try {
                    SpineVoxService.getService(context!!).registration(phone = binding.etPhone.text.toString().replace("\\s".toRegex(), ""), firstName = binding.etName.text.toString(), password = binding.etPassword.text.toString()).await()
                    showConfirmSmsCodeDialog()
                } catch (e: Throwable) {
                    showToast(context!!, "Помилка. Можливо користувач з таким номером телефона вже зареєстрований")
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
                try {
                    val result = SpineVoxService.getService(context!!).registrationConfirm(binding.etPhone.text.toString().replace("\\s".toRegex(), ""), confirmCode).await()
                    dialog.cancel()
                    showToast(context!!, "Реєстрація успішна. Увійдіть у ваш аккаунт.")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } catch (e: Throwable) {
                    showToast(context!!, "Не вірний код підтвердження")
                }
            }
        }

        alertDialog.setNegativeButton("Закрити") { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    override fun getLayoutId(): Int = R.layout.layout_register

}