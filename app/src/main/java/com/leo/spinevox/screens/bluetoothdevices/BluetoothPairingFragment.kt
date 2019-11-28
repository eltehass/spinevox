package com.leo.spinevox.screens.bluetoothdevices

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutBluetoothPairingBinding
import com.leo.spinevox.screens.base.LazyFragment
import java.util.*
import kotlin.collections.ArrayList

class BluetoothPairingFragment : LazyFragment<LayoutBluetoothPairingBinding>() {

    override lateinit var binding: LayoutBluetoothPairingBinding

    private var mDeviceList = ArrayList<String>()
    private var devices = ArrayList<BluetoothDevice>()
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var receiversWereRegistered = false

    private val mBluetoothConnection: BluetoothConnectionService by lazy { BluetoothConnectionService(context) }
    private val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    override fun initController(view: View) {
        initBluetooth()

        binding.ivConnect.setOnClickListener {
            if (binding.lv.checkedItemPosition >= 0) {
                devices[binding.lv.checkedItemPosition].createBond()
                if (devices[binding.lv.checkedItemPosition].bondState == BluetoothDevice.BOND_BONDED) {
                    onDeviceBonded()
                }
            }
        }
    }

    private fun initBluetooth() {
        if (ContextCompat.checkSelfPermission(activity!!, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(ACCESS_COARSE_LOCATION), 10101)
        }

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        } catch (e: Throwable) {
            return
        }

        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }

        mBluetoothAdapter.startDiscovery()

        val discoveryIntentFilter = IntentFilter()
        discoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        discoveryIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        activity!!.registerReceiver(bluetoothDiscoveryReceiver, discoveryIntentFilter)

        val bondIntentFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        activity!!.registerReceiver(bluetoothBondStateChangedReceiver, bondIntentFilter)

        receiversWereRegistered = true
    }

    private fun onDeviceBonded() {
        mBluetoothConnection.startClient(devices[binding.lv.checkedItemPosition], uuid)
        Log.d("BluetoothState", "BroadcastReceiver: BOND_BONDED.")
        findNavController().navigate(R.id.action_navigation_tab2_to_bluetoothDevicePreparingFragment)
    }

    private val bluetoothDiscoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("BluetoothState", "BluetoothAction" + intent.action)
            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device.name != null && device.name != "null") {
                    mDeviceList.add(device.name)
                    devices.add(device)
                    binding.lv.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_single_choice, mDeviceList)
                    binding.lv.choiceMode = ListView.CHOICE_MODE_SINGLE
                }
            }
        }
    }

    private val bluetoothBondStateChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device.bondState) {
                    BluetoothDevice.BOND_BONDED -> onDeviceBonded()
                    BluetoothDevice.BOND_BONDING -> Log.d("BluetoothState", "BroadcastReceiver: BOND_BONDED.")
                    BluetoothDevice.BOND_NONE -> Log.d("BluetoothState", "BroadcastReceiver: BOND_NONE.")
                }
            }
        }
    }

    override fun onDestroy() {
        if (receiversWereRegistered) {
            activity?.unregisterReceiver(bluetoothDiscoveryReceiver)
            activity?.unregisterReceiver(bluetoothBondStateChangedReceiver)
        }
        super.onDestroy()
    }

    override fun getLayoutId(): Int = R.layout.layout_bluetooth_pairing

}