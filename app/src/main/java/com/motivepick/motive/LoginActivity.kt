package com.motivepick.motive

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val config = Config(this)

        val loginVkButton: Button = findViewById(R.id.loginVkButton)
        loginVkButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, config.getVkOauth2Url()))
        }

        val loginFacebookButton: Button = findViewById(R.id.loginFacebookButton)
        loginFacebookButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, config.getFacebookOauth2Url()))
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            TokenStorage(this).storeToken(Token(intent.data))
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }
}