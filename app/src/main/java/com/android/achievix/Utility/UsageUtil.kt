package com.android.achievix.Utility

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.android.achievix.Model.AppBlockModel
import com.android.achievix.Model.AppSelectModel
import com.android.achievix.Model.AppUsageModel
import java.io.ByteArrayOutputStream
import java.util.Calendar

class UsageUtil {
    companion object {
        @JvmField
        var totalUsage: Long = 0

        @SuppressLint("ServiceCast", "QueryPermissionsNeeded", "UseCompatLoadingForDrawables")
        fun getInstalledAppsUsage(
            context: Context,
            sort: String?,
            packageNameStats: String? = null
        ): List<AppUsageModel> {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val calendar = Calendar.getInstance()
            when (sort) {
                "Daily" -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                }

                "Weekly" -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -7)
                }

                "Monthly" -> {
                    calendar.add(Calendar.MONTH, -1)
                }

                "Yearly" -> {
                    calendar.add(Calendar.YEAR, -1)
                }
            }

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()

            val events = usageStatsManager.queryAndAggregateUsageStats(
                beginTime,
                endTime
            )

            val usageTimes: MutableMap<String, Long> = HashMap()

            for (packageName in events.keys) {
                val stats = events[packageName]
                val usageTime = stats?.totalTimeInForeground
                if (usageTime != null) {
                    usageTimes[packageName] = usageTime
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
                        val bitmapData = stream.toByteArray()
                        icon = BitmapDrawable(
                            context.resources,
                            BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
                        )
                        appUsageModels.add(
                            AppUsageModel(
                                appName,
                                app.packageName,
                                icon,
                                usageTime.toString(),
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
            calendar.add(Calendar.DAY_OF_YEAR, -1)

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()

            val events = usageStatsManager.queryAndAggregateUsageStats(
                beginTime,
                endTime
            )

            val usageTimes: MutableMap<String, Long> = HashMap()
            for (packageName in events.keys) {
                val stats = events[packageName]
                val usageTime = stats?.totalTimeInForeground
                if (usageTime != null) {
                    usageTimes[packageName] = usageTime
                }
            }

            val networkUsageMap: HashMap<String, Float> = HashMap()
            if (caller == "InternetBlockActivity") {
                for (app in apps) {
                    if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0 || app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                        networkUsageMap[app.packageName] = NetworkUtil.getUID(
                            System.currentTimeMillis() - 86400000,
                            System.currentTimeMillis(),
                            app.packageName,
                            context
                        )
                    }
                }
            }

            return apps.mapNotNull { app ->
                if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                    val appName = app.loadLabel(pm).toString()
                    var icon = app.loadIcon(pm)
                    val bitmap: Bitmap
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
                    val bitmapData = stream.toByteArray()
                    icon = BitmapDrawable(
                        context.resources,
                        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
                    )
                    when (caller) {
                        "InternetBlockActivity" ->
                            AppBlockModel(
                                appName,
                                app.packageName,
                                icon,
                                networkUsageMap[app.packageName].toString(),
                                false,
                            )

                        "AppBlockActivity" ->
                            AppBlockModel(
                                appName,
                                app.packageName,
                                icon,
                                usageTime.toString(),
                                false
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
                "Usage" -> this.sortedByDescending {
                    it.extra.toDouble()
                }
                else -> this.sortedBy { it.appName }
            }
        }

        @SuppressLint("ServiceCast", "QueryPermissionsNeeded")
        fun getInstalledAppsForSelection(context: Context): List<AppSelectModel> {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()

            val events = usageStatsManager.queryAndAggregateUsageStats(
                beginTime,
                endTime
            )

            val usageTimes: MutableMap<String, Long> = HashMap()
            for (packageName in events.keys) {
                val stats = events[packageName]
                val usageTime = stats?.totalTimeInForeground
                if (usageTime != null) {
                    usageTimes[packageName] = usageTime
                }
            }

            return apps.mapNotNull { app ->
                if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                    val appName = app.loadLabel(pm).toString()
                    var icon = app.loadIcon(pm)
                    val bitmap: Bitmap
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
                    val bitmapData = stream.toByteArray()
                    icon = BitmapDrawable(
                        context.resources,
                        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
                    )
                    AppSelectModel(
                        appName,
                        app.packageName,
                        icon,
                        usageTime.toString()
                    )
                } else {
                    null
                }
            }.sortedBy { it.name }
        }
    }
}