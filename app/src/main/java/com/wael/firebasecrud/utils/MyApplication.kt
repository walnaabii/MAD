package com.wael.firebasecrud.utils

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //firebase initialize
        FirebaseApp.initializeApp(this)
    }
}