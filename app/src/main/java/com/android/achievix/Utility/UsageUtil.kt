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
import android.graphics.drawable.Drawable
import com.android.achievix.Database.BlockDatabase
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
        fun getInstalledAppsUsage(context: Context, sort: String?): List<AppUsageModel> {
            totalUsage = 0
            val usageTimes = getUsageTimes(context, sort)
            val totalUsageTime = usageTimes.values.sum()

            return getAppsList(context).mapNotNull { app ->
                val usageTime = usageTimes.getOrDefault(app.packageName, 0L)

                if (usageTime != 0L) {
                    totalUsage += usageTime
                    val usagePercentage = usageTime.toDouble() / totalUsageTime * 100
                    val appName = app.loadLabel(context.packageManager).toString()
                    val icon = getIcon(context, app)
                    AppUsageModel(appName, app.packageName, icon, usageTime.toString(), usagePercentage)
                } else {
                    null
                }
            }.sortedByDescending { it.progress ?: Double.MIN_VALUE }
        }

        fun getInstalledAppsSelect(context: Context): List<AppSelectModel> {
            return getAppsList(context).map { app ->
                val appName = app.loadLabel(context.packageManager).toString()
                val icon = getIcon(context, app)
                AppSelectModel(appName, app.packageName, icon, false)
            }
        }

        @SuppressLint("ServiceCast", "QueryPermissionsNeeded", "UseCompatLoadingForDrawables")
        fun getInstalledAppsBlock(context: Context, sort: String, caller: String): List<AppBlockModel> {
            val usageTimes = getUsageTimes(context, "Daily")
            val networkUsageMap = getNetworkUsageMap(context, caller)

            return getAppsList(context).mapNotNull { app ->
                val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                val appName = app.loadLabel(context.packageManager).toString()
                val icon = getIcon(context, app)

                when (caller) {
                    "InternetBlockActivity" -> {
                        val blocked = BlockDatabase(context).isInternetBlocked(app.packageName)
                        AppBlockModel(
                            appName,
                            app.packageName,
                            icon,
                            networkUsageMap[app.packageName].toString(),
                            blocked
                        )
                    }
                    "AppBlockActivity" -> {
                        val blocked = BlockDatabase(context).isAppBlocked(app.packageName)
                        AppBlockModel(appName, app.packageName, icon, usageTime.toString(), blocked)
                    }
                    else -> null
                }
            }.sort(sort)
        }

        private fun getUsageTimes(context: Context, sort: String?): MutableMap<String, Long> {
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val (beginTime, endTime) = getTimeRange(sort)
            val events = usageStatsManager.queryAndAggregateUsageStats(beginTime, endTime)
            val usageTimes: MutableMap<String, Long> = HashMap()

            for (packageName in events.keys) {
                val stats = events[packageName]
                val usageTime = stats?.totalTimeInForeground

                if (usageTime != null) {
                    usageTimes[packageName] = usageTime
                }
            }
            return usageTimes
        }

        private fun getTimeRange(sort: String?): Pair<Long, Long> {
            val calendar = Calendar.getInstance()

            when (sort) {
                "Daily" -> calendar.add(Calendar.DAY_OF_YEAR, -1)
                "Weekly" -> calendar.add(Calendar.DAY_OF_YEAR, -7)
                "Monthly" -> calendar.add(Calendar.MONTH, -1)
                "Yearly" -> calendar.add(Calendar.YEAR, -1)
            }

            val beginTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()
            return Pair(beginTime, endTime)
        }

        @SuppressLint("QueryPermissionsNeeded")
        private fun getAppsList(context: Context): List<ApplicationInfo> {
            val applicationInfo =  context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            return applicationInfo.filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0 }
        }

        private fun getIcon(context: Context, app: ApplicationInfo): Drawable {
            val icon = app.loadIcon(context.packageManager)
            val bitmap: Bitmap = if (icon is BitmapDrawable) {
                icon.bitmap
            } else {
                if (icon.intrinsicWidth <= 0 || icon.intrinsicHeight <= 0) {
                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                } else {
                    Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888)
                }.also {
                    val canvas = Canvas(it)
                    icon.setBounds(0, 0, canvas.width, canvas.height)
                    icon.draw(canvas)
                }
            }

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
            val bitmapData = stream.toByteArray()
            return BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size))
        }

        private fun getNetworkUsageMap(context: Context, caller: String): HashMap<String, Float> {
            val networkUsageMap: HashMap<String, Float> = HashMap()

            if (caller == "InternetBlockActivity") {
                for (app in getAppsList(context)) {
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

            return networkUsageMap
        }

        private fun List<AppBlockModel>.sort(sort: String): List<AppBlockModel> {
            return when (sort) {
                "Name" -> this.sortedBy { it.appName }
                "Usage" -> this.sortedByDescending { it.extra.toDouble() }
                "Blocked" -> this.sortedByDescending { it.blocked }
                else -> this.sortedBy { it.appName }
            }
        }
    }
}