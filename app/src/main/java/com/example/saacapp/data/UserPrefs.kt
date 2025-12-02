package com.example.saacapp.data

import android.content.Context

object UserPrefs {
    private const val PREFS_NAME = "saac_prefs"
    private const val KEY_ROLE = "user_role"
    private const val ROLE_CHILD = "child"
    private const val ROLE_TUTOR = "tutor"

    private const val KEY_SHOW_FRASES = "show_frases"
    private const val KEY_SHOW_BUSCAR = "show_buscar"
    private const val KEY_SHOW_NUEVO = "show_nuevo"

    fun setRole(context: Context, isTutor: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_ROLE, if (isTutor) ROLE_TUTOR else ROLE_CHILD)
            .apply()
    }

    fun isTutor(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ROLE, ROLE_CHILD) == ROLE_TUTOR
    }

    fun showFrases(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SHOW_FRASES, false)
    }

    fun setShowFrases(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_FRASES, value).apply()
    }

    fun showBuscar(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SHOW_BUSCAR, false)
    }

    fun setShowBuscar(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_BUSCAR, value).apply()
    }

    fun showNuevo(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SHOW_NUEVO, false)
    }

    fun setShowNuevo(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_NUEVO, value).apply()
    }
}
