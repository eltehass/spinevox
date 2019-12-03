package com.spinevox.app.screens.login

import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutLoginBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.screens.base.LazyFragment
import kotlinx.coroutines.launch

class LoginFragment : LazyFragment<LayoutLoginBinding>() {

    override lateinit var binding: LayoutLoginBinding

    override fun initController(view: View) {
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment)  }
        binding.btnLogin.setOnClickListener {
            if (binding.etPhone.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            //TODO:
            launch {
                try {
                    val loginResponse = SpineVoxService.getService(context!!).login(binding.etPhone.text.toString().replace("\\s".toRegex(), ""), binding.etPassword.text.toString())
                    val serverToken = loginResponse.await().data.token
                    sharedPreferences.edit { putString("serverToken", serverToken) }
                    findNavController().navigate(R.id.action_loginFragment_to_setAgeActivity)
                    activity?.finish()
                } catch (e: Throwable) {
                    val msg = e.message
                    //TODO show error
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_login

}