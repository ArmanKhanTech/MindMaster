package com.android.mindmaster.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.Model.ProfileModel
import com.android.mindmaster.R

class ProfileAdapter(private var profileList: List<ProfileModel>) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(view: View)
    }

    private var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileName: TextView = view.findViewById(R.id.profile_name)
        val profileAction: ImageView = view.findViewById(R.id.profile_action_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profileInfo = profileList[position]
        var status = profileInfo.status == "1"
        holder.profileName.text = profileInfo.profileName
        holder.profileAction.setImageResource(if (status) R.drawable.pause_icon_pink else R.drawable.play_icon_pink)
        holder.profileAction.setOnClickListener {
            BlockDatabase(holder.itemView.context).toggleProfile(profileInfo.profileName, status)
            status = !status
            profileInfo.status = if (status) "1" else "0"
            notifyItemChanged(position)
        }
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(it) }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun getItemCount() = profileList.size

    fun getItemAt(position: Int): ProfileModel {
        return profileList[position]
    }
}