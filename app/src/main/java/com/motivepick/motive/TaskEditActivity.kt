package com.motivepick.motive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_task_edit)

        val task: TaskViewItem = intent.extras!!.get("task") as TaskViewItem
        val taskName: TextView = findViewById(R.id.taskName)
        taskName.text = task.name
        val taskDescription: TextView = findViewById(R.id.taskDescription)
        taskDescription.text = task.description
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.deleteTaskMenuItem) {
            val task: TaskViewItem = intent.extras!!.get("task") as TaskViewItem
            val token: Token = TokenStorage(this).getToken()
            val repository: TaskRepository = TaskRepositoryFactory.create(Config(this))
            repository.deleteTask(token, task.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val returnIntent = Intent(this@TaskEditActivity, MainActivity::class.java)
                    returnIntent.putExtra("deletedTaskId", task.id)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, { error -> Log.e("Tasks", "Error happened $error") })
            true
        } else {
            super.onOptionsItemSelected(item)
        }
}
