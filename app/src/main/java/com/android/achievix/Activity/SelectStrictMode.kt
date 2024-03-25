package com.android.achievix.Activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Adapter.OnTimeClickListener
import com.android.achievix.Adapter.TimeLineAdapter
import com.android.achievix.Model.TimeLineModel
import com.android.achievix.R
import com.android.achievix.Utility.ItemStatus
import java.util.Objects

class SelectStrictMode : AppCompatActivity() {
    private lateinit var adapter: TimeLineAdapter
    private val dataList = ArrayList<TimeLineModel>()
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_strict_mode)

        setDataListItems()
        initRecyclerView()
    }

    private fun setDataListItems() {
        dataList.add(
            TimeLineModel(
                "Blocking Level",
                "Select what all will be blocked",
                ItemStatus.ACTIVE
            )
        )
        dataList.add(
            TimeLineModel(
                "Set Password",
                "Required for accessing Achievix",
                ItemStatus.COMPLETED
            )
        )
        dataList.add(
            TimeLineModel(
                "Activate Device Admin",
                "Grant device admin rights to Achievix",
                ItemStatus.COMPLETED
            )
        )
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_mode_strict)
        recyclerView.layoutManager = layoutManager
        adapter = TimeLineAdapter(dataList, object : OnTimeClickListener {
            override fun onTimeClick(position: Int) {
                when (position) {
                    0 -> {
                        val dialog = Dialog(this@SelectStrictMode)
                        dialog.setContentView(R.layout.strict_level_dialog)
                        dialog.setCancelable(true)
                        Objects.requireNonNull(
                            dialog
                                .window
                        )
                        dialog.window?.setLayout(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val one = dialog.findViewById<LinearLayout>(R.id.strict_level_one)
                        val two = dialog.findViewById<LinearLayout>(R.id.strict_level_two)
                        val three = dialog.findViewById<LinearLayout>(R.id.strict_level_three)
                        val four = dialog.findViewById<LinearLayout>(R.id.strict_level_four)

                        one.setOnClickListener { view1: View? ->
                            dialog.dismiss()
                        }

                        two.setOnClickListener { view1: View? ->
                            dialog.dismiss()
                        }

                        three.setOnClickListener { view1: View? ->
                            dialog.dismiss()
                        }

                        four.setOnClickListener { view1: View? ->
                            dialog.dismiss()
                        }

                        dialog.show()
                    }

                    1 -> {
                        Toast.makeText(
                            this@SelectStrictMode,
                            "Set Password",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    2 -> {
                        Toast.makeText(
                            this@SelectStrictMode,
                            "Activate Device Admin",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            this@SelectStrictMode,
                            "Unknown",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        recyclerView.adapter = adapter
    }
}