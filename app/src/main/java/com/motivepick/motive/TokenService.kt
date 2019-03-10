package com.motivepick.motive

import android.content.Context
import android.content.SharedPreferences

class TokenService(private val context: Context?) {

    fun storeToken(token: String) {
        val editor: SharedPreferences.Editor = context!!.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String {
        val preferences = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return preferences.getString("token", "")!!
    }

    fun removeToken() {
        val editor: SharedPreferences.Editor = context!!.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        editor.remove("token")
        editor.apply()
    }
}
