package com.motivepick.motive.model

class Tasks(val openTasks: List<TaskFromServer>, val closedTasks: List<TaskFromServer>, val closed: Boolean) {

    fun getTasksToDisplay(): List<TaskFromServer> {
        return if (closed) closedTasks else openTasks
    }
}
