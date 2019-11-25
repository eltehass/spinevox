package com.leo.spinevox.screens.login

import android.view.View
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutLoginOrRegisterBinding
import com.leo.spinevox.screens.base.LazyFragment

class LoginOrRegisterFragment : LazyFragment<LayoutLoginOrRegisterBinding>() {

    override lateinit var binding: LayoutLoginOrRegisterBinding

    override fun initController(view: View) {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_mainLoginFragment_to_loginFragment)
        }

        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_mainLoginFragment_to_registerFragment)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_login_or_register

}