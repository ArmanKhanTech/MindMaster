package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.android.achievix.Database.BlockDatabase
import com.android.achievix.R

class UsageTimeActivity : AppCompatActivity() {
    private lateinit var appLaunchSwitch: SwitchCompat
    private lateinit var notiSwitch: SwitchCompat
    private lateinit var monRadioButton: RadioButton
    private lateinit var tueRadioButton: RadioButton
    private lateinit var wedRadioButton: RadioButton
    private lateinit var thuRadioButton: RadioButton
    private lateinit var friRadioButton: RadioButton
    private lateinit var satRadioButton: RadioButton
    private lateinit var sunRadioButton: RadioButton
    private lateinit var hoursEditText: EditText
    private lateinit var minsEditText: EditText
    private lateinit var textEditText: EditText
    private lateinit var saveButton: Button
    private val days = mutableListOf<String>()
    private lateinit var blockDatabase: BlockDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage_time)

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
        monRadioButton = findViewById(R.id.monday)
        tueRadioButton = findViewById(R.id.tuesday)
        wedRadioButton = findViewById(R.id.wednesday)
        thuRadioButton = findViewById(R.id.thursday)
        friRadioButton = findViewById(R.id.friday)
        satRadioButton = findViewById(R.id.saturday)
        sunRadioButton = findViewById(R.id.sunday)
        hoursEditText = findViewById(R.id.usage_time_hrs)
        minsEditText = findViewById(R.id.usage_time_mins)
        textEditText = findViewById(R.id.usage_time_text)
        saveButton = findViewById(R.id.usage_time_block_button)

        blockDatabase = BlockDatabase(this)

        appLaunchSwitch.isChecked = true
        notiSwitch.isChecked = true
    }

    private fun attachListeners(name: String?, packageName: String?, type: String?) {
        saveButton.setOnClickListener {
            val hours = hoursEditText.text.toString().isEmpty().let {
                if (it) {
                    0
                } else {
                    hoursEditText.text.toString().toInt()
                }
            }
            val mins = minsEditText.text.toString().isEmpty().let {
                if (it) {
                    0
                } else {
                    minsEditText.text.toString().toInt()
                }
            }
            val text = textEditText.text.toString().let {
                it.ifEmpty {
                    null
                } ?: it
            }
            val appLaunch = appLaunchSwitch.isChecked
            val noti = notiSwitch.isChecked

            if (hours == 0 && mins == 0) {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (days.isEmpty()) {
                Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if(!appLaunch && !noti) {
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
                        "Usage Time",
                        "$hours $mins",
                        days.toString(),
                        null,
                        false,
                        text
                    )

                    Toast.makeText(this, "App blocked successfully", Toast.LENGTH_SHORT).show()
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
        setupDayCheckListeners()
    }

    private fun setupDayCheckListeners() {
        var isSundayChecked = false
        sunRadioButton.setOnClickListener {
            isSundayChecked = !isSundayChecked
            sunRadioButton.isChecked = isSundayChecked
            if (isSundayChecked) {
                days.add("Sunday")
            } else {
                days.remove("Sunday")
            }
        }

        var isMondayChecked = false
        monRadioButton.setOnClickListener {
            isMondayChecked = !isMondayChecked
            monRadioButton.isChecked = isMondayChecked
            if (isMondayChecked) {
                days.add("Monday")
            } else {
                days.remove("Monday")
            }
        }

        var isTuesdayChecked = false
        tueRadioButton.setOnClickListener {
            isTuesdayChecked = !isTuesdayChecked
            tueRadioButton.isChecked = isTuesdayChecked
            if (isTuesdayChecked) {
                days.add("Tuesday")
            } else {
                days.remove("Tuesday")
            }
        }

        var isWednesdayChecked = false
        wedRadioButton.setOnClickListener {
            isWednesdayChecked = !isWednesdayChecked
            wedRadioButton.isChecked = isWednesdayChecked
            if (isWednesdayChecked) {
                days.add("Wednesday")
            } else {
                days.remove("Wednesday")
            }
        }

        var isThursdayChecked = false
        thuRadioButton.setOnClickListener {
            isThursdayChecked = !isThursdayChecked
            thuRadioButton.isChecked = isThursdayChecked
            if (isThursdayChecked) {
                days.add("Thursday")
            } else {
                days.remove("Thursday")
            }
        }

        var isFridayChecked = false
        friRadioButton.setOnClickListener {
            isFridayChecked = !isFridayChecked
            friRadioButton.isChecked = isFridayChecked
            if (isFridayChecked) {
                days.add("Friday")
            } else {
                days.remove("Friday")
            }
        }

        var isSaturdayChecked = false
        satRadioButton.setOnClickListener {
            isSaturdayChecked = !isSaturdayChecked
            satRadioButton.isChecked = isSaturdayChecked
            if (isSaturdayChecked) {
                days.add("Saturday")
            } else {
                days.remove("Saturday")
            }
        }
    }
}