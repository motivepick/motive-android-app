package com.motivepick.motive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tasksListView: ListView = findViewById(R.id.tasksListView)
        val tasks: List<String> = listOf(
            "Finally tidy up the kitchen",
            "Buy a birthday present for mom",
            "Finalize the blog post",
            "Transfer money for the new illustration to Ann",
            "Finish the course about machine learning",
            "Buy the book of Simple Programmer",
            "Order CV from the CV writing service",
            "Buy some black tea",
            "Finish the Estonian homework",
            "Call to the agent to schedule the apartments overview",
            "Send documents related to a new permit"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        tasksListView.adapter = adapter
        tasksListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(applicationContext, tasks[position], Toast.LENGTH_SHORT).show()
        }
    }
}

