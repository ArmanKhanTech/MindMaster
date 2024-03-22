package com.android.achievix.Activity

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Adapter.AppSelectAdapter
import com.android.achievix.Model.AppSelectModel
import com.android.achievix.R
import java.util.Calendar

// TODO: Selection disppaers, checkbox shape and color, lazy loading, on click listener
class SelectAppActivity : AppCompatActivity() {
    private lateinit var appList: List<AppSelectModel>
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_app)

        recyclerView = findViewById(R.id.app_select_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        appList = getInstalledApps(this)
        recyclerView.adapter = AppSelectAdapter(appList)

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
    }

    @SuppressLint("ServiceCast", "QueryPermissionsNeeded")
    fun getInstalledApps(context: Context): List<AppSelectModel> {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val beginTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val events = usageStatsManager.queryEvents(beginTime, endTime)
        val event = UsageEvents.Event()

        val usageTimes = mutableMapOf<String, Long>()
        var lastEventTime = 0L
        var lastPackageName = ""

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                lastEventTime = event.timeStamp
                lastPackageName = event.packageName
            } else if (event.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                if (event.packageName == lastPackageName) {
                    val usageTime = usageTimes.getOrDefault(lastPackageName, 0L)
                    usageTimes[lastPackageName] = usageTime + event.timeStamp - lastEventTime
                }
            }
        }

        return apps.mapNotNull { app ->
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                val usageTime = usageTimes.getOrDefault(app.packageName, 0L)
                val appName = app.loadLabel(pm).toString()
                val icon = app.loadIcon(pm)
                AppSelectModel(
                    appName,
                    app.packageName,
                    icon,
                    usageTime.toString()
                )
            } else {
                null
            }
        }.sortedBy { it.name }
    }

    private fun filter(text: String) {
        val filteredList = appList.filter { it.name.contains(text, ignoreCase = true) }
        (recyclerView.adapter as AppSelectAdapter).updateListSelect(filteredList)
    }
}
