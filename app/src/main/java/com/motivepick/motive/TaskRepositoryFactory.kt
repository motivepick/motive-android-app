package com.motivepick.motive

import com.motivepick.motive.model.Config

object TaskRepositoryFactory {

    fun create(config: Config): TaskRepository = TaskRepository(TaskService.create(config))
}
