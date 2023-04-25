package com.example.soundprank.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.AssetFileDescriptor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.soundprank.R
import com.example.soundprank.databinding.ActivityDetailPrankSoundBinding
import com.example.soundprank.models.Sound
import com.example.soundprank.utils.Const
import com.example.soundprank.viewmodel.MyViewModel
import com.example.soundprank.viewmodel.SoundViewModel
import com.example.soundprank.viewmodel.SoundViewModelFactory
import java.util.concurrent.TimeUnit

class DetailPrankSoundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPrankSoundBinding

    private lateinit var viewModel: MyViewModel

    private val soundViewModel: SoundViewModel by viewModels() {
        SoundViewModelFactory(application)
    }
    private lateinit var sound: Sound
    private var nameSoundPrank: String = ""
    private var loop: Boolean = false
    private val mediaPlayer = MediaPlayer()
    private var mMediaState: Int = Const.MEDIA_IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPrankSoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sound = intent.getSerializableExtra("sound") as Sound

        init()

        binding.btnTime.setOnClickListener {
            showDialogChooseTime()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.time.observe(this) {
            binding.btnTime.text = it
        }

        binding.btnFavourite.setOnClickListener {
            setFavouriteSound()
        }

        binding.btnLoop.setOnClickListener {
            setLoopSound()
        }

        binding.btnPlayOrPause.setOnClickListener {
            playSound(loop)
        }
    }

    private fun playSound(loop: Boolean) {
        Log.d("ntt", "Loop $loop")
        if (mMediaState == Const.MEDIA_IDLE || mMediaState == Const.MEDIA_STOP) {
            mediaPlayer.reset()
            if (binding.btnTime.text == getString(R.string.string_off)) {
                Log.d("ntt", "Loop 1 $loop")
                mediaPlayer.isLooping = loop

                val descriptor: AssetFileDescriptor =
                    this.assets.openFd("${sound.folder}/${sound.path}")
                mediaPlayer.setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
                descriptor.close()

                mediaPlayer.prepare()

                mediaPlayer.start()

                checkSoundPlayOrStop(mediaPlayer)
            } else {
                val string = binding.btnTime.text.toString()

                val result = string.filter { it.isDigit() }

                binding.layoutInformTime.visibility = View.VISIBLE
                Log.d("ntt", "Loop 1 $loop")
                mediaPlayer.isLooping = loop

                val descriptor: AssetFileDescriptor =
                    this.assets.openFd("${sound.folder}/${sound.path}")
                mediaPlayer.setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
                descriptor.close()

                mediaPlayer.prepare()

                //mediaPlayer.isLooping = loop

                val timer = object : CountDownTimer((result.toInt() * 1000).toLong(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val timeText = String.format(
                            "%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            millisUntilFinished
                                        )
                                    )
                        );
                        binding.tvTime.text = timeText
                    }

                    override fun onFinish() {
                        mediaPlayer.start()
                        checkSoundPlayOrStop(mediaPlayer)
                    }
                }
                timer.start()
            }

        } else if (mMediaState == Const.MEDIA_PLAYING) {
            mediaPlayer.pause()
            mediaPlayer.isLooping = loop
            checkSoundPlayOrStop(mediaPlayer)
            mMediaState = Const.MEDIA_PAUSE

        } else if (mMediaState == Const.MEDIA_PAUSE) {
            mediaPlayer.start()
            mediaPlayer.isLooping = loop
            checkSoundPlayOrStop(mediaPlayer)
            mMediaState = Const.MEDIA_PLAYING
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setLoopSound() {
        if (!loop) {
            binding.btnLoop.background = resources.getDrawable(R.drawable.ic_loop_t)
            loop = true
        } else {
            binding.btnLoop.background = resources.getDrawable(R.drawable.ic_loop_f)

            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                checkSoundPlayOrStop(mediaPlayer)
            }

            loop = false
        }

    }

    private fun setFavouriteSound() {
        sound.favourite = !sound.favourite

        soundViewModel.updateSound(
            Sound(sound.name, sound.path, sound.folder, sound.image, sound.favourite)
        )

        loadFavourite()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        Glide.with(binding.imgSound)
            .load(sound.image)
            .centerCrop()
            .into(binding.imgSound)

        binding.tvPrankSound.text = sound.name

        binding.tvt.isSelected = true

        viewModel.setValueTime(getString(R.string.string_off))

        loadFavourite()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadFavourite() {
        if (sound.favourite) {
            binding.btnFavourite.background = resources.getDrawable(R.drawable.ic_favourite_t)
        } else {
            binding.btnFavourite.background = resources.getDrawable(R.drawable.ic_favourite_f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.pause()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private fun checkSoundPlayOrStop(mediaPlayer: MediaPlayer) {
        mMediaState = if (mediaPlayer.isPlaying) {
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause)
            Const.MEDIA_PLAYING
        } else {
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
            Const.MEDIA_PAUSE
        }
        mediaPlayer.setOnCompletionListener {
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
            mMediaState = Const.MEDIA_STOP
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showDialogChooseTime() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_time)
        val window: Window? = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val btnClose: ImageButton = dialog.findViewById(R.id.btn_close)
        val btnTimeOff: Button = dialog.findViewById(R.id.btn_time_off)
        val btnTime5s: Button = dialog.findViewById(R.id.btn_time_5s)
        val btnTime10s: Button = dialog.findViewById(R.id.btn_time_10s)
        val btnTime15s: Button = dialog.findViewById(R.id.btn_time_15s)
        val btnTime30s: Button = dialog.findViewById(R.id.btn_time_30s)

        val bgChoose = resources.getDrawable(R.drawable.bg_button_intro)
        val bgNChoose = resources.getDrawable(R.drawable.bg_btn_time)

        viewModel.time.observe(this) {
            when (it) {
                getString(R.string.string_off) -> {
                    btnTimeOff.background = bgChoose
                    btnTime5s.background = bgNChoose
                    btnTime10s.background = bgNChoose
                    btnTime15s.background = bgNChoose
                    btnTime30s.background = bgNChoose
                    if (binding.layoutInformTime.visibility != View.GONE) {
                        binding.layoutInformTime.visibility = View.GONE
                    }
                }

                getString(R.string.string_5s) -> {
                    btnTimeOff.background = bgNChoose
                    btnTimeOff.setTextColor(Color.parseColor("#000000"))
                    btnTime5s.background = bgChoose
                    btnTime10s.background = bgNChoose
                    btnTime15s.background = bgNChoose
                    btnTime30s.background = bgNChoose
                }

                getString(R.string.string_10s) -> {
                    btnTimeOff.setTextColor(Color.parseColor("#000000"))
                    btnTimeOff.background = bgNChoose
                    btnTime5s.background = bgNChoose
                    btnTime10s.background = bgChoose
                    btnTime15s.background = bgNChoose
                    btnTime30s.background = bgNChoose
                }

                getString(R.string.string_15s) -> {
                    btnTimeOff.setTextColor(Color.parseColor("#000000"))
                    btnTimeOff.background = bgNChoose
                    btnTime5s.background = bgNChoose
                    btnTime10s.background = bgNChoose
                    btnTime15s.background = bgChoose
                    btnTime30s.background = bgNChoose
                }

                getString(R.string.string_30s) -> {
                    btnTimeOff.setTextColor(Color.parseColor("#000000"))
                    btnTimeOff.background = bgNChoose
                    btnTime5s.background = bgNChoose
                    btnTime10s.background = bgNChoose
                    btnTime15s.background = bgNChoose
                    btnTime30s.background = bgChoose
                }
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnTimeOff.setOnClickListener {
            viewModel.setValueTime(getString(R.string.string_off))
            dialog.dismiss()
        }

        btnTime5s.setOnClickListener {
            viewModel.setValueTime(getString(R.string.string_5s))
            dialog.dismiss()
        }

        btnTime10s.setOnClickListener {
            viewModel.setValueTime(getString(R.string.string_10s))
            dialog.dismiss()
        }

        btnTime15s.setOnClickListener {
            viewModel.setValueTime(getString(R.string.string_15s))
            dialog.dismiss()
        }

        btnTime30s.setOnClickListener {
            viewModel.setValueTime(getString(R.string.string_30s))
            dialog.dismiss()
        }
        dialog.show()
    }

}