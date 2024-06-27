package com.android.mindmaster.Activity;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mindmaster.Adapter.ProfileAdapter;
import com.android.mindmaster.Database.BlockDatabase;
import com.android.mindmaster.Model.ProfileModel;
import com.android.mindmaster.R;
import com.android.mindmaster.Service.ForegroundService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressLint({"NonConstantResourceId", "InlinedApi", "SetTextI18n"})
public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8;
    private Button navButton;
    private String greeting = "";
    private TextView mode, modeDesc;
    private ImageView strictLevelOne, strictLevelTwo;
    private TextView appBlockCount, webBlockCount, keywordBlockCount, internetBlockCount;

    private final Calendar c = Calendar.getInstance();
    private final int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

    private final BlockDatabase blockDatabase = new BlockDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat
            .checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        if (!isMyServiceRunning(ForegroundService.class)) {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Background Service is Running");
            ContextCompat.startForegroundService(this, serviceIntent);
        }

        initializeViews();
        initializeCount();
        setupListeners();
        initRecyclerView();
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

        appBlockCount = findViewById(R.id.main_app_blocks);
        webBlockCount = findViewById(R.id.main_site_blocks);
        keywordBlockCount = findViewById(R.id.main_keywords_blocks);
        internetBlockCount = findViewById(R.id.main_internet_blocks);

        TextView greet = findViewById(R.id.greet);
        greet.setText(getGreetings());
    }

    private void initializeCount() {
        appBlockCount.setText(String.valueOf(blockDatabase.getAppBlockCount()));
        webBlockCount.setText(String.valueOf(blockDatabase.getWebBlockCount()));
        keywordBlockCount.setText(String.valueOf(blockDatabase.getKeysBlockCount()));
        internetBlockCount.setText(String.valueOf(blockDatabase.getInternetBlockCount()));
    }

    private void setupListeners() {
        NavigationView navigationMenu = findViewById(R.id.nav_menu);
        navigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1 -> startActivity(new Intent(this, SettingActivity.class));
                case R.id.menu2 ->
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
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

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.profiles_list_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<HashMap<String, String>> list = blockDatabase.readAllProfiles();
        List<ProfileModel> profileModelList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            profileModelList.add(
                new ProfileModel(
                    Objects.requireNonNull(list.get(i).get("id")),
                    Objects.requireNonNull(list.get(i).get("profileName")),
                    Objects.requireNonNull(list.get(i).get("profileStatus"))
                )
            );
        }

        ProfileAdapter profileAdapter = new ProfileAdapter(profileModelList);
        recyclerView.setAdapter(profileAdapter);

        profileAdapter.setOnItemClickListener(view -> {
            SharedPreferences profileSh = getSharedPreferences("mode", MODE_PRIVATE);
            int level = profileSh.getInt("level", 0);

            if (level > 1) {
                int i = profileSh.getInt("password", 0);
                Intent intent = new Intent(this, EnterPasswordActivity.class);
                intent.putExtra("password", i);
                intent.putExtra("invokedFrom", "main");

                int position = recyclerView.getChildLayoutPosition(view);
                ProfileModel profileModel = profileAdapter.getItemAt(position);

                intent.putExtra("profileId", profileModel.getId());
                intent.putExtra("profileName", profileModel.getProfileName());
                startActivity(intent);
            } else {
                int position = recyclerView.getChildLayoutPosition(view);
                ProfileModel profileModel = profileAdapter.getItemAt(position);

                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                intent.putExtra("profileId", profileModel.getId());
                intent.putExtra("profileName", profileModel.getProfileName());
                startActivity(intent);
            }
        });
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
                Toast.makeText(
                    MainActivity.this, "Normal Mode is already enabled", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(
                    MainActivity.this, "Strict Mode is already enabled", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, StrictModeActivity.class);
                dialog.dismiss();
                startActivityForResult(intent, 100);
            }
        });

        dialog.show();
    }

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