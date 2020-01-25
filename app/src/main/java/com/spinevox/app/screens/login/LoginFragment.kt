package com.spinevox.app.screens.login

import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.common.URL_TERMS_RULES
import com.spinevox.app.common.openWebPage
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutLoginBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.serverErrorMessage
import com.spinevox.app.screens.base.LazyFragment
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFragment : LazyFragment<LayoutLoginBinding>() {

    override lateinit var binding: LayoutLoginBinding

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

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment)  }
        binding.btnLogin.setOnClickListener {
            if (binding.etPhone.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            //TODO:
            launch {
                try {
                    showProgress()
                    val loginResponse = SpineVoxService.getService(context!!).login(binding.etPhone.text.toString().replace("\\s".toRegex(), ""), binding.etPassword.text.toString())
                    val serverToken = loginResponse.await().data.token
                    sharedPreferences.edit { putString("serverToken", serverToken) }

                    val height = SpineVoxService.getService(context!!, true).me("JWT $serverToken").await().data.profile.height
                    if (height == null || height < 50) {
                        findNavController().navigate(R.id.action_loginFragment_to_setAgeActivity)
                    } else {
                        sharedPreferences.edit { putInt("user_height", height.toInt()) }
                        findNavController().navigate(R.id.action_loginFragment_to_hostContentActivity)
                    }

                    activity?.finish()
                } catch (e: HttpException) {
                    showToast(context!!, e.serverErrorMessage())
                } catch (e: Throwable) {

                } finally {
                    hideProgress()
                }
            }
        }
    }

    private fun showProgress() {
        binding.btnLogin.isClickable = false
        binding.pbLoader.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.btnLogin.isClickable = true
        binding.pbLoader.visibility = View.GONE
    }

    override fun getLayoutId(): Int = R.layout.layout_login

}