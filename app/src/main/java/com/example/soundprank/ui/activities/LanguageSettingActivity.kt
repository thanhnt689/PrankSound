package com.example.soundprank.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundprank.R
import com.example.soundprank.adapters.LanguageAdapter
import com.example.soundprank.callback.OnClickItemLanguage
import com.example.soundprank.databinding.ActivityLanguageSettingBinding
import com.example.soundprank.models.Language
import com.example.soundprank.models.Sound
import com.example.soundprank.utils.LocaleHelper
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LanguageSettingActivity : AppCompatActivity(), OnClickItemLanguage {

    private lateinit var binding: ActivityLanguageSettingBinding

    private var sharedPreferences: SharedPreferences? = null

    private var listSound = arrayListOf<Sound>()

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    private lateinit var progressBar: ProgressDialog

    private var mLanguage: Language? = null

    private var adapter: LanguageAdapter? = null

    private val localeHelper = LocaleHelper()

    companion object {
        var checkChangeLanguage = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        soundViewModel.sounds.observe(this) {
            listSound.clear()
            listSound.addAll(it)
        }

    }

    private fun init() {
        localeHelper.setLanguage(this)

        binding.tvLanguage.text = getString(R.string.string_language)

        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        adapter = LanguageAdapter(setLanguageDefault(), this)
        binding.rvLanguage.adapter = adapter
        binding.rvLanguage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

    }

    override fun onClick(language: Language) {

        checkChangeLanguage = true

        adapter?.setSelectLanguage(language)
        mLanguage = language

        val localeHelper = LocaleHelper()
        val editor: SharedPreferences.Editor =
            getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit()
        editor.putBoolean("openLanguage", true)
        editor.apply()

        mLanguage?.let { localeHelper.setPreLanguage(this, it.languageCode) }
        localeHelper.setLanguage(this)

        //updateSound()

        progressBar = ProgressDialog(this)
        progressBar.setCancelable(false)
        progressBar.setMessage("Đang xử lý, vui lòng đợi...")
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressBar.show()

//        Thread {
//            updateSound()
//            Thread.sleep(1000)
//            progressBar.dismiss();
//            finish()
//        }.start()

        lifecycleScope.launch(Dispatchers.IO) {
            updateSound()
            delay(5000)
            withContext(Dispatchers.Main) {
                progressBar.dismiss();
                finish()
            }
        }

//        finish()

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

        val map = HashMap<String, Sound>()
        Log.d("ntt","${listSound.size}")
        for (sound: Sound in listSound) {
            val num = sound.name.filter { it.isDigit() }
            map[num] = sound
            Log.d("ntt", "${map.size}")
//            soundViewModel.updateSound(
//                Sound(
//                    "${getString(convert(sound.folder))} $num",
//                    sound.path,
//                    sound.folder,
//                    sound.image,
//                    sound.favourite
//                )
//            )
        }

//        val set: MutableSet<String> = map.keys
//        for (key in set) {
//            soundViewModel.updateSound(
//                Sound(
//                    "${getString(convert(map[key]!!.folder))} $key",
//                    map[key]!!.path,
//                    map[key]!!.folder,
//                    map[key]!!.image,
//                    map[key]!!.favourite
//                )
//            )
//        }

        for (entry: Map.Entry<String, Sound> in map.entries) {
            Log.d("ntt", "$entry")
            soundViewModel.updateSound(
                Sound(
                    "${getString(convert(entry.value.folder))} ${entry.key}",
                    entry.value.path,
                    entry.value.folder,
                    entry.value.image,
                    entry.value.favourite
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