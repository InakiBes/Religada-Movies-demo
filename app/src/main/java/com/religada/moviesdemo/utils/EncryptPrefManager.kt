package com.religada.moviesdemo.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptPrefManager @Inject constructor(@ApplicationContext val applicationContext : Context){

    val FIREBASE_TOKEN = "firebase_token"

    private val masterKeyAlias = MasterKey.Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        applicationContext,
        "MoviesEncryptedPrefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun putString(key:String, value:String){
        encryptedSharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key:String):String{
        return encryptedSharedPreferences.getString(key, "Encrypt data error").toString()
    }

    fun putEncryptInt(key:String, value:Int){
        encryptedSharedPreferences.edit().putInt(key, value).apply()
    }

    fun getEncryptInt(key:String):Int{
        return encryptedSharedPreferences.getInt(key,-1)
    }
}