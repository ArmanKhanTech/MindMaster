package com.android.mindmaster.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Activity.EditProfileActivity
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.Model.ProfileItemModel
import com.android.mindmaster.R

class ProfileItemAdapter(
    private var itemList: List<ProfileItemModel>,
    private val activity: EditProfileActivity
) : RecyclerView.Adapter<ProfileItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name_profile_block_list)
        val icon: ImageView = view.findViewById(R.id.icon_profile_block_list)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_profile_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemInfo = itemList[position]
        holder.name.text = itemInfo.name

        when (itemInfo.type) {
            "app" -> holder.icon.setImageResource(R.drawable.app_icon_profile)
            "web" -> holder.icon.setImageResource(R.drawable.web_icon)
            "key" -> holder.icon.setImageResource(R.drawable.keyword_icon)
        }

        holder.deleteButton.setOnClickListener {
            val blockDatabase = BlockDatabase(holder.itemView.context)
            blockDatabase.deleteProfileItem(itemInfo.profileName, itemInfo.name)
            itemList = itemList.filter { it.id != itemInfo.id }
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = itemList.size
}