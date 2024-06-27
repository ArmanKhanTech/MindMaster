package com.android.mindmaster.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Activity.EditProfileActivity
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.Model.ProfileScheduleModel
import com.android.mindmaster.R
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileScheduleAdapter(
    private var scheduleList: List<ProfileScheduleModel>,
    private val activity: EditProfileActivity
) : RecyclerView.Adapter<ProfileScheduleAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val daysActive: TextView = view.findViewById(R.id.days_active)
        val blockCondition: TextView = view.findViewById(R.id.block_condition)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_profile_schedule, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleInfo = scheduleList[position]

        if (scheduleInfo.scheduleDays.isNotEmpty()) {
            holder.daysActive.text = scheduleInfo.scheduleDays.filter {
                it != '[' && it != ']'
            }
        } else {
            holder.daysActive.text = "None"
        }

        val blockCondition = StringBuilder()
        when (scheduleInfo.scheduleType) {
            "Usage Time" -> {
                blockCondition.append("Usage Time: after ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(
                    time[0] + "." + time[1] + "hrs"
                )
            }

            "Specific Time" -> {
                blockCondition.append("Specific Time: from ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(refactorTime(time[0] + " " + time[1]))
                blockCondition.append(" to ")
                blockCondition.append(refactorTime(time[2] + " " + time[3]))
            }

            "Quick Block" -> {
                blockCondition.append("Quick Block: until ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(refactorTime(time[0] + " " + time[1]))
            }

            "Launch Count" -> {
                blockCondition.append("App Launch: after ")
                blockCondition.append(scheduleInfo.scheduleParams)
                blockCondition.append(" launches")
            }

            "Fixed Block" -> {
                blockCondition.append("Fixed Block")
            }
        }
        holder.blockCondition.text = blockCondition.toString()

        holder.deleteButton.setOnClickListener {
            val blockDatabase = BlockDatabase(holder.itemView.context)
            blockDatabase.deleteRecordById(scheduleInfo.id)
            blockDatabase.deleteProfileItems(
                scheduleInfo.profileName,
                scheduleInfo.scheduleType,
                scheduleInfo.scheduleParams,
                scheduleInfo.scheduleDays
            )
            scheduleList = scheduleList.filter { it.id != scheduleInfo.id }

            notifyItemRemoved(position)
            activity.initAppRecyclerView()
            activity.initWebRecyclerView()
            activity.initKeyRecyclerView()
            Toast.makeText(holder.itemView.context, "Schedule Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refactorTime(time: String): String {
        val inputFormat = SimpleDateFormat("HH mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(time)
        return if (date != null) outputFormat.format(date) else time
    }

    override fun getItemCount() = scheduleList.size
}