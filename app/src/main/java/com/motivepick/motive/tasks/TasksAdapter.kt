package com.motivepick.motive.tasks

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.START
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.motivepick.motive.R
import com.motivepick.motive.TaskViewHolder
import com.motivepick.motive.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TasksAdapter(private val context: Context, private var tasks: List<Task>, private val onTaskClose: (Task) -> Unit, private val onTaskClick: (Task) -> Unit) :
    Adapter<ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == SECTION_VIEW) {
            return SectionHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tasks_subtitle, parent, false))
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false)
            return TaskViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == SECTION_VIEW) {
            val size = tasks.size
            val holder = viewHolder as SectionHeaderViewHolder
            holder.numberOfTasksTextView.text = context.resources.getQuantityString(R.plurals.subtitle_tasks_section, size, size)
        } else {
            val holder = viewHolder as TaskViewHolder
            val task = tasks[position - 1]
            holder.checkBox.setOnClickListener { onTaskClose(task) }
            holder.textView.text = task.name
            holder.clickable.setOnClickListener { onTaskClick(task) }
            if (task.dueDate == null) {
                holder.textView.gravity = START or CENTER_VERTICAL
                holder.dueDateView.visibility = GONE
            } else {
                holder.dueDateView.visibility = VISIBLE
                holder.dueDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(task.dueDate)
                holder.dueDateView.setTextColor(if (task.isOverdue()) Color.parseColor("#E35446") else Color.parseColor("#78D174"))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) SECTION_VIEW else CONTENT_VIEW

    override fun getItemCount(): Int = tasks.size + 1

    class SectionHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        var numberOfTasksTextView: TextView = itemView.findViewById(R.id.number_of_tasks)
    }
}
