package com.motivemobileapp.task

import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Button
import android.widget.TextView
import com.motivemobileapp.*
import com.motivemobileapp.description.DescriptionEditActivity
import com.motivemobileapp.model.Config
import com.motivemobileapp.model.Task
import com.motivemobileapp.model.Token
import com.motivemobileapp.model.UpdateTaskRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class TaskEditActivity : AppCompatActivity() {

    val TASK_DESCRIPTION_EDIT_ACTICITY_REQUEST_CODE = 2

    private val calendar: Calendar = Calendar.getInstance()

    private var task: Task? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = resources.getText(R.string.title_edit_task)
        setContentView(R.layout.activity_task_edit)

        val view: View = findViewById(android.R.id.content)
        val token: Token = TokenStorage(this).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(this))

        task = intent.extras!!.get("task") as Task
        val taskName: TextView = findViewById(R.id.name)
        taskName.text = task!!.name
        taskName.setOnEditorActionListener { textView, actionId, event ->
            if (Keyboard.enterPressed(actionId, event)) {
                val updatedName = textView.text.toString()
                if (updatedName.isBlank()) {
                    taskName.text = task!!.name
                } else {
                    repository.updateTask(token, task!!.id, UpdateTaskRequest(updatedName))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ task ->
                            this.task = Task(this.task!!.id, task.name, this.task!!.description, this.task!!.dueDate, this.task!!.closed)
                        }, { Log.e("Tasks", "Error happened $it") })
                }
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }

        val dueDate: TextView = findViewById(R.id.dueDate)

        if (task!!.dueDate != null) {
            calendar.time = task!!.dueDate
            updateLabel()
        }

        val onDateSetListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
            repository.updateTask(token, task!!.id, UpdateTaskRequest(calendar.time))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    this.task = Task(this.task!!.id, this.task!!.name, this.task!!.description, it.dueDate, this.task!!.closed)
                }, { Log.e("Tasks", "Error happened $it") })
        }

        dueDate.setOnClickListener {
            DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val deleteDueDate: Button = findViewById(R.id.deleteDueDate)
        deleteDueDate.setOnClickListener {
            repository.updateTask(token, task!!.id, UpdateTaskRequest(true))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    task = Task(this.task!!.id, this.task!!.name, this.task!!.description, null, this.task!!.closed)
                    dueDate.text = ""
                }, { Log.e("Tasks", "Error happened $it") })
        }

        val descriptionView: TextView = findViewById(R.id.description)
        descriptionView.text = task!!.description
        descriptionView.setOnClickListener {
            val intent = Intent(this@TaskEditActivity, DescriptionEditActivity::class.java)
            intent.putExtra("task", task)
            startActivityForResult(intent, TASK_DESCRIPTION_EDIT_ACTICITY_REQUEST_CODE)
        }
    }

    private fun updateLabel() {
        val dueDate: TextView = findViewById(R.id.dueDate)
        dueDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(calendar.time)
    }

    override fun onSupportNavigateUp(): Boolean {
        val returnIntent = Intent(this@TaskEditActivity, MainActivity::class.java)
        returnIntent.putExtra("updatedTask", task)
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
            val task: Task = intent.extras!!.get("task") as Task
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_DESCRIPTION_EDIT_ACTICITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val description: String = data!!.getStringExtra("description")
            task = Task(this.task!!.id, this.task!!.name, description, this.task!!.dueDate, this.task!!.closed)
            val descriptionView: TextView = findViewById(R.id.description)
            descriptionView.text = task!!.description
        }
    }
}
