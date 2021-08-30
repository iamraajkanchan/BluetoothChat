package com.example.bluetoothchat

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bluetooth_check.*

class BluetoothCheckActivity : AppCompatActivity() {
    private val bluetoothBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                val state: Int? =
                    intent?.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )
                when (state) {
                    BluetoothAdapter.STATE_ON -> tv_bluetoothCheck.text = "Bluetooth is On"
                    BluetoothAdapter.STATE_TURNING_ON -> tv_bluetoothCheck.text =
                        "Bluetooth is Turning ON"
                    BluetoothAdapter.STATE_OFF -> tv_bluetoothCheck.text =
                        "Bluetooth is Off"
                    BluetoothAdapter.STATE_TURNING_OFF -> tv_bluetoothCheck.text =
                        "Bluetooth is Turning Off"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_check)
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        try {
            registerReceiver(bluetoothBroadcastReceiver, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(bluetoothBroadcastReceiver)
        super.onDestroy()
    }
}