package com.example.appverxo.ads

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.tarkinstudios.appverxo_android_sdk.BuildConfig
import com.tarkinstudios.appverxo_android_sdk.core.AdSdk
import com.tarkinstudios.appverxo_android_sdk.data.model.AdUnit
import com.tarkinstudios.appverxo_android_sdk.utils.AdOperationCallback
import com.tarkinstudios.appverxo_android_sdk.utils.AdSdkInitializationListener

object AdsManager {
    private var adSdkInstance: AdSdk? = null
    private val observers: MutableList<AdSdkInitializationObserver> = mutableListOf()

    fun initialize(context: Context) {
        if (adSdkInstance == null) {
            val apiKey = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            ).metaData.getString("com.example.appverxo.API_KEY")

            AdSdk.initialize(
                mDebug = BuildConfig.DEBUG,
                context = context,
                apiKey = apiKey,
                applicationId = "com.example.appverxo",
                object : AdSdkInitializationListener, AdOperationCallback {
                    override fun onInitialized(adUnits: List<AdUnit>) {
                        adSdkInstance = AdSdk.getInstance()
                        adUnits.forEach {
                            Log.wtf("[AdsManager]", "-AD: ${it.id}")
                            AdSdk.initAdUnit(it.id, context, this)
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
                        Log.wtf("[AdsManager]", "AdsManager: $adUnit")

                    }
                }
            )
        }
    }

    fun registerObserver(observer: AdSdkInitializationObserver) {
        observers.add(observer)
    }

    private fun notifyObserversSdkInitialized() {
        observers.forEach { it.onSdkInitialized() }
    }
}


interface AdSdkInitializationObserver {
    fun onSdkInitialized()
}