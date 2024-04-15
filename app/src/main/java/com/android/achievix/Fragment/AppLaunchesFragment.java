package com.android.achievix.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.achievix.Activity.AppInsightsActivity;
import com.android.achievix.Adapter.AppLaunchAdapter;
import com.android.achievix.Database.AppLaunchDatabase;
import com.android.achievix.Model.AppUsageModel;
import com.android.achievix.R;
import com.android.achievix.Utility.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AppLaunchesFragment extends Fragment {
    private RecyclerView recyclerView;
    private AppLaunchAdapter appLaunchAdapter;
    private TextView launchStats;
    private LinearLayout launchLayout;
    private LinearLayout loadingLayout;
    private Spinner sortSpinner;
    private HashMap<String, Integer> appLaunchCount;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final String date = sdf.format(new Date());
    private String startDate = "";
    private String sortValue = "Daily";
    private final String[] sort = {"Daily", "Weekly", "Monthly", "Yearly"};
    private final List<AppUsageModel> appLaunchModel = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();
    private int totalCount = 0;
    private GetAppLaunchCountTask getAppLaunchCountTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_launches, container, false);

        initializeViews(view);
        setupSpinner();
        setupRecyclerView();

        getAppLaunchCountTask = new GetAppLaunchCountTask(requireActivity(), sortValue);
        getAppLaunchCountTask.execute();
        return view;
    }

    private void initializeViews(View view) {
        sortSpinner = view.findViewById(R.id.app_launch_spinner);
        loadingLayout = view.findViewById(R.id.loading_app_launches);
        launchLayout = view.findViewById(R.id.ll_app_launches);
        launchStats = view.findViewById(R.id.tv_app_launches_count);
        recyclerView = view.findViewById(R.id.recycler_view_app_launches);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown_item, sort);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstTime) {
                    sortValue = parent.getItemAtPosition(position).toString();
                    new GetAppLaunchCountTask(requireActivity(), sortValue).execute();
                }
                isFirstTime = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getAppLaunchCountTask != null) {
            getAppLaunchCountTask.cancel(true);
        }
    }

    /** @noinspection DataFlowIssue*/
    @SuppressLint("StaticFieldLeak")
    public class GetAppLaunchCountTask extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final String sort;

        public GetAppLaunchCountTask(Context context, String sort) {
            this.context = context;
            this.sort = sort;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingLayout.setVisibility(View.VISIBLE);
            launchLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchAppLaunchData();
            return null;
        }

        private void fetchAppLaunchData() {
            try (AppLaunchDatabase appLaunchDatabase = new AppLaunchDatabase(context)) {
                switch (sort) {
                    case "Daily":
                        appLaunchCount = appLaunchDatabase.getDailyLaunchCount(date);
                        break;
                    case "Weekly":
                        adjustCalendar(Calendar.DAY_OF_MONTH, -30);
                        appLaunchCount = appLaunchDatabase.getWeeklyLaunchCount(startDate, date);
                        break;
                    case "Monthly":
                        adjustCalendar(Calendar.MONTH, -1);
                        appLaunchCount = appLaunchDatabase.getMonthlyLaunchCount(startDate, date);
                        break;
                    case "Yearly":
                        adjustCalendar(Calendar.YEAR, -1);
                        appLaunchCount = appLaunchDatabase.getYearlyLaunchCount(startDate, date);
                        break;
                }
            }

            processAppLaunchData();
        }

        private void adjustCalendar(int field, int amount) {
            calendar.add(field, amount);
            startDate = sdf.format(calendar.getTime());
        }

        private void processAppLaunchData() {
            List<String> packageNames = new ArrayList<>(appLaunchCount.keySet());
            PackageManager pm = context.getPackageManager();

            totalCount = 0;
            for (String packageName : packageNames) {
                if (appLaunchCount.get(packageName) != null && appLaunchCount.get(packageName) > 0 && !packageName.isEmpty()){
                    totalCount += appLaunchCount.get(packageName);
                }
            }

            appLaunchModel.clear();
            for (String packageName : packageNames) {
                try {
                    if (appLaunchCount.get(packageName) != null && appLaunchCount.get(packageName) > 0 && !packageName.isEmpty()){
                        ApplicationInfo appInfo = pm.getPackageInfo(packageName, 0).applicationInfo;

                        String appName = appInfo.loadLabel(pm).toString();
                        Drawable appIcon = new CommonUtil().compressIcon(appInfo.loadIcon(pm), context);

                        int launchCount = appLaunchCount.get(packageName);
                        double progress = (double) launchCount / totalCount * 100;
                        appLaunchModel.add(new AppUsageModel(appName, packageName, appIcon, String.valueOf(launchCount), progress));
                    }
                } catch (PackageManager.NameNotFoundException ignored) {}
            }

            appLaunchModel.sort((o1, o2) -> Integer.parseInt(Objects.requireNonNull(o2.getExtra())) - Integer.parseInt(Objects.requireNonNull(o1.getExtra())));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isCancelled()) {
                return;
            }

            launchStats.setText(String.valueOf(totalCount));

            appLaunchAdapter = new AppLaunchAdapter(appLaunchModel);
            recyclerView.setAdapter(appLaunchAdapter);
            appLaunchAdapter.setOnItemClickListener(view -> {
                int position = recyclerView.getChildAdapterPosition(view);
                AppUsageModel app = appLaunchAdapter.getItemAt(position);
                Intent intent = new Intent(requireActivity(), AppInsightsActivity.class);
                intent.putExtra("appName", app.getName());
                intent.putExtra("packageName", app.getPackageName());
                intent.putExtra("position", position);
                startActivity(intent);
            });

            loadingLayout.setVisibility(View.GONE);
            launchLayout.setVisibility(View.VISIBLE);
        }
    }
}