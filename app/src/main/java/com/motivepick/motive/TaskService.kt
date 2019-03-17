package com.motivepick.motive

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TaskService {

    @GET("/tasks")
    fun search(@Header("Cookie") cookie: String, @Query("closed") closed: Boolean): Observable<List<Task>>

    @POST("/tasks")
    fun create(@Header("Cookie") cookie: String, @Body task: Task): Observable<Task>

    @PUT("/tasks/{id}")
    fun update(@Header("Cookie") cookie: String, @Body task: Task): Observable<Task>

    @DELETE("/tasks/{id}")
    fun delete(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<Unit>

    @PUT("/tasks/{id}/closing")
    fun close(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<Task>

    @PUT("/tasks/{id}/undo-closing")
    fun undoClose(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<Task>

    companion object Factory {
        fun create(config: Config): TaskService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(config.getApiUrl())
                .build()

            return retrofit.create(TaskService::class.java);
        }
    }
}
