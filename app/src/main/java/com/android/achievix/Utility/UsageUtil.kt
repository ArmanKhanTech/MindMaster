package com.android.achievix.Utility

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.android.achievix.Model.AppBlockModel
import com.android.achievix.Model.AppUsageModel
import java.io.ByteArrayOutputStream
import java.util.Calendar

class UsageUtil {
    companion object {
        @JvmField
        var totalUsage: Long = 0

        @SuppressLint("ServiceCast", "QueryPermissionsNeeded", "UseCompatLoadingForDrawables")
        fun getInstalledAppsUsage(context: Context, sort: String?): List<AppUsageModel> {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val calendar = Calendar.getInstance()
            when (sort) {
                "Daily" -> calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    0,
                    0,
                    0
                )

                "Weekly" -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -7)
                    calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        0,
                        0,
                        0
                    )
                }

                "Monthly" -> calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    1,
                    0,
                    0,
                    0
                )

                "Yearly" -> calendar.set(
                    calendar.get(Calendar.YEAR) - 1,
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    0,
                    0,
                    0
                )

                else -> calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    0,
                    0,
                    0
                )
            }

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()

            val events = usageStatsManager.queryEvents(beginTime, endTime)
            val event = UsageEvents.Event()

            val usageTimes: MutableMap<String, Long> = HashMap()

            var lastEventTime = 0L
            var lastPackageName = ""

            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    lastEventTime = event.timeStamp
                    lastPackageName = event.packageName
                } else if (event.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                    if (event.packageName == lastPackageName) {
                        val usageTime = usageTimes.getOrDefault(lastPackageName, 0L)
                        usageTimes[lastPackageName] = usageTime + event.timeStamp - lastEventTime
                    }
                }
            }

            var totalUsageTime = 0L
            totalUsage = 0
            for (usageTime in usageTimes.values) {
                totalUsageTime += usageTime
            }

            val appUsageModels: MutableList<AppUsageModel> = ArrayList()
            for (app in apps) {
                if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0 || app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                    val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                    if (usageTime != 0L) {
                        totalUsage += usageTime
                        val usagePercentage = usageTime.toDouble() / totalUsageTime * 100
                        val appName = app.loadLabel(pm).toString()
                        var icon = app.loadIcon(pm)
                        var bitmap: Bitmap
                        if (icon is BitmapDrawable) {
                            bitmap = icon.bitmap
                        } else {
                            bitmap = if (icon.intrinsicWidth <= 0 || icon.intrinsicHeight <= 0) {
                                Bitmap.createBitmap(
                                    1,
                                    1,
                                    Bitmap.Config.ARGB_8888
                                )
                            } else {
                                Bitmap.createBitmap(
                                    icon.intrinsicWidth,
                                    icon.intrinsicHeight,
                                    Bitmap.Config.ARGB_8888
                                )
                            }
                            val canvas = Canvas(bitmap)
                            icon.setBounds(0, 0, canvas.width, canvas.height)
                            icon.draw(canvas)
                        }
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
                        val bitmapdata = stream.toByteArray()
                        icon = BitmapDrawable(
                            context.resources,
                            BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)
                        )
                        appUsageModels.add(
                            AppUsageModel(
                                appName,
                                app.packageName,
                                icon,
                                java.lang.Long.toString(usageTime),
                                usagePercentage
                            )
                        )
                    }
                }
            }
            appUsageModels.sortWith(compareByDescending { it.progress ?: Double.MIN_VALUE })
            return appUsageModels
        }

        @SuppressLint("ServiceCast", "QueryPermissionsNeeded", "UseCompatLoadingForDrawables")
        fun getInstalledApps(context: Context, sort: String, caller: String): List<AppBlockModel> {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()

            val events = usageStatsManager.queryEvents(beginTime, endTime)
            val event = UsageEvents.Event()

            val usageTimes = mutableMapOf<String, Long>()
            var lastEventTime = 0L
            var lastPackageName = ""

            while (events.hasNextEvent()) {
                events.getNextEvent(event)

                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    lastEventTime = event.timeStamp
                    lastPackageName = event.packageName
                } else if (event.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                    if (event.packageName == lastPackageName) {
                        val usageTime = usageTimes.getOrDefault(lastPackageName, 0L)
                        usageTimes[lastPackageName] = usageTime + event.timeStamp - lastEventTime
                    }
                }
            }

            var networkUsageMap: Map<String, Long>? = null
            if (caller == "InternetBlockActivity" && sort == "Usage") {
                networkUsageMap = NetworkUtil.getNetworkUsageStats(context)
            }

            return apps.mapNotNull { app ->
                if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                    val appName = app.loadLabel(pm).toString()
                    var icon = app.loadIcon(pm)
                    var bitmap: Bitmap
                    if (icon is BitmapDrawable) {
                        bitmap = icon.bitmap
                    } else {
                        bitmap = if (icon.intrinsicWidth <= 0 || icon.intrinsicHeight <= 0) {
                            Bitmap.createBitmap(
                                1,
                                1,
                                Bitmap.Config.ARGB_8888
                            )
                        } else {
                            Bitmap.createBitmap(
                                icon.intrinsicWidth,
                                icon.intrinsicHeight,
                                Bitmap.Config.ARGB_8888
                            )
                        }
                        val canvas = Canvas(bitmap)
                        icon.setBounds(0, 0, canvas.width, canvas.height)
                        icon.draw(canvas)
                    }
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
                    val bitmapdata = stream.toByteArray()
                    icon = BitmapDrawable(
                        context.resources,
                        BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)
                    )
                    when (caller) {
//                        "InternetBlockActivity" ->
//                            AppBlockList(
//                                appName,
//                                app.packageName,
//                                icon,
//                                networkUsageMap[app.packageName].toString(),
//                                false,
//                                "Extra",
//                                getDrawable(context, R.drawable.lock_icon_red)
//                            )

                        "AppBlockActivity" ->
                            AppBlockModel(
                                appName,
                                app.packageName,
                                icon,
                                usageTime.toString(),
                                false,
                                "Extra",
                            )

                        else -> {
                            null
                        }
                    }
                } else {
                    null
                }
            }.sort(sort)
        }

        private fun List<AppBlockModel>.sort(sort: String): List<AppBlockModel> {
            return when (sort) {
                "Name" -> this.sortedBy { it.appName }
                "Usage" -> this.sortedByDescending { it.extra1.toLong() }
                else -> this.sortedBy { it.appName }
            }
        }
    }
}