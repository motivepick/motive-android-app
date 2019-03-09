package com.motivepick.motive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import java.util.*

class TasksAdapter(private val dataSet: List<TaskViewItem>) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private val doneItems = ArrayList<TaskViewItem>()

    private var showCompletedTasks = true

    init {
        for (item in dataSet) {
            if (item.isDone()) {
                doneItems.add(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = dataSet[position]
        if (!listItem.isDone() || showCompletedTasks) {
            holder.textView.setText(listItem.getLabel())
            holder.textView.paint.isStrikeThruText = listItem.isDone()
            holder.doneLink.visibility = if (listItem.isDone()) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView

        val doneLink: ImageButton

        init {
            textView = itemView.findViewById(R.id.item_text) as TextView
            doneLink = itemView.findViewById(R.id.item_done) as ImageButton
        }

    }
}
