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
        val extra: TextView = view.findViewById(R.id.app_info_internet)
        val blocked: ImageView = view.findViewById(R.id.internet_action_block)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_internet_block, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]

        holder.appName.text = appInfo.appName
        holder.appIcon.setImageDrawable(appInfo.icon)
        holder.extra.text = appInfo.extra + " MB"
        //holder.blocked.setImageDrawable(appInfo.blocked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListInternet(newList: MutableList<AppBlockModel>) {
        appList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = appList.size
}