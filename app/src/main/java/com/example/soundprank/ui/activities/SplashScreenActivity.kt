package com.example.soundprank.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admob
import com.example.soundprank.utils.AdsInter
import com.example.soundprank.R
import com.example.soundprank.databinding.ActivitySplashScreenBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInter()

        binding.btnStart.setOnClickListener {
            showActivity()
        }
    }

    private fun loadInter() {
        if (AdsInter.inter_intro == null) {
            Admob.getInstance()
                .loadInterAds(this, getString(R.string.id_ads_inter), object : InterCallback() {
                    override fun onInterstitialLoad(interstitialAd2: InterstitialAd) {
                        super.onInterstitialLoad(interstitialAd2)
                        AdsInter.inter_intro = interstitialAd2
                        Log.d("ntt", "Load true")
                        // Show button
                        binding.btnStart.visibility = View.VISIBLE
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        Log.d("ntt", "Load false to load")
                        // Show button
                        binding.btnStart.visibility = View.VISIBLE
                    }

                    override fun onAdFailedToShow(adError: AdError?) {
                        super.onAdFailedToShow(adError)
                        Log.d("ntt", "Load false to show")
                        // Show button
                        binding.btnStart.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun showActivity() {
        try {
            Admob.getInstance().showInterAds(this@SplashScreenActivity, AdsInter.inter_intro, object : InterCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    val intent = Intent(this@SplashScreenActivity, IntroActivity::class.java)
                    startActivity(
                        intent
                    )
                    finish()
                }
            })
        } catch (exception: Exception) {
            Log.d("ntt",exception.toString())
        }
    }
}