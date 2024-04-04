package com.android.achievix.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        SharedPreferences sh1 = getSharedPreferences("takeBreak", Context.MODE_PRIVATE);
        SharedPreferences sh2 = getSharedPreferences("notificationBlock", Context.MODE_PRIVATE);

        boolean takeBreak = sh1.getBoolean("notification", false);
        boolean notificationBlock = sh2.getBoolean("notification", false);

        if (notificationBlock || takeBreak) {
            cancelNotification(sbn.getKey());
            SharedPreferences.Editor editor = sh2.edit();
            editor.putBoolean("notification", false);
            editor.apply();
        }

        System.gc();
        Runtime.getRuntime().runFinalization();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}