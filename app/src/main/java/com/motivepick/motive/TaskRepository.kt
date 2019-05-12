package com.motivepick.motive

import com.motivepick.motive.model.TaskFromServer
import com.motivepick.motive.model.Token
import com.motivepick.motive.model.UpdateTaskRequest
import io.reactivex.Observable

class TaskRepository(private val service: TaskService) {

    fun searchTasks(token: Token): Observable<List<TaskFromServer>> = service.search(token.toCookie())

    fun createTask(token: Token, task: TaskFromServer): Observable<TaskFromServer> = service.create(token.toCookie(), task)

    fun updateTask(token: Token, id: Long, request: UpdateTaskRequest): Observable<TaskFromServer> = service.update(token.toCookie(), id, request)

    fun closeTask(token: Token, id: Long): Observable<TaskFromServer> = service.close(token.toCookie(), id)

    fun undoCloseTask(token: Token, id: Long): Observable<TaskFromServer> = service.undoClose(token.toCookie(), id)

    fun deleteTask(token: Token, id: Long) = service.delete(token.toCookie(), id)
}
