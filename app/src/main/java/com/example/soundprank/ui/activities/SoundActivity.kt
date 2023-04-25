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

        lifecycleScope.launch(Dispatchers.IO) {
            listAssetFile(soundPrank.path)
        }

        viewModel.sounds.observe(this) {
            Log.d("ntt", it.toString())
            val list = arrayListOf<Sound>()

            for (sound: Sound in it) {
                if (sound.folder == soundPrank.path) {
                    list.add(sound)
                }
            }
            Log.d("ntt", sounds.toString())
            binding.tvPrankSound.text = soundPrank.name
            adapter = SoundAdapter(list, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager = GridLayoutManager(this, 2)

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onCLickItemSound(sound: Sound) {
        val intent = Intent(this, DetailPrankSoundActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("sound", sound)
        intent.putExtras(bundle)
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
                                folder = soundPrank.path,
                                image = soundPrank.image,
                                favourite = false
                            )
                        )
                    } else {
                        val sound = viewModel.getSoundByPath(file)
                        viewModel.updateSound(
                            Sound(
                                name = "${soundPrank.name} ${list.indexOf(file)}",
                                path = sound.path,
                                folder = sound.folder,
                                image = sound.image,
                                favourite = sound.favourite
                            )
                        )
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