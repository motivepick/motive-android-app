package com.motivepick.motive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.motivepick.motive.TasksAdapter.ViewHolder

class TasksAdapter(private var tasks: List<TaskViewItem>, private val onTaskClose: (TaskViewItem) -> Unit, private val onTaskClick: (TaskViewItem) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.checkBox.setOnClickListener { onTaskClose(task) }
        holder.textView.text = task.name
        holder.textView.setOnClickListener { onTaskClick(task) }
    }

    override fun getItemCount(): Int = tasks.size

    fun handleTaskCreateSuccess(task: TaskViewItem) {
        tasks = listOf(task) + tasks
        notifyItemInserted(0)
    }

    fun handleTaskCloseSuccess(task: TaskViewItem) {
        if (task.closed) {
            deleteTask(task.id)
        } else {
            handleTaskCreateSuccess(task)
        }
    }

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBox: ImageButton = itemView.findViewById(R.id.closeTaskBtn)
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }
}
