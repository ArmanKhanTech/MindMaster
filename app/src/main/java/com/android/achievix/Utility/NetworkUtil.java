package com.android.achievix.Utility;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NetworkUtil {
    public static float getUID(long startMillis, long endMillis, String packageName, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            return fetchNetworkStatsInfo(startMillis, endMillis, info.uid, context);
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static float fetchNetworkStatsInfo(long startMillis, long endMillis, int uid, Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);

        float totalWifi = getNetworkStatsTotal(networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, null, startMillis, endMillis, uid));
        float totalMobData = getNetworkStatsTotal(networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, null, startMillis, endMillis, uid));
        float total = (totalWifi + totalMobData) / (1024 * 1024);

        DecimalFormat df = new DecimalFormat("00000");
        df.setRoundingMode(RoundingMode.DOWN);
        return Float.parseFloat(df.format(total));
    }

    private static float getNetworkStatsTotal(NetworkStats networkStats) {
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        float total = 0;

        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            total += bucket.getRxBytes() + bucket.getTxBytes();
        }

        return total;
    }
}