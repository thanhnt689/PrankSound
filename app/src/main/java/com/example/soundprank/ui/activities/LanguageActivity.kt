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

    override fun onClick(language: Language) {
        adapter?.setSelectLanguage(language)
        mLanguage = language
    }

    private fun setLanguageDefault(): List<Language> {
        val languages: MutableList<Language> = ArrayList()
        val localeHelper = LocaleHelper()
        val key: String = localeHelper.getLanguage(this).toString()

        languages.add(Language(R.drawable.language_english, "English", "en", false))
        languages.add(Language(R.drawable.language_spanish, "Spanish", "es", false))
        languages.add(Language(R.drawable.language_french, "French", "fr", false))
        languages.add(Language(R.drawable.language_hindi, "Hindi", "hi", false))
        languages.add(Language(R.drawable.language_portuguese, "Portuguese", "pt", false))

        var lang = "en"

        lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }
        var count = 0
        for (language: Language in languages) {
            if (language.languageCode == lang) {
                count++
            }
        }
        if (count == 0) {
            mLanguage = Language(R.drawable.language_english, "English", "en", false)
            adapter?.setSelectLanguage(mLanguage!!)
        }
        Log.e("", "setLanguageDefault: $key")
        for (i in languages.indices) {
            if (key == languages[i].languageCode) {
                languages[i].isSelected = true
            }
        }
        return languages
    }
}