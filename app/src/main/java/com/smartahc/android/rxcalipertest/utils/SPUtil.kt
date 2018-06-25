package com.smartahc.android.rxcalipertest.utils

import android.content.Context
import android.content.SharedPreferences
import com.smartahc.android.rxcalipertest.App
import com.smartahc.android.rxcalipertest.BuildConfig

/**
 * Created by Leero on 2018/5/8.
 * Desc: SP工具类
 */
class SPUtil {

    companion object {
        private var instance: SPUtil? = null
        private const val CACHE_NAME = BuildConfig.APPLICATION_ID

        private fun getSp(): SharedPreferences {
            if (instance == null) {
                synchronized(SPUtil::class) {
                    if (instance == null) {
                        instance = SPUtil()
                    }
                }
            }

            return App.instance.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)
        }

        fun putString(key: String, value: String) {
            val edit = getSp().edit()
            edit.putString(key, value)
            edit.apply()
        }

        fun takeString(key: String): String? {
            return getSp().getString(key, null)
        }
    }

}