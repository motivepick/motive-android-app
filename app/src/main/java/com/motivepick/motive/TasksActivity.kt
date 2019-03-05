package com.motivepick.motive

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val editor: SharedPreferences.Editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            editor.remove("token")
            editor.apply()
            finish()
            val mainActivity = Intent(this@TasksActivity, MainActivity::class.java)
            startActivity(mainActivity)
        }
    }
}
