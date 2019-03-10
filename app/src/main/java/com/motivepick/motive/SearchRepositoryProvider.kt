package com.motivepick.motive

object SearchRepositoryProvider {

    fun provideSearchRepository(): TaskRepository {
        return TaskRepository(TaskService.create())
    }
}
