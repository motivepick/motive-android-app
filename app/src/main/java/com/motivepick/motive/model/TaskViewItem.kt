package com.motivepick.motive.model

import java.io.Serializable
import java.util.*

data class TaskViewItem(val id: Long, val name: String, val description: String, val dueDate: Date?, val closed: Boolean) : Serializable {

    companion object {
        fun from(task: Task): TaskViewItem =
            TaskViewItem(task.id!!, task.name, task.description ?: "", task.dueDate, task.closed)
    }

    fun isOverdue(): Boolean {
        return if (dueDate == null) false else Date().time > dueDate.time
    }
}
