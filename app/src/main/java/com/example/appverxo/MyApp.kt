package com.example.appverxo

import android.app.Application
import com.example.appverxo.ads.AdsManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AdsManager.initialize(this)
    }
}