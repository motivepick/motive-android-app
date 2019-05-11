package com.motivepick.motive

import com.motivepick.motive.model.Task
import com.motivepick.motive.model.Token
import com.motivepick.motive.model.UpdateTaskRequest
import io.reactivex.Observable

class TaskRepository(private val service: TaskService) {

    fun searchTasks(token: Token, closed: Boolean): Observable<List<Task>> = service.search(token.toCookie(), closed)

    fun createTask(token: Token, task: Task): Observable<Task> = service.create(token.toCookie(), task)

    fun updateTask(token: Token, id: Long, request: UpdateTaskRequest): Observable<Task> = service.update(token.toCookie(), id, request)

    fun closeTask(token: Token, id: Long): Observable<Task> = service.close(token.toCookie(), id)

    fun undoCloseTask(token: Token, id: Long): Observable<Task> = service.undoClose(token.toCookie(), id)

    fun deleteTask(token: Token, id: Long) = service.delete(token.toCookie(), id)
}
