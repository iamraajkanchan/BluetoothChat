package com.example.bluetoothchat

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val registerForBluetoothActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            if (activityResult.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext, "Request Denied!!!", Toast.LENGTH_SHORT).show()
            } else if (activityResult.resultCode == Activity.RESULT_OK) {
                bt_disable.isEnabled = true
                bt_showActivity.isEnabled = true
                bt_scanActivity.isEnabled = true
                bt_enable.isEnabled = false
                Toast.makeText(applicationContext, "Bluetooth is enabled", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        initButtons()
        bt_enable.setOnClickListener(this)
        bt_disable.setOnClickListener(this)
        bt_showActivity.setOnClickListener(this)
        bt_scanActivity.setOnClickListener(this)
        bt_bluetoothCheck.setOnClickListener(this)
    }

    private fun initButtons() {
        if (bluetoothAdapter!!.isEnabled) {
            bt_disable.isEnabled = true
            bt_showActivity.isEnabled = true
            bt_scanActivity.isEnabled = true
            bt_enable.isEnabled = false
        } else {
            bt_disable.isEnabled = false
            bt_showActivity.isEnabled = false
            bt_scanActivity.isEnabled = false
            bt_enable.isEnabled = true
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_enable -> {
                onEnableClick()
            }
            R.id.bt_disable -> {
                onDisableClick()
            }
            R.id.bt_showActivity -> {
                onShowClick()
            }
            R.id.bt_scanActivity -> {
                onScanClick()
            }
            R.id.bt_bluetoothCheck -> {
                onBluetoothCheckClick()
            }
        }
    }

    private fun onEnableClick() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter!!.isEnabled) {
                val enableBlueToothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                try {
                    registerForBluetoothActivity.launch(enableBlueToothIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        applicationContext,
                        "Something Went Wrong!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                "This device doesn't support Bluetooth.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /* Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bt_disable.isEnabled = true
                bt_showActivity.isEnabled = true
                bt_scanActivity.isEnabled = true
                bt_enable.isEnabled = false
                Toast.makeText(applicationContext, "Bluetooth is enabled", Toast.LENGTH_LONG).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(applicationContext, "Request Denied!!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    */

    private fun onDisableClick() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter!!.isEnabled) {
                bluetoothAdapter?.disable()
                bt_disable.isEnabled = false
                bt_showActivity.isEnabled = false
                bt_scanActivity.isEnabled = false
                bt_enable.isEnabled = true
                Toast.makeText(applicationContext, "Bluetooth is disabled", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Bluetooth is already disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onShowClick() {
        Intent(applicationContext, DeviceListActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun onScanClick() {
        Intent(applicationContext, DiscoverDeviceActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun onBluetoothCheckClick() {
        Intent(applicationContext, BluetoothCheckActivity::class.java).apply {
            startActivity(this)
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1 //Choose any value greater than 0
    }
}