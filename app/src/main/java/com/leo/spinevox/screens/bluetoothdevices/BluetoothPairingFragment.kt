package com.leo.spinevox.screens.bluetoothdevices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class BluetoothPairingFragment {


    private fun connect() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = mBluetoothAdapter.bondedDevices

        val s = ArrayList<String>()
        for (bt in pairedDevices) {
            var context: Context? = null


            if (true) {
                ConnectThread(bt, mBluetoothAdapter).start()
            }

            s.add(bt.name)
        }


        //setListAdapter(ArrayAdapter<String>(this, R.layout.list, s))
    }

    private inner class ConnectThread(device: BluetoothDevice, val bluetoothAdapter: BluetoothAdapter) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.randomUUID())
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                //TODO
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                //manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                //Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }


}