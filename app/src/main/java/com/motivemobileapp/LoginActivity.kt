package com.motivemobileapp

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.motivemobileapp.model.Config
import com.motivemobileapp.model.Token

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val config = Config(this)

        val loginVkButton: Button = findViewById(R.id.loginVkButton)
        loginVkButton.setOnClickListener {
            startActivity(Intent(ACTION_VIEW, config.getVkOauth2Url()))
        }

        val loginFacebookButton: Button = findViewById(R.id.loginFacebookButton)
        loginFacebookButton.setOnClickListener {
            startActivity(Intent(ACTION_VIEW, config.getFacebookOauth2Url()))
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == ACTION_VIEW) {
            TokenStorage(this).storeToken(Token(intent.data))
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
