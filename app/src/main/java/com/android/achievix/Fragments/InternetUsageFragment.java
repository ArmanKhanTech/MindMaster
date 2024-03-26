package com.android.achievix.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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

import com.android.achievix.Adapter.InternetUsageAdapter;
import com.android.achievix.Model.AppUsageModel;
import com.android.achievix.R;
import com.android.achievix.Utility.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InternetUsageFragment extends Fragment {
    RecyclerView recyclerView;
    List<AppUsageModel> internetUsageModel = new ArrayList<>();
    TextView stats;
    String[] sort = {"Daily", "Weekly", "Monthly", "Yearly"};
    String sortValue = "Daily";
    private LinearLayout llUsageOverview;
    private LinearLayout loadingLayout;
    long startMillis;
    long endMillis;
    float totalCount;
    private GetAppInternetUsage getAppInternetUsageTask;

    public InternetUsageFragment() {
        // do nothing
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_internet_usage, container, false);

        Spinner spinner = view.findViewById(R.id.internet_usage_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown_item, sort);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    boolean isFirstTime = true;

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isFirstTime) {
                            isFirstTime = false;
                        } else {
                            sortValue = parent.getItemAtPosition(position).toString();
                            new GetAppInternetUsage(requireActivity(), sortValue).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                }
        );

        loadingLayout = view.findViewById(R.id.loading_internet_usage);
        llUsageOverview = view.findViewById(R.id.ll_internet_usage);
        stats = view.findViewById(R.id.tv_total_internet_usage);

        recyclerView = view.findViewById(R.id.recycler_view_internet_usage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        getAppInternetUsageTask = new GetAppInternetUsage(requireActivity(), sortValue);
        getAppInternetUsageTask.execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public class GetAppInternetUsage extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final String sort;

        public GetAppInternetUsage(Context context, String sort) {
            this.context = context;
            this.sort = sort;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingLayout.setVisibility(View.VISIBLE);
            llUsageOverview.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<String> packageNames = new ArrayList<>();

            HashMap<String, Float> appUsage = new HashMap<>();

            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    packageNames.add(packageInfo.packageName);
                }
            }

            switch (sort) {
                case "Daily":
                    startMillis = System.currentTimeMillis() - 86400000;
                    endMillis = System.currentTimeMillis();
                    for (String packageName : packageNames) {
                        appUsage.put(packageName, NetworkUtil.getUID(startMillis, endMillis, packageName, context));
                    }
                    break;
                case "Weekly":
                    startMillis = System.currentTimeMillis() - 604800000;
                    endMillis = System.currentTimeMillis();
                    for (String packageName : packageNames) {
                        appUsage.put(packageName, NetworkUtil.getUID(startMillis, endMillis, packageName, context));
                    }
                    break;
                case "Monthly":
                    startMillis = System.currentTimeMillis() - 2592000000L;
                    endMillis = System.currentTimeMillis();
                    for (String packageName : packageNames) {
                        appUsage.put(packageName, NetworkUtil.getUID(startMillis, endMillis, packageName, context));
                    }
                    break;
                case "Yearly":
                    startMillis = System.currentTimeMillis() - 31536000000L;
                    endMillis = System.currentTimeMillis();
                    for (String packageName : packageNames) {
                        appUsage.put(packageName, NetworkUtil.getUID(startMillis, endMillis, packageName, context));
                    }
                    break;
            }

            totalCount = 0;
            for (String packageName : packageNames) {
                if (appUsage.get(packageName) != null && appUsage.get(packageName) > 0 && !packageName.isEmpty()) {
                    totalCount += appUsage.get(packageName);
                }
            }

            internetUsageModel.clear();
            for (String packageName : packageNames) {
                try {
                    if (appUsage.get(packageName) != null && appUsage.get(packageName) > 0 && !packageName.isEmpty()){
                        ApplicationInfo appInfo = pm.getPackageInfo(packageName, 0).applicationInfo;
                        String appName = appInfo.loadLabel(pm).toString();
                        Drawable appIcon = appInfo.loadIcon(pm);
                        Bitmap bitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        appIcon.draw(canvas);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        appIcon = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                        float launchCount = appUsage.get(packageName);
                        double progress = (double) launchCount / totalCount * 100;
                        internetUsageModel.add(new AppUsageModel(appName, packageName, appIcon, String.valueOf(launchCount), progress));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // do nothing
                }
            }

            internetUsageModel.sort((o1, o2) -> Float.compare(Float.parseFloat(Objects.requireNonNull(o2.getExtra())), Float.parseFloat(Objects.requireNonNull(o1.getExtra()))));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled()) {
                return;
            }
            stats.setText(totalCount + " MB");
            recyclerView.setAdapter(new InternetUsageAdapter(internetUsageModel));
            loadingLayout.setVisibility(View.GONE);
            llUsageOverview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAppInternetUsageTask != null) {
            getAppInternetUsageTask.cancel(true);
        }
    }
}
