package com.example.telutask.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager048(context048: Context) {
    private val prefs048: SharedPreferences =
        context048.getSharedPreferences("user_session_048", Context.MODE_PRIVATE)

    fun saveUserId048(userId048: Int) {
        prefs048.edit().putInt(KEY_USER_ID048, userId048).apply()
    }

    fun getUserId048(): Int? {
        val id048 = prefs048.getInt(KEY_USER_ID048, -1)
        return if (id048 != -1) id048 else null
    }

    fun clearSession048() {
        prefs048.edit().clear().apply()
    }

    companion object {
        private const val KEY_USER_ID048 = "key_user_id_048"
    }
}
