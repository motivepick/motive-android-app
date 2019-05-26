package com.motivemobileapp

import com.motivemobileapp.model.Config

object TaskRepositoryFactory {

    fun create(config: Config): TaskRepository = TaskRepository(TaskService.create(config))
}
