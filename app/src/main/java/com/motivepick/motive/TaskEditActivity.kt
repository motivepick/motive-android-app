package com.motivepick.motive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class TaskEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)

        val task: TaskViewItem = intent.extras!!.get("task") as TaskViewItem
        val taskName: TextView = findViewById(R.id.taskName)
        taskName.text = task.name
        val taskDescription: TextView = findViewById(R.id.taskDescription)
        taskDescription.text = task.description
    }
}
