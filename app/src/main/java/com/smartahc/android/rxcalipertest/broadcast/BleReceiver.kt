package com.smartahc.android.rxcalipertest.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.smartahc.android.rxcalipertest.listener.BleDataParseListener
import com.smartahc.android.rxcalipertest.listener.IBleBase
import com.smartahc.android.smartble.constant.Constant

/**
 * Created by Leero on 2018/6/6.
 * Desc:
 */
class BleReceiver : BroadcastReceiver() {

    private var searchConnectListener: IBleBase.BleSearchConnectListener? = null
    private var disconnectListener: IBleBase.BleDisconnectListener? = null
    private var dataParseListener: BleDataParseListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Constant.BLE_MESSAGE_DEVICE_FOUND -> {
                    searchConnectListener?.onDeviceFound(it.getParcelableExtra(Constant.BLE_DEVICE_DATA))
                }
                Constant.BLE_MESSAGE_DEVICE_CONNECT -> {
                    searchConnectListener?.onDeviceConnect()
                }
                Constant.BLE_MESSAGE_DEVICE_DISCONNECT -> {
                    disconnectListener?.onDeviceDisconnect()
                }
                Constant.BLE_EAR_TAG_PARSE_MSG -> {
                    dataParseListener?.onEarTagParse(it.getStringExtra(Constant.BLE_EAR_TAG_DATA))
                }
                else -> {
                    Log.d("BleReceiver", "-- other message --")
                }
            }
        }
    }

    fun setSearchConnectListener(listener: IBleBase.BleSearchConnectListener) {
        this.searchConnectListener = listener
    }

    fun setDisconnectListener(listener: IBleBase.BleDisconnectListener) {
        this.disconnectListener = listener
    }

    fun setDataParseListener(listener: BleDataParseListener) {
        this.dataParseListener = listener
    }
}