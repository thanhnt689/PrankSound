package com.example.soundprank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.databinding.ActivityAboutBinding
import com.example.soundprank.utils.LocaleHelper

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.tvAbout.text = getString(R.string.string_about)
        binding.tvAppName.text = getString(R.string.app_name)
        binding.tvVersion.text = getString(R.string.string_version_1_0)

        // Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))
    }
}