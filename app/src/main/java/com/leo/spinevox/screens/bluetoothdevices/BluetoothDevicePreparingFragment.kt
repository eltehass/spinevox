package com.leo.spinevox.screens.bluetoothdevices

import android.app.Dialog
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutBluetoothDevicePreparingBinding
import com.leo.spinevox.screens.base.LazyFragment

class BluetoothDevicePreparingFragment : LazyFragment<LayoutBluetoothDevicePreparingBinding>() {

    override lateinit var binding: LayoutBluetoothDevicePreparingBinding

    override fun initController(view: View) {
        binding.btnAngle.setOnClickListener {
            createDialog("Кут реакції", "", listOf("3°", "5°", "10°")) {
                binding.btnAngle.text = it
            }.show()
        }

        binding.btnRemind.setOnClickListener {
            createDialog("Нагадування", "Вирівняйся через", listOf("5 сек", "10 сек", "15 сек", "20 сек", "25 сек", "30 сек", "45 сек", "60 сек")) {
                binding.btnRemind.text = it
            }.show()
        }

        binding.btnTrainingTime.setOnClickListener {
            createDialog("Час тренування", "Рекомендований час", listOf("1 год", "2 год", "3 год", "4 год", "5 год")) {
                binding.btnTrainingTime.text = it
            }.show()
        }

        binding.ivStart.setOnClickListener {
            findNavController().navigate(R.id.action_bluetoothDevicePreparingFragment_to_bluetoothDeviceTrainingFragment, bundleOf("timeHours" to binding.btnTrainingTime.text.toString()[0].toString().toInt()))
        }
    }

    private fun createDialog(title: String, subtitle: String, data: List<String>, onResult: (value: String) -> Unit): Dialog {
        val numberPicker = NumberPicker(activity)

        numberPicker.minValue = 0
        numberPicker.maxValue = data.size - 1
        numberPicker.displayedValues = data.toTypedArray()

        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(title)
        builder.setMessage(subtitle)

        builder.setPositiveButton("Обрати") { dialog, _ ->
            onResult(data[numberPicker.value])
            dialog.cancel()
        }

        builder.setNegativeButton("Відмінити") { dialog, _ -> dialog.cancel() }

        builder.setView(numberPicker)
        return builder.create()
    }

    override fun getLayoutId(): Int = R.layout.layout_bluetooth_device_preparing

}