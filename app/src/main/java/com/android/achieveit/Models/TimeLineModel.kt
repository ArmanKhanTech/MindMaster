package com.android.achieveit.Models

import com.android.achieveit.Utilities.ItemStatus

data class TimeLineModel(
    val heading: String,
    var text: String,
    var status: ItemStatus
)