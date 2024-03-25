package com.android.achievix.Activity

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Adapter.AppBlockAdapter
import com.android.achievix.Model.AppBlockModel
import com.android.achievix.R
import com.android.achievix.Utility.UsageUtil.Companion.getInstalledApps

// TODO: Spinner theme, blocked, on click listener
@Suppress("DEPRECATION")
class AppBlockActivity : AppCompatActivity() {
    lateinit var appList: List<AppBlockModel>
    lateinit var recyclerView: RecyclerView
    private var sort: Array<String?> = arrayOf("Name", "Usage", "Blocked")
    private lateinit var llAppBlock: LinearLayout
    private lateinit var loadingLayout: LinearLayout
    private var sortValue = "Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_block)

        llAppBlock = findViewById(R.id.ll_block_apps)
        loadingLayout = findViewById(R.id.loading_block_apps)

        recyclerView = findViewById(R.id.app_block_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()

        GetInstalledAppsTask(this, this, sortValue).execute()

        val searchView = findViewById<EditText>(R.id.search_app_block)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })

        val spinner = findViewById<Spinner>(R.id.app_block_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sort)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var isFirstTime = true

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (isFirstTime) {
                    isFirstTime = false
                } else {
                    sortValue = parent!!.getItemAtPosition(position).toString()
                    GetInstalledAppsTask(this@AppBlockActivity, this@AppBlockActivity, sortValue)
                        .execute()
                }
            }
        }
    }

    private fun filter(text: String) {
        val filteredList = appList.filter { it.appName.contains(text, ignoreCase = true) }
        (recyclerView.adapter as AppBlockAdapter).updateListBlock(filteredList)
    }


    @SuppressLint("StaticFieldLeak")
    class GetInstalledAppsTask(
        private val activity: AppBlockActivity,
        private val context: Context,
        private val sort: String
    ) : AsyncTask<Void?, Void?, List<AppBlockModel>>() {

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            activity.llAppBlock.visibility = View.GONE
            activity.loadingLayout.visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<AppBlockModel> {
            return getInstalledApps(context, sort, "AppBlockActivity")
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<AppBlockModel>?) {
            if (result != null) {
                activity.appList = result
            }
            activity.recyclerView.adapter = AppBlockAdapter(activity.appList)
            activity.llAppBlock.visibility = View.VISIBLE
            activity.loadingLayout.visibility = View.GONE
        }
    }
}