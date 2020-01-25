package com.spinevox.app.screens.bluetoothdevices

import android.app.Dialog
import android.bluetooth.BluetoothAdapter.STATE_CONNECTED
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutBluetoothDevicePreparingBinding
import com.spinevox.app.screens.base.LazyFragment
import com.spinevox.app.screens.bluetoothdevices.BluetoothData.SERVICE_UUID
import com.spinevox.app.screens.bluetoothdevices.BluetoothData.WRITE_UUID
import java.nio.ByteBuffer
import java.nio.ByteOrder


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
            //findNavController().navigate(R.id.action_bluetoothDevicePreparingFragment_to_bluetoothDeviceTrainingFragment, bundleOf("timeHours" to binding.btnTrainingTime.text.toString()[0].toString().toInt()))
            val gattCallback: BluetoothGattCallback = object: BluetoothGattCallback() {
                override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                    if (newState == STATE_CONNECTED){
                        gatt?.discoverServices()
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
//                    val characteristic = gatt!!.getService(HEART_RATE_SERVICE_UUID)
//                        .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)

                    val blService = gatt?.getService(SERVICE_UUID)
                    val charac = blService?.getCharacteristic(WRITE_UUID)
                    val tsLong = (System.currentTimeMillis() / 1000).toInt()
                    val buffer = ByteBuffer.allocate(5)
                    buffer.order(ByteOrder.LITTLE_ENDIAN)
                    buffer.put(0.toByte())
                    buffer.putInt(tsLong)
                    charac?.value = buffer.array()
                    val status = gatt?.writeCharacteristic(charac)

     //               binding.pbLoader.visibility = View.GONE

                    findNavController().navigate(R.id.action_bluetoothDevicePreparingFragment_to_bluetoothDeviceTrainingFragment, bundleOf(
                        "timeHours" to binding.btnTrainingTime.text.toString()[0].toString().toInt(),
                        "timeToRemind" to binding.btnRemind.text.toString().split(' ')[0].toInt(),
                        "angleSpine" to binding.btnAngle.text.toString().substring(0, binding.btnAngle.text.toString().length - 1).toInt()
                    )
                    )


                    //gatt.setCharacteristicNotification(characteristic, true)
//                    val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
//                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//                    gatt.writeDescriptor(descriptor)



                }


                override fun onDescriptorWrite(
                    gatt: BluetoothGatt?,
                    descriptor: BluetoothGattDescriptor?,
                    status: Int
                ) {
                    val a = 5
//                val characteristic =
//                    gatt!!.getService(HEART_RATE_SERVICE_UUID)
//                        .getCharacteristic(HEART_RATE_CONTROL_POINT_CHAR_UUID)
//                characteristic.value = byteArrayOf(1, 1)
//                gatt.writeCharacteristic(characteristic)
                }

                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?
                ) {
                    val a = 5
                    //TODO!!!
//                processData(characteristic.getValue());
                }
            }

            val gatt: BluetoothGatt = BluetoothData.bluetoothDevice!!.connectGatt(context!!, true, gattCallback)
            BluetoothData.bluetoothGatt = gatt
            binding.pbLoader.visibility = View.VISIBLE
          //  gatt.connect()
//          //  gatt.discoverServices()
//            val blService = gatt.getService(SERVICE_UUID)
//            val charac = blService.getCharacteristic(WRITE_UUID)
//            val tsLong = (System.currentTimeMillis() / 1000).toInt()
//            val buffer = ByteBuffer.allocate(5)
//            buffer.order(ByteOrder.LITTLE_ENDIAN)
//            buffer.put(0.toByte())
//            buffer.putInt(tsLong)
//            charac.value = buffer.array()
//            val status = gatt.writeCharacteristic(charac)
//            val b = 5
        }



        initBluetoothDevices()
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

    private fun initBluetoothDevices() {






        //val status = gatt.writeCharacteristic(charac)
        //return status;



        //gatt.writeCharacteristic(BluetoothGattCharacteristic(uuid, 0, 0))
    }

    override fun getLayoutId(): Int = R.layout.layout_bluetooth_device_preparing

}