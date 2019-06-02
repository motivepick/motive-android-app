package com.motivemobileapp.tasks

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
import com.motivemobileapp.Keyboard
import com.motivemobileapp.R
import com.motivemobileapp.UiState
import com.motivemobileapp.model.State
import com.motivemobileapp.model.Task
import com.motivemobileapp.model.TasksViewModel
import com.motivemobileapp.task.TaskEditActivity

class TasksFragment : Fragment() {

    val TASK_EDIT_ACTICITY_REQUEST_CODE = 1
    // TODO: when click on a task, animate from right ot left, not from bottom to top

    private lateinit var model: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(TasksViewModel::class.java) } ?: throw Exception("invalid activity")
        model.getState().observe(this, Observer<State> { state ->
            val tasksRecyclerView: RecyclerView = view!!.findViewById(R.id.tasksRecyclerView)
            val adapter = tasksRecyclerView.adapter as TasksAdapter
            adapter.setTasks(state!!.getTasks(), state.closed)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val tasksRecyclerView: RecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)
        tasksRecyclerView.adapter = TasksAdapter(activity!!, emptyList(), false, model::closeTask, ::handleTaskClick, model::toggleClosedTasks)

        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText) as EditText
        taskNameEditText.setOnEditorActionListener { textView, actionId, event ->
            if (Keyboard.enterPressed(actionId, event)) {
                val name = textView.text.toString()
                if (name.isBlank()) {
                    textView.text = ""
                } else {
                    model.createTask(name) {
                        textView.text = ""
                        tasksRecyclerView.scrollToPosition(0)
                    }
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
            val id: Long = data!!.getLongExtra("deletedTaskId", Long.MIN_VALUE)
            if (id == Long.MIN_VALUE) {
                model.updateTask(data.getSerializableExtra("updatedTask") as Task)
            } else {
                model.deleteTask(id)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        UiState.save("tasks", view!!.findViewById(R.id.tasksRecyclerView))
    }

    override fun onResume() {
        super.onResume()
        UiState.restore("tasks", view!!.findViewById(R.id.tasksRecyclerView))
    }

    private fun handleTaskClick(task: Task) {
        val intent = Intent(activity, TaskEditActivity::class.java)
        intent.putExtra("task", task)
        startActivityForResult(intent, TASK_EDIT_ACTICITY_REQUEST_CODE)
    }

    companion object {
        fun newInstance(): TasksFragment = TasksFragment()
    }
}
