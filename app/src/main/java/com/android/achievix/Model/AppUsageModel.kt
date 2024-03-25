package com.android.achievix.Model

import android.graphics.drawable.Drawable

data class AppUsageModel(
    val name: String,
    var packageName: String?,
    val icon: Drawable,
    var extra: String?,
    var progress: Double?
)