package com.motivepick.motive

object TaskRepositoryFactory {

    fun create(): TaskRepository {
        return TaskRepository(TaskService.create())
    }
}
