package com.motivepick.motive

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class TasksFragment : Fragment() {

    val TASK_EDIT_ACTICITY_REQUEST_CODE = 1

    private lateinit var model: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(TasksViewModel::class.java) } ?: throw Exception("invalid activity")
        model.getTasks().observe(this, Observer<List<Task>> { tasks ->
            val tasksRecyclerView: RecyclerView = view!!.findViewById(R.id.tasksRecyclerView)
            tasksRecyclerView.adapter = TasksAdapter(tasks!!.map { TaskViewItem.from(it) }, model::closeTask, ::handleTaskClick)
        })
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val tasksRecyclerView: RecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText) as EditText
        taskNameEditText.setOnEditorActionListener { textView, actionId, event ->
            if (Keyboard.enterPressed(actionId, event)) {
                model.createTask(textView.text.toString()) {
                    textView.text = ""
                    tasksRecyclerView.scrollToPosition(0)
                }
                val manager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_EDIT_ACTICITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val tasksRecyclerView: RecyclerView = activity!!.findViewById(R.id.tasksRecyclerView)
            val id: Long = data!!.getLongExtra("deletedTaskId", Long.MIN_VALUE)
            if (id != Long.MIN_VALUE) {
                (tasksRecyclerView.adapter as TasksAdapter).handleTaskDeleteSuccess(id)
            } else {
                val updatedTask: TaskViewItem = data.getSerializableExtra("updatedTask") as TaskViewItem
                (tasksRecyclerView.adapter as TasksAdapter).handleTaskUpdateSuccess(updatedTask)
            }
        }
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
