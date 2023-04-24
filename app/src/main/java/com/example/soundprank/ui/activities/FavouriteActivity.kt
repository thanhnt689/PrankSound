package com.example.soundprank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundprank.R
import com.example.soundprank.adapters.SoundFavouriteAdapter
import com.example.soundprank.callback.OnClickItemSoundFavourite
import com.example.soundprank.databinding.ActivityFavouriteBinding
import com.example.soundprank.models.Sound

class FavouriteActivity : AppCompatActivity(), OnClickItemSoundFavourite {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter: SoundFavouriteAdapter
    private var sounds = arrayListOf<Sound>(

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SoundFavouriteAdapter(sounds, this)
        binding.rvSoundPrank.adapter = adapter
        binding.rvSoundPrank.layoutManager =
            GridLayoutManager(this, 2)
    }

    override fun onClickItemSoundFavourite(sound: Sound) {

    }
}