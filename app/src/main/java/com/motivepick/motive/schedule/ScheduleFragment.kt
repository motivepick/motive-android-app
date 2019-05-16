package com.motivepick.motive.schedule

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
import com.motivepick.motive.R
import com.motivepick.motive.TaskEditActivity
import com.motivepick.motive.common.CurrentDateFactoryImpl
import com.motivepick.motive.model.Schedule
import com.motivepick.motive.model.State
import com.motivepick.motive.model.Task
import com.motivepick.motive.model.TasksViewModel

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

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}
