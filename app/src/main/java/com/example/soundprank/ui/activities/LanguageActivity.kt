package com.example.soundprank.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazic.ads.callback.NativeCallback
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.LanguageAdapter
import com.example.soundprank.callback.OnClickItemLanguage
import com.example.soundprank.databinding.ActivityLanguageBinding
import com.example.soundprank.models.Language
import com.example.soundprank.utils.LocaleHelper
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


class LanguageActivity : AppCompatActivity(), OnClickItemLanguage {

    private lateinit var binding: ActivityLanguageBinding

    private lateinit var sharedPreferences: SharedPreferences

    private var mLanguage: Language? = null

    private var adapter: LanguageAdapter? = null

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.btnDone.setOnClickListener {
            clickButtonDone()
        }


    }

    private fun init() {
        localeHelper.setLanguage(this)

        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        adapter = LanguageAdapter(setLanguageDefault(), this)
        binding.rvLanguage.adapter = adapter
        binding.rvLanguage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Admob.getInstance().loadNativeAd(
            this,
            getString(R.string.id_ads_native),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    super.onNativeAdLoaded(nativeAd)
                    Log.d("ThanhNT", "onNativeAdLoaded")
                    val adView = LayoutInflater.from(this@LanguageActivity)
                        .inflate(R.layout.ads_native, null) as NativeAdView
                    binding.frAds3.removeAllViews()
                    binding.frAds3.addView(adView)

                    Admob.getInstance().pushAdsToViewCustom(nativeAd, adView)
                }

                override fun onAdFailedToLoad() {
                    binding.frAds3.visibility = View.GONE
                    binding.frAds3.removeAllViews()
                }
            })
    }

    private fun clickButtonDone() {

        if (mLanguage == null) {
            Toast.makeText(this, "Please select language", Toast.LENGTH_SHORT).show()
        } else {
            val localeHelper = LocaleHelper()
            val editor: SharedPreferences.Editor =
                getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit()
            editor.putBoolean("openLanguage", true)
            editor.apply()
            mLanguage?.let { localeHelper.setPreLanguage(this@LanguageActivity, it.languageCode) }
            localeHelper.setLanguage(this)
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onClick(language: Language) {
        adapter?.setSelectLanguage(language)
        mLanguage = language
    }

    private fun setLanguageDefault(): List<Language> {
        val languages: MutableList<Language> = ArrayList()
        val localeHelper = LocaleHelper()
        val key: String = localeHelper.getLanguage(this).toString()

        when (key) {
            "en" -> mLanguage = Language(R.drawable.language_english, "English", "en", false)
            "es" -> mLanguage = Language(R.drawable.language_spanish, "Spanish", "es", false)
            "fr" -> mLanguage = Language(R.drawable.language_french, "French", "fr", false)
            "hi" -> mLanguage = Language(R.drawable.language_hindi, "Hindi", "hi", false)
            "pt" -> mLanguage = Language(R.drawable.language_portuguese, "Portuguese", "pt", false)
        }

        languages.add(Language(R.drawable.language_english, "English", "en", false))
        languages.add(Language(R.drawable.language_spanish, "Spanish", "es", false))
        languages.add(Language(R.drawable.language_french, "French", "fr", false))
        languages.add(Language(R.drawable.language_hindi, "Hindi", "hi", false))
        languages.add(Language(R.drawable.language_portuguese, "Portuguese", "pt", false))

        for (i in languages.indices) {
            if (key == languages[i].languageCode) {
                languages[i].isSelected = true
            }
        }
        adapter?.setSelectLanguage(mLanguage!!)

        return languages
    }
}