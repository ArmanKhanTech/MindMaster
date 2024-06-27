package com.android.mindmaster.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Model.WebKeyModel
import com.android.mindmaster.R

class WebKeyBlockAdapter(
    private var websiteKeywordList: List<WebKeyModel>,
    private var isWeb: Boolean
) : RecyclerView.Adapter<WebKeyBlockAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(view: View)
    }

    private var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val webName: TextView = view.findViewById(R.id.web_name)
        val icon: ImageView = view.findViewById(R.id.list_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_web_keyword, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = websiteKeywordList[position]
        holder.webName.text = info.name

        if (isWeb) {
            holder.icon.setImageResource(R.drawable.web_icon)
        } else {
            holder.icon.setImageResource(R.drawable.keyword_icon)
        }

        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(it) }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListBlock(newList: List<WebKeyModel>) {
        websiteKeywordList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = websiteKeywordList.size

    fun getItemAt(position: Int): WebKeyModel {
        return websiteKeywordList[position]
    }
}