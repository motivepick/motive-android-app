package com.motivepick.motive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginVkButton: Button = findViewById(R.id.loginVkButton)
        loginVkButton.setOnClickListener(View.OnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.motivepick.com/oauth2/authorization/vk?mobile"))
            startActivity(intent)
        })

        val loginFacebookButton: Button = findViewById(R.id.loginFacebookButton)
        loginFacebookButton.setOnClickListener(View.OnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.motivepick.com/oauth2/authorization/facebook?mobile"))
            startActivity(intent)
        })
    }
}
