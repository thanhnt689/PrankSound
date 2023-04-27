package com.example.soundprank.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.LanguageAdapter
import com.example.soundprank.callback.OnClickItemLanguage
import com.example.soundprank.databinding.ActivityLanguageSettingBinding
import com.example.soundprank.models.Language
import com.example.soundprank.models.Sound
import com.example.soundprank.utils.LocaleHelper
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory

class LanguageSettingActivity : AppCompatActivity(), OnClickItemLanguage {

    private lateinit var binding: ActivityLanguageSettingBinding

    private var sharedPreferences: SharedPreferences? = null

    private var listSound = arrayListOf<Sound>()

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    private var mLanguage: Language? = null

    private var adapter: LanguageAdapter? = null

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localeHelper.setLanguage(this)

        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        adapter = LanguageAdapter(setLanguageDefault(), this)
        binding.rvLanguage.adapter = adapter
        binding.rvLanguage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.btnBack.setOnClickListener {

            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        soundViewModel.sounds.observe(this) {
            listSound.clear()
            listSound.addAll(it)
        }

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))
    }

    override fun onClick(language: Language) {
        adapter?.setSelectLanguage(language)
        mLanguage = language

        val localeHelper = LocaleHelper()
        val editor: SharedPreferences.Editor =
            getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit()
        editor.putBoolean("openLanguage", true)
        editor.apply()

        mLanguage?.let { localeHelper.setPreLanguage(this, it.languageCode) }
        localeHelper.setLanguage(this)

        updateSound()

        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
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

    private fun updateSound() {
        for (sound: Sound in listSound) {

            val num = sound.name.filter { it.isDigit() }

            soundViewModel.updateSound(
                Sound(
                    "${getString(convert(sound.folder))} $num",
                    sound.path,
                    sound.folder,
                    sound.image,
                    sound.favourite
                )
            )

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
}