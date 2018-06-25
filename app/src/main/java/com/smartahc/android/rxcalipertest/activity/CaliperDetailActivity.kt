package com.smartahc.android.rxcalipertest.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
            tvResultNum.text = "0"
            tvOpenSignalNum.text = "0"
            tvCloseSignal.text = "0"

            resetCaliperData()
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
//        filter.addAction(com.smartahc.android.smartble.constant.Constant.BLE_SWITCH_MSG)
//        filter.addAction(com.smartahc.android.smartble.constant.Constant.BLE_MODULE_MSG)
//        filter.addAction(com.smartahc.android.smartble.constant.Constant.BLE_MAGNET_MSG)
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

    /**
     * 重置卡钳数据
     */
    private fun resetCaliperData() {
        tvCurOpen.text = "-1"
        tvOpenData.text = ""
        tvOpenData16.text = ""
        tvEarMark.text = "-1"
        tvEarData.text = ""
        tvEarData16.text = ""
        tvCurClose.text = "-1"
        tvCloseData.text = ""
        tvCloseData16.text = ""
        tvCurResult.text = "-1"
        tvResultData.text = ""
        tvResultData16.text = ""
        tvMagnetResult.text = "[0]"

        chart.data = null
        chart.invalidate()
    }

    override fun onDeviceDisconnect() {
        tvConnectStatus.text = "蓝牙已断开"
    }

    override fun onEarTagParse(earTag: String) {
        tvMarkNum.text =  (tvMarkNum.text.toString().toInt() + 1).toString() // 标号总数
        tvEarMark.text = earTag // 耳标号数据
//        tvEarData.text = earTag.originData.toString() // 耳标号原数据
//        tvEarData16.text = HexUtil.byteStringToString(earTag.originData.toString())
    }

    /*override fun onSwitchParse(switch: SwitchBean) {
        when (switch.result) {
            1 -> {
                resetCaliperData() // 收到开信号重置界面

                tvOpenSignalNum.text =  (tvOpenSignalNum.text.toString().toInt() + 1).toString() // 开信号总数
                tvCurOpen.text = switch.result.toString() // 开信号结果
                tvOpenData.text = switch.originData.toString() // 开信号原数据
                tvOpenData16.text = HexUtil.byteStringToString(switch.originData.toString())
            }
            0 -> {
                tvCloseSignal.text = (tvCloseSignal.text.toString().toInt() + 1).toString() // 关信号总数
                tvCurClose.text = switch.result.toString() // 关信号结果
                tvCloseData.text = switch.originData.toString() // 关信号原数据
                tvCloseData16.text = HexUtil.byteStringToString(switch.originData.toString())
            }
        }
    }

    override fun onModuleParse(module: ModuleBean) {
        tvResultNum.text = (tvResultNum.text.toString().toInt() + 1).toString() // 固件结果数据总数
        tvCurResult.text = module.startPoint.toString() // 固件结果SP值
        tvResultData.text = module.originData.toString() // 固件结果原数据
        tvResultData16.text = HexUtil.byteStringToString(module.originData.toString())
    }

    override fun onMagnetParse(magnet: MagnetBean) {
        tvMagnetResult.text = magnet.parseData
        getChart(magnet.parseData)
    }*/

    /**
     * 生成折线图
     */
    private fun getChart(data: String) {
        // 坐标点数据
        val chartData = arrayListOf<Entry>()
        // x轴数据
        var chartLabels = arrayListOf<String>()
        val strList = data.split(",").toList()
        strList.forEachIndexed { index, s ->
            if (!s.contentEquals("") && !s.contentEquals("0")) {
                chartData.add(Entry(index.toFloat(), s.toFloat()))
                chartLabels.add("X$index")
            }
        }
        val dataSet = LineDataSet(chartData, "Magnet")
        dataSet.circleRadius = 1.0f
        dataSet.valueTextSize = 0.0f
        dataSet.lineWidth = 1.0f
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    override fun onDestroy() {
        mReceiver?.let {
            unregisterReceiver(it)
        }
        super.onDestroy()
    }
}