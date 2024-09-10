package com.example.appverxo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appverxo.ads.AdSdkInitializationObserver
import com.example.appverxo.ads.AdsManager
import com.example.appverxo.databinding.ActivityMainBinding
import com.kimia.feed.sdk.core.AdSdk
import com.kimia.feed.sdk.data.listeners.AdListener
import com.kimia.feed.sdk.ui.AdView

class MainActivity : AppCompatActivity(), AdSdkInitializationObserver {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdsManager.registerObserver(this)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAdInstance(adSdkInstance: AdSdk?) {
        binding.btnShowAd.setOnClickListener {
            if (binding.etAdId.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //hide keyboard
            hideKeyboard()

            val adId = binding.etAdId.text.toString().toLong()
            adSdkInstance?.show(
                adId,
                this@MainActivity,
                object :
                    AdListener {
                    override fun onAdClicked() {
                        Log.wtf("[onAdClicked]", "onAdClicked: $adId")
                    }

                    override fun onAdClosed() {
                        Log.wtf("[onAdClosed]", "onAdClosed: $adId")
                    }

                    override fun onAdStarted() {
                        Log.wtf("[onAdStarted]", "onAdStarted: $adId")
                    }

                    override fun onAdFailedToLoad(adError: String) {
                        Log.wtf("[onAdFailedToLoad]", "onAdFailedToLoad: $adId")
                        Toast.makeText(this@MainActivity, adError, Toast.LENGTH_SHORT).show()
                    }
                }) ?: run {
                Toast.makeText(this, "AdSdk no inicializado", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnShowInflatedAd.setOnClickListener {
            if (binding.etAdId.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //hide keyboard
            hideKeyboard()

            val adId = binding.etAdId.text.toString().toLong()
            val adView = AdView(this@MainActivity)
            binding.adContainer.addView(adView)
            adView.adUnitId = adId
            adView.loadAd(object :
                AdListener {
                override fun onAdClicked() {
                    Log.wtf("[onAdClicked]", "onAdClicked: $adId")
                }

                override fun onAdClosed() {
                    Log.wtf("[onAdClosed]", "onAdClosed: $adId")
                }

                override fun onAdFailedToLoad(adError: String) {
                    Log.wtf("[onAdFailedToLoad]", "onAdFailedToLoad: $adId")
                }

                override fun onAdStarted() {
                    Log.wtf("[onAdStarted]", "onAdStarted: $adId")
                }
            })
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etAdId.windowToken, 0)
    }

    override fun onSdkInitialized(adSdkInstance: AdSdk?) {
        binding.circleProgress.visibility = View.GONE
        binding.linearInputs.visibility = View.VISIBLE
        setupAdInstance(adSdkInstance)
    }
}