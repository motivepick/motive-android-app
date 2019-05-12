package com.motivepick.motive.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.motivepick.motive.TaskRepository
import com.motivepick.motive.TaskRepositoryFactory
import com.motivepick.motive.TokenStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val tasks: MutableLiveData<Tasks> by lazy {
        MutableLiveData<Tasks>().also { loadTasks() }
    }

    fun createTask(name: String, onTaskCreated: () -> Unit) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        if (name.isNotBlank()) {
            val disposable = repository.createTask(token, TaskFromServer(null, name, null, null, false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { task: TaskFromServer ->
                    val current = tasks.value!!
                    tasks.value = Tasks(listOf(task) + current.openTasks, current.closedTasks, current.closed)
                    onTaskCreated()
                }
        }
    }

    fun getTasks(): LiveData<Tasks> = tasks

    // TODO: consider calling server from this method and calling the method from task edit activity itself; same for task deletion
    fun updateTask(updated: Task) {
        val current = tasks.value!!
        tasks.value = Tasks(update(current.openTasks, updated), update(current.closedTasks, updated), current.closed)
    }

    private fun update(tasks: List<TaskFromServer>, updated: Task): List<TaskFromServer> {
        val left: List<TaskFromServer> = tasks.takeWhile { it.id != updated.id }
        val right: List<TaskFromServer> = tasks.takeLastWhile { it.id != updated.id }
        val found = left.size + right.size < tasks.size
        return if (found) left + listOf(TaskFromServer(updated.id, updated.name, updated.description, updated.dueDate, updated.closed)) + right else tasks
    }

    fun deleteTask(id: Long) {
        val current = tasks.value!!
        tasks.value = Tasks(current.openTasks.filterNot { it.id == id }, current.closedTasks.filterNot { it.id == id }, current.closed)
    }

    fun closeTask(task: Task) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        val observable: Observable<TaskFromServer> = if (task.closed) repository.undoCloseTask(token, task.id) else repository.closeTask(token, task.id)
        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                val current = tasks.value!!
                if (response.closed) {
                    tasks.value = Tasks(current.openTasks.filterNot { it.id == response.id }, listOf(response) + current.closedTasks, current.closed)
                } else {
                    tasks.value = Tasks(listOf(response) + current.openTasks, current.closedTasks.filterNot { it.id == response.id }, current.closed)
                }
            }
    }

    fun toggleClosedTasks() {
        val current = tasks.value!!
        tasks.value = Tasks(current.openTasks, current.closedTasks, !current.closed)
    }

    private fun loadTasks() {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        val observable: Observable<List<TaskFromServer>> = repository.searchTasks(token)
        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                tasks.value = Tasks(response.filterNot { it.closed }, response.filter { it.closed }, false)
            }
    }
}
