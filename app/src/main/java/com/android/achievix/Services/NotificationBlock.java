package com.android.achievix.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.android.achievix.Activity.DrawOnTopScreenActivity;
import com.android.achievix.Database.LimitPackages;
import com.android.achievix.Database.RestrictPackages;

import java.util.ArrayList;

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
        boolean notificationAllowed = sh.getBoolean("notification", false);
        if (notificationAllowed) {
            cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}