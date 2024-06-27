package com.android.mindmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mindmaster.R;
import com.mindmaster.passcodeview.PasscodeView;

import java.util.Objects;

public class EnterPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        PasscodeView passcodeView = findViewById(R.id.passcode_view);

        Intent intent = getIntent();
        int password = intent.getIntExtra("password", 0);
        String invokedFrom = intent.getStringExtra("invokedFrom");

        ImageButton back = findViewById(R.id.back_enter_password);
        assert invokedFrom != null;
        if (invokedFrom.equals("main") || invokedFrom.equals("newProfile")) {
            back.setVisibility(ImageButton.VISIBLE);
            back.setOnClickListener(v -> finish());
        } else {
            back.setVisibility(ImageButton.GONE);
        }


        passcodeView.setLocalPasscode(String.valueOf(password));
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail(String wrongNumber) {}

            @Override
            public void onSuccess(String number) {
                Intent intent;
                if (invokedFrom.equals("main") || invokedFrom.equals("newProfile")) {
                    intent = new Intent(EnterPasswordActivity.this, EditProfileActivity.class);
                    intent.putExtra(
                        "profileId",
                        Objects.requireNonNull(
                            Objects.requireNonNull(getIntent().getExtras()).get("profileId")).toString()
                    );
                    intent.putExtra(
                        "profileName",
                        Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("profileName")).toString()
                    );
                } else {
                    intent = new Intent(EnterPasswordActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void finish(View v) {
        finish();
    }
}