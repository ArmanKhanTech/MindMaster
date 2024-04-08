package com.android.achievix.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.achievix.R;
import com.android.achievix.Utility.NetworkUtil;
import com.android.achievix.Utility.UsageUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AppStatsActivity extends AppCompatActivity {

    private static final NetworkUtil networkUtil = new NetworkUtil();
    private static final UsageUtil usageUtil = new UsageUtil();
    private static String packageName = "";
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private String category;
    private int position;
    private float daily, weekly, monthly, yearly, data;
    private TextView t6, t21, t13, t14, xy1, xyz1, t18;
    private BarChart barChart;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_stats);

        context = getApplicationContext();

        position = getIntent().getIntExtra("position", 0);

        packageName = getIntent().getStringExtra("packageName");

        String appName = getIntent().getStringExtra("appName");
        TextView app_name = findViewById(R.id.app_name);
        app_name.setText(appName);

        barChart = findViewById(R.id.barChart_view);

        t6 = findViewById(R.id.textView6);
        t21 = findViewById(R.id.textView21);
        t13 = findViewById(R.id.textView13);
        t14 = findViewById(R.id.textView14);
        t18 = findViewById(R.id.textView18);
        xy1 = findViewById(R.id.xy1);
        xyz1 = findViewById(R.id.xyz1);
        layout = findViewById(R.id.ly1);

        ImageView appIcon = findViewById(R.id.app_icon);
        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        new loadData().execute();
    }

    public void loadGraph() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startMillis = calendar.getTimeInMillis();
        long endMillis = System.currentTimeMillis();

        for (int daysAgo = 0; daysAgo < 7; daysAgo++) {
            float usageTime = usageUtil.getUsageByPackageNameAndMillis(this, packageName, startMillis, endMillis);

            if (usageTime < (float) 0) {
                usageTime = 0;
            }

            entries.add(new BarEntry(usageTime, 7 - daysAgo - 1));
            long DAY_IN_MILLIS = 86400 * 1000;

            startMillis -= DAY_IN_MILLIS;
            endMillis -= DAY_IN_MILLIS;
        }

        ArrayList<String> labels = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);

        for (int i = 0; i < 7; i++) {
            labels.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }

        String title = "Weekly Usage Stats (in Hours)";

        BarDataSet barDataSet = new BarDataSet(entries, title);
        barDataSet.setColor(getResources().getColor(R.color.purple_500));

        BarData data = new BarData(labels, barDataSet);
        barChart.setData(data);
        barChart.setDescription(null);
        barChart.getData().setValueFormatter(new TimeFormatter());
        barChart.getXAxis().setLabelsToSkip(0);
        barChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
    }

    private void getStatsInfo(String pkgName) {
        daily = usageUtil.getUsageByPackageName(this, packageName, "Daily");
        weekly = usageUtil.getUsageByPackageName(this, packageName, "Weekly");
        monthly = usageUtil.getUsageByPackageName(this, packageName, "Monthly");
        yearly = usageUtil.getUsageByPackageName(this, packageName, "Yearly");
    }

    private void getCategoryTitle() {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = pm.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        int appCategory = Objects.requireNonNull(applicationInfo).category;
        category = (String) ApplicationInfo.getCategoryTitle(context, appCategory);
    }

    private static class TimeFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0) {
                return "0s";
            }

            String hour = String.valueOf(value);
            String time = "";
            time += hour + " hrs";
            return time;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class loadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getStatsInfo(packageName);
            getCategoryTitle();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void results) {
            super.onPostExecute(results);

            layout.setVisibility(View.GONE);
            t6.setText(String.valueOf(daily));
            t21.setText(String.valueOf(weekly));
            t13.setText(String.valueOf(monthly));
            t14.setText(String.valueOf(yearly));
            xyz1.setText(category);

            if (category == null) {
                xyz1.setText("Other");
            }

            xy1.setText(String.valueOf(position));
            if (position < 10) {
                String p = "0" + position;
                xy1.setText(p);
            }

            if (position > 99) {
                xy1.setText("99+");
                xy1.setTextSize(18);
            }

            t18.setText(String.valueOf(data));
            loadGraph();
        }
    }

    public void finish(View v) {
        finish();
    }
}
