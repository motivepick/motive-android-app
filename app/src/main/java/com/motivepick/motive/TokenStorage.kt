package com.motivepick.motive

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class TokenStorage(private val context: Context?) {

    fun storeToken(token: Token) {
        val editor: Editor = preferencesFromContext().edit()
        editor.putString("token", token.toString())
        editor.apply()
    }

    fun getToken(): Token = Token(preferencesFromContext().getString("token", "")!!)

    fun removeToken() {
        val editor: Editor = preferencesFromContext().edit()
        editor.remove("token")
        editor.apply()
    }

    private fun preferencesFromContext(): SharedPreferences = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
}
