package com.spinevox.app.screens.diagnostic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.spinevox.app.R
import kotlinx.android.synthetic.main.activity_waiting_doctor_results.*

class WaitingDoctorResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_doctor_results)

        btn_good.setOnClickListener { finish() }
        Glide.with(this).load(R.drawable.wating_resa).into(iv_running_robot)
    }
}
