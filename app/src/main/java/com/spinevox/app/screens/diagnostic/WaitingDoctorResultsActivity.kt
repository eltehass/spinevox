package com.spinevox.app.screens.diagnostic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spinevox.app.R
import kotlinx.android.synthetic.main.activity_photo_preview.iv_close
import kotlinx.android.synthetic.main.activity_waiting_doctor_results.*

class WaitingDoctorResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_doctor_results)

        iv_close.setOnClickListener { onBackPressed() }
        btn_good.setOnClickListener { finish() }

    }
}
