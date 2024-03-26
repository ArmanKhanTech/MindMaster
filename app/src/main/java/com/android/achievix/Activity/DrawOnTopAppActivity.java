package com.android.achievix.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.achievix.R;

import java.util.Objects;

public class DrawOnTopAppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_top_app);

        TextView tv = findViewById(R.id.app_block_app_name);
        ImageView appIcon = findViewById(R.id.app_block_app_icon);

        Button exit = findViewById(R.id.exit_app_block);

        PackageManager packageManager = getApplicationContext().getPackageManager();

        String appName;
        Drawable icon;
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(Objects.requireNonNull(getIntent().getStringExtra("packageName")), PackageManager.GET_META_DATA));
            icon = getPackageManager().getApplicationIcon(Objects.requireNonNull(getIntent().getStringExtra("packageName")));
            tv.setText(appName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }

        exit.setOnClickListener(v -> {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startMain);
            finish();
        });
    }
}
