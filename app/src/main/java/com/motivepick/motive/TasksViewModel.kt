package com.motivepick.motive

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.motivepick.motive.model.Task
import com.motivepick.motive.model.TaskViewItem
import com.motivepick.motive.model.Token
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val tasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>().also { loadTasks() }
    }

    fun createTask(name: String, onTaskCreated: () -> Unit) {
        val application = getApplication<Application>()
        val token: Token = TokenStorage(application).getToken()
        val repository: TaskRepository = TaskRepositoryFactory.create(Config(application))
        if (name.isNotBlank()) {
            val disposable = repository.createTask(token, Task(null, name, null, null, false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { task: Task ->
                    tasks.value = listOf(task) + tasks.value!!
                    onTaskCreated()
                }
        }
    }

    fun getTasks(): LiveData<List<Task>> {
        return tasks
    }

    fun closeTask(task: TaskViewItem) {
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
        val observable: Observable<List<Task>> = repository.searchTasks(token, false)
        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                tasks.value = it
            }
    }
}
