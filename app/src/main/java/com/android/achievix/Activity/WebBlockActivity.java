package com.android.achievix.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.achievix.Adapter.WebBlockAdapter;
import com.android.achievix.Database.BlockDatabase;
import com.android.achievix.Model.AppSelectModel;
import com.android.achievix.R;
import com.android.achievix.Service.LogURLService;

import java.util.ArrayList;

import kotlin.text.Regex;

// TODO: Fix accessibility settings
public class WebBlockActivity extends AppCompatActivity {
    BlockDatabase db;
    TextView tv;
    ArrayList<AppSelectModel> webList = new ArrayList<>();
    RecyclerView recyclerView;
    WebBlockAdapter webBlockAdapter;
    Button block_web;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_block);

        block_web = findViewById(R.id.block_web_button);
        tv = findViewById(R.id.no_web_block);
        recyclerView = findViewById(R.id.blocked_website);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        populateList();

        block_web.setOnClickListener(v -> {
            if (isAccessibilitySettingsOn(this)) {
                EditText website = findViewById(R.id.add_website);

                if (TextUtils.isEmpty(website.getText().toString())) {
                    Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    Regex urlRegex = new Regex("/^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+(\\.[a-z]{2,}){1,3}(#?\\/?[a-zA-Z0-9#]+)*\\/?(\\?[a-zA-Z0-9-_]+=[a-zA-Z0-9-%]+&?)?$/");
                    if (!urlRegex.matches(website.getText().toString())) {
                        Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = website.getText().toString();
//                        db.addNewWebsite(url);
                        populateList();
                    }
                }
            } else {
                Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(i);
            }
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    private void populateList() {
//        ArrayList<String> w;
//        db = new BlockDatabase(this);
//        w = db.readWebsites();
//        webList.clear();
//
//        for (int i = 0; i < w.size(); i++) {
//            String url = w.get(i);
//            webList.add(new AppSelectModel(url, getDrawable(R.drawable.lock_icon_grey)));
//        }
//
//        webBlockAdapter = new WebBlockAdapter(webList, true);
//        recyclerView.setAdapter(webBlockAdapter);
//
//        if (w.isEmpty()) {
//            tv.setText("No Website Blocked");
//        } else {
//            tv.setText("");
//        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + LogURLService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
            //
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