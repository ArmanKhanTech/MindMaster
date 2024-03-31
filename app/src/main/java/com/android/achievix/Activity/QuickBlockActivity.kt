package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.android.achievix.Database.BlockDatabase
import com.android.achievix.R

class QuickBlockActivity : AppCompatActivity() {
    private lateinit var appLaunchSwitch: SwitchCompat
    private lateinit var notiSwitch: SwitchCompat
    private lateinit var untilTimePicker: TimePicker
    private lateinit var saveButton: Button
    private lateinit var textEditText: EditText
    private lateinit var blockDatabase: BlockDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_block)

        val intent = intent
        val name = intent.getStringExtra("name")
        val packageName = intent.getStringExtra("packageName")
        val type = intent.getStringExtra("type")

        initializeViews()
        attachListeners(name, packageName, type)
    }

    private fun initializeViews() {
        appLaunchSwitch = findViewById(R.id.block_app_launch_usage_time)
        notiSwitch = findViewById(R.id.block_noti_usage_time)
        untilTimePicker = findViewById(R.id.time_picker_quick_block)
        saveButton = findViewById(R.id.quick_block_button)
        textEditText = findViewById(R.id.quick_block_text)

        blockDatabase = BlockDatabase(this)

        appLaunchSwitch.isChecked = true
        notiSwitch.isChecked = true
    }

    private fun attachListeners(name: String?, packageName: String?, type: String?) {
        saveButton.setOnClickListener {
            val untilHours = untilTimePicker.hour
            val untilMins = untilTimePicker.minute

            val text = textEditText.text.toString().let {
                it.ifEmpty {
                    null
                } ?: it
            }

            val appLaunch = appLaunchSwitch.isChecked
            val noti = notiSwitch.isChecked

            if (untilHours == 0 && untilMins == 0) {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!appLaunch && !noti) {
                Toast.makeText(this, "Please select at least one option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (type) {
                "app" -> {
                    blockDatabase.addRecord(
                        name,
                        packageName,
                        "app",
                        appLaunch,
                        noti,
                        "Quick Block",
                        "$untilHours $untilMins",
                        null,
                        null,
                        false,
                        text
                    )

                    Toast.makeText(this, "Schedule added", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }, 1000)
                }
            }
        }
    }
}