package com.android.achievix.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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

class SelectLockMode : AppCompatActivity() {
    private lateinit var adapter: TimeLineAdapter
    private val dataList = ArrayList<TimeLineModel>()
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_lock_mode)

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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_mode_lock)
        recyclerView.layoutManager = layoutManager

        adapter = TimeLineAdapter(dataList, object : OnTimeClickListener {
            override fun onTimeClick(position: Int) {
                when (position) {
                    0 -> {
                        val dialog = Dialog(this@SelectLockMode)
                        dialog.setContentView(R.layout.block_level_dialog)
                        dialog.setCancelable(true)
                        Objects.requireNonNull(
                            dialog
                                .window
                        )
                        dialog.window?.setLayout(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val one = dialog.findViewById<LinearLayout>(R.id.lock_level_one)
                        val two = dialog.findViewById<LinearLayout>(R.id.lock_level_two)
                        val three = dialog.findViewById<LinearLayout>(R.id.lock_level_three)

                        one.setOnClickListener {
                            dialog.dismiss()
                        }

                        two.setOnClickListener {
                            dialog.dismiss()
                        }

                        three.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.show()
                    }

                    1 -> {
                        Intent(this@SelectLockMode, NewPasswordActivity::class.java).also {
                            startActivity(it)
                        }
                    }

                    2 -> {
                        Toast.makeText(
                            this@SelectLockMode,
                            "Activate Device Admin",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        recyclerView.adapter = adapter
    }
}