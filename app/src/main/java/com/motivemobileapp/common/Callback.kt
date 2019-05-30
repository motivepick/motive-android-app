package com.motivemobileapp.common

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Callback {

    fun <T> callback(success: ((T?) -> Unit)?, failure: ((t: Throwable) -> Unit)? = null): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                success?.invoke(response.body())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                failure?.invoke(t)
            }
        }
    }
}