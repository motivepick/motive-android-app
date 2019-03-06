package com.motivepick.motive

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val editor: SharedPreferences.Editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            editor.remove("token")
            editor.apply()
            finish()
            val mainActivity = Intent(this@TasksActivity, MainActivity::class.java)
            startActivity(mainActivity)
        }

        val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")!!
        Log.i("Token", token)

        val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
        val subscribe: Disposable = repository.searchTasks("SESSION=" + token, false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ tasks ->
                val textView: TextView = findViewById(R.id.textView)
                textView.text = "There are ${tasks.size} tasks"
            }, { error ->
                Log.e("Tasks", "Error happened $error")
            })
    }
}
