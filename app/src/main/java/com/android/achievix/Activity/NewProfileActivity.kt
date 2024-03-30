package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.achievix.R

class NewProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_profile)

        val newProfile: EditText = findViewById(R.id.add_profile)
        newProfile.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    val intent = Intent(this@NewProfileActivity, NewScheduleActivity::class.java)
                    intent.putExtra("profile_name", newProfile.text.toString())
                    startActivity(intent)
                    return true
                }
                return false
            }
        })

    }
}