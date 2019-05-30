package com.motivemobileapp

import com.motivemobileapp.model.TaskFromServer
import com.motivemobileapp.model.Token
import com.motivemobileapp.model.UpdateTaskRequest
import retrofit2.Call

class TaskRepository(private val service: TaskService) {

    fun searchTasks(token: Token): Call<List<TaskFromServer>> = service.search(token.toCookie())

    fun createTask(token: Token, task: TaskFromServer): Call<TaskFromServer> = service.create(token.toCookie(), task)

    fun updateTask(token: Token, id: Long, request: UpdateTaskRequest): Call<TaskFromServer> = service.update(token.toCookie(), id, request)

    fun closeTask(token: Token, id: Long): Call<TaskFromServer> = service.close(token.toCookie(), id)

    fun undoCloseTask(token: Token, id: Long): Call<TaskFromServer> = service.undoClose(token.toCookie(), id)

    fun deleteTask(token: Token, id: Long) = service.delete(token.toCookie(), id)
}
