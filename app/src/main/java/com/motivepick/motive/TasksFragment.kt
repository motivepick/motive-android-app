package com.motivepick.motive

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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

    val TASK_EDIT_ACTICITY_REQUEST_CODE = 1

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val token: Token = TokenStorage(activity).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(activity!!))

        val tasksRecyclerView: RecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText) as EditText
        taskNameEditText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.action == KeyEvent.ACTION_DOWN)) {
                repository.createTask(token, Task(null, textView.text.toString(), null, false))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ task ->
                        (tasksRecyclerView.adapter as TasksAdapter).handleTaskCreateSuccess(TaskViewItem.createFrom(task))
                        textView.text = ""
                        tasksRecyclerView.scrollToPosition(0)
                    }, { Log.e("Tasks", "Error happened $it") })
                val manager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }

        repository.searchTasks(token, false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ tasks ->
                tasksRecyclerView.adapter = TasksAdapter(tasks.map { TaskViewItem.createFrom(it) }, {
                    handleTaskClose(token, repository, it) { taskViewItem ->
                        (tasksRecyclerView.adapter as TasksAdapter).handleTaskCloseSuccess(taskViewItem)
                    }
                }, ::handleTaskClick)
            }, { Log.e("Tasks", "Error happened $it") })
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_EDIT_ACTICITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val tasksRecyclerView: RecyclerView = activity!!.findViewById(R.id.tasksRecyclerView)
            val id: Long = data!!.getLongExtra("deletedTaskId", Long.MIN_VALUE)
            if (id != Long.MIN_VALUE) {
                (tasksRecyclerView.adapter as TasksAdapter).handleTaskDeleteSuccess(id)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun handleTaskClose(token: Token, repository: TaskRepository, item: TaskViewItem, onSuccess: (TaskViewItem) -> Unit) {
        if (item.closed) repository.undoCloseTask(token, item.id) else repository.closeTask(token, item.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ task -> onSuccess(TaskViewItem.createFrom(task)) }, { Log.e("Tasks", "Error happened $it") })
    }

    private fun handleTaskClick(task: TaskViewItem) {
        val intent = Intent(activity, TaskEditActivity::class.java)
        intent.putExtra("task", task)
        startActivityForResult(intent, TASK_EDIT_ACTICITY_REQUEST_CODE)
    }

    companion object {
        fun newInstance(): TasksFragment = TasksFragment()
    }
}
