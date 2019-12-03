package com.spinevox.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.spinevox.app.screens.HostContentActivity
import com.spinevox.app.screens.login.HostLoginActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
        val serverToken = sharedPreferences.getString("serverToken", "") ?: ""

        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this@SplashActivity, if (serverToken.isEmpty()) HostLoginActivity::class.java else HostContentActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH)

    }
}
