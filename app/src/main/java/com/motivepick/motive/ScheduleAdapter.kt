package com.motivepick.motive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ScheduleAdapter(private val tasks: List<TaskViewItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SECTION_VIEW = 0
    private val CONTENT_VIEW = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SECTION_VIEW) {
            return SectionHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_header_title, parent, false))
        } else {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) SECTION_VIEW else CONTENT_VIEW // TODO: get item by position and check if it is task or header
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (SECTION_VIEW == getItemViewType(position)) {
            val sectionHeaderViewHolder = holder as SectionHeaderViewHolder
            val sectionItem = tasks[position]
            sectionHeaderViewHolder.headerTitleTextView.text = sectionItem.name
        } else {
            val itemViewHolder = holder as ItemViewHolder
            val task = tasks[position]
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
