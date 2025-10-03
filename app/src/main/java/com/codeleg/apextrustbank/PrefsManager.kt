package com.codeleg.apextrustbank


import android.content.Context

object PrefsManager {
    private const val PREFS_NAME = "apextrust_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LOGGED_IN = "logged_in"
    fun saveLogin(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putInt(KEY_USER_ID, userId)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
