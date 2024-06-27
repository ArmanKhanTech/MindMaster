package com.android.mindmaster.Activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mindmaster.R;

import java.util.Objects;

@SuppressLint({"CustomSplashScreen", "SetTextI18n"})
public class DrawOnTopLaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_top_app);

        TextView title = findViewById(R.id.draw_block_title);
        TextView name = findViewById(R.id.draw_block_name);
        TextView text = findViewById(R.id.draw_motivational_text);
        ImageView icon = findViewById(R.id.draw_block_icon);
        Button exitButton = findViewById(R.id.draw_exit);

        if (getIntent().getStringExtra("text") != null &&
            !Objects.requireNonNull(getIntent().getStringExtra("text")).isEmpty()
        ) {
            text.setText(getIntent().getStringExtra("text"));
        } else {
            text.setText(R.string.motivational_text);
        }

        if (Objects.requireNonNull(getIntent().getStringExtra("type")).equals("app") ||
            Objects.requireNonNull(getIntent().getStringExtra("type")).equals("internet")
        ) {
            title.setText("App blocked by MindMaster");

            PackageManager packageManager = getApplicationContext().getPackageManager();
            try {
                String appName = (String) packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(
                        Objects.requireNonNull(getIntent().getStringExtra("packageName")),
                        PackageManager.GET_META_DATA
                    )
                );
                Drawable appIcon = getPackageManager()
                    .getApplicationIcon(Objects.requireNonNull(getIntent().getStringExtra("packageName")));
                name.setText(appName);
                icon.setImageDrawable(appIcon);
            } catch (PackageManager.NameNotFoundException ignored) {
            }

            exitButton.setOnClickListener(view -> {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startMain);
                finish();
            });
        } else if (Objects.requireNonNull(getIntent().getStringExtra("type")).equals("web")) {
            title.setText("Website blocked by MindMaster");

            name.setText(getIntent().getStringExtra("name"));
            icon.setImageResource(R.drawable.web_icon);

            exitButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setPackage(getIntent().getStringExtra("packageName"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    intent.setPackage(null);
                    startActivity(intent);
                }
                finish();
            });
        } else if (Objects.requireNonNull(getIntent().getStringExtra("type")).equals("key")) {
            title.setText("Keyword blocked by MindMaster");

            name.setText("'" + getIntent().getStringExtra("name") + "'");
            icon.setImageResource(R.drawable.keyword_icon);

            exitButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setPackage(getIntent().getStringExtra("packageName"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    intent.setPackage(null);
                    startActivity(intent);
                }
                finish();
            });
        }
    }
}
