package com.android.mindmaster.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Adapter.AppSelectAdapter
import com.android.mindmaster.Model.AppSelectModel
import com.android.mindmaster.R
import com.android.mindmaster.Utility.UsageUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppSelectActivity : AppCompatActivity() {
    private lateinit var appSelectModelList: List<AppSelectModel>

    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var appSelectLayout: LinearLayout
    private lateinit var loadingLayout: LinearLayout

    private val selectedApps = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_select)

        initializeViews()
        attachListeners()
        getInstalledApps()
    }

    private fun initializeViews() {
        appSelectLayout = findViewById(R.id.layout_select_apps)
        loadingLayout = findViewById(R.id.loading_select_apps)
        recyclerView = findViewById(R.id.app_select_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        saveButton = findViewById(R.id.save_selected_app_button)
        searchEditText = findViewById(R.id.search_app_select)
    }

    private fun attachListeners() {
        saveButton.setOnClickListener {
            val intent = Intent()

            selectedApps.clear()
            appSelectModelList.forEach {
                if (it.selected) {
                    selectedApps.add(it.packageName)
                }
            }

            intent.putStringArrayListExtra("selectedApps", ArrayList(selectedApps))
            setResult(RESULT_OK, intent)
            finish()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filter(text: String) {
        val filteredList = appSelectModelList.filter { it.name.contains(text, ignoreCase = true) }
        (recyclerView.adapter as AppSelectAdapter).updateListSelect(filteredList)
    }

    private fun getInstalledApps() {
        appSelectLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            var apps = UsageUtility.getInstalledAppsSelect(this@AppSelectActivity)
            apps = apps.sortedBy { it.name }
            withContext(Dispatchers.Main) {
                appSelectModelList = apps
                recyclerView.adapter = AppSelectAdapter(appSelectModelList)
                appSelectLayout.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
            }
        }
    }

    fun finish(v: View?) {
        finish()
    }
}
