package com.motivepick.motive

import io.reactivex.Observable

class SearchRepository(private val service: TaskService) {

    fun searchTasks(token: String, closed: Boolean): Observable<List<Task>> = service.search(token, closed)
}
