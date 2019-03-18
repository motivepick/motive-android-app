package com.motivepick.motive

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


class TaskEditActivity : AppCompatActivity() {

    private val calendar: Calendar = Calendar.getInstance()

    private var task: TaskViewItem? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_task_edit)

        val view: View = findViewById(android.R.id.content)
        val token: Token = TokenStorage(this).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(this))

        this.task = intent.extras!!.get("task") as TaskViewItem
        val taskName: TextView = findViewById(R.id.taskName)
        taskName.text = task!!.name
        taskName.setOnEditorActionListener { textView, actionId, event ->
            if (Keyboard.enterPressed(actionId, event)) {
                repository.updateTask(token, task!!.id, UpdateTaskRequest(textView.text.toString()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ task ->
                        this.task = TaskViewItem(this.task!!.id, task.name, this.task!!.description, this.task!!.dueDate, this.task!!.closed)
                    }, { Log.e("Tasks", "Error happened $it") })
                val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }

        val dueDate: TextView = findViewById(R.id.editText2)

        if (task!!.dueDate != null) {
            calendar.time = task!!.dueDate
            updateLabel()
        }

        val onDateSetListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            this.task = TaskViewItem(this.task!!.id, this.task!!.name, this.task!!.description, calendar.time, this.task!!.closed)
            updateLabel()
        }

        dueDate.setOnClickListener {
            DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val taskDescription: TextView = findViewById(R.id.textView)
        taskDescription.text = task!!.description
        taskDescription.setOnClickListener {
            val intent = Intent(this@TaskEditActivity, DescriptionEditActivity::class.java)
            intent.putExtra("task", task)
            startActivityForResult(intent, 2)
        }
    }

    private fun updateLabel() {
        val dueDate: TextView = findViewById(R.id.editText2)
        dueDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(calendar.time)
    }

    override fun onSupportNavigateUp(): Boolean {
        val returnIntent = Intent(this@TaskEditActivity, MainActivity::class.java)
        returnIntent.putExtra("updatedTask", this.task)
        setResult(RESULT_OK, returnIntent)
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
