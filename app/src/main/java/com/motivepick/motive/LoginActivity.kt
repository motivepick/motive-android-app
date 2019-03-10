package com.motivepick.motive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginVkButton: Button = findViewById(R.id.loginVkButton)
        loginVkButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.motivepick.com/oauth2/authorization/vk?mobile")))
        }

        val loginFacebookButton: Button = findViewById(R.id.loginFacebookButton)
        loginFacebookButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.motivepick.com/oauth2/authorization/facebook?mobile")))
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val url: String = intent.data!!.toString()
            val token = url.replace("motive://", "").replace("#_=_", "")
            TokenService(this).storeToken(token)
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }
}
