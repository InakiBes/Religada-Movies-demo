package com.religada.moviesdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) {
//            //Optional to debug => Timber.plant(Timber.DebugTree())
//        }
    }
}