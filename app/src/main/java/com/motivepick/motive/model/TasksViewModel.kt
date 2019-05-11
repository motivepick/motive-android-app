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

    private val tasks: MutableLiveData<List<TaskFromServer>> by lazy {
        MutableLiveData<List<TaskFromServer>>().also { loadTasks() }
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
                    tasks.value = listOf(task) + tasks.value!!
                    onTaskCreated()
                }
        }
    }

    fun getTasks(): LiveData<List<TaskFromServer>> {
        return tasks
    }

    // TODO: consider calling server from this method and calling the method from task edit activity itself; same for task deletion
    fun updateTask(updated: Task) {
        val left: List<TaskFromServer> = tasks.value!!.takeWhile { it.id != updated.id }
        val right: List<TaskFromServer> = tasks.value!!.takeLastWhile { it.id != updated.id }
        tasks.value = left + listOf(TaskFromServer(updated.id, updated.name, updated.description, updated.dueDate, updated.closed)) + right
    }

    fun deleteTask(id: Long) {
        tasks.value = tasks.value!!.filterNot { it.id == id }
    }

    fun closeTask(task: Task) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        if (task.closed) repository.undoCloseTask(token, task.id) else repository.closeTask(token, task.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response ->
                if (response.closed) {
                    tasks.value = tasks.value!!.filterNot { it.id == response.id }
                } else {
                    throw Exception("not yet implemented")
                }
            }
    }

    private fun loadTasks() {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        val observable: Observable<List<TaskFromServer>> = repository.searchTasks(token, false)
        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                tasks.value = it
            }
    }
}
