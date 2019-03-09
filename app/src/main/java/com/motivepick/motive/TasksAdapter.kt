package com.motivepick.motive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motivepick.motive.TasksAdapter.ViewHolder

class TasksAdapter(private val tasks: List<TaskViewItem>, private val listener: (TaskViewItem) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.textView.text = task.name
        holder.textView.setOnClickListener { listener(task) }
    }

    override fun getItemCount(): Int = tasks.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById(R.id.item_text) as TextView
    }
}
