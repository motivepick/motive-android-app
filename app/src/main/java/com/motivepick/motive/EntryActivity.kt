package com.motivepick.motive

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class EntryActivity : AppCompatActivity() {

    private val NO_ANIMATION = 0

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
        overridePendingTransition(NO_ANIMATION, NO_ANIMATION)

        finish()
    }

    private fun getScreenClassName(): String =
        if (TokenStorage(this).getToken().isBlank()) LoginActivity::class.java.name else MainActivity::class.java.name
}
