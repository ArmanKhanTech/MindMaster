package com.android.achievix.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Model.AppSelectModel
import com.android.achievix.R

class WebBlockAdapter(
    private var websiteKeywordList: List<AppSelectModel>,
    private var isWeb: Boolean
) :
    RecyclerView.Adapter<WebBlockAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val webName: TextView = view.findViewById(R.id.web_name)
        val blocked: ImageView = view.findViewById(R.id.web_action_block)
//        val icon: ImageView = view.findViewById(R.id.web_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_web_keyword, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = websiteKeywordList[position]

        holder.webName.text = appInfo.name
//        if (isWeb) {
//            holder.icon.setImageResource(R.drawable.web_icon)
//        } else {
//            holder.icon.setImageResource(R.drawable.keyword_icon)
//        }
    }

    override fun getItemCount() = websiteKeywordList.size
}