package com.smartahc.android.rxcalipertest.listener

import android.bluetooth.BluetoothDevice

/**
 * Created by Leero on 2018/6/6.
 * Desc:
 */
class IBleBase {

    interface BleSearchConnectListener {
        fun onDeviceFound(device: BluetoothDevice)
        fun onDeviceConnect()
    }

    interface BleDisconnectListener {
        fun onDeviceDisconnect()
    }
}