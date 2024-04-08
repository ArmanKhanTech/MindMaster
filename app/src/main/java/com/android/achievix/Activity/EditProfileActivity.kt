package com.android.achievix.Activity

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
import com.android.achievix.Adapter.ProfileItemAdapter
import com.android.achievix.Adapter.ProfileScheduleAdapter
import com.android.achievix.Database.BlockDatabase
import com.android.achievix.Model.ProfileItemModel
import com.android.achievix.Model.ProfileScheduleModel
import com.android.achievix.R
import com.android.achievix.Utility.AccessibilityUtil
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity() {
    private lateinit var scheduleAdapter: ProfileScheduleAdapter
    private lateinit var profileName: TextView
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var noSchedule: TextView
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var addScheduleButton: ImageButton
    private lateinit var addAppButton: ImageButton
    private lateinit var noApp: TextView
    private lateinit var webRecyclerView: RecyclerView
    private lateinit var noWeb: TextView
    private lateinit var addWebButton: ImageButton
    private lateinit var keyRecyclerView: RecyclerView
    private lateinit var noKey: TextView
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
        updateText()
    }

    private fun initializeView() {
        profileName = findViewById(R.id.edit_profile_name)
        profileName.text = intent.getStringExtra("profileName")
        scheduleRecyclerView = findViewById(R.id.edit_profile_schedule_list)
        noSchedule = findViewById(R.id.no_schedule)
        appRecyclerView = findViewById(R.id.edit_profile_block_list_app)
        addScheduleButton = findViewById(R.id.add_schedule)
        addAppButton = findViewById(R.id.add_app)
        noApp = findViewById(R.id.no_app)
        webRecyclerView = findViewById(R.id.edit_profile_block_list_web)
        noWeb = findViewById(R.id.no_web)
        addWebButton = findViewById(R.id.add_web)
        keyRecyclerView = findViewById(R.id.edit_profile_block_list_key)
        noKey = findViewById(R.id.no_key)
        addKeyButton = findViewById(R.id.add_key)
        doneButton = findViewById(R.id.save_edit_profile_button)
    }

    private fun initScheduleRecyclerView() {
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

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun attachListeners() {
        addScheduleButton.setOnClickListener {
            val intent = Intent(this, NewScheduleActivity::class.java)
            intent.putExtra("caller", "profile")
            intent.putExtra("profileName", profileName.text.toString())
            startActivity(intent)
        }

        addAppButton.setOnClickListener {
            val intent = Intent(this, AppSelectActivity::class.java)
            startActivityForResult(intent, 100)
        }

        addWebButton.setOnClickListener {
            val dialog = Dialog(this@EditProfileActivity)
            dialog.setContentView(R.layout.dialog_add_web_key)
            dialog.setCancelable(true)

            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val title = dialog.findViewById<TextView>(R.id.dialogTitle)
            title.text = "Add Website"
            val editText = dialog.findViewById<TextView>(R.id.dialogEditText)
            editText.hint = "Enter URL"
            val addWeb = dialog.findViewById<Button>(R.id.dialogButton)

            addWeb.setOnClickListener {
                if (editText.text.toString() == "google.com") {
                    Toast.makeText(this, "Cannot block Google", Toast.LENGTH_SHORT).show()
                } else {
                    if (AccessibilityUtil().isAccessibilitySettingsOn(this)) {
                        if (TextUtils.isEmpty(editText.text.toString())) {
                            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                        } else {
                            val urlPattern =
                                Pattern.compile("^((https?|ftp|smtp)://)?(www.)?[a-z0-9]+(\\.[a-z]{2,}){1,3}(#?/?[a-zA-Z0-9#]+)*/?(\\?[a-zA-Z0-9-_]+=[a-zA-Z0-9-%]+&?)?$")
                            if (!urlPattern.matcher(editText.text.toString())
                                    .matches()
                            ) {
                                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
                            } else if (scheduleModelList.isNotEmpty()) {
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
                                    updateText()
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
        }

        addKeyButton.setOnClickListener {
            val dialog = Dialog(this@EditProfileActivity)
            dialog.setContentView(R.layout.dialog_add_web_key)
            dialog.setCancelable(true)

            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val title = dialog.findViewById<TextView>(R.id.dialogTitle)
            title.text = "Add Keyword"
            val editText = dialog.findViewById<TextView>(R.id.dialogEditText)
            editText.hint = "Enter Keyword"
            val addKey = dialog.findViewById<Button>(R.id.dialogButton)

            addKey.setOnClickListener {
                if (Pattern.matches("^(http|https)://.*", editText.text.toString()) ||
                    Pattern.matches(
                        "^(www\\.)?([a-zA-Z0-9]+\\.)+[a-zA-Z]{2,}$",
                        editText.text.toString()
                    )
                ) {
                    Toast.makeText(this, "Please enter a keyword, not a URL", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (AccessibilityUtil().isAccessibilitySettingsOn(this)) {
                        if (TextUtils.isEmpty(editText.text.toString())) {
                            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
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
                                updateText()
                                dialog.dismiss()
                            }
                        } else {
                            Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        startActivity(i)
                    }
                }
            }

            dialog.show()
        }

        doneButton.setOnClickListener {
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                finish()
            }, 1000)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateText() {
        if (scheduleRecyclerView.adapter!!.itemCount == 0) {
            noSchedule.text = "No Schedule"
        } else {
            noSchedule.text = ""
        }

        if (appRecyclerView.adapter!!.itemCount == 0) {
            noApp.text = "No App"
        } else {
            noApp.text = ""
        }

        if (webRecyclerView.adapter!!.itemCount == 0) {
            noWeb.text = "No Website"
        } else {
            noWeb.text = ""
        }

        if (keyRecyclerView.adapter!!.itemCount == 0) {
            noKey.text = "No Keyword"
        } else {
            noKey.text = ""
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (data != null && scheduleModelList.isNotEmpty()) {
                val selectedApps = data.getStringArrayListExtra("selectedApps")
                for (i in selectedApps!!.indices) {
                    val app = selectedApps[i]
                    val appInfo = packageManager.getApplicationInfo(app, 0)
                    val appName = packageManager.getApplicationLabel(appInfo).toString()

                    for (j in scheduleModelList) {
                        if (app == "com.android.achievix") {
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
                updateText()
            } else {
                Toast.makeText(this, "Add a schedule first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun finish(v: View?) {
        finish()
    }
}