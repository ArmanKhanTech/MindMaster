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

class InternetBlockAdapter(private var appList: List<AppBlockModel>) :
    RecyclerView.Adapter<InternetBlockAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.app_name_internet)
        val appIcon: ImageView = view.findViewById(R.id.app_icon_internet)
        val extra1: TextView = view.findViewById(R.id.app_info_internet)
        val blocked: ImageView = view.findViewById(R.id.internet_action_block)
        val extra2: TextView = view.findViewById(R.id.internet_text_block_extra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.internet_block_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]

        holder.appName.text = appInfo.appName
        holder.appIcon.setImageDrawable(appInfo.icon)
        holder.extra1.text = appInfo.extra1.let { convertToMegabytes(it.toLong()) }
        holder.extra2.text = appInfo.extra2
        //holder.blocked.setImageDrawable(appInfo.blocked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListInternet(newList: MutableList<AppBlockModel>) {
        appList = newList
        notifyDataSetChanged()
    }

    private fun convertToMegabytes(bytes: Long): String {
        val megabytes = bytes / 1000 / 1000
        return "$megabytes MB"
    }

    override fun getItemCount() = appList.size
}