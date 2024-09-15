package com.example.appverxo.ads

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.example.appverxo.BuildConfig
import com.example.appverxo.Singleton
import com.kimia.feed.sdk.core.AdSdk
import com.kimia.feed.sdk.data.listeners.AdOperationCallback
import com.kimia.feed.sdk.data.listeners.AdSdkInitializationListener
import com.kimia.feed.sdk.data.model.AdUnit
import com.kimia.feed.sdk.data.model.SdkOptions

object AdsManager {
    private var adSdk: AdSdk? = null
    private val observers: MutableList<AdSdkInitializationObserver> = mutableListOf()

    fun initialize(context: Context) {
        if (adSdk == null) {
            val apiKey = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            ).metaData.getString("com.example.appverxo.API_KEY")

            adSdk = AdSdk.getInstance(
                SdkOptions(
                    context = context,
                    apiKey = apiKey!!,
                    applicationId = "com.example.appverxo",
                    debug = BuildConfig.DEBUG
                )
            )
            adSdk?.initialize(object : AdSdkInitializationListener, AdOperationCallback {
                override fun onInitialized(adUnits: List<AdUnit>) {
                    adUnits.forEach {
                        Log.wtf("[AdsManager]", "-AD: ${it.id}")
                        Singleton.addAdUnit(it)
                        adSdk?.initAdUnit(it.id, this)
                    }
                    notifyObserversSdkInitialized()
                }
                override fun onInitializationFailed(errorMessage: String) {
                    Log.wtf("[AdsManager]", "onInitializationFailed: $errorMessage")
                }

                override fun onFailure(error: String) {
                    Log.wtf("[AdsManager]", "onFailure: $error")
                }

                override fun onSuccess(adUnit: String) {
                    Log.wtf("[AdsManager]", "onSuccess: $adUnit")
                }
            })
        } else {
            Log.wtf("[AdsManager]", "AdsManager: already initialized")
        }
    }

    fun registerObserver(observer: AdSdkInitializationObserver) {
        observers.add(observer)
    }

    private fun notifyObserversSdkInitialized() {
        observers.forEach { it.onSdkInitialized(adSdk) }
    }
}


interface AdSdkInitializationObserver {
    fun onSdkInitialized(adSdkInstance: AdSdk?)
}