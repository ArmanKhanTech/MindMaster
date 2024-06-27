@file:Suppress("DEPRECATION")

package com.android.mindmaster.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Adapter.AppBlockAdapter
import com.android.mindmaster.Model.AppBlockModel
import com.android.mindmaster.R
import com.android.mindmaster.Utility.UsageUtility.Companion.getInstalledAppsBlock

@Suppress("DEPRECATION")
@SuppressLint("StaticFieldLeak")
class AppBlockActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortSpinner: Spinner
    private lateinit var searchEditText: EditText
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var appBlockLayout: LinearLayout
    private lateinit var loadingLayout: LinearLayout

    private var sortValue = "Name"
    private var appBlockAdapter: AppBlockAdapter? = null
    private var appBlockModelList: List<AppBlockModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_block)

        initializeViews()
        setupRecyclerView()
        setupSearchView()
        setupSpinner()

        GetInstalledAppsTask().execute()
    }

    private fun initializeViews() {
        appBlockLayout = findViewById(R.id.layout_block_apps)
        loadingLayout = findViewById(R.id.loading_block_apps)
        recyclerView = findViewById(R.id.app_block_recycler_view)
        sortSpinner = findViewById(R.id.app_block_spinner)
        searchEditText = findViewById(R.id.search_app_block)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setupSearchView() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSpinner() {
        arrayAdapter = ArrayAdapter(
        this, android.R.layout.simple_spinner_item, arrayOf("Name", "Usage", "Blocked"))
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        sortSpinner.adapter = arrayAdapter
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var isFirstTime = true

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isFirstTime) {
                    sortValue = parent!!.getItemAtPosition(position).toString()
                    GetInstalledAppsTask().execute()
                }
                isFirstTime = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun filter(text: String) {
        val filteredList = appBlockModelList.filter {
            it.appName.contains(text, ignoreCase = true)
        }
        appBlockAdapter?.updateListBlock(filteredList)
    }

    private inner class GetInstalledAppsTask : AsyncTask<Void?, Void?, List<AppBlockModel>>() {
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            appBlockLayout.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<AppBlockModel> {
            return getInstalledAppsBlock(this@AppBlockActivity, sortValue, "AppBlockActivity")
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<AppBlockModel>?) {
            result?.let {
                appBlockModelList = it
                appBlockAdapter = AppBlockAdapter(appBlockModelList)
                recyclerView.adapter = appBlockAdapter

                appBlockLayout.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE

                appBlockAdapter?.setOnItemClickListener(object : AppBlockAdapter.OnItemClickListener {
                    override fun onItemClick(view: View) {
                        val position = recyclerView.getChildAdapterPosition(view)
                        val app = appBlockAdapter?.getItemAt(position)

                        if (app != null) {
                            launchActivity(app.appName, app.packageName, app.blocked!!)
                        }
                    }
                })
            }
        }
    }

    private fun launchActivity(appName: String, packageName: String, blocked: Boolean) {
        val intent = if (!blocked) {
            Intent(this, NewScheduleActivity::class.java).apply {
                putExtra("name", appName)
                putExtra("packageName", packageName)
                putExtra("type", "app")
                putExtra("caller", "appBlock")
            }
        } else {
            Intent(this, EditScheduleActivity::class.java).apply {
                putExtra("name", appName)
                putExtra("packageName", packageName)
                putExtra("type", "app")
            }
        }

        if (packageName == "com.android.mindmaster") {
            Toast.makeText(this, "Cannot block MindMaster", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(intent)
        }
    }

    fun finish(v: View?) {
        finish()
    }
}