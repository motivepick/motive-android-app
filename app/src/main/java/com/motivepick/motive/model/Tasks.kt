package com.motivepick.motive.model

class Tasks(val openTasks: List<Task>, val closedTasks: List<Task>, val closed: Boolean) {

    fun getTasksToDisplay(): List<Task> {
        return if (closed) closedTasks else openTasks
    }
}
