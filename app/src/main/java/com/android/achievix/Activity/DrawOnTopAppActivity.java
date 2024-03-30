package com.android.achievix.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.achievix.R;

import java.util.Objects;

public class DrawOnTopAppActivity extends AppCompatActivity {
    TextView appName;
    ImageView appIcon;
    Button exitButton;

    String name;
    Drawable icon;

    PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_top_app);

        appName = findViewById(R.id.app_block_app_name);
        appIcon = findViewById(R.id.app_block_app_icon);
        exitButton = findViewById(R.id.exit_app_block);

        packageManager = getApplicationContext().getPackageManager();

        try {
            name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(Objects.requireNonNull(getIntent().getStringExtra("packageName")), PackageManager.GET_META_DATA));
            icon = getPackageManager().getApplicationIcon(Objects.requireNonNull(getIntent().getStringExtra("packageName")));
            appName.setText(name);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }

        exitButton.setOnClickListener(v -> {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startMain);
            finish();
        });
    }
}
