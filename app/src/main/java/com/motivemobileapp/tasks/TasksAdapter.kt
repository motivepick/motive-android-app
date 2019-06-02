package com.motivemobileapp.tasks

import android.content.Context
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.motivemobileapp.R
import com.motivemobileapp.TaskViewHolder
import com.motivemobileapp.common.TaskViewHolderRenderer
import com.motivemobileapp.model.Task

class TasksAdapter(
    private val context: Context,
    private var tasks: List<Task>,
    private var closed: Boolean,
    private val onTaskClose: (Task) -> Unit,
    private val onTaskClick: (Task) -> Unit,
    private val onShowClosedTasksClick: () -> Unit
) : Adapter<ViewHolder>() {

    private val SECTION_VIEW = 0
    private val TASK_VIEW = 1

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
            TaskViewHolderRenderer.render(viewHolder as TaskViewHolder, tasks[position - 1], onTaskClose, onTaskClick)
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) SECTION_VIEW else TASK_VIEW

    override fun getItemCount(): Int = tasks.size + 1

    fun setTasks(tasks: List<Task>, closed: Boolean) {
        this.tasks = tasks
        this.closed = closed
        notifyDataSetChanged()
    }

    class SectionHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        val numberOfTasksTextView: TextView = itemView.findViewById(R.id.number_of_tasks)
        val showClosedTasksButton: Button = itemView.findViewById(R.id.button2)
    }
}
