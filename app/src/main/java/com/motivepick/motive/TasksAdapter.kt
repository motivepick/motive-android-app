package com.motivepick.motive

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.motivepick.motive.model.TaskViewItem
import java.text.SimpleDateFormat
import java.util.*

class TasksAdapter(private var tasks: List<TaskViewItem>, private val onTaskClose: (TaskViewItem) -> Unit, private val onTaskClick: (TaskViewItem) -> Unit) :
    RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.checkBox.setOnClickListener { onTaskClose(task) }
        holder.textView.text = task.name
        holder.clickable.setOnClickListener { onTaskClick(task) }
        if (task.dueDate == null) {
            holder.textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            holder.dueDateView.visibility = View.GONE
        } else {
            holder.dueDateView.visibility = View.VISIBLE
            holder.dueDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(task.dueDate)
            holder.dueDateView.setTextColor(if (task.isOverdue()) Color.parseColor("#E35446") else Color.parseColor("#78D174"))
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun handleTaskDeleteSuccess(id: Long) = deleteTask(id)

    fun handleTaskUpdateSuccess(updated: TaskViewItem) {
        val left: List<TaskViewItem> = tasks.takeWhile { it.id != updated.id }
        val right: List<TaskViewItem> = tasks.takeLastWhile { it.id != updated.id }
        tasks = left + listOf(updated) + right
        notifyItemChanged(left.size)
    }

    private fun deleteTask(id: Long) {
        val task: TaskViewItem = tasks.find { it.id == id }!!
        val position = tasks.indexOf(task)
        tasks = tasks.filterNot { it.id == id }
        notifyItemRemoved(position)
    }
}
