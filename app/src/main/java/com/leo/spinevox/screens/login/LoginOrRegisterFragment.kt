package com.leo.spinevox.screens.login

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutLoginOrRegisterBinding
import com.leo.spinevox.screens.base.LazyFragment

class LoginOrRegisterFragment : LazyFragment<LayoutLoginOrRegisterBinding>() {

    override lateinit var binding: LayoutLoginOrRegisterBinding

    //Facebook
    private val facebookAuthCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    //Google
    private val GOOGLE_RC_SIGN_IN = 228

    private val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("808856282077-kb8jqpkf5nllutarcmqvq0gcl94hor26.apps.googleusercontent.com")
        .requestEmail()
        .build()

    private val googleSignInClient: GoogleSignInClient by lazy { GoogleSignIn.getClient(context!!, googleSignInOptions) }

    override fun initController(view: View) {
        initFacebookAuth()
        initGoogleAuth()

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_mainLoginFragment_to_loginFragment)
        }

        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_mainLoginFragment_to_registerFragment)
        }

        binding.flLoginFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        }

        binding.flLoginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN)
        }
    }

    private fun initFacebookAuth() {
        LoginManager.getInstance().registerCallback(facebookAuthCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.i("FacebookLogin", "Token: ${loginResult.accessToken.token}")
                }

                override fun onCancel() {
                    Log.i("FacebookLogin", "Cancel")
                }

                override fun onError(exception: FacebookException) {
                    Log.i("FacebookLogin", "Exc: ${exception.message}")
                }
            })
    }

    private fun initGoogleAuth() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?.let { handleGoogleAccount(it) }
    }

    private fun handleGoogleAccount(account: GoogleSignInAccount) {
        Log.i("GoogleLogin", "Token: ${account.id}")
        Log.i("GoogleLogin", "Token: ${account.idToken}")
        Log.i("GoogleLogin", "AuthCode: ${account.serverAuthCode}")
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            var account = completedTask.getResult(ApiException::class.java)
            account?.let { handleGoogleAccount(it) }
        } catch (e: ApiException) {
            //12500 - update play services
            //TODO code 10
            Log.i("GoogleLogin", "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            return
        }

        facebookAuthCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun getLayoutId(): Int = R.layout.layout_login_or_register

}