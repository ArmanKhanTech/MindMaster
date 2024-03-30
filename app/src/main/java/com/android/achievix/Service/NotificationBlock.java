package com.android.achievix.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationBlock extends NotificationListenerService {
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
        SharedPreferences sh = getSharedPreferences("takeBreak", Context.MODE_PRIVATE);
        boolean notificationBlocked = sh.getBoolean("notification", false);
        if (notificationBlocked) {
            cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}