package com.smartahc.android.rxcalipertest.activity

import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.smartahc.android.rxcalipertest.R
import com.smartahc.android.rxcalipertest.adapter.BluetoothScanAdapter
import com.smartahc.android.rxcalipertest.broadcast.BleReceiver
import com.smartahc.android.rxcalipertest.listener.IBleBase
import com.smartahc.android.rxcalipertest.utils.SPUtil
import com.smartahc.android.rxcalipertest.utils.ToolbarUtils
import com.smartahc.android.smartble.constant.Constant
import com.smartahc.android.smartble.service.BleService
import com.smartahc.android.smartble.service.LocalBinder
import kotlinx.android.synthetic.main.activity_bluetooth_scan.*


/**
 * Created by Leero on 2018/5/7.
 * Desc:
 */
class BluetoothScanActivity : AppCompatActivity(), IBleBase.BleDisconnectListener, IBleBase.BleSearchConnectListener {

    private var mListData = arrayListOf<BluetoothDevice>()
    private var mBluetoothAdapter: BluetoothScanAdapter? = null
    private var loadingDialog: Dialog? = null

    private lateinit var currentBleName: String // 当前连接的BLE名字
    private lateinit var currentBleMac: String // 当前连接的BLE地址

    private var mReceiver: BleReceiver? = null
    private var mService: BleService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = (service as LocalBinder).service
            // 开始搜索
            mService?.search()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_scan)
        bindService(Intent(this, BleService::class.java), connection, Context.BIND_AUTO_CREATE)

        ToolbarUtils.instance.setToolbar(this, "连接设备", true)

        registerBroadCast()

        rvBluetooth.layoutManager = LinearLayoutManager(this)
        mBluetoothAdapter = BluetoothScanAdapter(this, mListData)
        rvBluetooth.adapter = mBluetoothAdapter

        mBluetoothAdapter!!.setItemClickListener(object : BluetoothScanAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, name: String) {
                AlertDialog.Builder(this@BluetoothScanActivity)
                        .setTitle("提示")
                        .setMessage("确定连接:$name")
                        .setCancelable(true)
                        .setPositiveButton("确定", { dialog, which ->
                            currentBleName = mListData[position].name
                            currentBleMac = mListData[position].address

                            // 停止搜索
                            pbScan.visibility = View.GONE
                            tvScanStatus.text = "开始连接，搜索停止"
                            mService?.stopScanBLE()
                            // 连接蓝牙设备
                            mService?.connect(mListData[position].name, mListData[position].address)

                            // 显示loading
                            showLoading("连接中")
                        }).setNegativeButton("取消", { dialog, which ->
                            dialog.dismiss()
                        }).show()
            }
        })
    }

    /**
     * 注册广播
     */
    private fun registerBroadCast() {
        mReceiver = BleReceiver()
        val filter = IntentFilter()
        filter.addAction(Constant.BLE_MESSAGE_DEVICE_FOUND)
        filter.addAction(Constant.BLE_MESSAGE_DEVICE_CONNECT)
        filter.addAction(Constant.BLE_MESSAGE_DEVICE_DISCONNECT)
        registerReceiver(mReceiver, filter)

        mReceiver?.let {
            it.setSearchConnectListener(this)
            it.setDisconnectListener(this)
        }
    }

    override fun onDeviceFound(device: BluetoothDevice) {
        if (!mListData.contains(device)) {
            device.name?.let{
                mListData.add(device)
                mBluetoothAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDeviceConnect() {
        hideLoading()
        tvScanStatus.text = "连接成功"

        SPUtil.putString(com.smartahc.android.rxcalipertest.utils.Constant.BLE_NAME, currentBleName)
        SPUtil.putString(com.smartahc.android.rxcalipertest.utils.Constant.BLE_MAC, currentBleMac)

        Handler().postDelayed({ kotlin.run {
            startActivity(Intent(this, CaliperDetailActivity::class.java))
            finish()
        } }, 1000)
    }

    override fun onDeviceDisconnect() {
        hideLoading()
        tvScanStatus.text = "连接失败，请重新选择"
    }

    private fun showLoading(msg: String) {
        loadingDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.view_loading_dialog, null)
        view.findViewById<TextView>(R.id.tvTips).text = msg
        loadingDialog?.setContentView(view)
        loadingDialog?.window?.setGravity(Gravity.CENTER)
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.let {
            loadingDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        unbindService(connection)
        mReceiver?.let {
            unregisterReceiver(it)
        }
        super.onDestroy()
    }

}