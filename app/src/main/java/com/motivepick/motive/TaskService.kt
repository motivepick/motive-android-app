package com.motivepick.motive

import com.google.gson.GsonBuilder
import com.motivepick.motive.model.Config
import com.motivepick.motive.model.TaskFromServer
import com.motivepick.motive.model.UpdateTaskRequest
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TaskService {

    @GET("/tasks")
    fun search(@Header("Cookie") cookie: String, @Query("closed") closed: Boolean): Observable<List<TaskFromServer>>

    @GET("/tasks")
    fun search(@Header("Cookie") cookie: String): Observable<List<TaskFromServer>>

    @POST("/tasks")
    fun create(@Header("Cookie") cookie: String, @Body task: TaskFromServer): Observable<TaskFromServer>

    @PUT("/tasks/{id}")
    fun update(@Header("Cookie") cookie: String, @Path("id") id: Long, @Body request: UpdateTaskRequest): Observable<TaskFromServer>

    @DELETE("/tasks/{id}")
    fun delete(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<Unit>

    @PUT("/tasks/{id}/closing")
    fun close(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<TaskFromServer>

    @PUT("/tasks/{id}/undo-closing")
    fun undoClose(@Header("Cookie") cookie: String, @Path("id") id: Long): Observable<TaskFromServer>

    companion object Factory {
        fun create(config: Config): TaskService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create()))
                .baseUrl(config.getApiUrl())
                .build()

            return retrofit.create(TaskService::class.java);
        }
    }
}
