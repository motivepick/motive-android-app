package com.motivemobileapp.model

import android.graphics.Color
import com.motivemobileapp.common.StartOfDay
import java.io.Serializable
import java.util.*

data class Task(val id: Long, val name: String, val description: String, val dueDate: Date?, var closed: Boolean) : Serializable {

    companion object {
        fun from(task: TaskFromServer): Task =
            Task(task.id!!, task.name, task.description ?: "", task.dueDate, task.closed)
    }

    fun getNameColor(): Int = if (closed) Color.parseColor("#8E8E93") else Color.parseColor("#000000")

    fun getDueDateColor(): Int = if (closed) Color.parseColor("#8E8E93") else if (isOverdue()) Color.parseColor("#E35446") else Color.parseColor("#78D174")

    private fun isOverdue(): Boolean = if (dueDate == null) false else dueDate.time < StartOfDay(Date()).toTime()
}
