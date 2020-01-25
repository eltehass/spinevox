package com.spinevox.app.screens.login

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.edit
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
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.spinevox.app.R
import com.spinevox.app.common.URL_TERMS_RULES
import com.spinevox.app.common.openWebPage
import com.spinevox.app.common.showToast
import com.spinevox.app.databinding.LayoutLoginOrRegisterBinding
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.serverErrorMessage
import com.spinevox.app.screens.base.LazyFragment
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


class LoginOrRegisterFragment : LazyFragment<LayoutLoginOrRegisterBinding>() {

    override lateinit var binding: LayoutLoginOrRegisterBinding

    //Facebook
    private val facebookAuthCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    //Google
    private val GOOGLE_RC_SIGN_IN = 228

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("455987406157-vt3bd2fvdlft7s992r7t4q70kb5m4350.apps.googleusercontent.com")
            .requestServerAuthCode("455987406157-vt3bd2fvdlft7s992r7t4q70kb5m4350.apps.googleusercontent.com")
            .requestScopes(Scope(Scopes.PROFILE))
            .requestScopes(Scope(Scopes.EMAIL))
            .requestEmail()
            .build()
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
                context!!,
                googleSignInOptions
        )
    }

    override fun initController(view: View) {
        binding.llBottom.setOnClickListener { activity?.openWebPage(URL_TERMS_RULES) }
        initFacebookAuth()
        //initGoogleAuth()

        // Simple login and pass
        binding.btnLogin.setOnClickListener { findNavController().navigate(R.id.action_mainLoginFragment_to_loginFragment) }
        binding.btnRegistration.setOnClickListener { findNavController().navigate(R.id.action_mainLoginFragment_to_registerFragment) }

        // Facebook and Google auth
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
                        val accessToken = loginResult.accessToken.token
                        loginFacebook(accessToken)
                    }

                    override fun onCancel() {}
                    override fun onError(exception: FacebookException) {}
                })
    }

//    private fun initGoogleAuth() {
//        val account = GoogleSignIn.getLastSignedInAccount(context)
////        GoogleSignIn.getAccountForExtension(context).  .currentUser.authentication.accessToken
//        account?.let { handleGoogleAccount(it) }
//    }

    private fun handleGoogleAccount(account: GoogleSignInAccount) {
        //TODODO
        val serverAuthCode = account.serverAuthCode
        serverAuthCode?.let {
            getGoogleAccessToken(it) { accessToken ->
                loginGoogle(accessToken)
            }
        }

//        accessToken?.let { loginGoogle(it) }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let { handleGoogleAccount(it) }
        } catch (e: ApiException) {
            //12500 - update play services
            Log.i("GoogleLogin", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun loginFacebook(accessToken: String) {
        launch {
            try {
                showProgress()
                val serverResponse = SpineVoxService.getService(context!!).facebookLogin(accessToken)
                val serverToken = serverResponse.await().data.token
                sharedPreferences.edit { putString("serverToken", serverToken) }

                val height = SpineVoxService.getService(context!!, true).me("JWT $serverToken").await().data.profile.height
                if (height == null || height < 50) {
                    findNavController().navigate(R.id.action_mainLoginFragment_to_setAgeActivity)
                } else {
                    sharedPreferences.edit { putInt("user_height", height.toInt()) }
                    findNavController().navigate(R.id.action_mainLoginFragment_to_hostContentActivity)
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

    private fun loginGoogle(accessToken: String) {
        launch {
            try {
                showProgress()
                val serverResponse = SpineVoxService.getService(context!!).googleLogin(accessToken)
                val serverToken = serverResponse.await().data.token
                sharedPreferences.edit { putString("serverToken", serverToken) }

                val height = SpineVoxService.getService(context!!, true).me("JWT $serverToken").await().data.profile.height
                if (height == null || height < 50) {
                    findNavController().navigate(R.id.action_mainLoginFragment_to_setAgeActivity)
                } else {
                    sharedPreferences.edit { putInt("user_height", height.toInt()) }
                    findNavController().navigate(R.id.action_mainLoginFragment_to_hostContentActivity)
                }

                activity?.finish()
            } catch (e: HttpException) {
                showToast(context!!, e.serverErrorMessage())
            }  catch (e: Throwable) {

            } finally {
                hideProgress()
            }
        }

    }

    fun getGoogleAccessToken(authCode: String, onAccessTokenReceive: ((String) -> Unit)) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", "455987406157-vt3bd2fvdlft7s992r7t4q70kb5m4350.apps.googleusercontent.com")
            .add("client_secret", "FKwd8bjeUXPTA5igzhVreqKf")
            .add("code", authCode)
            .build()

        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body?.string())
                    val mAccessToken = jsonObject.get("access_token").toString()
                    onAccessTokenReceive(mAccessToken)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun showProgress() {
        binding.flLoginFacebook.isClickable = false
        binding.flLoginGoogle.isClickable = false
        binding.pbLoader.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.flLoginFacebook.isClickable = true
        binding.flLoginGoogle.isClickable = true
        binding.pbLoader.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Google
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            return
        }

        //Facebook
        facebookAuthCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun getLayoutId(): Int = R.layout.layout_login_or_register

}