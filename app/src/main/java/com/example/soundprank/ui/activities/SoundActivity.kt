package com.example.soundprank.ui.activities

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.SoundAdapter
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.ActivitySoundBinding
import com.example.soundprank.models.Sound
import com.example.soundprank.models.SoundPrank
import com.example.soundprank.utils.Const
import com.example.soundprank.viewmodel.MyViewModel
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SoundActivity : AppCompatActivity(), OnClickItemSound {
    private lateinit var binding: ActivitySoundBinding

    private lateinit var soundPrank: SoundPrank

    private lateinit var adapter: SoundAdapter

    private val viewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }

    private lateinit var myViewModel: MyViewModel

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editTor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundPrank = intent.getSerializableExtra("sound_prank") as SoundPrank

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

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
            binding.tvPrankSound.text = soundPrank.name
            adapter = SoundAdapter(list, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager = GridLayoutManager(this, 2)

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        editTor = sharedPreferences.edit()

    }

    override fun onResume() {
        super.onResume()

        if (DetailPrankSoundActivity.check) {

            DetailPrankSoundActivity.check = false

            val numShowRating = sharedPreferences.getInt(Const.NUM_SHOW_RATING, 1)

            if (numShowRating % 2 == 1) {
                openRatingDialog()
            }

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

    private fun openRatingDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_rating)
        val window: Window? = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val tvTitle: TextView = dialog.findViewById(R.id.tv_title)
        val tvContent: TextView = dialog.findViewById(R.id.tv_content)
        val ratingBar: RatingBar = dialog.findViewById(R.id.rtb)
        val imgIcon: ImageView = dialog.findViewById(R.id.img_icon)
        val btnRate: Button = dialog.findViewById(R.id.btn_rate)
        val btnExit: Button = dialog.findViewById(R.id.btn_exit)
        val tvt: TextView = dialog.findViewById(R.id.tvt)

        tvt.isSelected = true

        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                when (ratingBar?.rating.toString()) {
                    "0.0" -> {
                        tvTitle.text = getString(R.string.string_do_you_like_the_app)
                        tvContent.text = getString(R.string.string_let_us_know_your_experience)
                        imgIcon.setImageResource(R.drawable.rate_0)
                    }

                    "1.0" -> {
                        tvTitle.text = getString(R.string.string_oh_no)
                        tvContent.text = getString(R.string.string_please_give_us_some_feedback)
                        imgIcon.setImageResource(R.drawable.rate_1)
                    }

                    "2.0" -> {
                        tvTitle.text = getString(R.string.string_oh_no)
                        tvContent.text = getString(R.string.string_please_give_us_some_feedback)
                        imgIcon.setImageResource(R.drawable.rate_2)
                    }

                    "3.0" -> {
                        tvTitle.text = getString(R.string.string_oh_no)
                        tvContent.text = getString(R.string.string_please_give_us_some_feedback)
                        imgIcon.setImageResource(R.drawable.rate_3)
                    }

                    "4.0" -> {
                        tvTitle.text = getString(R.string.string_we_like_you_too)
                        tvContent.text = getString(R.string.string_thanks_for_your_feedback)
                        imgIcon.setImageResource(R.drawable.rate_4)
                    }

                    "5.0" -> {
                        tvTitle.text = getString(R.string.string_we_like_you_too)
                        tvContent.text = getString(R.string.string_thanks_for_your_feedback)
                        imgIcon.setImageResource(R.drawable.rate_5)
                    }
                }
            }

        btnRate.setOnClickListener {
            if (ratingBar.rating.toString() == "0.0") {
                Toast.makeText(
                    this,
                    "Please feedback",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Thank for the rate: ${ratingBar.rating}",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
        }

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}