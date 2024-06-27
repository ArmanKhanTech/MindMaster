package com.android.mindmaster.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.R
import com.android.mindmaster.Utility.CommonUtility

@SuppressLint("SetTextI18n")
class SpecificTimeActivity : AppCompatActivity() {
    private lateinit var launchSwitch: SwitchCompat
    private lateinit var notiSwitch: SwitchCompat
    private lateinit var monRadioButton: RadioButton
    private lateinit var tueRadioButton: RadioButton
    private lateinit var wedRadioButton: RadioButton
    private lateinit var thuRadioButton: RadioButton
    private lateinit var friRadioButton: RadioButton
    private lateinit var satRadioButton: RadioButton
    private lateinit var sunRadioButton: RadioButton
    private lateinit var fromTimePicker: TimePicker
    private lateinit var toTimePicker: TimePicker
    private lateinit var textEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var text: TextView

    private val days = mutableListOf<String>()
    private lateinit var blockDatabase: BlockDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_time)

        val intent = intent
        val name = intent.getStringExtra("name")
        val packageName = intent.getStringExtra("packageName")
        val type = intent.getStringExtra("type")

        initializeViews()
        attachListeners(name, packageName, type)
    }

    private fun initializeViews() {
        launchSwitch = findViewById(R.id.block_app_launch_specific_time)
        notiSwitch = findViewById(R.id.block_noti_specific_time)
        monRadioButton = findViewById(R.id.monday)
        tueRadioButton = findViewById(R.id.tuesday)
        wedRadioButton = findViewById(R.id.wednesday)
        thuRadioButton = findViewById(R.id.thursday)
        friRadioButton = findViewById(R.id.friday)
        satRadioButton = findViewById(R.id.saturday)
        sunRadioButton = findViewById(R.id.sunday)
        fromTimePicker = findViewById(R.id.time_picker_time_interval_from)
        toTimePicker = findViewById(R.id.time_picker_time_interval_to)
        textEditText = findViewById(R.id.time_interval_text)
        saveButton = findViewById(R.id.specific_time_button)
        text = findViewById(R.id.specific_time_launch_text)

        blockDatabase = BlockDatabase(this)

        launchSwitch.isChecked = true
        notiSwitch.isChecked = true
    }

    private fun attachListeners(name: String?, packageName: String?, type: String?) {
        if (type == "web" || type == "key") {
            text.text = "Site Launch"
        }

        saveButton.setOnClickListener {
            val fromHours = fromTimePicker.hour
            val fromMins = fromTimePicker.minute
            val toHours = toTimePicker.hour
            val toMins = toTimePicker.minute

            val motivationalText = textEditText.text.toString().let {
                it.ifEmpty {
                    null
                } ?: it
            }

            val launch = launchSwitch.isChecked
            val noti = notiSwitch.isChecked

            if (fromHours > toHours || (fromHours == toHours && fromMins >= toMins)) {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (days.isEmpty()) {
                Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!launch && !noti) {
                Toast.makeText(this, "Please select at least one option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (type) {
                "app" -> {
                    blockDatabase.addRecord(
                        name,
                        packageName,
                        "app",
                        launch,
                        noti,
                        "Specific Time",
                        "$fromHours $fromMins $toHours $toMins",
                        days.toString(),
                        null,
                        true,
                        motivationalText
                    )
                }

                "web" -> {
                    blockDatabase.addRecord(
                        name,
                        null,
                        "web",
                        launch,
                        noti,
                        "Specific Time",
                        "$fromHours $fromMins $toHours $toMins",
                        days.toString(),
                        null,
                        true,
                        motivationalText
                    )
                }

                "key" -> {
                    blockDatabase.addRecord(
                        name,
                        null,
                        "key",
                        launch,
                        noti,
                        "Specific Time",
                        "$fromHours $fromMins $toHours $toMins",
                        days.toString(),
                        null,
                        true,
                        motivationalText
                    )
                }

                "profile" -> {
                    blockDatabase.addRecord(
                        null,
                        null,
                        null,
                        launch,
                        noti,
                        "Specific Time",
                        "$fromHours $fromMins $toHours $toMins",
                        days.toString(),
                        intent.getStringExtra("profileName"),
                        true,
                        motivationalText
                    )

                    blockDatabase.addAllItemsToNewProfileSchedule(
                        launch,
                        noti,
                        intent.getStringExtra("profileName"),
                        "Specific Time",
                        "$fromHours $fromMins $toHours $toMins",
                        days.toString(),
                        true,
                        motivationalText
                    )
                }
            }

            Toast.makeText(this, "Schedule Added", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }, 1000)
        }

        CommonUtility().setupRadioListeners(
            sunRadioButton,
            monRadioButton,
            tueRadioButton,
            wedRadioButton,
            thuRadioButton,
            friRadioButton,
            satRadioButton,
            days
        )
    }

    fun finish(v: View?) {
        finish()
    }
}