package com.android.mindmaster.Model

import android.graphics.drawable.Drawable

data class AppBlockModel(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val extra: String,
    val blocked: Boolean?
)