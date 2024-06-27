package com.android.mindmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mindmaster.Adapter.InternetBlockAdapter;
import com.android.mindmaster.Model.AppBlockModel;
import com.android.mindmaster.R;
import com.android.mindmaster.Utility.UsageUtility;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class InternetBlockActivity extends AppCompatActivity {
    private final String[] sort = new String[]{"Name", "Usage", "Blocked"};
    private String sortValue = "Name";
    private List<AppBlockModel> appBlockModelList;

    private RecyclerView recyclerView;
    private LinearLayout loadingLayout;
    private LinearLayout internetUsageLayout;
    private Spinner sortSpinner;
    private EditText searchEditText;
    private InternetBlockAdapter internetBlockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_block);

        initializeViews();
        attachListeners();

        new GetInstalledAppsInternetTask(this, sortValue).execute();
    }

    private void initializeViews() {
        loadingLayout = findViewById(R.id.loading_block_internet);
        internetUsageLayout = findViewById(R.id.layout_block_internet);
        searchEditText = findViewById(R.id.search_internet_block);

        recyclerView = findViewById(R.id.internet_block_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sortSpinner = findViewById(R.id.internet_block_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, sort
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
    }

    private void attachListeners() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        sortSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                boolean isFirstTime = true;

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
                public void onNothingSelected(AdapterView<?> parent) {}
            }
        );
    }

    private void filter(String text) {
        List<AppBlockModel> filteredList = new ArrayList<>();
        for (AppBlockModel item : appBlockModelList) {
            if (item.getAppName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        internetBlockAdapter.updateListInternet(filteredList);
    }

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
            internetUsageLayout.setVisibility(View.GONE);
        }

        @Override
        protected List<AppBlockModel> doInBackground(Void... voids) {
            return UsageUtility.Companion.getInstalledAppsBlock(context, sort, "InternetBlockActivity");
        }

        @Override
        protected void onPostExecute(List<AppBlockModel> result) {
            appBlockModelList = result;
            internetBlockAdapter = new InternetBlockAdapter(appBlockModelList);
            recyclerView.setAdapter(internetBlockAdapter);

            loadingLayout.setVisibility(View.GONE);
            internetUsageLayout.setVisibility(View.VISIBLE);

            internetBlockAdapter.setOnItemClickListener(view -> {
                int position = recyclerView.getChildAdapterPosition(view);
                AppBlockModel app = internetBlockAdapter.getItemAt(position);
                launchActivity(app.getAppName(), app.getPackageName(), Boolean.TRUE.equals(app.getBlocked()));
            });
        }
    }

    private void launchActivity(String appName, String packageName, boolean blocked) {
        Intent intent;
        if (!blocked) {
            intent = new Intent(this, BlockDataActivity.class);
            intent.putExtra("name", appName);
            intent.putExtra("packageName", packageName);
        } else {
            intent = new Intent(this, EditScheduleActivity.class);
            intent.putExtra("name", appName);
            intent.putExtra("packageName", packageName);
            intent.putExtra("type", "internet");
        }

        if ("com.android.mindmaster".equals(packageName)) {
            Toast.makeText(this, "Cannot block MindMaster", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

    public void finish(View v) {
        finish();
    }
}