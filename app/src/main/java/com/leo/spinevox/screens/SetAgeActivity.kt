package com.leo.spinevox.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import com.leo.spinevox.R

class SetAgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_age)

        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)
        numberPicker.minValue = 100
        numberPicker.maxValue = 300
        numberPicker.value = 150

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            startActivity(Intent(this@SetAgeActivity, HostContentActivity::class.java))
        }
    }
}
