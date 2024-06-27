package com.android.mindmaster.Model

import com.android.mindmaster.Utility.ItemStatus

data class TimeLineModel(
    val heading: String,
    var text: String,
    var status: ItemStatus
)