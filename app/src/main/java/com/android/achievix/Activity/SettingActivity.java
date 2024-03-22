package com.android.achievix.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.achievix.R;
import com.android.achievix.Services.AdminReceiver;
import com.android.achievix.Services.ForegroundService;

public class SettingActivity extends AppCompatActivity {
    private ComponentName cn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cn = new ComponentName(this, AdminReceiver.class);

        ImageButton b2 = findViewById(R.id.back_settings);
        b2.setOnClickListener(view -> finish());
    }

    public void uninstallProtection(View v) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        startActivity(intent);
    }

    public void repair(View v) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service is Running");
        ContextCompat.startForegroundService(this, serviceIntent);
        Toast.makeText(this, "App Repaired", Toast.LENGTH_SHORT).show();
    }
}