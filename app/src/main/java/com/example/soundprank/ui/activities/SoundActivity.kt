package com.example.soundprank.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.soundprank.R
import com.example.soundprank.adapters.SoundAdapter
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.ActivitySoundBinding
import com.example.soundprank.models.Sound
import com.example.soundprank.models.SoundPrank
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class SoundActivity : AppCompatActivity(), OnClickItemSound {
    private lateinit var binding: ActivitySoundBinding
    private lateinit var soundPrank: SoundPrank
    private lateinit var adapter: SoundAdapter
    private val sounds: ArrayList<Sound> = arrayListOf()

    private val viewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundPrank = intent.getSerializableExtra("sound_prank") as SoundPrank

        lifecycleScope.launch(Dispatchers.Main) {
            listAssetFile(soundPrank.path)
        }

        viewModel.sounds.observe(this) {
            for (sound: Sound in it){
                if(sound.path == soundPrank.path){
                    sounds.add(sound)
                }
            }

            binding.tvPrankSound.text = soundPrank.name
            adapter = SoundAdapter(sounds, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager = GridLayoutManager(this, 2)
        }


        binding.btnBack.setOnClickListener {
            finish()
        }

//        binding.tvPrankSound.text = soundPrank.name
//        adapter = SoundAdapter(sounds, this)
//        binding.rvSoundPrank.adapter = adapter
//        binding.rvSoundPrank.layoutManager = GridLayoutManager(this, 2)

    }

    override fun onCLickItemSound(sound: Sound) {
        val intent = Intent(this, DetailPrankSoundActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("sound", sound)
        intent.putExtras(bundle)
        intent.putExtra("soundPrank", soundPrank.name.lowercase())
        startActivity(intent)
    }

    private suspend fun listAssetFile(path: String) {
        val list: Array<String>?

        try {
            list = assets.list(path)
            if (list!!.isNotEmpty()) {
                for (file in list) {
                    println("File path = $file")
                    if (!viewModel.checkExist(file)) {
                        viewModel.insertSound(
                            Sound(
                                name = "${soundPrank.name} ${list.indexOf(file)}",
                                path = file,
                                image = soundPrank.image,
                                favourite = false
                            )
                        )
//                        sounds.add(
//                            Sound(
//                                name = "${soundPrank.name} ${list.indexOf(file)}",
//                                path = file,
//                                image = soundPrank.image,
//                                favourite = false
//                            )
//                        )
                    }
                    if (file.indexOf(".") < 0) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
                        if (path == "") {
                            listAssetFile(file) // <<-- To get subdirectory files and directories list and check
                        } else {
                            listAssetFile("$path/$file") // <<-- For Multiple level subdirectories
                        }
                    } else {
                        println("This is a file = $path/$file")
                    }
                }
            } else {
                println("Failed Path = $path")
                println("Check path again.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}