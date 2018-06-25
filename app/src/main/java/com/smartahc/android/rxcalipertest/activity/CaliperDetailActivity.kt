package com.smartahc.android.rxcalipertest.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smartahc.android.rxcalipertest.R
import com.smartahc.android.rxcalipertest.broadcast.BleReceiver
import com.smartahc.android.rxcalipertest.listener.BleDataParseListener
import com.smartahc.android.rxcalipertest.listener.IBleBase
import com.smartahc.android.rxcalipertest.utils.Constant
import com.smartahc.android.rxcalipertest.utils.SPUtil
import com.smartahc.android.rxcalipertest.utils.ToolbarUtils
import kotlinx.android.synthetic.main.activity_caliper_detail.*


/**
 * Created by Leero on 2018/5/8.
 * Desc:
 */
class CaliperDetailActivity : AppCompatActivity(), BleDataParseListener, IBleBase.BleDisconnectListener {

    private var mReceiver: BleReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caliper_detail)
        registerBroadCast()

        ToolbarUtils.instance.setToolbar(this, "卡钳详情", true)
        loadData()

        tvClear.setOnClickListener {
            tvMarkNum.text = "0"
            tvEarMark.text = "-1"
        }
    }

    /**
     * 注册广播
     */
    private fun registerBroadCast() {
        mReceiver = BleReceiver()
        val filter = IntentFilter()
        filter.addAction(com.smartahc.android.smartble.constant.Constant.BLE_MESSAGE_DEVICE_DISCONNECT)
        filter.addAction(com.smartahc.android.smartble.constant.Constant.BLE_EAR_TAG_PARSE_MSG)
        registerReceiver(mReceiver, filter)

        mReceiver?.let {
            it.setDisconnectListener(this)
            it.setDataParseListener(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        var bleName = SPUtil.takeString(Constant.BLE_NAME)
        if (!bleName.equals("") && bleName != null) {
            tvConnectStatus.text = "$bleName/已连接"
        }
    }

    override fun onDeviceDisconnect() {
        tvConnectStatus.text = "蓝牙已断开"
    }

    override fun onEarTagParse(earTag: String) {
        tvMarkNum.text =  (tvMarkNum.text.toString().toInt() + 1).toString() // 标号总数
        tvEarMark.text = earTag // 耳标号数据
    }

    override fun onDestroy() {
        mReceiver?.let {
            unregisterReceiver(it)
        }
        super.onDestroy()
    }
}