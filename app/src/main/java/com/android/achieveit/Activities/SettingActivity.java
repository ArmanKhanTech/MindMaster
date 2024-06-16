package com.android.achieveit.Activities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.achieveit.R;
import com.android.achieveit.Services.AdminReceiver;
import com.android.achieveit.Services.BackgroundService;
import com.android.achieveit.Utilities.AccessibilityUtil;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void uninstallProtection(View v) {
        ComponentName cn = new ComponentName(this, AdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        startActivity(intent);
    }

    public void repair(View v) {
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service is Running");
        ContextCompat.startForegroundService(this, serviceIntent);
        Toast.makeText(this, "App repaired", Toast.LENGTH_SHORT).show();
    }

    public void repairLog(View v) {
        if (!new AccessibilityUtil().isAccessibilitySettingsOn(this)) {
            Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(i);
        } else {
            Toast.makeText(this, "Accessibility Service is already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void finish(View v) {
        finish();
    }
}