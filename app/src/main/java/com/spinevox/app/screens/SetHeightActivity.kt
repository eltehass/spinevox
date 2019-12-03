package com.spinevox.app.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.spinevox.app.R

class SetHeightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height_age)

        val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
//        val userHeight = sharedPreferences.getInt("user_height", -1)
//        if (userHeight != -1) {
//            startActivity(Intent(this@SetHeightActivity, HostContentActivity::class.java))
//            finish()
//        }

        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)
        numberPicker.minValue = 100
        numberPicker.maxValue = 300
        numberPicker.value = 150

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            sharedPreferences.edit { putInt("user_height", numberPicker.value) }
            startActivity(Intent(this@SetHeightActivity, HostContentActivity::class.java))
            finish()
        }
    }
}