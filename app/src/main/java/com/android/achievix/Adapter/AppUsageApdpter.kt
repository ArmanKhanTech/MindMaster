package com.android.achievix.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Model.AppUsageModel
import com.android.achievix.R

class AppUsageAdapter(private var appList: List<AppUsageModel>) :
    RecyclerView.Adapter<AppUsageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.app_name)
        val appIcon: ImageView = view.findViewById(R.id.app_icon)
        val stats: TextView = view.findViewById(R.id.app_stats)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar_usage_stats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_app_usage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.appName.text = appInfo.name
        holder.appIcon.setImageDrawable(appInfo.icon)
        holder.stats.text = appInfo.extra?.let { convertToHrsMins(it.toLong()) }
        holder.progressBar.max = 100
        holder.progressBar.isIndeterminate = false
        holder.progressBar.progress = appInfo.progress?.toInt()!!
    }

    private fun convertToHrsMins(millis: Long): String {
        val hours = millis / 1000 / 60 / 60
        val minutes = millis / 1000 / 60 % 60
        return "$hours hrs $minutes mins"
    }

    override fun getItemCount() = appList.size
}