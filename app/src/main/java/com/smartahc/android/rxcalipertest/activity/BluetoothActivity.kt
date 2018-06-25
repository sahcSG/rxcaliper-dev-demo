package com.smartahc.android.rxcalipertest.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.smartahc.android.rxcalipertest.R
import com.smartahc.android.rxcalipertest.utils.Constant
import com.smartahc.android.rxcalipertest.utils.SPUtil
import com.smartahc.android.rxcalipertest.utils.ToolbarUtils
import com.smartahc.android.smartble.service.BleService
import com.smartahc.android.smartble.service.LocalBinder
import kotlinx.android.synthetic.main.activity_bluetooth.*

/**
 * Created by Leero on 2018/5/31.
 * Desc:
 */
class BluetoothActivity : AppCompatActivity(), View.OnClickListener {

    private val ACCESS_LOCATION = 101 // 定位权限请求码
    private var currentTime: Long = 0

    // 服务相关
    private var mService: BleService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = (service as LocalBinder).service
            mService?.let {
                if (!it.isBleEnable) {
                    Toast.makeText(this@BluetoothActivity, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!it.isBleStart) {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        startService(Intent(this, BleService::class.java))
        bindService(Intent(this, BleService::class.java), connection, Context.BIND_AUTO_CREATE)

        getPermission()
        SPUtil.putString(Constant.BLE_NAME, "")
        SPUtil.putString(Constant.BLE_MAC, "")

        ToolbarUtils.instance.setToolbar(this, "蓝牙测试", false)
        tvScan.setOnClickListener(this)
        tvStop.setOnClickListener(this)
        llCurrentBle.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        tvBleName.text = SPUtil.takeString(Constant.BLE_NAME)
        tvBleMac.text = SPUtil.takeString(Constant.BLE_MAC)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvScan -> {
                startActivity(Intent(this, BluetoothScanActivity::class.java))
            }
            R.id.tvStop -> {
                mService?.let {
                    if (it.disConnect()) {
                        tvBleName.text = ""
                        tvBleMac.text = ""

                        SPUtil.putString(Constant.BLE_NAME, "")
                        SPUtil.putString(Constant.BLE_MAC, "")

                        Toast.makeText(this, "蓝牙连接已断开", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.llCurrentBle -> {
                if (!TextUtils.isEmpty(tvBleName.text.toString())) {
                    startActivity(Intent(this, CaliperDetailActivity::class.java))
                } else {
                    startActivity(Intent(this, BluetoothScanActivity::class.java))
                }
            }
        }
    }

    /**
     * 权限请求
     */
    @SuppressLint("WrongConstant")
    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var permissionCheck = 0
            permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionCheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

            // 需要请求权限
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(kotlin.arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), ACCESS_LOCATION)
            } else {
                return
            }
        }
    }

    private fun hasAllPermissionGranted(grantResults: IntArray): Boolean {
        return !grantResults.contains(PackageManager.PERMISSION_DENIED)
    }

    /**
     * 权限请求回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_LOCATION -> {
                if (hasAllPermissionGranted(grantResults)) {
                    Log.d("BluetoothActivity", "onRequestPermissionsResult: OK")
                } else {
                    Toast.makeText(this, "请打开定位设置", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > currentTime) {
            Toast.makeText(this, "再按一次即可退出", Toast.LENGTH_SHORT).show()
        } else {
            finish()
            System.exit(0)
        }
        currentTime = System.currentTimeMillis() + 2000
    }

    override fun onDestroy() {
        unbindService(connection)
        stopService(Intent(this, BleService::class.java))

        super.onDestroy()
    }
}