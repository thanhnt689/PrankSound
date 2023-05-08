package com.example.soundprank.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.soundprank.R
import com.example.soundprank.adapters.IntroSlideAdapter
import com.example.soundprank.databinding.ActivityIntroBinding
import com.example.soundprank.models.IntroSlide
import com.example.soundprank.models.Sound
import com.example.soundprank.models.SoundPrank
import com.example.soundprank.utils.LocaleHelper
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Random


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

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        // loadInter()

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

        lifecycleScope.launch(Dispatchers.IO) {
            updateData()
        }

    }

    private suspend fun updateData() {
        val listPrankSound = getDataListPrankSound()
        for (soundPrank: SoundPrank in listPrankSound) {
            listAssetFile(soundPrank.path, soundPrank)
        }
    }


    private fun getDataListPrankSound(): List<SoundPrank> {
        return listOf(
            SoundPrank(
                R.drawable.ic_fart,
                getString(R.string.string_fart),
                "fart",
                "#FF9832",
                "#FFD59E"
            ),
            SoundPrank(
                R.drawable.ic_hair_clipper,
                getString(R.string.string_hair_clipper),
                "hair clipper", "#915EFF", "#CBB4FD"
            ),
            SoundPrank(
                R.drawable.ic_air_horn,
                getString(R.string.string_air_horn),
                "air horn",
                "#77B6FF",
                "#BADEFF"
            ),
            SoundPrank(
                R.drawable.ic_scary,
                getString(R.string.string_scary),
                "scary",
                "#E26CFF",
                "#F4C9FF"
            ),
            SoundPrank(
                R.drawable.ic_animals,
                getString(R.string.string_animals),
                "animals",
                "#6B53FF",
                "#BEB4FF"
            ),
            SoundPrank(
                R.drawable.ic_alarm,
                getString(R.string.string_alarm),
                "alarm",
                "#FF7A7A",
                "#FFC9C9"
            ),
            SoundPrank(
                R.drawable.ic_cough,
                getString(R.string.string_cough),
                "cough",
                "#59D45E",
                "#CBFDB4"
            ),
            SoundPrank(
                R.drawable.ic_burp,
                getString(R.string.string_burp),
                "burp",
                "#FFE300",
                "#FFF5A3"
            ),
            SoundPrank(
                R.drawable.ic_breaking,
                getString(R.string.string_breaking),
                "breaking",
                "#2260FF",
                "#C1D3FF"
            ),
            SoundPrank(
                R.drawable.ic_meme_sound,
                getString(R.string.string_meme_sound),
                "meme sound", "#49BEFF", "#C1E7FD"
            ),
            SoundPrank(
                R.drawable.ic_toilet,
                getString(R.string.string_toilet),
                "toilet",
                "#FF7B52",
                "#FFC5B2"
            ),
            SoundPrank(
                R.drawable.ic_gun,
                getString(R.string.string_gun),
                "gun",
                "#FFCE22",
                "#FFECA8"
            ),
            SoundPrank(
                R.drawable.ic_bomb,
                getString(R.string.string_bomb),
                "bomb",
                "#EC3F37",
                "#FFBFBC"
            ),
            SoundPrank(
                R.drawable.ic_snoring,
                getString(R.string.string_snoring),
                "snoring",
                "#ABEC42",
                "#E3FFB5"
            ),
            SoundPrank(
                R.drawable.ic_crying,
                getString(R.string.string_crying),
                "crying",
                "#B455FF",
                "#E1BBFF"
            ),
            SoundPrank(
                R.drawable.ic_door_bell,
                getString(R.string.string_door_bell),
                "door bell",
                "#FF9832",
                "#FFD59E"
            ),
            SoundPrank(
                R.drawable.ic_cat,
                getString(R.string.string_cat),
                "cat",
                "#77B6FF",
                "#BADEFF"
            ),
            SoundPrank(
                R.drawable.ic_dog,
                getString(R.string.string_dog),
                "dog",
                "#E26CFF",
                "#F4C9FF"
            ),
            SoundPrank(
                R.drawable.ic_scissors,
                getString(R.string.string_scissors),
                "scissors",
                "#6B53FF",
                "#BEB4FF"
            ),
            SoundPrank(
                R.drawable.ic_sneezing,
                getString(R.string.string_sneezing),
                "sneezing",
                "#FF7A7A",
                "#FFC9C9"
            ),
            SoundPrank(
                R.drawable.ic_car_horn,
                getString(R.string.string_car_horn),
                "car horn",
                "#59D45E",
                "#CBFDB4"
            ),
        )
    }

    private suspend fun listAssetFile(path: String, soundPrank: SoundPrank) {
        val list: Array<String>?

        try {
            list = assets.list(path)
            if (list!!.isNotEmpty()) {
                for (file in list) {
                    println("File path = $file")
                    if (!soundViewModel.checkExist(file)) {
                        soundViewModel.insertSound(
                            Sound(
                                num = list.indexOf(file) + 1,
                                path = file,
                                folder = soundPrank.path,
                                image = soundPrank.image,
                                favourite = false,
                                idString = convert(soundPrank.path)
                            )
                        )
                    } else {
                        val sound = soundViewModel.getSoundByPath(file)
                        soundViewModel.updateSound(
                            Sound(
                                num = list.indexOf(file) + 1,
                                path = sound.path,
                                folder = sound.folder,
                                image = sound.image,
                                favourite = sound.favourite,
                                idString = convert(soundPrank.path)
                            )
                        )
                    }
                    if (file.indexOf(".") < 0) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
                        if (path == "") {
                            listAssetFile(
                                file,
                                soundPrank
                            ) // <<-- To get subdirectory files and directories list and check
                        } else {
                            listAssetFile(
                                "$path/$file",
                                soundPrank
                            ) // <<-- For Multiple level subdirectories
                        }
                    } else {
                        println("This is a file = $path/$file")
                    }
                }
            } else {
                println("Failed Path = $path")
                println("Check path again.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun convert(string: String): Int {
        var resource = 0
        when (string) {
            "fart" -> resource = R.string.string_fart
            "hair clipper" -> resource = R.string.string_hair_clipper
            "air horn" -> resource = R.string.string_air_horn
            "scary" -> resource = R.string.string_scary
            "animals" -> resource = R.string.string_animals
            "alarm" -> resource = R.string.string_alarm
            "cough" -> resource = R.string.string_cough
            "burp" -> resource = R.string.string_burp
            "breaking" -> resource = R.string.string_breaking
            "meme sound" -> resource = R.string.string_meme_sound
            "toilet" -> resource = R.string.string_toilet
            "gun" -> resource = R.string.string_gun
            "bomb" -> resource = R.string.string_bomb
            "snoring" -> resource = R.string.string_snoring
            "crying" -> resource = R.string.string_crying
            "door bell" -> resource = R.string.string_door_bell
            "cat" -> resource = R.string.string_cat
            "dog" -> resource = R.string.string_dog
            "scissors" -> resource = R.string.string_scissors
            "sneezing" -> resource = R.string.string_sneezing
            "car horn" -> resource = R.string.string_car_horn
        }
        return resource
    }

    private fun clickButtonNext() {

        if (binding.vpIntroSlider.currentItem + 1 < adapter.itemCount) {
            binding.vpIntroSlider.currentItem += 1
            if (binding.vpIntroSlider.currentItem + 1 == adapter.itemCount) {
                binding.btnNext.setText(R.string.string_start)
            }
        } else {
            if (sharedPreferences?.getBoolean("openLanguage", false) == true) {
//                try {
//                    Admob.getInstance().showInterAds(
//                        this@IntroActivity,
//                        AdsInter.inter_intro,
//                        object : InterCallback() {
//                            override fun onNextAction() {
//                                super.onNextAction()
//                                val intent =
//                                    Intent(this@IntroActivity, HomeScreenActivity::class.java)
//                                startActivity(
//                                    intent
//                                )
//                                finish()
//                            }
//                        })
//                } catch (exception: Exception) {
//                    Log.d("ntt", exception.toString())
//                }
                val intent =
                    Intent(this@IntroActivity, HomeScreenActivity::class.java)
                startActivity(
                    intent
                )
                finish()
            } else {
//                try {
//                    Admob.getInstance().showInterAds(
//                        this@IntroActivity,
//                        AdsInter.inter_intro,
//                        object : InterCallback() {
//                            override fun onNextAction() {
//                                super.onNextAction()
//                                val intent =
//                                    Intent(this@IntroActivity, LanguageActivity::class.java)
//                                startActivity(
//                                    intent
//                                )
//                                finish()
//                            }
//                        })
//                } catch (exception: Exception) {
//                    Log.d("ntt", exception.toString())
//                }
                val intent =
                    Intent(this@IntroActivity, LanguageActivity::class.java)
                startActivity(
                    intent
                )
                finish()
            }
            //finish()
        }
    }

    private fun initData() {

//        Admob.getInstance().loadNativeAd(
//            this,
//            getString(R.string.id_ads_native),
//            object : NativeCallback() {
//                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
//                    super.onNativeAdLoaded(nativeAd)
//                    Log.d("ThanhNT", "onNativeAdLoaded")
//                    val adView = LayoutInflater.from(this@IntroActivity)
//                        .inflate(R.layout.ads_navite_small, null) as NativeAdView
//
//                    binding.frAds3.removeAllViews()
//                    binding.frAds3.addView(adView)
//
//                    Admob.getInstance().pushAdsToViewCustom(nativeAd, adView)
//                }
//
//                override fun onAdFailedToLoad() {
//                    binding.frAds3.visibility = View.GONE
//                    binding.frAds3.removeAllViews()
//                }
//            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.BLUE
            window.setTitleColor(resources.getColor(R.color.white))
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

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

//    private fun loadInter() {
//        if (AdsInter.inter_intro == null) {
//            Admob.getInstance()
//                .loadInterAds(this, getString(R.string.id_ads_inter), object : InterCallback() {
//                    override fun onInterstitialLoad(interstitialAd2: InterstitialAd) {
//                        super.onInterstitialLoad(interstitialAd2)
//                        AdsInter.inter_intro = interstitialAd2
//                        Log.d("ntt", "Load true")
//                        // Show button
//                        binding.btnNext.visibility = View.VISIBLE
//                    }
//
//                    override fun onAdFailedToLoad(i: LoadAdError?) {
//                        super.onAdFailedToLoad(i)
//                        Log.d("ntt", "Load false to load")
//                        // Show button
//                        binding.btnNext.visibility = View.VISIBLE
//                    }
//
//                    override fun onAdFailedToShow(adError: AdError?) {
//                        super.onAdFailedToShow(adError)
//                        Log.d("ntt", "Load false to show")
//                        // Show button
//                        binding.btnNext.visibility = View.VISIBLE
//                    }
//                })
//        }
//    }

}