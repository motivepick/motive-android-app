package com.motivepick.motive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motivepick.motive.model.Header
import com.motivepick.motive.model.Schedule
import com.motivepick.motive.model.TaskViewItem
import java.io.Serializable
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList

class ScheduleAdapter(schedule: Schedule) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1
    private val WEEK: Map<Int, String> = mapOf(
        MONDAY to "Monday",
        TUESDAY to "Tuesday",
        WEDNESDAY to "Wednesday",
        THURSDAY to "Thursday",
        FRIDAY to "Friday",
        SATURDAY to "Saturday",
        SUNDAY to "Sunday"
    )

    private val tasks: List<Serializable>

    init {
        this.tasks = asTasks(schedule)
    }

    private fun asTasks(schedule: Schedule): List<Serializable> {
        val result = ArrayList<Serializable>()
        for (day in schedule.week.keys) {
            val dayOfWeek = asDayOfWeek(day)
            result.add(Header(WEEK[dayOfWeek]!!))
            schedule.week[day]!!.forEach { result.add(it) }
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
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false))
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
            val itemViewHolder = holder as ItemViewHolder
            val task = tasks[position] as TaskViewItem
            itemViewHolder.nameTextView.text = task.name
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.item_text)
    }

    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerTitleTextView: TextView = itemView.findViewById(R.id.header_title)
    }
}
