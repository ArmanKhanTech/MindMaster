package com.android.achievix.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.achievix.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DrawOnTopScreenActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_on_top_screen)

        val pauseButton = findViewById<ImageButton>(R.id.pause_button)
        val callButton = findViewById<ImageButton>(R.id.call_button)

        val timing = findViewById<TextView>(R.id.timing)

        val intent = intent
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val pause = intent.getBooleanExtra("pause", false)
        val call = intent.getBooleanExtra("call", false)

        if(hour <= Calendar.getInstance()[Calendar.HOUR_OF_DAY] &&
            minute <= Calendar.getInstance()[Calendar.MINUTE]
        ) {
            timing.text = "Your break is over now!"
        } else {
            timing.text = "Until : " + convertTo12HourFormat(hour, minute)
        }

        if(pause) {
            pauseButton.visibility = VISIBLE
        } else {
            pauseButton.visibility = GONE
        }

        if(call) {
            callButton.visibility = VISIBLE
        } else {
            callButton.visibility = GONE
        }

        pauseButton.setOnClickListener {
            timing.text = "You stopped the break!"

            val sh: SharedPreferences = getSharedPreferences("takeBreak", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sh.edit()
            editor.putInt("hour", 0)
            editor.putInt("minute", 0)
            editor.putBoolean("pause", false)
            editor.putBoolean("call", false)
            editor.putBoolean("notification", false)
            editor.apply()
        }

        callButton.setOnClickListener {
            Intent(Intent.ACTION_DIAL).also {
                startActivity(it)
            }
        }
    }

    private fun convertTo12HourFormat(hour: Int, minute: Int): String? {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse("$hour:$minute")
        return date?.let { outputFormat.format(it) }
    }
}