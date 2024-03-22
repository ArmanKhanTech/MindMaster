package com.android.achievix.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Model.AppBlockModel
import com.android.achievix.R

class AppBlockAdapter(private var appList: List<AppBlockModel>) :
    RecyclerView.Adapter<AppBlockAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.app_name_block)
        val appIcon: ImageView = view.findViewById(R.id.app_icon_block)
        val extra1: TextView = view.findViewById(R.id.app_info_block)
        val blocked: ImageView = view.findViewById(R.id.app_action_block)
        val extra2: TextView = view.findViewById(R.id.app_text_block_extra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.app_list_block, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]

        holder.appName.text = appInfo.appName
        holder.appIcon.setImageDrawable(appInfo.icon)
        holder.extra1.text = appInfo.extra1.let { convertToHrsMins(it.toLong()) }
        holder.extra2.text = appInfo.extra2
        //holder.blocked.setImageDrawable(appInfo.blocked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListBlock(newList: List<AppBlockModel>) {
        appList = newList
        notifyDataSetChanged()
    }

    private fun convertToHrsMins(millis: Long): String {
        val hours = millis / 1000 / 60 / 60
        val minutes = millis / 1000 / 60 % 60
        return "$hours hrs $minutes mins"
    }

    override fun getItemCount() = appList.size
}