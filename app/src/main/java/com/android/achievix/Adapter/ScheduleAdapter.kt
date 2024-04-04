package com.android.achievix.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Activity.EditScheduleActivity
import com.android.achievix.Database.BlockDatabase
import com.android.achievix.Model.ScheduleModel
import com.android.achievix.R
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleAdapter(
    private var scheduleList: List<ScheduleModel>,
    private val activity: EditScheduleActivity
) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val daysActive: TextView = view.findViewById(R.id.days_active)
        val blockCondition: TextView = view.findViewById(R.id.block_condition)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_edit_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleInfo = scheduleList[position]

        holder.daysActive.text = scheduleInfo.scheduleDays.filter {
            it != '[' && it != ']'
        }

        val blockCondition = StringBuilder()
        when (scheduleInfo.scheduleType) {
            "Usage Time" -> {
                blockCondition.append("Usage Time: after ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(
                    refactorTime(
                        time[0] + " " + time[1],
                        "Usage Time"
                    ) + " hours"
                )
            }

            "Specific Time" -> {
                blockCondition.append("Specific Time: from ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(refactorTime(time[0] + " " + time[1], "Specific Time"))
                blockCondition.append(" to ")
                blockCondition.append(refactorTime(time[2] + " " + time[3], "Specific Time"))
            }

            "Quick Block" -> {
                blockCondition.append("Quick Block: until ")
                val time = scheduleInfo.scheduleParams.split(" ")
                blockCondition.append(refactorTime(time[0] + " " + time[1], "Quick Block"))
            }

            "Launch Count" -> {
                blockCondition.append("App Launch: after ")
                blockCondition.append(scheduleInfo.scheduleParams)
                blockCondition.append(" launches")
            }

            "Fixed Block" -> {
                blockCondition.append("Fixed Block")
            }

            "Block Data" -> {
                blockCondition.append("Block Data: after ")
                blockCondition.append(scheduleInfo.scheduleParams)
                blockCondition.append(" MB")
            }
        }
        holder.blockCondition.text = blockCondition.toString()

        holder.deleteButton.setOnClickListener {
            val blockDatabase = BlockDatabase(holder.itemView.context)
            blockDatabase.deleteRecordById(scheduleInfo.id)
            scheduleList = scheduleList.filter { it.id != scheduleInfo.id }
            notifyItemRemoved(position)
            Toast.makeText(holder.itemView.context, "Schedule deleted", Toast.LENGTH_SHORT).show()
            activity.updateNoScheduleVisibility()
        }
    }

    private fun refactorTime(time: String, type: String): String {
        val inputFormat = SimpleDateFormat("HH mm", Locale.getDefault())
        val outputFormat = if (type == "Usage Time") {
            SimpleDateFormat("hh:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("hh:mm a", Locale.getDefault())
        }
        val date = inputFormat.parse(time)
        return if (date != null) outputFormat.format(date) else time
    }

    override fun getItemCount() = scheduleList.size
}