package com.android.mindmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mindmaster.Permission.GetUsageStatsPermissionActivity;
import com.android.mindmaster.R;

import java.util.Objects;

@SuppressLint({"CustomSplashScreen", "InlinedApi"})
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sh1 = getSharedPreferences("mindmaster", MODE_PRIVATE);
        SharedPreferences sh2 = getSharedPreferences("mode", MODE_PRIVATE);

        Handler handler = new Handler();

        if (Objects.equals(sh1.getString("firstTime", "yes"), "no")) {
            handler.postDelayed(() -> {
                int i = sh2.getInt("password", 0);
                Intent intent;

                if (i != 0) {
                    intent = new Intent(this, EnterPasswordActivity.class);
                    intent.putExtra("password", i);
                    intent.putExtra("invokedFrom", "splashScreen");
                } else {
                    intent = new Intent(this, MainActivity.class);
                }

                startActivity(intent);
                finish();
            }, 1800);
        } else {
            handler.postDelayed(() -> {
                Intent intent =
                    new Intent(SplashScreenActivity.this, GetUsageStatsPermissionActivity.class);
                startActivity(intent);
                finish();
            }, 1800);
        }
    }
}
