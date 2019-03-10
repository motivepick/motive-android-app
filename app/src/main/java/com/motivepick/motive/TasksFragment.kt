package com.motivepick.motive

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksFragment : Fragment() {

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val token: String = TokenService(activity).getToken()
        val repository: TaskRepository = SearchRepositoryProvider.provideSearchRepository()

        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText) as EditText
        taskNameEditText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.action == KeyEvent.ACTION_DOWN)) {
                repository.createTask(token, Task(null, textView.text.toString(), null))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ task -> Log.i("Tasks", "Task created " + task.id) }, { error -> Log.e("Tasks", "Error happened $error") })
                val manager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }

        val tasksRecyclerView: RecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        repository.searchTasks(token, false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ tasks ->
                tasksRecyclerView.adapter = TasksAdapter(tasks.map { TaskViewItem(it.id!!, it.name, it.description ?: "") }) {
                    val intent = Intent(activity, TaskEditActivity::class.java)
                    intent.putExtra("task", it)
                    startActivity(intent)
                }
            }, { error ->
                Log.e("Tasks", "Error happened $error")
            })
        return view
    }

    companion object {
        fun newInstance(): TasksFragment = TasksFragment()
    }
}
