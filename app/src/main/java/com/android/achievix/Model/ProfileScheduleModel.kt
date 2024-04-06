package com.android.achievix.Model

data class ProfileScheduleModel(
    val id: String,
    val type: String,
    val appLaunch: String,
    val notification: String,
    val scheduleType: String,
    val scheduleParams: String,
    val scheduleDays: String,
    val profileName: String,
    val profileStatus: String,
    val text: String
)