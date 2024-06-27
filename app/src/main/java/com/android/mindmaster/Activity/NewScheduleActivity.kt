package com.android.mindmaster.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.mindmaster.R

class NewScheduleActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var packageName: String
    private lateinit var type: String
    private lateinit var profileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        val intent = intent
        val caller = intent.getStringExtra("caller")

        if(caller != "profile") {
            name = intent.getStringExtra("name").toString()
            packageName = intent.getStringExtra("packageName").toString()
            type = intent.getStringExtra("type").toString()
            setupListeners(caller, type)
        } else {
            profileName = intent.getStringExtra("profileName")!!
            setupListeners(caller, "profile")
        }
    }

    private fun setupListeners(caller: String?, type: String?) {
        if (type == "app" || type == "profile") {
            findViewById<LinearLayout>(R.id.usage_limit_button).setOnClickListener {
                startNewActivity(caller, UsageTimeActivity::class.java)
            }
        } else {
            findViewById<LinearLayout>(R.id.usage_limit_button).visibility = LinearLayout.GONE
        }

        findViewById<LinearLayout>(R.id.specific_time_button).setOnClickListener {
            startNewActivity(caller, SpecificTimeActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.quick_block_button).setOnClickListener {
            startNewActivity(caller, QuickBlockActivity::class.java)
        }

        if (type == "app" || type == "profile") {
            findViewById<LinearLayout>(R.id.launch_block_button).setOnClickListener {
                startNewActivity(caller, NumberOfLaunchesActivity::class.java)
            }
        } else {
            findViewById<LinearLayout>(R.id.launch_block_button).visibility = LinearLayout.GONE
        }

        findViewById<LinearLayout>(R.id.wait_timer_button).setOnClickListener {
            startNewActivity(caller, FixedBlockActivity::class.java)
        }
    }

    private fun startNewActivity(caller: String?, activityClass: Class<*>) {
        Intent(this, activityClass).also {
            if (caller != "profile") {
                it.putExtra("name", name)
                it.putExtra("packageName", packageName)
                it.putExtra("type", type)
            } else {
                it.putExtra("profileName", profileName)
                it.putExtra("type", "profile")
            }

            startActivity(it)
        }
    }

    fun finish(v: View?) {
        finish()
    }
}