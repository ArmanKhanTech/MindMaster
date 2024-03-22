package com.android.achievix.Model

import com.android.achievix.Utility.ItemStatus

data class TimeLineModel(
    val heading: String,
    val text: String,
    val status: ItemStatus
)