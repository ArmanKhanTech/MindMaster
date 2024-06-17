package com.android.achieveit.Model

import com.android.achieveit.Utility.ItemStatus

data class TimeLineModel(
    val heading: String,
    var text: String,
    var status: ItemStatus
)