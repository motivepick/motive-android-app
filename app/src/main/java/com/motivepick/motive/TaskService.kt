package com.motivepick.motive

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TaskService {

    @GET("/tasks")
    fun search(@Header("Cookie") cookie: String, @Query("closed") closed: Boolean): Observable<List<Task>>

    companion object Factory {
        fun create(): TaskService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.motivepick.com")
                .build()

            return retrofit.create(TaskService::class.java);
        }
    }
}
