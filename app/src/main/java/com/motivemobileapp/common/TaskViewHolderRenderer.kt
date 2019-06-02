package com.motivemobileapp.common

import android.graphics.Paint
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity
import android.view.View
import com.motivemobileapp.R
import com.motivemobileapp.TaskViewHolder
import com.motivemobileapp.model.Task

object TaskViewHolderRenderer {

    fun render(viewHolder: ViewHolder, task: Task, onTaskClose: (Task) -> Unit, onTaskClick: (Task) -> Unit) {
        val holder = viewHolder as TaskViewHolder
        holder.checkBox.setImageResource(if (task.closed) R.drawable.check_circle_checked_24dp else R.drawable.check_circle_unchecked_24dp)
        holder.checkBox.setColorFilter(task.getNameColor())
        holder.checkBox.setOnClickListener {
            task.closed = !task.closed
            holder.checkBox.setImageResource(if (task.closed) R.drawable.check_circle_checked_24dp else R.drawable.check_circle_unchecked_24dp)
            holder.textView.paintFlags =
                if (task.closed) holder.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            onTaskClose(task)
        }
        holder.textView.text = task.name
        holder.textView.setTextColor(task.getNameColor())
        holder.textView.paintFlags =
            if (task.closed) holder.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        holder.clickable.setOnClickListener { onTaskClick(task) }
        if (task.dueDate == null) {
            holder.textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            holder.dueDateView.visibility = View.GONE
        } else {
            holder.dueDateView.visibility = View.VISIBLE
            holder.dueDateView.text = DateFormat.format(task.dueDate)
            holder.dueDateView.setTextColor(task.getDueDateColor())
        }
    }
}
