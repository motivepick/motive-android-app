package com.motivemobileapp.description

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.motivemobileapp.R
import com.motivemobileapp.TaskRepository
import com.motivemobileapp.TaskRepositoryFactory
import com.motivemobileapp.TokenStorage
import com.motivemobileapp.common.Callback.callback
import com.motivemobileapp.model.Config
import com.motivemobileapp.model.Task
import com.motivemobileapp.model.Token
import com.motivemobileapp.model.UpdateTaskRequest
import com.motivemobileapp.task.TaskEditActivity

class DescriptionEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = resources.getText(R.string.title_edit_description)
        setContentView(R.layout.activity_description_edit)

        val task = intent.extras!!.get("task") as Task
        val description: EditText = findViewById(R.id.editText)
        description.setText(task.description)
        description.requestFocus()
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun onSupportNavigateUp(): Boolean {
        val description: EditText = findViewById(R.id.editText)
        val item = intent.extras!!.get("task") as Task
        val token: Token = TokenStorage(this).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(this))
        repository.updateTask(token, item.id, UpdateTaskRequest(null, description.text.toString()))
            .enqueue(callback(
                { task ->
                    val returnIntent = Intent(this@DescriptionEditActivity, TaskEditActivity::class.java)
                    returnIntent.putExtra("description", task!!.description)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            ))
        return true
    }

    override fun onPause() {
        super.onPause()
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val description: EditText = findViewById(R.id.editText)
        manager.hideSoftInputFromWindow(description.windowToken, 0)
    }
}
