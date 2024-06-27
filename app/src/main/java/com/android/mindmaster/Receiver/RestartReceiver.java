package com.android.mindmaster.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.mindmaster.Service.ForegroundService;

import java.util.Objects;

public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, ForegroundService.class));
        }
    }
}
