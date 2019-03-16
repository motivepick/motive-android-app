package com.motivepick.motive

import io.reactivex.Observable

class TaskRepository(private val service: TaskService) {

    fun searchTasks(token: Token, closed: Boolean): Observable<List<Task>> = service.search(token.toCookie(), closed)

    fun createTask(token: Token, task: Task): Observable<Task> = service.create(token.toCookie(), task)

    fun closeTask(token: Token, id: Long): Observable<Task> = service.close(token.toCookie(), id)

    fun undoCloseTask(token: Token, id: Long): Observable<Task> = service.undoClose(token.toCookie(), id)

    fun deleteTask(token: Token, id: Long) = service.delete(token.toCookie(), id)
}
