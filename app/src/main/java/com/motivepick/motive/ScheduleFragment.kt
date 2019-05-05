package com.motivepick.motive

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ScheduleFragment : Fragment() {

    private lateinit var model: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(TasksViewModel::class.java) } ?: throw Exception("invalid activity")
        model.getTasks().observe(this, Observer<List<Task>> {
            Log.i(this.javaClass.simpleName, "Tasks loaded: " + it!!.size)
            val text: TextView = view!!.findViewById(R.id.textView6)
            text.text = it.size.toString()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}
