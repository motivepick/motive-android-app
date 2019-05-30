package com.motivemobileapp

import com.google.gson.GsonBuilder
import com.motivemobileapp.model.Config
import com.motivemobileapp.model.TaskFromServer
import com.motivemobileapp.model.UpdateTaskRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TaskService {

    @GET("/tasks")
    fun search(@Header("Cookie") cookie: String): Call<List<TaskFromServer>>

    @POST("/tasks")
    fun create(@Header("Cookie") cookie: String, @Body task: TaskFromServer): Call<TaskFromServer>

    @PUT("/tasks/{id}")
    fun update(@Header("Cookie") cookie: String, @Path("id") id: Long, @Body request: UpdateTaskRequest): Call<TaskFromServer>

    @DELETE("/tasks/{id}")
    fun delete(@Header("Cookie") cookie: String, @Path("id") id: Long): Call<Unit>

    @PUT("/tasks/{id}/closing")
    fun close(@Header("Cookie") cookie: String, @Path("id") id: Long): Call<TaskFromServer>

    @PUT("/tasks/{id}/undo-closing")
    fun undoClose(@Header("Cookie") cookie: String, @Path("id") id: Long): Call<TaskFromServer>

    companion object Factory {
        fun create(config: Config): TaskService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
                .baseUrl(config.getApiUrl())
                .build()

            return retrofit.create(TaskService::class.java);
        }
    }
}
