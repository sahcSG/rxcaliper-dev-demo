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
        private val CACHE_NAME = BuildConfig.APPLICATION_ID

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

        fun putInt(key: String, value: Int) {
            val edit = getSp().edit()
            edit.putInt(key, value)
            edit.apply()
        }

        fun putFloat(key: String, value: Float) {
            val edit = getSp().edit()
            edit.putFloat(key, value)
            edit.apply()
        }

        fun putDouble(key: String, value: Double) {
            val edit = getSp().edit()
            edit.putString(key, value.toString())
            edit.apply()
        }

        fun putBoolean(key: String, value: Boolean) {
            val edit = getSp().edit()
            edit.putBoolean(key, value)
            edit.apply()
        }

        fun putLong(key: String, value: Long) {
            val edit = getSp().edit()
            edit.putLong(key, value)
            edit.apply()
        }

        fun remove(key: String) {
            val edit = getSp().edit()
            edit.remove(key)
            edit.apply()
        }

        fun removeAll() {
            val edit = getSp().edit()
            edit.clear()
            edit.apply()
        }

        fun takeString(key: String): String? {
            return getSp().getString(key, null)
        }

        fun takeInt(key: String): Int {
            return getSp().getInt(key, 0)
        }

        fun takeBoolean(key: String): Boolean {
            return getSp().getBoolean(key, false)
        }

        fun takeLong(key: String): Long {
            return getSp().getLong(key, 0)
        }

        fun takeFloat(key: String): Float {
            return getSp().getFloat(key, 0.0f)
        }

        fun takeDouble(key: String): Double {
            return java.lang.Double.valueOf(getSp().getString(key, "0.0"))!!
        }
    }

}