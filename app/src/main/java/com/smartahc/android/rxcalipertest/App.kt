package com.smartahc.android.rxcalipertest

import android.app.Application
import com.smartahc.android.smartble.config.Config

/**
 * Created by Leero on 2018/5/31.
 * Desc:
 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initConfig()
    }

    private fun initConfig() {
        Config.INSTANCE.appKey = "4f6d98ecfb9d06bb84e7b64337b299e2d0b1c82d"
        Config.INSTANCE.appID = 6056
    }
}