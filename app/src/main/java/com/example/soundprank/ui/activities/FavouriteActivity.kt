package com.example.soundprank.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.soundprank.adapters.SoundFavouriteAdapter
import com.example.soundprank.callback.OnClickCbSound
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.ActivityFavouriteBinding
import com.example.soundprank.models.Sound
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory

class FavouriteActivity : AppCompatActivity(), OnClickItemSound, OnClickCbSound {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter: SoundFavouriteAdapter
    private var show = false
    private var showAll = false

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCheck.setOnClickListener {
            show = !show
            adapter.setShowOrHideCheckBox(show)
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.visibility = View.VISIBLE
            binding.btnCheck.visibility = View.GONE
        }

        binding.btnSelectAll.setOnClickListener {
            showAll = !showAll
            adapter.setCheckBoxAll(show)
            binding.btnCheck.visibility = View.GONE
            binding.btnRemoveAll.visibility = View.VISIBLE
        }
    }

    private fun init() {
        soundViewModel.soundFavourite.observe(this) {
            Log.d("ntt", it.toString())
            val listSoundFavourite = arrayListOf<Sound>()
            listSoundFavourite.addAll(it)
            adapter = SoundFavouriteAdapter(listSoundFavourite, this, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager =
                GridLayoutManager(this, 2)
        }
    }

    override fun onCLickItemSound(sound: Sound) {
        val intent = Intent(this, DetailPrankSoundActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("sound", sound)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClickCbSound(check: Boolean) {
        if (check) {
            binding.btnRemoveAll.visibility = View.VISIBLE
        } else {
            binding.btnRemoveAll.visibility = View.GONE
        }
    }
}