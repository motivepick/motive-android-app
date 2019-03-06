package com.motivepick.motive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launchIntent = Intent()
        val launchActivity = try {
            Class.forName(getScreenClassName())
        } catch (e: ClassNotFoundException) {
            LoginActivity::class.java
        }

        launchIntent.setClass(applicationContext, launchActivity)
        startActivity(launchIntent)

        finish()
    }

    private fun getScreenClassName(): String {
        val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")!!
        return if (token.isBlank()) LoginActivity::class.java.name else TasksActivity::class.java.name
    }
}
