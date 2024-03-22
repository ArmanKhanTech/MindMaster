package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.achievix.R

class NewScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        val intent = intent
        var profileName = intent.getStringExtra("profile_name")

        val usageLimitButton: LinearLayout = findViewById(R.id.usage_limit_button)
        usageLimitButton.setOnClickListener {
            Intent(this, UsageTimeActivity::class.java).also {
                startActivity(it)
            }
        }

        val specificTimeButton: LinearLayout = findViewById(R.id.specific_time_button)
        specificTimeButton.setOnClickListener {
            Intent(this, SpecificTimeActivity::class.java).also {
                startActivity(it)
            }
        }

        val quickBlockButton: LinearLayout = findViewById(R.id.quick_block_button)
        quickBlockButton.setOnClickListener {
            Intent(this, QuickBlockActivity::class.java).also {
                startActivity(it)
            }
        }

        var launchBlockButton: LinearLayout = findViewById(R.id.launch_block_button)
        launchBlockButton.setOnClickListener {
            Intent(this, NoOfLaunchesActivity::class.java).also {
                startActivity(it)
            }
        }

        val waitTimerButton: LinearLayout = findViewById(R.id.wait_timer_button)
        waitTimerButton.setOnClickListener {
            Intent(this, FixedBlockActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}