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

class BlockDataActivity : AppCompatActivity() {
    private lateinit var mobileDataSwitch: SwitchCompat
    private lateinit var wifiSwitch: SwitchCompat
    private lateinit var monRadioButton: RadioButton
    private lateinit var tueRadioButton: RadioButton
    private lateinit var wedRadioButton: RadioButton
    private lateinit var thuRadioButton: RadioButton
    private lateinit var friRadioButton: RadioButton
    private lateinit var satRadioButton: RadioButton
    private lateinit var sunRadioButton: RadioButton
    private lateinit var usageDataEditText: EditText
    private lateinit var textEditText: EditText
    private lateinit var saveButton: Button

    private lateinit var blockDatabase: BlockDatabase
    private val days = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_data)

        val intent = intent
        val name = intent.getStringExtra("name")
        val packageName = intent.getStringExtra("packageName")

        initializeViews()
        attachListeners(name, packageName)
    }

    private fun initializeViews() {
        mobileDataSwitch = findViewById(R.id.block_mobile_data)
        wifiSwitch = findViewById(R.id.block_wifi_data)
        monRadioButton = findViewById(R.id.monday)
        tueRadioButton = findViewById(R.id.tuesday)
        wedRadioButton = findViewById(R.id.wednesday)
        thuRadioButton = findViewById(R.id.thursday)
        friRadioButton = findViewById(R.id.friday)
        satRadioButton = findViewById(R.id.saturday)
        sunRadioButton = findViewById(R.id.sunday)
        usageDataEditText = findViewById(R.id.data_usage)
        textEditText = findViewById(R.id.block_data_text)
        saveButton = findViewById(R.id.block_data_button)

        blockDatabase = BlockDatabase(this)

        mobileDataSwitch.isChecked = true
        wifiSwitch.isChecked = true
    }

    private fun attachListeners(name: String?, packageName: String?) {
        saveButton.setOnClickListener {
            val data = usageDataEditText.text.toString().toIntOrNull()

            val text = textEditText.text.toString().let {
                it.ifEmpty {
                    null
                } ?: it
            }

            val mobile = mobileDataSwitch.isChecked
            val wifi = wifiSwitch.isChecked

            if (data == null || data <= 0) {
                Toast.makeText(this, "Please enter data usage", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (days.isEmpty()) {
                Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!mobile && !wifi) {
                Toast.makeText(this, "Please select at least one option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            blockDatabase.addRecord(
                name,
                packageName,
                "internet",
                mobile,
                wifi,
                "Block Data",
                "$data",
                days.toString(),
                null,
                true,
                text
            )

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