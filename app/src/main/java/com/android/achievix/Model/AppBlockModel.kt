package com.android.achievix.Model

import android.graphics.drawable.Drawable

data class AppBlockModel(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val extra1: String,
    val blocked: Boolean?,
    val extra2: String
)