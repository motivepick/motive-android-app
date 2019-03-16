package com.motivepick.motive

object TaskRepositoryFactory {

    fun create(config: Config): TaskRepository {
        return TaskRepository(TaskService.create(config))
    }
}
