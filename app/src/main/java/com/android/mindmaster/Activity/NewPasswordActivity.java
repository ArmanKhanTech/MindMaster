package com.android.mindmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mindmaster.R;
import com.mindmaster.passcodeview.PasscodeView;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class NewPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        SharedPreferences sh = getSharedPreferences("mode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();

        PasscodeView passcodeView = findViewById(R.id.passcode_view_set);
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail(String wrongNumber) {}

            @Override
            public void onSuccess(String number) {
                editor.putInt("password", Integer.parseInt(number));
                editor.apply();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void finish(View v) {
        finish();
    }
}