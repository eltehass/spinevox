package com.leo.spinevox

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.leo.spinevox.screens.login.HostLoginActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this@SplashActivity, HostLoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH)

    }
}
