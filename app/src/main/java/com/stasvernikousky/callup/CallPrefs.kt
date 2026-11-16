package com.stasvernikousky.callup

import android.content.Context

object CallPrefs {

    private const val PREFS_NAME = "callup_prefs"
    private const val KEY_NUMBER = "target_number"
    private const val KEY_AUTO_CALL = "auto_call"

    fun getNumber(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NUMBER, "")
            .orEmpty()

    fun setNumber(context: Context, value: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_NUMBER, value)
            .apply()
    }

    fun isAutoCallEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_AUTO_CALL, false)

    fun setAutoCallEnabled(context: Context, value: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_AUTO_CALL, value)
            .apply()
    }
}
