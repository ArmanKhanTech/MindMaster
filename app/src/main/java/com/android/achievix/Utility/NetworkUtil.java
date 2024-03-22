package com.android.achievix.Utility;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkUtil {
    public static Map<String, Long> getNetworkUsageStats(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connectivityManager.getAllNetworks();

        Map<String, Long> usageStats = new HashMap<>();

        for (ApplicationInfo applicationInfo : installedApplications) {
            int uid = applicationInfo.uid;
            String packageName = applicationInfo.packageName;

            long mobileDataUsage = 0L;
            long wifiDataUsage = 0L;

            for (Network network : networks) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    NetworkStats networkStats;
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        networkStats = networkStatsManager.queryDetailsForUid(
                                ConnectivityManager.TYPE_MOBILE,
                                packageName,
                                getTimesTamp(),
                                System.currentTimeMillis(),
                                uid);
                        mobileDataUsage += getNetworkUsage(networkStats);
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        networkStats = networkStatsManager.queryDetailsForUid(
                                ConnectivityManager.TYPE_WIFI,
                                packageName,
                                getTimesTamp(),
                                System.currentTimeMillis(),
                                uid);
                        wifiDataUsage += getNetworkUsage(networkStats);
                    }
                }
            }

            long totalDataUsageMB = (mobileDataUsage + wifiDataUsage) / (1024 * 1024); // Convert bytes to MB
            usageStats.put(packageName, totalDataUsageMB);
        }

        return usageStats;
    }

    private static long getTimesTamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private static long getNetworkUsage(NetworkStats networkStats) {
        long totalBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            totalBytes += bucket.getRxBytes() + bucket.getTxBytes();
        }
        return totalBytes;
    }
}