package com.android.achievix.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Model.TimeLineModel
import com.android.achievix.R
import com.android.achievix.Utility.ItemStatus
import com.android.achievix.Utility.VectorDrawable
import com.github.vipulasri.timelineview.TimelineView

interface OnTimeClickListener {
    fun onTimeClick(position: Int)
}

class TimeLineAdapter(
    private val mFeedList: List<TimeLineModel>,
    private val onTimeClickListener: OnTimeClickListener
) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(
            layoutInflater.inflate(R.layout.item_timeline, parent, false),
            viewType
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val timeLineModel = mFeedList[position]

        when (timeLineModel.status) {
            ItemStatus.ACTIVE -> {
                setMarker(holder, R.drawable.marker_active_theme, R.color.skyBlue)
            }

            ItemStatus.COMPLETED -> {
                setMarker(holder, R.drawable.marker_completed_theme, R.color.grey)
            }
        }

        holder.heading.text = timeLineModel.heading
        holder.title.text = timeLineModel.text
        holder.cardView.setOnClickListener {
            onTimeClickListener.onTimeClick(position)
        }
    }

    private fun setMarker(holder: TimeLineViewHolder, drawableResId: Int, colorFilter: Int) {
        holder.timeline.marker = VectorDrawable
            .getDrawable(
                holder.itemView.context,
                drawableResId,
                ContextCompat.getColor(holder.itemView.context, colorFilter)
            )
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val timeline: TimelineView = itemView.findViewById(R.id.timeline_mode)
        val heading: TextView = itemView.findViewById(R.id.heading_timeline)
        val title: TextView = itemView.findViewById(R.id.text_timeline)

        init {
            timeline.initLine(viewType)
        }
    }
}