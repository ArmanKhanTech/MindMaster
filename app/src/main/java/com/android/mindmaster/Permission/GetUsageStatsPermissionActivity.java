package com.android.mindmaster.Permission;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mindmaster.R;
import com.android.mindmaster.View.ExpandableTextView;

@SuppressLint("SetTextI18n")
public class GetUsageStatsPermissionActivity extends AppCompatActivity {
    int mode;
    Button finishButton;
    TextView status;
    ExpandableTextView expandableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_usage_stats_permission);

        expandableTextView = findViewById(R.id.expandable_tv_usage);
        status = findViewById(R.id.usage_per);
        finishButton = findViewById(R.id.grant_usage_access);
        granted();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        granted();
    }

    public void granted() {
        if (checkForPermission(this)) {
            status.setText("Permission Granted");
            finishButton.setVisibility(View.GONE);
        } else {
            status.setText("Permission Refused");
            finishButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void toggle(View v) {
        expandableTextView.toggle();
    }

    public void getUsagePermission(View view) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    public void done(View view) {
        Intent intent = new Intent(GetUsageStatsPermissionActivity.this, GetDrawOverAppsPermission.class);
        startActivity(intent);
        finish();
    }
}