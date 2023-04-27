package com.example.soundprank.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.SoundFavouriteAdapter
import com.example.soundprank.callback.OnClickCbSound
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.ActivityFavouriteBinding
import com.example.soundprank.models.Sound
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import kotlin.system.exitProcess

class FavouriteActivity : AppCompatActivity(), OnClickItemSound, OnClickCbSound {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter: SoundFavouriteAdapter

    private var show = false

    private var showAll = false

    private var listSoundCheck = arrayListOf<Sound>()

    private var listSound = arrayListOf<Sound>()

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
            adapter.setShowOrHideCheckBox(true)
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.visibility = View.VISIBLE
            binding.btnCheck.visibility = View.GONE
        }

        binding.btnSelectAll.setOnClickListener {
            showAll = !showAll

            if (showAll) {
                binding.btnSelectAll.text = getString(R.string.string_remove_all)
            } else {
                binding.btnSelectAll.text = getString(R.string.string_select_all)
            }

            adapter.setCheckBoxAll(showAll)
            binding.btnRemoveAll.visibility = View.VISIBLE
        }

        binding.btnRemoveAll.setOnClickListener {
            for (sound: Sound in listSoundCheck) {
                soundViewModel.updateSound(
                    Sound(
                        sound.name,
                        sound.path,
                        sound.folder,
                        sound.image,
                        false
                    )
                )
            }

            binding.btnSelectAll.visibility = View.GONE
            binding.btnCheck.visibility = View.VISIBLE
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.text = getString(R.string.string_select_all)

            listSoundCheck.clear()
        }
    }

    private fun init() {

        soundViewModel.soundFavourite.observe(this) {
            listSound.clear()
            Log.d("ntt", it.toString())
            val listSoundFavourite = arrayListOf<Sound>()
            listSoundFavourite.addAll(it)
            listSound.addAll(it)
            adapter = SoundFavouriteAdapter(listSoundFavourite, this, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager =
                GridLayoutManager(this, 2)

            if (it.isEmpty()) {
                binding.layoutNoFavourite.visibility = View.VISIBLE
                binding.btnRemoveAll.visibility = View.GONE
                binding.btnCheck.visibility = View.GONE
                binding.btnSelectAll.visibility = View.GONE
            }

            if (it.isNotEmpty()) {
                binding.layoutNoFavourite.visibility = View.GONE
            }
        }

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

        binding.btnSelectAll.isSelected = true
        binding.tvNoFavouriteYet.isSelected = true
    }

    override fun onCLickItemSound(sound: Sound) {
        val intent = Intent(this, DetailPrankSoundActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("sound", sound)
        intent.putExtras(bundle)
        startActivity(intent)
        finishActivity(0)
    }

    override fun onClickCbSound(check: Boolean, sound: Sound) {
        if (check) {
            listSoundCheck.add(sound)
        } else {
            listSoundCheck.remove(sound)
        }

        Log.d("ntt", listSoundCheck.toString())

        if (listSoundCheck.isEmpty()) {
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.text = getText(R.string.string_select_all)
            showAll = false
        } else if (listSoundCheck.size == listSound.size) {
            binding.btnSelectAll.text = getText(R.string.string_remove_all)
            showAll = true
        } else {
            binding.btnRemoveAll.visibility = View.VISIBLE
        }
    }

}