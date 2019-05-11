package com.motivepick.motive

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motivepick.motive.model.Header
import com.motivepick.motive.model.Schedule
import com.motivepick.motive.model.TaskViewItem
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_WEEK
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList

class ScheduleAdapter(private val week: Map<Int, String>, schedule: Schedule) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1

    private val tasks: List<Serializable>

    init {
        this.tasks = asTasks(schedule)
    }

    private fun asTasks(schedule: Schedule): List<Serializable> {
        val result = ArrayList<Serializable>()
        for (day in schedule.week.keys) {
            val dayOfWeek = asDayOfWeek(day)
            val tasks = schedule.week[day]!!
            if (tasks.isNotEmpty()) {
                result.add(Header(week[dayOfWeek]!!))
                tasks.forEach { result.add(it) }
            }
        }
        val future = schedule.future
        if (future.isNotEmpty()) {
            result.add(Header("Future"))
            future.forEach { result.add(it) }
        }
        val overdue = schedule.overdue
        if (overdue.isNotEmpty()) {
            result.add(Header("Overdue"))
            overdue.forEach { result.add(it) }
        }
        return result
    }

    private fun asDayOfWeek(date: Date): Int {
        val calendar = getInstance()
        calendar.time = date
        return calendar.get(DAY_OF_WEEK)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SECTION_VIEW) {
            SectionHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_header_title, parent, false))
        } else {
            TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int = if (tasks[position] is TaskViewItem) CONTENT_VIEW else SECTION_VIEW

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (SECTION_VIEW == getItemViewType(position)) {
            val sectionHeaderViewHolder = holder as SectionHeaderViewHolder
            val sectionItem = tasks[position] as Header
            sectionHeaderViewHolder.headerTitleTextView.text = sectionItem.title
        } else {
            val itemViewHolder = holder as TaskViewHolder
            val task = tasks[position] as TaskViewItem
            itemViewHolder.textView.text = task.name
            if (task.dueDate == null) {
                holder.textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                holder.dueDateView.visibility = View.GONE
            } else {
                holder.dueDateView.visibility = View.VISIBLE
                holder.dueDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(task.dueDate)
                holder.dueDateView.setTextColor(if (task.isOverdue()) Color.parseColor("#E35446") else Color.parseColor("#78D174"))
            }
        }
    }

    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerTitleTextView: TextView = itemView.findViewById(R.id.header_title)
    }
}