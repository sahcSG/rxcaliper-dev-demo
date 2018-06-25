package com.smartahc.android.rxcalipertest.adapter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartahc.android.rxcalipertest.R
import kotlinx.android.synthetic.main.activity_bluetooth_scan_item.view.*


/**
 * Created by Leero on 2018/5/7.
 * Desc:
 */
class BluetoothScanAdapter(context: Context, list: ArrayList<BluetoothDevice>) : RecyclerView.Adapter<BluetoothScanAdapter.BluetoothHolder>(), View.OnClickListener {

    private lateinit var mItemClickListener: OnItemClickListener

    private var mContext: Context? = context
    private var mList: ArrayList<BluetoothDevice>? = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_bluetooth_scan_item, parent, false)
        view.setOnClickListener(this)
        return BluetoothHolder(view)
    }

    override fun onBindViewHolder(holder: BluetoothHolder, position: Int) {
        holder.name.text = "名称：" + mList?.get(position)?.name
        holder.address.text = "Mac地址：" + mList?.get(position)?.address
        holder.itemView.tag = position
    }

    override fun getItemCount(): Int {
        return mList?.size as Int
    }

    override fun onClick(v: View?) {
        if (mItemClickListener != null) {
            var position = v!!.tag as Int

            mItemClickListener.onItemClick(position, mList?.get(position)?.name ?: "不知名设备")
        }
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        mItemClickListener = itemClickListener
    }

    class BluetoothHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.tvName as TextView
        var address: TextView = view.tvMacAdr as TextView

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, name: String)
    }
}