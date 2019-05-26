package com.motivemobileapp.schedule

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motivemobileapp.R
import com.motivemobileapp.TaskViewHolder
import com.motivemobileapp.model.Schedule
import com.motivemobileapp.model.ScheduleSection
import com.motivemobileapp.model.Task
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList

class ScheduleAdapter(private val context: Context, schedule: Schedule, private val onTaskClose: (Task) -> Unit, private val onTaskClick: (Task) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1

    private var tasks: List<Serializable>
    private val week: Map<Int, String> = mapOf(
        MONDAY to context.getString(R.string.title_monday),
        TUESDAY to context.getString(R.string.title_tuesday),
        WEDNESDAY to context.getString(R.string.title_wednesday),
        THURSDAY to context.getString(R.string.title_thursday),
        FRIDAY to context.getString(R.string.title_friday),
        SATURDAY to context.getString(R.string.title_saturday),
        SUNDAY to context.getString(R.string.title_sunday)
    )

    init {
        this.tasks = asTasks(schedule)
    }

    private fun asTasks(schedule: Schedule): List<Serializable> {
        val result = ArrayList<Serializable>()
        for (day in schedule.week.keys) {
            val dayOfWeek = asDayOfWeek(day)
            val tasks = schedule.week[day]!!
            if (tasks.isNotEmpty()) {
                result.add(ScheduleSection(week[dayOfWeek]!!))
                tasks.forEach { result.add(it) }
            }
        }
        val future = schedule.future
        if (future.isNotEmpty()) {
            result.add(ScheduleSection(context.getString(R.string.title_future)))
            future.forEach { result.add(it) }
        }
        val overdue = schedule.overdue
        if (overdue.isNotEmpty()) {
            result.add(ScheduleSection(context.getString(R.string.title_overdue)))
            overdue.forEach { result.add(it) }
        }
        return result
    }

    private fun asDayOfWeek(date: Date): Int {
        val calendar = getInstance()
        calendar.time = date
        return calendar.get(DAY_OF_WEEK)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == SECTION_VIEW) {
            SectionHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_subtitle, parent, false))
        } else {
            TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int = if (tasks[position] is Task) CONTENT_VIEW else SECTION_VIEW

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == SECTION_VIEW) {
            val holder = viewHolder as SectionHeaderViewHolder
            val sectionItem = tasks[position] as ScheduleSection
            holder.headerTitleTextView.text = sectionItem.title
        } else {
            val holder = viewHolder as TaskViewHolder
            val task = tasks[position] as Task
            holder.checkBox.setOnClickListener { onTaskClose(task) }
            holder.textView.text = task.name
            holder.clickable.setOnClickListener { onTaskClick(task) }
            if (task.dueDate == null) {
                holder.textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                holder.dueDateView.visibility = View.GONE
            } else {
                holder.dueDateView.visibility = View.VISIBLE
                holder.dueDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(task.dueDate)
                holder.dueDateView.setTextColor(task.getDueDateColor())
            }
        }
    }

    fun setSchedule(schedule: Schedule) {
        this.tasks = asTasks(schedule)
        notifyDataSetChanged()
    }

    class SectionHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        var headerTitleTextView: TextView = itemView.findViewById(R.id.header_title)
    }
}
