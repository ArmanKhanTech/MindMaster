package com.android.mindmaster.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.R
import com.android.mindmaster.Utility.CommonUtility

class NumberOfLaunchesActivity : AppCompatActivity() {
    private lateinit var appLaunchSwitch: SwitchCompat
    private lateinit var notiSwitch: SwitchCompat
    private lateinit var monRadioButton: RadioButton
    private lateinit var tueRadioButton: RadioButton
    private lateinit var wedRadioButton: RadioButton
    private lateinit var thuRadioButton: RadioButton
    private lateinit var friRadioButton: RadioButton
    private lateinit var satRadioButton: RadioButton
    private lateinit var sunRadioButton: RadioButton
    private lateinit var launchEditText: EditText
    private lateinit var textEditText: EditText
    private lateinit var saveButton: Button

    private val days = mutableListOf<String>()
    private lateinit var blockDatabase: BlockDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_of_launches)

        val intent = intent
        val name = intent.getStringExtra("name")
        val packageName = intent.getStringExtra("packageName")
        val type = intent.getStringExtra("type")

        initializeViews()
        attachListeners(name, packageName, type)
    }

    private fun initializeViews() {
        appLaunchSwitch = findViewById(R.id.block_app_launch_launches)
        notiSwitch = findViewById(R.id.block_noti_launches)
        monRadioButton = findViewById(R.id.monday)
        tueRadioButton = findViewById(R.id.tuesday)
        wedRadioButton = findViewById(R.id.wednesday)
        thuRadioButton = findViewById(R.id.thursday)
        friRadioButton = findViewById(R.id.friday)
        satRadioButton = findViewById(R.id.saturday)
        sunRadioButton = findViewById(R.id.sunday)
        launchEditText = findViewById(R.id.launch_count)
        textEditText = findViewById(R.id.no_of_launches_text)
        saveButton = findViewById(R.id.no_of_launches_button)

        blockDatabase = BlockDatabase(this)

        appLaunchSwitch.isChecked = true
        notiSwitch.isChecked = true
    }

    private fun attachListeners(name: String?, packageName: String?, type: String?) {
        saveButton.setOnClickListener {
            val launchCount = launchEditText.text.toString().toIntOrNull()

            val motivationalText = textEditText.text.toString().let {
                it.ifEmpty {
                    null
                } ?: it
            }

            val launch = appLaunchSwitch.isChecked
            val noti = notiSwitch.isChecked

            if (launchCount == null || launchCount <= 0) {
                Toast.makeText(this, "Please enter a launch count", Toast.LENGTH_SHORT).show()
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
                        "Launch Count",
                        "$launchCount",
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
                        "Launch Count",
                        "$launchCount",
                        days.toString(),
                        intent.getStringExtra("profileName"),
                        true,
                        motivationalText
                    )

                    blockDatabase.addAllItemsToNewProfileSchedule(
                        launch,
                        noti,
                        intent.getStringExtra("profileName"),
                        "Launch Count",
                        "$launchCount",
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