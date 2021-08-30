package com.example.bluetoothchat

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_discover_device.*

class DiscoverDeviceActivity : AppCompatActivity(), View.OnClickListener {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var devices: ArrayList<String>? = ArrayList()
    private lateinit var deviceNameAdapter: ArrayAdapter<String>
    private val registerForBluetoothActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult ->
        if (activityResult.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(
                applicationContext,
                "Request Denied!!!",
                Toast.LENGTH_SHORT
            )
                .show()
        } else if (activityResult.resultCode == Activity.RESULT_OK) {
            showScanResult()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_device)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bt_scan.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_scan -> {
                onScanClick()
            }
        }
    }


    private fun onScanClick() {
        if (bluetoothAdapter?.isEnabled == true) {
            showScanResult()
        } else {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            /* Deprecated
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT)
            */
            try {
                registerForBluetoothActivity.launch(enableBluetoothIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Something went wrong!!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /* Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(applicationContext, "Bluetooth is disabled", Toast.LENGTH_SHORT).show()
        } else if (resultCode == RESULT_OK) {
            showScanResult()
        }
    }
    */

    private fun showScanResult() {
//        bluetoothAdapter?.startDiscovery()
        var intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(bluetoothBroadcastReceiver, intentFilter)
        /*intentFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        try {
            registerReceiver(bluetoothBroadcastReceiver, intentFilter)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery()
        }
    }

    private fun scanComplete() {
        bluetoothAdapter?.cancelDiscovery()
        unregisterReceiver(bluetoothBroadcastReceiver)
    }

    private val bluetoothBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "BroadCastReceiver is running", Toast.LENGTH_SHORT).show()
            println("DiscoverDeviceActivity :: BroadcastReceiver :: onReceive")
            val action: String? = intent?.action
            try {
                if (BluetoothDevice.ACTION_FOUND.equals(action, true)) {
                    val device: BluetoothDevice? =
                        intent!!.getParcelableExtra(BluetoothDevice.EXTRA_NAME)
                    if (device?.bondState != BluetoothDevice.BOND_BONDED) {
                        devices?.add(device!!.name)
                        deviceNameAdapter =
                            ArrayAdapter<String>(
                                applicationContext,
                                android.R.layout.simple_list_item_2,
                                devices!!
                            )
                        deviceNameAdapter.notifyDataSetChanged()
                        lv_discoveredDevices.adapter = deviceNameAdapter
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action, true)) {
                        scanComplete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}

/*Reference
*
*  https://stackoverflow.com/questions/16184420/bluetooth-on-android-startdiscovery-not-working-cannot-scan-devices
*
*/