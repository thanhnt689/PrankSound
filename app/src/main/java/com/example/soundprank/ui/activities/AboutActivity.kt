package com.example.soundprank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.soundprank.R
import com.example.soundprank.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_about)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}