package com.smartahc.android.rxcalipertest.utils

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.smartahc.android.rxcalipertest.R

/**
 * Created by Leero on 2018/5/4.
 * Desc:
 */
class ToolbarUtils {

    companion object {
        val instance = ToolbarUtils()
    }

    fun setToolbar(activity: AppCompatActivity, title: String, back: Boolean = false) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        setToolbar(activity, toolbar, title, back)
    }

    private fun setToolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String, back: Boolean) {
        activity.setSupportActionBar(toolbar)
        val actionBar = activity.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = title
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (back) {
            toolbar.setNavigationOnClickListener { activity.onBackPressed() }
        } else {
            toolbar.navigationIcon = null
        }
    }
}