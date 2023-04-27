package com.example.soundprank.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.IntroSlideAdapter
import com.example.soundprank.databinding.ActivityIntroBinding
import com.example.soundprank.models.IntroSlide
import com.example.soundprank.utils.AdsInter
import com.example.soundprank.utils.LocaleHelper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var adapter: IntroSlideAdapter
    private var sharedPreferences: SharedPreferences? = null
    private val localeHelper = LocaleHelper()
    private val introSlides: List<IntroSlide> = listOf(
        IntroSlide(R.drawable.intro_slide_1),
        IntroSlide(R.drawable.intro_slide_2),
        IntroSlide(R.drawable.intro_slide_3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        loadInter()

        binding.vpIntroSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        binding.btnNext.setOnClickListener {
            clickButtonNext()
        }

    }

    private fun clickButtonNext() {

        if (binding.vpIntroSlider.currentItem + 1 < adapter.itemCount) {
            binding.vpIntroSlider.currentItem += 1
            if (binding.vpIntroSlider.currentItem + 1 == adapter.itemCount) {
                binding.btnNext.setText(R.string.string_start)
            }
        } else {
            if (sharedPreferences?.getBoolean("openLanguage", false) == true) {
                try {
                    Admob.getInstance().showInterAds(this@IntroActivity, AdsInter.inter_intro, object : InterCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            val intent = Intent(this@IntroActivity, HomeScreenActivity::class.java)
                            startActivity(
                                intent
                            )
                            finish()
                        }
                    })
                } catch (exception: Exception) {
                    Log.d("ntt",exception.toString())
                }
            } else {
                try {
                    Admob.getInstance().showInterAds(this@IntroActivity, AdsInter.inter_intro, object : InterCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            val intent = Intent(this@IntroActivity, LanguageActivity::class.java)
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
            //finish()
        }
    }

    private fun initData() {
        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        if (sharedPreferences?.getBoolean("openLanguage", false) == true) {
            localeHelper.setLanguage(this)
        }

        adapter = IntroSlideAdapter(introSlides)
        binding.vpIntroSlider.adapter = adapter

        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorsContainer.childCount
        if (index == 2) {
            binding.btnNext.text = getString(R.string.string_start)
        } else {
            binding.btnNext.text = getString(R.string.string_next)
        }
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
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
                        binding.btnNext.visibility = View.VISIBLE
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        Log.d("ntt", "Load false to load")
                        // Show button
                        binding.btnNext.visibility = View.VISIBLE
                    }

                    override fun onAdFailedToShow(adError: AdError?) {
                        super.onAdFailedToShow(adError)
                        Log.d("ntt", "Load false to show")
                        // Show button
                        binding.btnNext.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun showActivity() {
        try {
            Admob.getInstance().showInterAds(this, AdsInter.inter_intro, object : InterCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    val intent = Intent(this@IntroActivity, IntroActivity::class.java)
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