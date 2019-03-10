package com.motivepick.motive

import io.reactivex.Observable

class TaskRepository(private val service: TaskService) {

    fun searchTasks(token: String, closed: Boolean): Observable<List<Task>> = service.search(cookie(token), closed)

    fun createTask(token: String, task: Task): Observable<Task> = service.create(cookie(token), task)

    fun deleteTask(token: String, id: Long) = service.delete(cookie(token), id)

    private fun cookie(token: String) = "SESSION=$token"
}
