package com.example.soundprank.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

class LocaleHelper {

    private lateinit var myLocale: Locale

    /**
     * function reloads saved languages and changes them
     *
     */
    fun setLanguage(context: Context) {
        val language = getLanguage(context)
        if (language == "") {
            val configuration = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        } else {
            if (language != null) {
                changeLanguage(context, language)
            }
        }
    }

    /**
     * function change language
     *
     */
    private fun changeLanguage(context: Context, language: String) {
        myLocale = Locale(language)
        if (language == "") {
            return
        } else {
            val preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
            preferences.edit().putString("KEY_LANGUAGE", language).apply()
        }

        Locale.setDefault(myLocale)

        val configuration = Configuration()
        configuration.locale = myLocale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    /**
     * function change the selected language code
     *
     */
    fun setPreLanguage(context: Context, language: String) {
        if (language == "") {
            return
        } else {
            val preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
            preferences.edit().putString("KEY_LANGUAGE", language).apply()
        }
    }

    /**
     * function get language
     *
     */
    fun getLanguage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        Locale.getDefault().displayLanguage

        val language = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }

        return if (getLanguageApp(context)
                .contains(language)
        ) {
            sharedPreferences.getString("KEY_LANGUAGE", "en")
        } else {
            sharedPreferences.getString("KEY_LANGUAGE", language)
        }
    }

    /**
     * function get language code app
     *
     */
    private fun getLanguageApp(context: Context): List<String> {
        val sharedPreferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        val openLanguage = sharedPreferences.getBoolean("openLanguage", false)
        val languages: MutableList<String> = ArrayList()
        if (openLanguage) {
            languages.add("en")
            languages.add("es")
            languages.add("fr")
            languages.add("hi")
            languages.add("pt")
        } else {
            languages.add("en")
            languages.add("es")
            languages.add("fr")
            languages.add("hi")
            languages.add("pt")
        }
        return languages
    }
}