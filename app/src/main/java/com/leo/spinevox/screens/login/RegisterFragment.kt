package com.leo.spinevox.screens.login

import android.view.View
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutRegisterBinding
import com.leo.spinevox.screens.base.LazyFragment

class RegisterFragment : LazyFragment<LayoutRegisterBinding>() {

    override lateinit var binding: LayoutRegisterBinding

    override fun initController(view: View) {
        binding.tvEnter.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment)  }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getLayoutId(): Int = R.layout.layout_register

}