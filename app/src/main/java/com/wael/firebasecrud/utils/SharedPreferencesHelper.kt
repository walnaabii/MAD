package com.wael.firebasecrud.utils

import android.content.Context
import android.content.SharedPreferences

//local storage
class SharedPreferencesHelper(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("studentRecord", Context.MODE_PRIVATE)

    fun saveStringData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveIntData(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getIntData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue) ?: defaultValue
    }

    fun saveBoolData(key: String,value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun getBoolData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun clearData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
