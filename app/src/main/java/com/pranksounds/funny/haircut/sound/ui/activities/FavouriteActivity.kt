package com.pranksounds.funny.haircut.sound.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amazic.ads.util.Admob
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.adapters.SoundFavouriteAdapter
import com.pranksounds.funny.haircut.sound.callback.OnClickCbSound
import com.pranksounds.funny.haircut.sound.callback.OnClickItemSound
import com.pranksounds.funny.haircut.sound.databinding.ActivityFavouriteBinding
import com.pranksounds.funny.haircut.sound.models.Sound
import com.pranksounds.funny.haircut.sound.utils.LocaleHelper
import com.pranksounds.funny.haircut.sound.viewmodel.MyViewModel
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModel
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModelFactory
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

    private val myViewModel: MyViewModel by viewModels()

    private val localeHelper = LocaleHelper()

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

            myViewModel.num.observe(this) {
                if (it != 0) {
                    binding.btnRemoveAll.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSelectAll.setOnClickListener {
            Log.d("ntt","Click Select all")
            listSoundCheck.clear()

            showAll = !showAll

            if (showAll) {

                binding.btnSelectAll.text = getString(R.string.string_remove_all)
                listSoundCheck.addAll(listSound)

                for (sound: Sound in listSound) {
                    sound.isSelected = true
                }
                adapter.notifyDataSetChanged()

                binding.btnRemoveAll.visibility = View.VISIBLE

                myViewModel.setValueNum(listSound.size)
            } else {

                binding.btnSelectAll.text = getString(R.string.string_select_all)

                for (sound: Sound in listSound) {
                    sound.isSelected = false
                }
                adapter.notifyDataSetChanged()

                listSoundCheck.clear()

                myViewModel.setValueNum(0)

                binding.btnRemoveAll.visibility = View.GONE

            }

        }

        binding.btnRemoveAll.setOnClickListener {
            for (sound: Sound in listSound) {
                if (sound.isSelected) {
                    soundViewModel.updateSound(
                        Sound(
                            sound.num,
                            sound.path,
                            sound.folder,
                            sound.image,
                            false,
                            sound.idString
                        )
                    )
                }
            }

            binding.btnSelectAll.visibility = View.GONE
            binding.btnCheck.visibility = View.VISIBLE
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.text = getString(R.string.string_select_all)

            listSoundCheck.clear()
            showAll = false
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ntt","onResume")
        localeHelper.setLanguage(this)
//        binding.btnRemoveAll.visibility = View.GONE
//
//        binding.btnSelectAll.text = getText(R.string.string_select_all)
//        myViewModel.sounds.observe(this) {
//            if (it != null) {
//                binding.btnRemoveAll.visibility = View.VISIBLE
//                binding.btnSelectAll.visibility = View.VISIBLE
//                binding.btnCheck.visibility = View.GONE
//            }
//        }

        binding.btnRemoveAll.visibility = View.GONE

        init()

        myViewModel.num.observe(this) {
            Log.d("ntt", "it $it")
            if (it == 0) {
                binding.btnRemoveAll.visibility = View.GONE
                binding.btnSelectAll.text = getText(R.string.string_select_all)
                showAll = false
            } else if (it == listSound.size) {
                binding.btnSelectAll.text = getText(R.string.string_remove_all)
                adapter.setShowOrHideCheckBox(true)
                show = true
                binding.btnCheck.visibility = View.GONE
                binding.btnSelectAll.visibility = View.VISIBLE
                showAll = true
                binding.btnRemoveAll.visibility = View.VISIBLE
            }else if(it == -1){
                Log.d("ntt","-1")
                binding.btnCheck.visibility = View.VISIBLE
                binding.btnSelectAll.visibility = View.GONE
                binding.btnRemoveAll.visibility = View.GONE
                showAll = false
            }
            else {
                //binding.btnRemoveAll.visibility = View.
                binding.btnSelectAll.text = getText(R.string.string_remove_all)
                adapter.setShowOrHideCheckBox(true)
                show = true
                binding.btnCheck.visibility = View.GONE
                binding.btnSelectAll.visibility = View.VISIBLE
                showAll = true
                binding.btnRemoveAll.visibility = View.VISIBLE
            }
        }
    }


    override fun onStop() {
        super.onStop()

        binding.btnCheck.visibility = View.VISIBLE
        binding.btnSelectAll.visibility = View.GONE
        binding.btnRemoveAll.visibility = View.GONE

        adapter.setShowOrHideCheckBox(false)

//        listSoundCheck.clear()
//
//        if (listSoundCheck != null) {
//            binding.btnRemoveAll.visibility = View.VISIBLE
//        }

    }

    private fun init() {
        soundViewModel.soundFavourite.observe(this) {
            Log.d("ThanhNT", "observer")
            listSound.clear()
            listSound.addAll(it)

            if (it.isEmpty()) {
                binding.cardViewCategory.visibility = View.VISIBLE
                binding.rvSoundPrank.visibility = View.GONE
                binding.btnRemoveAll.visibility = View.GONE
                binding.btnCheck.visibility = View.GONE
                binding.btnSelectAll.visibility = View.GONE
            } else {
                binding.cardViewCategory.visibility = View.GONE
                binding.rvSoundPrank.visibility = View.VISIBLE
            }

            adapter = SoundFavouriteAdapter(this, listSound, this, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager =
                GridLayoutManager(this, 2)

        }


        // Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

        binding.btnSelectAll.isSelected = true
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
        for (soundI: Sound in listSound) {
            if (soundI == sound) {
                soundI.isSelected = check
            }
        }

        if (check) {

            if (!listSoundCheck.contains(sound)) {
                listSoundCheck.add(sound)
            }


        } else {
            //listSoundCheck.remove(sound)

            if (listSoundCheck.contains(sound)) {
                listSoundCheck.remove(sound)
            }
            //myViewModel.getSounds(listSoundCheck)
        }

        myViewModel.setValueNum(listSoundCheck.size)

        Log.d("ThanhNT", "OnCLickSound: ${listSoundCheck.size}")
        if (listSoundCheck.isEmpty()) {
            binding.btnRemoveAll.visibility = View.GONE
            binding.btnSelectAll.text = getText(R.string.string_select_all)
            showAll = false
        } else if (listSoundCheck.size == listSound.size) {
            binding.btnSelectAll.text = getText(R.string.string_remove_all)
            showAll = true
            binding.btnRemoveAll.visibility = View.VISIBLE
        } else {
            binding.btnRemoveAll.visibility = View.VISIBLE
        }
    }

}
