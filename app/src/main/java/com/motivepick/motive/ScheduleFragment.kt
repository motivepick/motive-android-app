package com.motivepick.motive

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.motivepick.motive.common.CurrentDateFactoryImpl
import com.motivepick.motive.model.Task
import com.motivepick.motive.model.TaskViewItem
import com.motivepick.motive.model.TasksViewModel

class ScheduleFragment : Fragment() {

    private lateinit var model: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(TasksViewModel::class.java) } ?: throw Exception("invalid activity")
        model.getTasks().observe(this, Observer<List<Task>> { tasks ->
            val scheduleRecyclerView: RecyclerView = view!!.findViewById(R.id.scheduleRecyclerView)
            val week = WeekFactory(activity!!).createWeek()
            val scheduleFactory = ScheduleFactory(CurrentDateFactoryImpl())
            scheduleRecyclerView.adapter = ScheduleAdapter(week, scheduleFactory.scheduleFor(tasks!!.map { TaskViewItem.from(it) }))
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val scheduleRecyclerView: RecyclerView = view.findViewById(R.id.scheduleRecyclerView)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}
