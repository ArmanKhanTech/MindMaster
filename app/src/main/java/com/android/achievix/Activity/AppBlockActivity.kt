package com.android.achievix.Activity

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
import com.android.achievix.Adapter.AppBlockAdapter
import com.android.achievix.Model.AppBlockModel
import com.android.achievix.R
import com.android.achievix.Utility.UsageUtil.Companion.getInstalledAppsBlock

@Suppress("DEPRECATION")
class AppBlockActivity : AppCompatActivity() {
    private lateinit var appList: List<AppBlockModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var searchView: EditText
    private lateinit var adapter: ArrayAdapter<String>
    private var appBlockAdapter: AppBlockAdapter? = null
    private lateinit var llAppBlock: LinearLayout
    private lateinit var loadingLayout: LinearLayout
    private var sortValue = "Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_block)
        initializeViews()
        setupRecyclerView()
        setupSearchView()
        setupSpinner()
        GetInstalledAppsTask(sortValue).execute()
    }

    private fun initializeViews() {
        llAppBlock = findViewById(R.id.ll_block_apps)
        loadingLayout = findViewById(R.id.loading_block_apps)
        recyclerView = findViewById(R.id.app_block_recycler_view)
        spinner = findViewById(R.id.app_block_spinner)
        searchView = findViewById(R.id.search_app_block)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setupSearchView() {
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSpinner() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("Name", "Usage", "Blocked"))
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var isFirstTime = true

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isFirstTime) {
                    sortValue = parent!!.getItemAtPosition(position).toString()
                    GetInstalledAppsTask(sortValue).execute()
                }
                isFirstTime = false
            }
        }
    }

    private fun filter(text: String) {
        val filteredList = appList.filter { it.appName.contains(text, ignoreCase = true) }
        appBlockAdapter?.updateListBlock(filteredList)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetInstalledAppsTask(private val sort: String) : AsyncTask<Void?, Void?, List<AppBlockModel>>() {
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            llAppBlock.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<AppBlockModel> {
            return getInstalledAppsBlock(this@AppBlockActivity, sortValue, "AppBlockActivity")
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<AppBlockModel>?) {
            result?.let {
                appList = it
                appBlockAdapter = AppBlockAdapter(appList)
                recyclerView.adapter = appBlockAdapter
                llAppBlock.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE

                appBlockAdapter?.setOnItemClickListener(object : AppBlockAdapter.OnItemClickListener {
                    override fun onItemClick(view: View) {
                        val position = recyclerView.getChildAdapterPosition(view)
                        val app = appList[position]
                        launchActivity(app.appName, app.packageName)
                    }
                })
            }
        }
    }

    private fun launchActivity(appName: String, packageName: String) {
        val intent = Intent(this, NewScheduleActivity::class.java).apply {
            putExtra("name", appName)
            putExtra("packageName", packageName)
            putExtra("type", "app")
            putExtra("caller", "appBlock")
        }
        if(packageName == "com.android.achievix") {
            Toast.makeText(this, "Cannot block Achievix", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(intent)
        }
    }
}