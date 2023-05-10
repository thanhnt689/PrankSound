package com.pranksounds.funny.haircut.sound.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazic.ads.util.Admob
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.adapters.LanguageAdapter
import com.pranksounds.funny.haircut.sound.callback.OnClickItemLanguage
import com.pranksounds.funny.haircut.sound.databinding.ActivityLanguageSettingBinding
import com.pranksounds.funny.haircut.sound.models.Language
import com.pranksounds.funny.haircut.sound.models.Sound
import com.pranksounds.funny.haircut.sound.utils.Const
import com.pranksounds.funny.haircut.sound.utils.LocaleHelper
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModel
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModelFactory


class LanguageSettingActivity : AppCompatActivity(), OnClickItemLanguage {

    private lateinit var binding: ActivityLanguageSettingBinding

    private var sharedPreferences: SharedPreferences? = null

    private var listSound = arrayListOf<Sound>()

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    private lateinit var progressDialog: ProgressDialog

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

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

    }

    override fun onClick(language: Language) {

        checkChangeLanguage = true

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.string_its_loading))
        progressDialog.setCancelable(false)

        adapter?.setSelectLanguage(language)
        mLanguage = language

        val localeHelper = LocaleHelper()
        val editor: SharedPreferences.Editor =
            getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit()
        editor.putBoolean("openLanguage", true)

        editor.putInt(Const.NUM_SHOW_INTER, 1)

        editor.apply()

        mLanguage?.let { localeHelper.setPreLanguage(this, it.languageCode) }
        localeHelper.setLanguage(this)

        progressDialog.show()

        object : Thread() {
            override fun run() {
                super.run()
                try {
                    sleep(500)
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()


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
