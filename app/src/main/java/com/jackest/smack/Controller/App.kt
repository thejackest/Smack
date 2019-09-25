package com.jackest.smack.Controller

import android.app.Application
import com.jackest.smack.Utilities.SharedPrefs

class App : Application(){

    companion object{
        lateinit var sharedPreferences: SharedPrefs
    }
    override fun onCreate() {
        sharedPreferences = SharedPrefs(applicationContext)
        super.onCreate()
    }
}