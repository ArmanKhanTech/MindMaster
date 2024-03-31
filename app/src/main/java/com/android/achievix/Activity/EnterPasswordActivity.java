package com.android.achievix.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.achievix.R;
import com.hanks.passcodeview.PasscodeView;

public class EnterPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        PasscodeView passcodeView = findViewById(R.id.passcodeViewCheck);

        Intent intent = getIntent();
        int password = intent.getIntExtra("password", 0);
        String invokedFrom = intent.getStringExtra("invokedFrom");

        ImageButton back = findViewById(R.id.back_enter_password);
        assert invokedFrom != null;
        if(invokedFrom.equals("MainActivity")) {
//
        } else {
//
        }

        passcodeView.setLocalPasscode(String.valueOf(password));
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail(String wrongNumber) {}
            @Override
            public void onFail() {}

            @Override
            public void onSuccess(String number) {
                Intent intent = new Intent(EnterPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}