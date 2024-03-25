package com.android.achievix.Services;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.android.achievix.Database.LimitPackages;
import com.android.achievix.Database.RestrictPackages;

import java.util.ArrayList;

public class NotificationBlock extends NotificationListenerService {
    private LimitPackages limitPackagesDb;
    private RestrictPackages restrictPackagesDb;

    @Override
    public void onCreate() {
        super.onCreate();

        limitPackagesDb = new LimitPackages(this);
        restrictPackagesDb = new RestrictPackages(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        limitPackagesDb.close();
        restrictPackagesDb.close();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        ArrayList<String> limitPacks = limitPackagesDb.readLimitPacks();
        ArrayList<String> restrictPacks = restrictPackagesDb.readRestrictPacks();

        if (limitPacks.contains(sbn.getPackageName()) || restrictPacks.contains(sbn.getPackageName())) {
            cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}