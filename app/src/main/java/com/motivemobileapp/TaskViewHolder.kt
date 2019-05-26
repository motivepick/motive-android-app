package com.motivemobileapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val checkBox: ImageButton = itemView.findViewById(R.id.closeTaskBtn)
    val clickable: View = itemView.findViewById(R.id.item_clickable)
    val textView: TextView = itemView.findViewById(R.id.item_text)
    val dueDateView: TextView = itemView.findViewById(R.id.item_date)
}
