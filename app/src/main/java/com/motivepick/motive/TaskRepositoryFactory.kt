package com.motivepick.motive

object TaskRepositoryFactory {

    fun create(config: Config): TaskRepository = TaskRepository(TaskService.create(config))
}
