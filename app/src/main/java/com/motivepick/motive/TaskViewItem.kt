package com.motivepick.motive

import java.io.Serializable

data class TaskViewItem(val id: Long, val name: String, val description: String, val closed: Boolean) : Serializable {

    companion object {
        fun from(task: Task): TaskViewItem = TaskViewItem(task.id!!, task.name, task.description ?: "", task.closed)
    }
}
