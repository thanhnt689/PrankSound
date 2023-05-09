package com.pranksounds.funny.haircut.sound.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admob
import com.pranksounds.funny.haircut.sound.utils.AdsInter
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.databinding.ActivitySplashScreenBinding
import com.pranksounds.funny.haircut.sound.utils.Const
import com.pranksounds.funny.haircut.sound.utils.LocaleHelper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var editTor: SharedPreferences.Editor

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdsInter.inter_splash = null

        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        editTor = sharedPreferences.edit()

        if (sharedPreferences.getBoolean("openLanguage", false)) {
            localeHelper.setLanguage(this)
            binding.btnStart.text = getText(R.string.string_start)
        }

        loadInter()

        binding.btnStart.setOnClickListener {
            showActivity()

//            val intent = Intent(this@SplashScreenActivity, IntroActivity::class.java)
//            startActivity(
//                intent
//            )
//            finish()
//
//            editTor.putInt(Const.NUM_SHOW_INTER, 1)
//
//            editTor.putInt(Const.NUM_SHOW_RATING, 0)
//
//            editTor.apply()
        }
    }

    private fun loadInter() {
        //if (AdsInter.inter_splash == null) {
            Admob.getInstance()
                .loadInterAds(this, getString(R.string.id_ads_inter_splash), object : InterCallback() {
                    override fun onInterstitialLoad(interstitialAd2: InterstitialAd) {
                        super.onInterstitialLoad(interstitialAd2)
                        AdsInter.inter_splash = interstitialAd2
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
       // }
    }

    private fun showActivity() {
        try {
            Admob.getInstance().showInterAds(this, AdsInter.inter_splash, object : InterCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    val intent = Intent(this@SplashScreenActivity, IntroActivity::class.java)
                    startActivity(
                        intent
                    )
                    AdsInter.inter_splash = null
                    finish()

                    editTor.putInt(Const.NUM_SHOW_INTER, 1)

                    editTor.putInt(Const.NUM_SHOW_RATING, 0)

                    editTor.apply()
                }
            })
        } catch (exception: Exception) {
            Log.d("ntt", exception.toString())
        }
    }
}