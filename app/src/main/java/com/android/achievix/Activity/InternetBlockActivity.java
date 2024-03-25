package com.android.achievix.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.achievix.Adapter.InternetBlockAdapter;
import com.android.achievix.Model.AppBlockModel;
import com.android.achievix.R;
import com.android.achievix.Utility.UsageUtil;

import java.util.ArrayList;
import java.util.List;

/** @noinspection ALL*/ // TODO: Lazy loading, optimize, edt cursor, spinner theme
public class InternetBlockActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private final String[] sort = new String[]{"Name", "Usage", "Blocked"};
    private String sortValue = "Name";
    private List<AppBlockModel> appList;
    private RecyclerView recyclerView;
    private LinearLayout loadingLayout;
    private LinearLayout llInternetUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_block);

        loadingLayout = findViewById(R.id.loading_block_internet);
        llInternetUsage = findViewById(R.id.ll_block_internet);

        recyclerView = findViewById(R.id.internet_block_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new GetInstalledAppsInternetTask(this, sortValue).execute();

        EditText searchView = findViewById(R.id.search_internet_block);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
        });

        Spinner spinner = findViewById(R.id.internet_block_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sort);
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
                            new GetInstalledAppsInternetTask(InternetBlockActivity.this, sortValue).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                }
        );
    }

    private void filter(String text) {
        List<AppBlockModel> filteredList = new ArrayList<>();
        for (AppBlockModel item : appList) {
            if (item.getAppName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        ((InternetBlockAdapter) recyclerView.getAdapter()).updateListInternet(filteredList);
    }

    @SuppressLint("StaticFieldLeak")
    public class GetInstalledAppsInternetTask extends AsyncTask<Void, Void, List<AppBlockModel>> {
        private final Context context;
        private final String sort;

        public GetInstalledAppsInternetTask(Context context, String sort) {
            this.context = context;
            this.sort = sort;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingLayout.setVisibility(View.VISIBLE);
            llInternetUsage.setVisibility(View.GONE);
        }

        @Override
        protected List<AppBlockModel> doInBackground(Void... voids) {
            return UsageUtil.Companion.getInstalledApps(context, sort, "InternetBlockActivity");
        }

        @Override
        protected void onPostExecute(List<AppBlockModel> result) {
            appList = result;
            recyclerView.setAdapter(new InternetBlockAdapter(appList));
            loadingLayout.setVisibility(View.GONE);
            llInternetUsage.setVisibility(View.VISIBLE);
        }
    }
}