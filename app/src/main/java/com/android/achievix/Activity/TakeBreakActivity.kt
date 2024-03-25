package com.android.achievix.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.android.achievix.R

class TakeBreakActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_break)

        val timePicker = findViewById<TimePicker>(R.id.time_picker_take_break)
        val pauseSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.pause_switch)
        val callSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.call_switch)
        val notificationSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.notification_switch)
        val button = findViewById<Button>(R.id.start_break_button)

        button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("takeBreak", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("hour", timePicker.hour)
            editor.putInt("minute", timePicker.minute)
            editor.putBoolean("pause", pauseSwitch.isChecked)
            editor.putBoolean("call", callSwitch.isChecked)
            editor.putBoolean("notification", notificationSwitch.isChecked)
            editor.apply()
        }
    }
}