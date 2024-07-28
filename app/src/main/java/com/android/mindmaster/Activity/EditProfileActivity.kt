package com.android.mindmaster.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mindmaster.Adapter.ProfileItemAdapter
import com.android.mindmaster.Adapter.ProfileScheduleAdapter
import com.android.mindmaster.Database.BlockDatabase
import com.android.mindmaster.Model.ProfileItemModel
import com.android.mindmaster.Model.ProfileScheduleModel
import com.android.mindmaster.R
import com.android.mindmaster.Utility.AccessibilityUtility

@SuppressLint("CutPasteId", "SetTextI18n")
@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity() {
    private lateinit var scheduleAdapter: ProfileScheduleAdapter
    private lateinit var profileName: TextView
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var addScheduleButton: ImageButton
    private lateinit var addAppButton: ImageButton
    private lateinit var webRecyclerView: RecyclerView
    private lateinit var addWebButton: ImageButton
    private lateinit var keyRecyclerView: RecyclerView
    private lateinit var addKeyButton: ImageButton
    private lateinit var doneButton: Button

    private var profileItemListApp = ArrayList<ProfileItemModel>()
    private var profileItemListWeb = ArrayList<ProfileItemModel>()
    private var profileItemListKey = ArrayList<ProfileItemModel>()

    private val scheduleModelList: MutableList<ProfileScheduleModel> = ArrayList()
    private val blockDatabase = BlockDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeView()
        initScheduleRecyclerView()
        initAppRecyclerView()
        initWebRecyclerView()
        initKeyRecyclerView()
        attachListeners()
    }

    private fun initializeView() {
        profileName = findViewById(R.id.edit_profile_name)
        profileName.text = intent.getStringExtra("profileName")
        scheduleRecyclerView = findViewById(R.id.edit_profile_schedule_list)
        appRecyclerView = findViewById(R.id.edit_profile_block_list_app)
        addScheduleButton = findViewById(R.id.add_schedule)
        addAppButton = findViewById(R.id.add_app)
        webRecyclerView = findViewById(R.id.edit_profile_block_list_web)
        addWebButton = findViewById(R.id.add_web)
        keyRecyclerView = findViewById(R.id.edit_profile_block_list_key)
        addKeyButton = findViewById(R.id.add_key)
        doneButton = findViewById(R.id.save_edit_profile_button)
    }

    private fun initScheduleRecyclerView() {
        scheduleModelList.clear()
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        scheduleRecyclerView.itemAnimator = DefaultItemAnimator()

        val result = blockDatabase.readProfileSchedule(profileName.text.toString())
        for (i in result.indices) {
            val map = result[i]
            val schedule = ProfileScheduleModel(
                id = map["id"].toString(),
                type = map["type"].toString(),
                launch = map["launch"].toString(),
                notification = map["notification"].toString(),
                scheduleType = map["scheduleType"].toString(),
                scheduleParams = map["scheduleParams"].toString(),
                scheduleDays = map["scheduleDays"].toString(),
                profileName = map["profileName"].toString(),
                profileStatus = map["profileStatus"].toString(),
                text = map["text"].toString()
            )

            if (schedule.type == "null") {
                scheduleModelList.add(schedule)
            }
        }

        scheduleAdapter = ProfileScheduleAdapter(scheduleModelList, this)
        scheduleRecyclerView.adapter = scheduleAdapter
    }

    fun initAppRecyclerView() {
        profileItemListApp.clear()
        appRecyclerView.layoutManager = LinearLayoutManager(this)
        appRecyclerView.itemAnimator = DefaultItemAnimator()

        val result = blockDatabase.readProfileApps(profileName.text.toString())
        for (i in result.indices) {
            val map = result[i]
            val app = ProfileItemModel(
                id = map["id"].toString(),
                name = map["name"].toString(),
                packageName = map["packageName"].toString(),
                type = map["type"].toString(),
                profileName = map["profileName"].toString()
            )
            profileItemListApp.add(app)
        }

        profileItemListApp =
            profileItemListApp.distinctBy { it.name } as ArrayList<ProfileItemModel>

        val appAdapter = ProfileItemAdapter(profileItemListApp, this)
        appRecyclerView.adapter = appAdapter
    }

    fun initWebRecyclerView() {
        profileItemListWeb.clear()
        webRecyclerView.layoutManager = LinearLayoutManager(this)
        webRecyclerView.itemAnimator = DefaultItemAnimator()

        val result = blockDatabase.readProfileWebs(profileName.text.toString())
        for (i in result.indices) {
            val map = result[i]
            val web = ProfileItemModel(
                id = map["id"].toString(),
                name = map["name"].toString(),
                packageName = map["packageName"].toString(),
                type = map["type"].toString(),
                profileName = map["profileName"].toString()
            )
            profileItemListWeb.add(web)
        }

        profileItemListWeb =
            profileItemListWeb.distinctBy { it.name } as ArrayList<ProfileItemModel>

        val webAdapter = ProfileItemAdapter(profileItemListWeb, this)
        webRecyclerView.adapter = webAdapter
    }

    fun initKeyRecyclerView() {
        profileItemListKey.clear()
        keyRecyclerView.layoutManager = LinearLayoutManager(this)
        keyRecyclerView.itemAnimator = DefaultItemAnimator()

        val result = blockDatabase.readProfileKeys(profileName.text.toString())
        for (i in result.indices) {
            val map = result[i]
            val key = ProfileItemModel(
                id = map["id"].toString(),
                name = map["name"].toString(),
                packageName = map["packageName"].toString(),
                type = map["type"].toString(),
                profileName = map["profileName"].toString()
            )
            profileItemListKey.add(key)
        }

        profileItemListKey =
            profileItemListKey.distinctBy { it.name } as ArrayList<ProfileItemModel>

        val keyAdapter = ProfileItemAdapter(profileItemListKey, this)
        keyRecyclerView.adapter = keyAdapter
    }

    private fun attachListeners() {
        addScheduleButton.setOnClickListener {
            val intent = Intent(this, NewScheduleActivity::class.java)
            intent.putExtra("caller", "profile")
            intent.putExtra("profileName", profileName.text.toString())
            startActivity(intent)
        }

        addAppButton.setOnClickListener {
            if (scheduleModelList.isNotEmpty()) {
                val intent = Intent(this, AppSelectActivity::class.java)
                startActivityForResult(intent, 100)
            } else {
                Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT).show()
            }
        }

        addWebButton.setOnClickListener {
            if (scheduleModelList.isNotEmpty()) {
                val dialog = Dialog(this@EditProfileActivity)
                dialog.setContentView(R.layout.dialog_add_web_key)
                dialog.setCancelable(true)

                dialog.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val title = dialog.findViewById<TextView>(R.id.dialog_title)
                title.text = "New Website"

                val editText = dialog.findViewById<TextView>(R.id.dialog_edit_text)
                editText.hint = "eg: youtube"

                val addWeb = dialog.findViewById<Button>(R.id.dialog_button)

                addWeb.setOnClickListener {
                    if (editText.text.toString() == "google") {
                        Toast.makeText(this, "Cannot block google", Toast.LENGTH_SHORT).show()
                    } else {
                        if (AccessibilityUtility().isAccessibilitySettingsOn(this)) {
                            if (TextUtils.isEmpty(editText.text.toString())) {
                                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                if (scheduleModelList.isNotEmpty()) {
                                    val web = editText.text.toString()
                                    if (web.isNotEmpty()) {
                                        for (i in scheduleModelList) {
                                            blockDatabase.addRecord(
                                                web,
                                                null,
                                                "web",
                                                i.launch == "1",
                                                i.notification == "1",
                                                i.scheduleType,
                                                i.scheduleParams,
                                                i.scheduleDays,
                                                profileName.text.toString(),
                                                i.profileStatus == "1",
                                                i.text
                                            )
                                        }

                                        initWebRecyclerView()
                                        dialog.dismiss()
                                    }
                                } else {
                                    Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            startActivity(i)
                        }
                    }
                }

                dialog.show()
            } else {
                Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT).show()
            }
        }

        addKeyButton.setOnClickListener {
            if (scheduleModelList.isNotEmpty()) {
                val dialog = Dialog(this@EditProfileActivity)
                dialog.setContentView(R.layout.dialog_add_web_key)
                dialog.setCancelable(true)

                dialog.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val title = dialog.findViewById<TextView>(R.id.dialog_title)
                title.text = "New Keyword"

                val editText = dialog.findViewById<TextView>(R.id.dialog_edit_text)
                editText.hint = "eg: facebook"

                val addKey = dialog.findViewById<Button>(R.id.dialog_button)

                addKey.setOnClickListener {
                    if (AccessibilityUtility().isAccessibilitySettingsOn(this)) {
                        if (TextUtils.isEmpty(editText.text.toString())) {
                            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT)
                                .show()
                        } else if (scheduleModelList.isNotEmpty()) {
                            val key = editText.text.toString()
                            if (key.isNotEmpty()) {
                                for (i in scheduleModelList) {
                                    blockDatabase.addRecord(
                                        key,
                                        null,
                                        "key",
                                        i.launch == "1",
                                        i.notification == "1",
                                        i.scheduleType,
                                        i.scheduleParams,
                                        i.scheduleDays,
                                        profileName.text.toString(),
                                        i.profileStatus == "1",
                                        i.text
                                    )
                                }

                                initKeyRecyclerView()
                                dialog.dismiss()
                            }
                        } else {
                            Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        startActivity(i)
                    }
                }

                dialog.show()
            } else {
                Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT).show()
            }
        }

        doneButton.setOnClickListener {
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                finish()
            }, 1000)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (data != null) {
                if (scheduleModelList.isNotEmpty()) {
                    val selectedApps = data.getStringArrayListExtra("selectedApps")
                    for (i in selectedApps!!.indices) {
                        val app = selectedApps[i]
                        val appInfo = packageManager.getApplicationInfo(app, 0)
                        val appName = packageManager.getApplicationLabel(appInfo).toString()

                        for (j in scheduleModelList) {
                            if (app == "com.android.mindmaster") {
                                Toast
                                    .makeText(this, "Cannot block MindMaster", Toast.LENGTH_SHORT)
                                    .show()
                                continue
                            } else {
                                blockDatabase.addRecord(
                                    appName,
                                    app,
                                    "app",
                                    j.launch == "1",
                                    j.notification == "1",
                                    j.scheduleType,
                                    j.scheduleParams,
                                    j.scheduleDays,
                                    profileName.text.toString(),
                                    j.profileStatus == "1",
                                    j.text
                                )
                            }
                        }
                    }

                    initAppRecyclerView()
                }
            }
        }
    }

    fun finish(v: View?) {
        finish()
    }
}