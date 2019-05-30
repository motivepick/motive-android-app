package com.motivemobileapp

import android.content.Context
import android.content.SharedPreferences
import com.motivemobileapp.model.Token

class TokenStorage(private val context: Context?) {

    fun storeToken(token: Token) = preferencesFromContext().edit().putString("token", token.toString()).apply()

    fun getToken(): Token = Token(preferencesFromContext().getString("token", "")!!)

    fun removeToken() = preferencesFromContext().edit().remove("token").apply()

    private fun preferencesFromContext(): SharedPreferences = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
}
