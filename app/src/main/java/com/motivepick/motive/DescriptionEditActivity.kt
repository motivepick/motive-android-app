package com.motivepick.motive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DescriptionEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = resources.getText(R.string.title_edit_description)
        setContentView(R.layout.activity_description_edit)

        val task = intent.extras!!.get("task") as TaskViewItem
        val description: EditText = findViewById(R.id.editText)
        description.setText(task.description)
    }

    @SuppressLint("CheckResult")
    override fun onSupportNavigateUp(): Boolean {
        val description: EditText = findViewById(R.id.editText)
        val item = intent.extras!!.get("task") as TaskViewItem
        val token: Token = TokenStorage(this).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(this))
        repository.updateTask(token, item.id, UpdateTaskRequest(null, description.text.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ task ->
                val returnIntent = Intent(this@DescriptionEditActivity, TaskEditActivity::class.java)
                returnIntent.putExtra("description", task.description)
                setResult(RESULT_OK, returnIntent)
                finish()
            }, { Log.e("Tasks", "Error happened $it") })
        return true
    }
}
