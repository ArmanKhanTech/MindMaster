package com.android.achievix.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.android.achievix.Adapter.AppUsageAdapter;
import com.android.achievix.Model.AppUsageModel;
import com.android.achievix.R;
import com.android.achievix.Utility.UsageUtil;

import java.util.ArrayList;
import java.util.List;

public class UsageOverviewFragment extends Fragment {
    RecyclerView recyclerView;
    List<AppUsageModel> appUsageModels = new ArrayList<>();
    TextView stats;
    String[] sort = {"Daily", "Weekly", "Monthly", "Yearly"};
    String sortValue = "Daily";
    private LinearLayout llUsageOverview;
    private LinearLayout loadingLayout;

    public UsageOverviewFragment() {
        //
    }

    private static String convertMillisToHoursAndMinutes(long millis) {
        long hours = millis / 1000 / 60 / 60;
        long minutes = (millis / 1000 / 60) % 60;
        return hours + " hrs " + minutes + " mins";
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_usage_overview, container, false);

        Spinner spinner = view.findViewById(R.id.usage_spinner);

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
                            new GetInstalledAppsUsageTask(requireActivity(), sortValue).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                }
        );

        loadingLayout = view.findViewById(R.id.loading_usage_overview);
        llUsageOverview = view.findViewById(R.id.ll_usage_overview);
        stats = view.findViewById(R.id.tv_total_internet_usage);

        recyclerView = view.findViewById(R.id.recycler_view_usage_overview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        new GetInstalledAppsUsageTask(requireActivity(), "Daily").execute();
        recyclerView.setAdapter(new AppUsageAdapter(appUsageModels));

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public class GetInstalledAppsUsageTask extends AsyncTask<Void, Void, List<AppUsageModel>> {
        private final Context context;
        private final String sort;

        public GetInstalledAppsUsageTask(Context context, String sort) {
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
        protected List<AppUsageModel> doInBackground(Void... voids) {
            return UsageUtil.Companion.getInstalledAppsUsage(context, sort, null);
        }

        @Override
        protected void onPostExecute(List<AppUsageModel> result) {
            appUsageModels = result;
            recyclerView.setAdapter(new AppUsageAdapter(appUsageModels));

            loadingLayout.setVisibility(View.GONE);
            llUsageOverview.setVisibility(View.VISIBLE);

            stats.setText(convertMillisToHoursAndMinutes(UsageUtil.totalUsage));
        }
    }
}