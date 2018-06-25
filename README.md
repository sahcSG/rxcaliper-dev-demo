# RxCaliper dev demo (kotlin)
睿畜卡钳SDK开发demo

### 简介
睿畜卡钳SDK提供蓝牙搜索、连接（断开连接）以及睿畜卡钳耳标数据读取等功能，本文将对SDK使用的关键地方进行说明，详情可以参考demo源码。

开发环境：Android Studio 3.0 </br>
Android版本支持：5.0以上

### 权限声明
睿畜卡钳SDK是基于BLE的开发支持，需要声明的权限包含蓝牙权限、位置权限以及网络权限，而位置权限属于隐私权限，对于6.0及以上版本需要动态申请。
```
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET"/>
```

### 服务声明
在使用SDK的相关功能之前请先确保启动BleService。
```
<service android:name="com.smartahc.android.smartble.service.BleService" />
```

### 设置AppKey和AppID
在自定义的Application的onCreate()方法中，把AppKey和AppID赋值到Config的对应变量中。
```
private fun initConfig() {
    Config.INSTANCE.appKey = "4f6d98ecfb9d06bb84e7b64337b299e2d0b1c82d"
    Config.INSTANCE.appID = 6056
}
```

### 功能说明

* **蓝牙设备搜索** </br>
调用BleService的search()方法可以进行BLE设备搜索，当搜索到BLE设备时SDK会发出值为 **Constant.BLE_MESSAGE_DEVICE_FOUND** 的广播，广播的Intent中会携带包含蓝牙信息的 **BluetoothDevice** 对象，对应的key值为 **Constant.BLE_DEVICE_DATA**，通过该对象可以获取搜索到的蓝牙设备的名字、Mac地址等信息。

* **连接蓝牙设备** </br>
调用BleService的connect()方法，传入对应的蓝牙名字和Mac地址，可以与该设备建立连接，连接建立成功时，会有 **Constant.BLE_MESSAGE_DEVICE_CONNECT** 广播发出，而断开连接或者连接失败时会发出 **Constant.BLE_MESSAGE_DEVICE_DISCONNECT** 广播。

* **耳标数据获取** </br>
当SDk接收到睿畜卡钳返回的耳标数据后，经解析会发出 **Constant.BLE_EAR_TAG_PARSE_MSG** 广播，监听该广播可以在Intent中获取key值为**Constant.BLE_EAR_TAG_DATA**的String类型的耳标数据。
