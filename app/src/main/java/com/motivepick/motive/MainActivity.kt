package com.motivepick.motive

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")!!

        if (token.isBlank()) {
            val loginVkButton: Button = findViewById(R.id.loginVkButton)
            loginVkButton.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://api.motivepick.com/oauth2/authorization/vk?mobile"))
                startActivity(intent)
            }

            val loginFacebookButton: Button = findViewById(R.id.loginFacebookButton)
            loginFacebookButton.setOnClickListener {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.motivepick.com/oauth2/authorization/facebook?mobile")
                    )
                startActivity(intent)
            }

            handleIntent(intent)
        } else {
            finish()
            val tasksActivity = Intent(this@MainActivity, TasksActivity::class.java)
            startActivity(tasksActivity)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            val url: String = appLinkData.toString()
            val token = url.replace("motive://", "").replace("#_=_", "")
            Log.i("OPENED", "Opened $token")
            val editor: Editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            editor.putString("token", token)
            editor.apply()
            finish()
            val tasksActivity = Intent(this@MainActivity, TasksActivity::class.java)
            startActivity(tasksActivity)
        }
    }
}
