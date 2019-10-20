package com.motivemobileapp.schedule

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.motivemobileapp.LoginActivity
import com.motivemobileapp.R
import com.motivemobileapp.TokenStorage
import com.motivemobileapp.UiState
import com.motivemobileapp.common.CurrentDateFactoryImpl
import com.motivemobileapp.model.Schedule
import com.motivemobileapp.model.State
import com.motivemobileapp.model.Task
import com.motivemobileapp.model.TasksViewModel
import com.motivemobileapp.task.TaskEditActivity

class ScheduleFragment : Fragment() {

    val TASK_EDIT_ACTICITY_REQUEST_CODE = 1

    private lateinit var model: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(TasksViewModel::class.java) } ?: throw Exception("invalid activity")
        model.getState().observe(this, Observer<State> { state ->
            val scheduleRecyclerView: RecyclerView = view!!.findViewById(R.id.scheduleRecyclerView)
            val scheduleFactory = ScheduleFactory(CurrentDateFactoryImpl())
            val adapter = scheduleRecyclerView.adapter as ScheduleAdapter
            adapter.setSchedule(scheduleFactory.scheduleFor(state!!.openTasks))
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val scheduleRecyclerView: RecyclerView = view.findViewById(R.id.scheduleRecyclerView)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(activity)
        scheduleRecyclerView.adapter = ScheduleAdapter(activity!!, Schedule(emptyMap(), emptyList(), emptyList()), model::closeTask, ::handleTaskClick)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            TokenStorage(activity).removeToken()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
        return view
    }

    // TODO: DRY
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

    // TODO: DRY
    private fun handleTaskClick(task: Task) {
        val intent = Intent(activity, TaskEditActivity::class.java)
        intent.putExtra("task", task)
        startActivityForResult(intent, TASK_EDIT_ACTICITY_REQUEST_CODE)
    }

    override fun onPause() {
        super.onPause()
        UiState.save("schedule", view!!.findViewById(R.id.scheduleRecyclerView))
    }

    override fun onResume() {
        super.onResume()
        UiState.restore("schedule", view!!.findViewById(R.id.scheduleRecyclerView))
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}
