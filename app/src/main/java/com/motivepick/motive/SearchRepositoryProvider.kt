package com.motivepick.motive

object SearchRepositoryProvider {

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(TaskService.create())
    }
}
