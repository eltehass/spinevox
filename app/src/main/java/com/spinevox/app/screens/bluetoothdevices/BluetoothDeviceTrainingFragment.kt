package com.spinevox.app.screens.bluetoothdevices

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutBluetoothDeviceTrainingBinding
import com.spinevox.app.screens.base.LazyFragment
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BluetoothDeviceTrainingFragment : LazyFragment<LayoutBluetoothDeviceTrainingBinding>() {

    override lateinit var binding: LayoutBluetoothDeviceTrainingBinding

    private var timerIsWorking = false

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 0L


    override fun initController(view: View) {
        //TODO
        val time = (arguments?.getInt("timeHours")?.toLong() ?: 1) * 60 * 60 * 1000
        val timeToRemind = (arguments?.getInt("timeToRemind")?.toLong() ?: 30)
        val angle = (arguments?.getInt("angleSpine")?.toLong() ?: 5)
        timeLeft = time

        startTimer()
        timerIsWorking = true

        binding.btnPause.setOnClickListener {
            if (timerIsWorking) {
                //ACTION PAUSE

                val blService = BluetoothData.bluetoothGatt?.getService(BluetoothData.SERVICE_UUID)
                val charac = blService?.getCharacteristic(BluetoothData.WRITE_UUID)
//        val tsLong = (System.currentTimeMillis() / 1000).toInt()

                val buffer = ByteBuffer.allocate(17)
                buffer.order(ByteOrder.LITTLE_ENDIAN)
                buffer.put(0x01)
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0xFF.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())
                buffer.put(0x00.toByte())

//        buffer.putInt(tsLong)
                charac?.value = buffer.array()
                val status = BluetoothData.bluetoothGatt?.writeCharacteristic(charac)

                timerIsWorking = false
                binding.btnPause.text = "Продовжити"
                countDownTimer.cancel()
            } else {
                //ACTION CONTINUE

                val blService = BluetoothData.bluetoothGatt?.getService(BluetoothData.SERVICE_UUID)
                val charac = blService?.getCharacteristic(BluetoothData.WRITE_UUID)
//        val tsLong = (System.currentTimeMillis() / 1000).toInt()

                val buffer = ByteBuffer.allocate(9)
                buffer.order(ByteOrder.LITTLE_ENDIAN)
                buffer.put(1.toByte())
                buffer.putInt(timeLeft.toInt() / 1000)
                buffer.putShort(timeToRemind.toShort())
                buffer.put((angle * 2).toByte())
                buffer.put(90.toByte())

//        buffer.putInt(tsLong)
                charac?.value = buffer.array()
                val status = BluetoothData.bluetoothGatt?.writeCharacteristic(charac)


                timerIsWorking = true
                binding.btnPause.text = "Пауза"
                startTimer()
            }
        }

        binding.btnStop.setOnClickListener {
            val blService = BluetoothData.bluetoothGatt?.getService(BluetoothData.SERVICE_UUID)
            val charac = blService?.getCharacteristic(BluetoothData.WRITE_UUID)
//        val tsLong = (System.currentTimeMillis() / 1000).toInt()

            val buffer = ByteBuffer.allocate(17)
            buffer.order(ByteOrder.LITTLE_ENDIAN)
            buffer.put(0x01)
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0xFF.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())
            buffer.put(0x00.toByte())

//        buffer.putInt(tsLong)
            charac?.value = buffer.array()
            val status = BluetoothData.bluetoothGatt?.writeCharacteristic(charac)

            findNavController().popBackStack()
        }

        val blService = BluetoothData.bluetoothGatt?.getService(BluetoothData.SERVICE_UUID)
        val charac = blService?.getCharacteristic(BluetoothData.WRITE_UUID)
//        val tsLong = (System.currentTimeMillis() / 1000).toInt()

        val buffer = ByteBuffer.allocate(9)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(1.toByte())
        buffer.putInt(timeLeft.toInt() / 1000)
        buffer.putShort(timeToRemind.toShort())
        buffer.put((angle * 2).toByte())
        buffer.put(90.toByte())

//        buffer.putInt(tsLong)
        charac?.value = buffer.array()
        val status = BluetoothData.bluetoothGatt?.writeCharacteristic(charac)

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