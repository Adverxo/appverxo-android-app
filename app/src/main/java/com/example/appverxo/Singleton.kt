package com.example.appverxo

import com.kimia.feed.sdk.data.model.AdUnit

object Singleton {
    private var adUnits : MutableList<AdUnit> = mutableListOf()

    fun addAdUnit(adUnit: AdUnit) {
        adUnits.add(adUnit)
    }

    fun getAdUnits(): List<AdUnit> {
        return adUnits
    }

}