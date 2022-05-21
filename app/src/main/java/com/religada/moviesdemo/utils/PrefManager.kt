package com.religada.moviesdemo.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefManager @Inject constructor(@ApplicationContext val context: Context) {

    private val FILE_PREF = "religadaMoviesPrefs"

    val IS_FIRST_TIME = "first_time"
    val LANGUAGE = "language"

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(FILE_PREF, 0)

    private val prefsEditor: SharedPreferences.Editor
        get() {
            val pref = context.getSharedPreferences(FILE_PREF, 0)
            return pref.edit()
        }

    //Boolean preferences
    fun getBoolean(key:String):Boolean{
        return sharedPreferences.getBoolean(key, true)
    }
    fun putBoolean(key: String, value: Boolean){
        prefsEditor.putBoolean(key, value).commit()
    }

    //Long preferences
    fun getLong(key:String): Long {
        return sharedPreferences.getLong(key,0)
    }
    fun putLong(key: String, value: Long){
        prefsEditor.putLong(key, value).commit()
    }

    //Int preferences
    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key,0)
    }
    fun putInt(key: String, value: Int){
        prefsEditor.putInt(key, value).commit()
    }

    //String preferences
    fun getString(key:String): String {
        return sharedPreferences.getString(key,"") ?: ""
    }
    fun putString(key: String, value: String){
        prefsEditor.putString(key, value).commit()
    }
}