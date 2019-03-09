package com.motivepick.motive

data class TaskViewItem(val name: String) {

    fun getLabel(): String = name

    fun isDone(): Boolean = false
}
