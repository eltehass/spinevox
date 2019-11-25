package com.leo.spinevox.screens.login

import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutLoginBinding
import com.leo.spinevox.screens.base.LazyFragment

class LoginFragment : LazyFragment<LayoutLoginBinding>() {

    override lateinit var binding: LayoutLoginBinding

    override fun initController(view: View) {
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment)  }
        binding.btnLogin.setOnClickListener {
            //TODO:
            findNavController().navigate(R.id.action_loginFragment_to_setAgeActivity)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_login

}