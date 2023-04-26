package com.example.soundprank.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundprank.R
import com.example.soundprank.adapters.LanguageAdapter
import com.example.soundprank.callback.OnClickItemLanguage
import com.example.soundprank.databinding.ActivityLanguageBinding
import com.example.soundprank.models.Language
import com.example.soundprank.utils.LocaleHelper


class LanguageActivity : AppCompatActivity(), OnClickItemLanguage {

    private lateinit var binding: ActivityLanguageBinding

    private var sharedPreferences: SharedPreferences? = null

    private var mLanguage: Language? = null

    private var adapter: LanguageAdapter? = null

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localeHelper.setLanguage(this)

        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        adapter = LanguageAdapter(setLanguageDefault(), this)
        binding.rvLanguage.adapter = adapter
        binding.rvLanguage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.btnDone.setOnClickListener {
            clickButtonDone()
        }
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