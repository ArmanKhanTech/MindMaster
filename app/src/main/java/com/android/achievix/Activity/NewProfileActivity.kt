package com.android.achievix.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.achievix.Adapter.ProfileAdapter
import com.android.achievix.Database.BlockDatabase
import com.android.achievix.Model.ProfileModel
import com.android.achievix.R

class NewProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_profile)

        val blockDatabase = BlockDatabase(this)
        val newProfile: EditText = findViewById(R.id.add_profile)
        findViewById<Button>(R.id.add_profile_button).setOnClickListener {
            if (newProfile.text.isNotEmpty()) {
                blockDatabase.addRecord(
                    null,
                    null,
                    "profile",
                    false,
                    false,
                    null,
                    null,
                    null,
                    newProfile.text.toString(),
                    true,
                    " "
                )

                Toast.makeText(this, "Profile added", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1000)
            } else {
                Toast.makeText(this, "Please enter a profile name", Toast.LENGTH_SHORT).show()
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.profiles_list_new_profile)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()

        val list = blockDatabase.readAllProfiles()
        var profileModelList = mutableListOf<ProfileModel>()

        for (i in 0 until list.size) {
            profileModelList.add(
                ProfileModel(
                    id = list[i]["id"].toString(),
                    profileName = list[i]["profileName"].toString(),
                    status = list[i]["profileStatus"].toString()
                )
            )
        }

        val profileAdapter = ProfileAdapter(profileModelList)
        recyclerView.adapter = profileAdapter

        profileAdapter.setOnItemClickListener(object : ProfileAdapter.OnItemClickListener {
            override fun onItemClick(view: View) {
                val position = recyclerView.getChildLayoutPosition(view)
                val profileModel = profileAdapter.getItemAt(position)
                val intent = Intent(this@NewProfileActivity, EditProfileActivity::class.java)
                intent.putExtra("profileId", profileModel.id)
                startActivity(intent)
            }
        })

        if (profileModelList.isEmpty()) {
            findViewById<View>(R.id.no_profile_added_text).visibility = View.VISIBLE
        }
    }
}