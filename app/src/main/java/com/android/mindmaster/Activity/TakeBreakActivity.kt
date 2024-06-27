package com.android.mindmaster.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.android.mindmaster.R

@SuppressLint("UseSwitchCompatOrMaterialCode")
class TakeBreakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_break)

        val timePicker = findViewById<TimePicker>(R.id.time_picker_take_break)
        val stopSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.stop_switch)
        val callSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.call_switch)
        val notificationSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.notification_switch)
        val button = findViewById<Button>(R.id.start_break_button)

        button.setOnClickListener {
            val sh = getSharedPreferences("takeBreak", MODE_PRIVATE)
            val editor = sh.edit()
            editor.putInt("hour", timePicker.hour)
            editor.putInt("minute", timePicker.minute)
            editor.putBoolean("stop", stopSwitch.isChecked)
            editor.putBoolean("call", callSwitch.isChecked)
            editor.putBoolean("notification", notificationSwitch.isChecked)
            editor.apply()
        }
    }

    fun finish(v: View?) {
        finish()
    }
}