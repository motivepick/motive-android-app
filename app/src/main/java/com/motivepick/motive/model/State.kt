package com.motivepick.motive.model

class State(val openTasks: List<Task>, val closedTasks: List<Task>, val closed: Boolean) {

    fun getTasks(): List<Task> = if (closed) closedTasks else openTasks
}
