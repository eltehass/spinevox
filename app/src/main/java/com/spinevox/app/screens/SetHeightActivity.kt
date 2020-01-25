package com.spinevox.app.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.spinevox.app.R
import com.spinevox.app.network.ProfileWithHeight
import com.spinevox.app.network.RequestUserUpdate
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.screens.diagnostic.camera.CustomCameraUIActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SetHeightActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    companion object {
        var needToStartCamera = false
    }

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height_age)
        job = Job()

        val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
        val serverToken = sharedPreferences.getString("serverToken", "") ?: ""

        val height = sharedPreferences.getInt("user_height", 185)
//        val userHeight = sharedPreferences.getInt("user_height", -1)
//        if (userHeight != -1) {
//            startActivity(Intent(this@SetHeightActivity, HostContentActivity::class.java))
//            finish()
//        }

        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)
        numberPicker.minValue = 100
        numberPicker.maxValue = 300
        numberPicker.value = height

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            sharedPreferences.edit { putInt("user_height", numberPicker.value) }
            launch {
                try {
                    val profile = SpineVoxService.getService(this@SetHeightActivity, true).me("JWT $serverToken").await().data

                    SpineVoxService.getService(this@SetHeightActivity, true)
                            .updateMe(
                                    "JWT $serverToken",
                                    RequestUserUpdate(profile.first_name, ProfileWithHeight(numberPicker.value),  profile.last_name)).await()

                    if (!needToStartCamera) {
                        startActivity(Intent(this@SetHeightActivity, HostContentActivity::class.java))
                    } else {
                        startActivity(Intent(this@SetHeightActivity, CustomCameraUIActivity::class.java))
                        needToStartCamera = false
                    }

                    finish()
                } catch (e: Exception) {
                    val a = 5
                }
            }
        }
    }
}