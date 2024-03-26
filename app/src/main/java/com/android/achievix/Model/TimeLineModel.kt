package com.android.achievix.Model

import com.android.achievix.Utility.ItemStatus

data class TimeLineModel(
    val heading: String,
    var text: String,
    var status: ItemStatus
)