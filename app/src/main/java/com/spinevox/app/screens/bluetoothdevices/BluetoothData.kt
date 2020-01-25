package com.spinevox.app.screens.bluetoothdevices

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import java.util.*

object BluetoothData {
    val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    val HEART_RATE_CONTROL_POINT_CHAR_UUID = convertFromInteger(0x2A39)
    val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)

    var uuid: UUID? = null
    var bluetoothDevice: BluetoothDevice? = null
    var bluetoothGatt: BluetoothGatt? = null

    fun convertFromInteger(i: Int): UUID? {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and ((-0x1).toLong()).toInt()).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }


    const val BL_SERVICE = "F19BD8F6-4B25-40C4-991B-95F549BF75C1"
    const val BL_CHAR_WRITE = "F19BD8F8-4B25-40C4-991B-95F549BF75C1"

    const val BL_CHAR_READ = "F19BD8F7-4B25-40C4-991B-95F549BF75C1"

    val SERVICE_UUID = UUID.fromString(BL_SERVICE)
    val WRITE_UUID = UUID.fromString(BL_CHAR_WRITE)

}

