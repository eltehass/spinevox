package com.spinevox.app.screens.bluetoothdevices

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutBluetoothDeviceTrainingBinding
import com.spinevox.app.screens.base.LazyFragment

class BluetoothDeviceTrainingFragment : LazyFragment<LayoutBluetoothDeviceTrainingBinding>() {

    override lateinit var binding: LayoutBluetoothDeviceTrainingBinding

    private var timerIsWorking = false

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 0L


    override fun initController(view: View) {
        //TODO
        val time = (arguments?.getInt("timeHours")?.toLong() ?: 1) * 60 * 60 * 1000
        timeLeft = time

        startTimer()
        timerIsWorking = true

        binding.btnPause.setOnClickListener {
            if (timerIsWorking) {
                timerIsWorking = false
                binding.btnPause.text = "Продовжити"
                countDownTimer.cancel()
            } else {
                timerIsWorking = true
                binding.btnPause.text = "Пауза"
                startTimer()
            }
        }

        binding.btnStop.setOnClickListener { findNavController().popBackStack() }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeft, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                binding.tvTimer.text = "${timeLeft / 3_600_000}:${timeLeft % 3_600_000 / 60_000}:${timeLeft % 3_600_000 % 60_000 / 1000}"
            }

            override fun onFinish() {}
        }.start()
    }

    override fun getLayoutId(): Int = R.layout.layout_bluetooth_device_training

}