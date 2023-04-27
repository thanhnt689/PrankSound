package com.example.soundprank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.soundprank.R
import com.example.soundprank.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private lateinit var btnBack:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}