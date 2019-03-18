package com.motivepick.motive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class DescriptionEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_description_edit)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
