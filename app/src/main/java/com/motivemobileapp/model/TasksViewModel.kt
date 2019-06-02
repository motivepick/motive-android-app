package com.motivemobileapp.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.motivemobileapp.TaskRepository
import com.motivemobileapp.TaskRepositoryFactory
import com.motivemobileapp.TokenStorage
import com.motivemobileapp.common.Callback.callback
import retrofit2.Call

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val state: MutableLiveData<State> by lazy {
        MutableLiveData<State>().also { loadTasks() }
    }

    fun createTask(name: String, onTaskCreated: () -> Unit) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        repository.createTask(token, TaskFromServer(null, name, null, null, false))
            .enqueue(callback({ task ->
                val current = state.value!!
                state.value = State(listOf(Task.from(task!!)) + current.openTasks, current.closedTasks, current.closed)
                onTaskCreated()
            }))
    }

    fun getState(): LiveData<State> = state

    // TODO: consider calling server from this method and calling the method from task edit activity itself; same for task deletion
    fun updateTask(updated: Task) {
        val current = state.value!!
        state.value = State(update(current.openTasks, updated), update(current.closedTasks, updated), current.closed)
    }

    fun deleteTask(id: Long) {
        val current = state.value!!
        state.value = State(current.openTasks.filterNot { it.id == id }, current.closedTasks.filterNot { it.id == id }, current.closed)
    }

    fun closeTask(task: Task) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        val callback: Call<TaskFromServer> = if (task.closed) repository.undoCloseTask(token, task.id) else repository.closeTask(token, task.id)
        callback.enqueue(callback({ response ->
            val current = state.value!!
            state.value = if (response!!.closed) {
                State(current.openTasks.filterNot { it.id == response.id }, listOf(Task.from(response)) + current.closedTasks, current.closed)
            } else {
                State(listOf(Task.from(response)) + current.openTasks, current.closedTasks.filterNot { it.id == response.id }, current.closed)
            }
        }))
    }

    fun toggleClosedTasks() {
        val current = state.value!!
        state.value = State(current.openTasks, current.closedTasks, !current.closed)
    }

    private fun update(tasks: List<Task>, updated: Task): List<Task> {
        val left: List<Task> = tasks.takeWhile { it.id != updated.id }
        val right: List<Task> = tasks.takeLastWhile { it.id != updated.id }
        val found = left.size + right.size < tasks.size
        return if (found) left + listOf(Task(updated.id, updated.name, updated.description, updated.dueDate, updated.closed)) + right else tasks
    }

    private fun loadTasks() {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        repository.searchTasks(token).enqueue(callback(
            { response ->
                val tasks = response!!.map { Task.from(it) }
                state.value = State(tasks.filterNot { it.closed }, tasks.filter { it.closed }, false)
            },
            { error ->
                Log.e(javaClass.name, "Error: ", error)
            }
        ))
    }
}
