package com.android.mindmaster.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Model.AppSelectModel
import com.android.mindmaster.R

class AppSelectAdapter(private var appList: List<AppSelectModel>) :
    RecyclerView.Adapter<AppSelectAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.app_name_select)
        val appIcon: ImageView = view.findViewById(R.id.app_icon_select)
        val checkBox: CheckBox = view.findViewById(R.id.app_select_cb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_app_select, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.appName.text = appInfo.name
        holder.appIcon.setImageDrawable(appInfo.icon)
        holder.checkBox.setOnCheckedChangeListener(null) // remove previous listener
        holder.checkBox.isChecked = appInfo.selected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            appInfo.selected = isChecked
            holder.checkBox.post { notifyItemChanged(position) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListSelect(newList: List<AppSelectModel>) {
        appList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = appList.size
}