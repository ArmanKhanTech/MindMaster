package com.android.achievix.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.achievix.R;
import com.android.achievix.Service.ForegroundService;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8;
    private Button navButton;
    private final Calendar c = Calendar.getInstance();
    private final int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
    private String greeting = "";
    private TextView mode, modeDesc;
    private ImageView strictLevelOne, strictLevelTwo;

    @SuppressLint({"NonConstantResourceId", "InlinedApi", "SetTextI18n"})
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

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        mode = findViewById(R.id.main_mode);
        modeDesc = findViewById(R.id.main_mode_desc);
        strictLevelOne = findViewById(R.id.strictness_level_img_one);
        strictLevelTwo = findViewById(R.id.strictness_level_img_two);

        SharedPreferences sh = getSharedPreferences("mode", MODE_PRIVATE);
        boolean _strict = sh.getBoolean("strict", false);

        if (_strict) {
            mode.setText("Strict Mode");
            modeDesc.setText(R.string.strict_mode);
            strictLevelOne.setImageResource(R.drawable.lock_icon_red);
            strictLevelTwo.setImageResource(R.drawable.lock_icon_red);
        } else {
            mode.setText("Normal Mode");
            modeDesc.setText(R.string.normal_mode);
            strictLevelOne.setImageResource(R.drawable.lock_icon_red);
            strictLevelTwo.setImageResource(R.drawable.lock_icon_grey);
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navButton = findViewById(R.id.nav_button);

        ll1 = findViewById(R.id.goto_block_apps);
        ll2 = findViewById(R.id.goto_block_sites);
        ll3 = findViewById(R.id.goto_block_keywords);
        ll4 = findViewById(R.id.goto_block_internet);
        ll5 = findViewById(R.id.add_profile);
        ll6 = findViewById(R.id.mode_button);
        ll7 = findViewById(R.id.stats_button);
        ll8 = findViewById(R.id.take_a_break_button);

        TextView greet = findViewById(R.id.greet);
        greet.setText(getGreetings());
    }

    private void setupListeners() {
        NavigationView navigationMenu = findViewById(R.id.nav_menu);
        navigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1 -> startActivity(new Intent(this, SettingActivity.class));
                case R.id.menu5 -> startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
            return false;
        });

        navButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        ll1.setOnClickListener(view12 -> startActivity(new Intent(this, AppBlockActivity.class)));
        ll2.setOnClickListener(view12 -> startActivity(new Intent(this, WebBlockActivity.class)));
        ll3.setOnClickListener(view12 -> startActivity(new Intent(this, KeywordBlockActivity.class)));
        ll4.setOnClickListener(view12 -> startActivity(new Intent(this, InternetBlockActivity.class)));
        ll5.setOnClickListener(view12 -> startActivity(new Intent(this, NewProfileActivity.class)));
        ll6.setOnClickListener(view12 -> showModeDialog());
        ll7.setOnClickListener(view12 -> startActivity(new Intent(this, UsageOverviewActivity.class)));
        ll8.setOnClickListener(view12 -> startActivity(new Intent(this, TakeBreakActivity.class)));
    }

    private void showModeDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_mode);
        dialog.setCancelable(true);

        Objects.requireNonNull(dialog.getWindow())
                .setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout normal = dialog.findViewById(R.id.mode_button_normal);
        LinearLayout strict = dialog.findViewById(R.id.mode_button_strict);

        SharedPreferences shDialog = getSharedPreferences("mode", MODE_PRIVATE);
        boolean strictDialog = shDialog.getBoolean("strict", false);
        SharedPreferences.Editor editorDialog = shDialog.edit();

        normal.setOnClickListener(view1 -> {
            if(!strictDialog) {
                Toast.makeText(MainActivity.this, "Normal Mode is already enabled", Toast.LENGTH_SHORT).show();
            } else {
                editorDialog.putBoolean("strict", false);
                editorDialog.putInt("level", 0);
                editorDialog.putInt("password", 0);
                editorDialog.apply();
                updateMode(false);
                dialog.dismiss();
            }
        });

        strict.setOnClickListener(view1 -> {
            if(strictDialog) {
                Toast.makeText(MainActivity.this, "Strict Mode is already enabled", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, StrictModeActivity.class);
                dialog.dismiss();
                startActivityForResult(intent, 100);
            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateMode(boolean _strict) {
        if (_strict) {
            mode.setText("Strict Mode");
            modeDesc.setText(R.string.strict_mode);
            strictLevelOne.setImageResource(R.drawable.lock_icon_red);
            strictLevelTwo.setImageResource(R.drawable.lock_icon_red);
        } else {
            mode.setText("Normal Mode");
            modeDesc.setText(R.string.normal_mode);
            strictLevelOne.setImageResource(R.drawable.lock_icon_red);
            strictLevelTwo.setImageResource(R.drawable.lock_icon_grey);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                SharedPreferences sh = getSharedPreferences("mode", MODE_PRIVATE);
                boolean _strict = sh.getBoolean("strict", false);
                updateMode(_strict);
            }
        }
    }
}