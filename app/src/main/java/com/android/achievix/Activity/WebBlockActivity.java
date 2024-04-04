package com.android.achievix.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.achievix.Adapter.WebKeyBlockAdapter;
import com.android.achievix.Database.BlockDatabase;
import com.android.achievix.Model.WebKeyModel;
import com.android.achievix.R;
import com.android.achievix.Service.LogURLService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

// TODO: Fix accessibility settings
public class WebBlockActivity extends AppCompatActivity {
    private final BlockDatabase blockDatabase = new BlockDatabase(this);
    private final List<WebKeyModel> webKeyModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TextView noWebBlock;
    private Button blockButton;
    private WebKeyBlockAdapter webKeyBlockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_block);
        initializeViews();
        setupRecyclerView();
        setupSearchView();
        setUpButton();
        getBlockedWebsite();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.blocked_website);
        searchEditText = findViewById(R.id.search_web_block);
        noWebBlock = findViewById(R.id.no_web_block);
        blockButton = findViewById(R.id.add_web_button);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupSearchView() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void setUpButton() {
        blockButton.setOnClickListener(v -> {
            if (webKeyModelList.contains(new WebKeyModel(searchEditText.getText().toString()))) {
                Toast.makeText(this, "Website already blocked", Toast.LENGTH_SHORT).show();
            } else if (searchEditText.getText().toString().equals("google.com")) {
                Toast.makeText(this, "Cannot block Google", Toast.LENGTH_SHORT).show();
            } else {
                if (isAccessibilitySettingsOn(this)) {
                    if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                        Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Pattern urlPattern = Pattern.compile("^((https?|ftp|smtp)://)?(www.)?[a-z0-9]+(\\.[a-z]{2,}){1,3}(#?\\/?[a-zA-Z0-9#]+)*\\/?(\\?[a-zA-Z0-9-_]+=[a-zA-Z0-9-%]+&?)?$");
                        if (!urlPattern.matcher(searchEditText.getText().toString()).matches()) {
                            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(this, NewScheduleActivity.class);
                            intent.putExtra("name", searchEditText.getText().toString());
                            intent.putExtra("type", "web");
                            startActivity(intent);
                        }
                    }
                } else {
                    Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(i);
                }
            }
        });
    }

    private void filter(String text) {
        if (!webKeyModelList.isEmpty()) {
            List<WebKeyModel> filteredList = new ArrayList<>();
            for (WebKeyModel item : webKeyModelList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            webKeyBlockAdapter.updateListBlock(filteredList);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getBlockedWebsite() {
        List<HashMap<String, String>> list = blockDatabase.readAllRecordsWeb();
        if (!list.isEmpty()) {
            for (HashMap<String, String> map : list) {
                webKeyModelList.add(new WebKeyModel(Objects.requireNonNull(map.get("name"))));
            }
        }
        if (webKeyModelList.isEmpty()) {
            noWebBlock.setVisibility(TextView.VISIBLE);
        } else {
            webKeyBlockAdapter = new WebKeyBlockAdapter(webKeyModelList, true);
            recyclerView.setAdapter(webKeyBlockAdapter);

            webKeyBlockAdapter.setOnItemClickListener(view -> {
                int position = recyclerView.getChildAdapterPosition(view);
                WebKeyModel app = webKeyBlockAdapter.getItemAt(position);
                launchActivity(app.getName());
            });
        }
    }

    private void launchActivity(String webName) {
        Intent intent = new Intent(this, EditScheduleActivity.class);
        intent.putExtra("name", webName);
        intent.putExtra("type", "web");
        startActivity(intent);
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + LogURLService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}