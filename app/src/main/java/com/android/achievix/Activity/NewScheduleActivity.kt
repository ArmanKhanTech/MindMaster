package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.achievix.R

class NewScheduleActivity : AppCompatActivity() {
    private var name: String? = null
    private var packageName: String? = null
    private var type: String? = null
    private var profileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        val intent = intent
        val caller = intent.getStringExtra("caller")

        if(caller != "profile") {
            name = intent.getStringExtra("name")
            packageName = intent.getStringExtra("packageName")
            type = intent.getStringExtra("type")
        } else {
            profileName = intent.getStringExtra("profileName")
        }

        val usageLimitButton: LinearLayout = findViewById(R.id.usage_limit_button)
        usageLimitButton.setOnClickListener {
            Intent(this, UsageTimeActivity::class.java).also {
                if (caller != "profile") {
                    it.putExtra("name", name)
                    it.putExtra("packageName", packageName)
                    it.putExtra("type", type)
                } else {
                    it.putExtra("profileName", profileName)
                }
                startActivity(it)
            }
        }

        val specificTimeButton: LinearLayout = findViewById(R.id.specific_time_button)
        specificTimeButton.setOnClickListener {
            Intent(this, SpecificTimeActivity::class.java).also {
                if (caller != "profile") {
                    it.putExtra("name", name)
                    it.putExtra("packageName", packageName)
                    it.putExtra("type", type)
                } else {
                    it.putExtra("profileName", profileName)
                }
                startActivity(it)
            }
        }

        val quickBlockButton: LinearLayout = findViewById(R.id.quick_block_button)
        quickBlockButton.setOnClickListener {
            Intent(this, QuickBlockActivity::class.java).also {
                if (caller != "profile") {
                    it.putExtra("name", name)
                    it.putExtra("packageName", packageName)
                    it.putExtra("type", type)
                } else {
                    it.putExtra("profileName", profileName)
                }
                startActivity(it)
            }
        }

        val launchBlockButton: LinearLayout = findViewById(R.id.launch_block_button)
        launchBlockButton.setOnClickListener {
            Intent(this, NoOfLaunchesActivity::class.java).also {
                if (caller != "profile") {
                    it.putExtra("name", name)
                    it.putExtra("packageName", packageName)
                    it.putExtra("type", type)
                } else {
                    it.putExtra("profileName", profileName)
                }
                startActivity(it)
            }
        }

        val fixedBlockButton: LinearLayout = findViewById(R.id.wait_timer_button)
        fixedBlockButton.setOnClickListener {
            Intent(this, FixedBlockActivity::class.java).also {
                if (caller != "profile") {
                    it.putExtra("name", name)
                    it.putExtra("packageName", packageName)
                    it.putExtra("type", type)
                } else {
                    it.putExtra("profileName", profileName)
                }
                startActivity(it)
            }
        }
    }
}