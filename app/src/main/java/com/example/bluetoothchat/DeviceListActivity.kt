package com.example.bluetoothchat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_device_list.*

class DeviceListActivity : AppCompatActivity(), View.OnClickListener {

    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        bt_show.setOnClickListener(this)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_show -> onShowClicked()
        }
    }

    private fun onShowClicked() {
        val bondedDeviceList: Set<BluetoothDevice> =
            bluetoothAdapter?.bondedDevices as Set<BluetoothDevice>
        var deviceNames: ArrayList<String>? = ArrayList()
        if (bondedDeviceList.isNotEmpty()) {
            for (device in bondedDeviceList) {
                deviceNames!!.add(device.name)
            }
            val bluetoothDevicesAdapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_expandable_list_item_1,
                deviceNames!!
            )
            lv_bluetoothDevices.adapter = bluetoothDevicesAdapter
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BL = 1
    }
}