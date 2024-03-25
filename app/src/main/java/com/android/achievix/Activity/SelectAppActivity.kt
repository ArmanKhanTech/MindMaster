package com.android.achievix.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
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
import com.android.achievix.Adapter.AppSelectAdapter
import com.android.achievix.Model.AppSelectModel
import com.android.achievix.R
import com.android.achievix.Utility.UsageUtil

// TODO: Selection disappears on scrolling
class SelectAppActivity : AppCompatActivity() {
    private lateinit var appList: List<AppSelectModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button
    private val selectedApps = mutableListOf<String>()
    private lateinit var llAppBlock: LinearLayout
    private lateinit var loadingLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_app)

        llAppBlock = findViewById(R.id.ll_select_apps)
        loadingLayout = findViewById(R.id.loading_select_apps)

        recyclerView = findViewById(R.id.app_select_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        button = findViewById(R.id.save_selected_app_button)
        button.setOnClickListener {
            val intent = Intent()
            selectedApps.clear()
            appList.forEach {
                if (it.selected) {
                    selectedApps.add(it.packageName)
                }
            }
            intent.putStringArrayListExtra("selectedApps", ArrayList(selectedApps))
            setResult(RESULT_OK, intent)
            finish()
        }

        val searchView = findViewById<EditText>(R.id.search_app_select)
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

        GetInstalledAppsTask(this, this).execute()
    }

    private fun filter(text: String) {
        val filteredList = appList.filter { it.name.contains(text, ignoreCase = true) }
        (recyclerView.adapter as AppSelectAdapter).updateListSelect(filteredList)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    class GetInstalledAppsTask(
        private val activity: SelectAppActivity,
        private val context: Context
    ) : AsyncTask<Void?, Void?, List<AppSelectModel>>() {

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            activity.llAppBlock.visibility = View.GONE
            activity.loadingLayout.visibility = View.VISIBLE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<AppSelectModel> {
            return UsageUtil.getInstalledAppsForSelection(context)
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<AppSelectModel>?) {
            if (result != null) {
                activity.appList = result
            }
            activity.recyclerView.adapter = AppSelectAdapter(activity.appList)
            activity.llAppBlock.visibility = View.VISIBLE
            activity.loadingLayout.visibility = View.GONE
        }
    }
}
