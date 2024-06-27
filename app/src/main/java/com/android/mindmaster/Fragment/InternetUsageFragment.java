package com.android.mindmaster.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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

import com.android.mindmaster.Activity.AppInsightsActivity;
import com.android.mindmaster.Adapter.InternetUsageAdapter;
import com.android.mindmaster.Model.AppUsageModel;
import com.android.mindmaster.R;
import com.android.mindmaster.Utility.CommonUtility;
import com.android.mindmaster.Utility.NetworkUtility;
import com.android.mindmaster.Utility.UsageUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kotlin.Pair;

/** @noinspection DataFlowIssue*/
@SuppressLint({"StaticFieldLeak", "SetTextI18n", "QueryPermissionsNeeded"})
public class InternetUsageFragment extends Fragment {
    private final List<AppUsageModel> internetUsageModelList = new ArrayList<>();
    private final String[] sort = {"Daily", "Weekly", "Monthly", "Yearly"};

    private RecyclerView recyclerView;
    private InternetUsageAdapter internetUsageAdapter;
    private TextView stats, noData;
    private String sortValue = "Daily";
    private Spinner sortSpinner;
    private LinearLayout internetUsageLayout;
    private LinearLayout loadingLayout;

    private long startMillis;
    private long endMillis;
    private float totalCount;

    private AppInternetUsageTask appInternetUsageTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet_usage, container, false);

        initializeViews(view);
        setupSpinner();
        setupRecyclerView();

        appInternetUsageTask = new AppInternetUsageTask(requireActivity(), sortValue);
        appInternetUsageTask.execute();
        return view;
    }

    private void initializeViews(View view) {
        loadingLayout = view.findViewById(R.id.loading_internet_usage);
        internetUsageLayout = view.findViewById(R.id.ll_internet_usage);
        stats = view.findViewById(R.id.tv_total_internet_usage);
        noData = view.findViewById(R.id.tv_no_internet_usage_data);
        recyclerView = view.findViewById(R.id.recycler_view_internet_usage);
        sortSpinner = view.findViewById(R.id.internet_usage_spinner);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown_item, sort);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isFirstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstTime) {
                    sortValue = parent.getItemAtPosition(position).toString();
                    new AppInternetUsageTask(requireActivity(), sortValue).execute();
                }
                isFirstTime = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (appInternetUsageTask != null) {
            appInternetUsageTask.cancel(true);
        }
    }

    private class AppInternetUsageTask extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final String sort;

        public AppInternetUsageTask(Context context, String sort) {
            this.context = context;
            this.sort = sort;
        }

        @Override
        protected void onPreExecute() {
            loadingLayout.setVisibility(View.VISIBLE);
            internetUsageLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PackageManager pm = context.getPackageManager();

            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<String> packageNames = new ArrayList<>();

            HashMap<String, Float> appUsage = getAppUsage(packages, packageNames);

            totalCount = 0;
            for (String packageName : packageNames) {
                Float usage = appUsage.get(packageName);
                if (usage != null && usage > 0 && !packageName.isEmpty()) {
                    totalCount += usage;
                }
            }

            internetUsageModelList.clear();
            for (String packageName : packageNames) {
                try {
                    if (appUsage.get(packageName) != null &&
                            appUsage.get(packageName) > 0 && !packageName.isEmpty()
                    ) {
                        internetUsageModelList.add(createAppUsageModel(pm, appUsage, packageName));
                    }
                } catch (PackageManager.NameNotFoundException | IOException ignored) {}
            }

            internetUsageModelList.sort((o1, o2) ->
                Float.compare(
                    Float.parseFloat(Objects.requireNonNull(o2.getExtra())),
                    Float.parseFloat(Objects.requireNonNull(o1.getExtra()))));
            return null;
        }

        private HashMap<String, Float> getAppUsage(
            List<PackageInfo> packages, List<String> packageNames
        ) {
            HashMap<String, Float> appUsage = new HashMap<>();
            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ||
                        (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                ) {
                    packageNames.add(packageInfo.packageName);
                }
            }

            Pair<Long, Long> pair;
            switch (sort) {
                case "Daily":
                    pair = UsageUtility.Companion.getTimeRange("Daily");
                    startMillis = pair.getFirst();
                    endMillis = pair.getSecond();
                    break;
                case "Weekly":
                    pair = UsageUtility.Companion.getTimeRange("Weekly");
                    startMillis = pair.getFirst();
                    endMillis = pair.getSecond();
                    break;
                case "Monthly":
                    pair = UsageUtility.Companion.getTimeRange("Monthly");
                    startMillis = pair.getFirst();
                    endMillis = pair.getSecond();
                    break;
                case "Yearly":
                    pair = UsageUtility.Companion.getTimeRange("Yearly");
                    startMillis = pair.getFirst();
                    endMillis = pair.getSecond();
                    break;
            }

            NetworkUtility networkUtility = new NetworkUtility();
            for (String packageName : packageNames) {
                appUsage.put(
                    packageName,
                    networkUtility.getPackageInfo(startMillis, endMillis, packageName, true, true, context)
                );
            }

            return appUsage;
        }

        private AppUsageModel createAppUsageModel(
                PackageManager pm, HashMap<String, Float> appUsage, String packageName
        ) throws PackageManager.NameNotFoundException, IOException {
            ApplicationInfo appInfo = pm.getPackageInfo(packageName, 0).applicationInfo;

            String appName = appInfo.loadLabel(pm).toString();
            Drawable appIcon = new CommonUtility().compressIcon(appInfo.loadIcon(pm), context);

            float launchCount = appUsage.get(packageName);
            double progress = (double) launchCount / totalCount * 100;
            return new AppUsageModel(appName, packageName, appIcon, String.valueOf(launchCount), progress);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!internetUsageModelList.isEmpty()) {
                internetUsageAdapter = new InternetUsageAdapter(internetUsageModelList);
                recyclerView.setAdapter(internetUsageAdapter);

                internetUsageAdapter.setOnItemClickListener(view -> {
                    int position = recyclerView.getChildAdapterPosition(view);
                    AppUsageModel app = internetUsageAdapter.getItemAt(position);

                    Intent intent = new Intent(requireActivity(), AppInsightsActivity.class);
                    intent.putExtra("appName", app.getName());
                    intent.putExtra("packageName", app.getPackageName());
                    intent.putExtra("position", position);
                    startActivity(intent);
                });

                stats.setText(totalCount + " MB");
            } else {
                noData.setVisibility(View.VISIBLE);
            }
            loadingLayout.setVisibility(View.GONE);
            internetUsageLayout.setVisibility(View.VISIBLE);
        }
    }
}
