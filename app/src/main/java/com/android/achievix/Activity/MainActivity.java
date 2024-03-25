package com.android.achievix.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.achievix.R;
import com.android.achievix.Services.ForegroundService;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    LinearLayout ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8;
    Button b;
    Calendar c = Calendar.getInstance();
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
    String greeting = "";

    @SuppressLint({"NonConstantResourceId", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        if (!isMyServiceRunning(ForegroundService.class)) {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service is Running");
            ContextCompat.startForegroundService(this, serviceIntent);
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationMenu = findViewById(R.id.nav_menu);

        b = findViewById(R.id.nav_button);
        b.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        ib1 = findViewById(R.id.goto_block_apps);
        ib2 = findViewById(R.id.goto_block_sites);
        ib3 = findViewById(R.id.goto_block_keywords);
        ib4 = findViewById(R.id.goto_block_internet);

        ib5 = findViewById(R.id.add_profile);

        ib6 = findViewById(R.id.mode_button);

        ib7 = findViewById(R.id.stats_button);

        ib8 = findViewById(R.id.take_a_break_button);

        TextView greet = findViewById(R.id.greet);
        greet.setText(getGreetings());

        navigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1 -> startActivity(new Intent(this, SettingActivity.class));
                case R.id.menu5 ->
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }

            return false;
        });

        ib1.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, AppBlockActivity.class);
            startActivity(intent);
        });

        ib2.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, WebBlockActivity.class);
            startActivity(intent);
        });

        ib3.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, KeywordBlockActivity.class);
            startActivity(intent);
        });

        ib4.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, InternetBlockActivity.class);
            startActivity(intent);
        });

        ib5.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, NewProfileActivity.class);
            startActivity(intent);
        });

        ib6.setOnClickListener(view12 -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.mode_dailog);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog
                            .getWindow())
                    .setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout normal = dialog.findViewById(R.id.mode_button_normal);
            LinearLayout strict = dialog.findViewById(R.id.mode_button_strict);

            normal.setOnClickListener(view1 -> {
                //
            });

            strict.setOnClickListener(view1 -> {
                Intent intent = new Intent(MainActivity.this, SelectStrictMode.class);
                dialog.dismiss();
                startActivity(intent);
            });

            dialog.show();
        });

        ib7.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, UsageOverviewActivity.class);
            startActivity(intent);
        });

        ib8.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, TakeBreakActivity.class);
            startActivity(intent);
        });
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String getGreetings() {
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting = "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greeting = "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greeting = "Good Evening";
        }

        return greeting;
    }
}