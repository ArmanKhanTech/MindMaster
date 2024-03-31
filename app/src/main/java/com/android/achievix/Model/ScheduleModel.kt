package com.android.achievix.Model

data class ScheduleModel(
    val id: String,
    val name: String,
    val packageName: String,
    val type: String,
    val appLaunch: String,
    val notification: String,
    val scheduleType: String,
    val scheduleParams: String,
    val scheduleDays: String,
    val profileName: String,
    val profileStatus: Boolean,
    val text: String
)