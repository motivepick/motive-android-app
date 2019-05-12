package com.motivepick.motive.tasks

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.START
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.motivepick.motive.R
import com.motivepick.motive.TaskViewHolder
import com.motivepick.motive.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TasksAdapter(
    private val context: Context,
    private val tasks: List<Task>,
    private val closed: Boolean,
    private val onTaskClose: (Task) -> Unit,
    private val onTaskClick: (Task) -> Unit,
    private val onShowClosedTasksClick: () -> Unit
) : Adapter<ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == SECTION_VIEW) {
            SectionHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tasks_subtitle, parent, false))
        } else {
            TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == SECTION_VIEW) {
            val size = tasks.size
            val holder = viewHolder as SectionHeaderViewHolder
            holder.numberOfTasksTextView.text = context.resources.getQuantityString(R.plurals.subtitle_tasks_section, size, size)
            holder.showClosedTasksButton.text = context.getString(if (closed) R.string.button_show_open_tasks else R.string.button_show_closed_tasks)
            holder.showClosedTasksButton.setOnClickListener {
                onShowClosedTasksClick()
            }
        } else {
            val holder = viewHolder as TaskViewHolder
            val task = tasks[position - 1]
            val color = task.getColor()
            holder.checkBox.setOnClickListener { onTaskClose(task) }
            holder.textView.text = task.name
            if (task.closed) {
                holder.textView.setTextColor(color)
                holder.textView.paintFlags = holder.textView.paintFlags or STRIKE_THRU_TEXT_FLAG
            }
            holder.clickable.setOnClickListener { onTaskClick(task) }
            if (task.dueDate == null) {
                holder.textView.gravity = START or CENTER_VERTICAL
                holder.dueDateView.visibility = GONE
            } else {
                holder.dueDateView.visibility = VISIBLE
                holder.dueDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(task.dueDate)
                holder.dueDateView.setTextColor(color)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) SECTION_VIEW else CONTENT_VIEW

    override fun getItemCount(): Int = tasks.size + 1

    class SectionHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        val numberOfTasksTextView: TextView = itemView.findViewById(R.id.number_of_tasks)
        val showClosedTasksButton: Button = itemView.findViewById(R.id.button2)
    }
}