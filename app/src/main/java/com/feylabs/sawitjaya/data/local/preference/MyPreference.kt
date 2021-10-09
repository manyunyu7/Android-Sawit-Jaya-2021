package com.feylabs.sawitjaya.data.local.preference

import android.content.Context
import android.content.SharedPreferences

class MyPreference(context: Context) {

    private val PREFS_NAME = "kotlinpref"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun save(KEY_NAME: String, value: Int) {
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String) {
        editor.putString(KEY_NAME, value)
        editor.commit()
    }

    fun getToken(): String? {
        return sharedPref.getString("TOKEN", "")
    }

    fun getRole(): String? {
        return sharedPref.getString("ROLE", "")
    }

    fun getTokenRaw(): String? {
        return sharedPref.getString("TOKEN_RAW", "")
    }

    fun getUserID(): String? {
        return sharedPref.getString("USER_ID", "")
    }

    fun saveUserID(id: String) {
        editor.putString("USER_ID", id)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: Boolean) {
        editor.putBoolean(KEY_NAME, value)
        editor.commit()
    }

    fun getPrefBool(KEY_NAME: String): Boolean? {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun getPrefString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }


    fun clearPreferences() {
        editor.clear()
        editor.commit()
    }

}