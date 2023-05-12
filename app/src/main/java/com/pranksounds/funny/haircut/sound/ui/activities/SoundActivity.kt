package com.pranksounds.funny.haircut.sound.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.recyclerview.widget.GridLayoutManager
import com.amazic.ads.util.Admob
import com.amazic.ads.util.AppOpenManager
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.adapters.SoundAdapter
import com.pranksounds.funny.haircut.sound.callback.OnClickItemSound
import com.pranksounds.funny.haircut.sound.databinding.ActivitySoundBinding
import com.pranksounds.funny.haircut.sound.models.Sound
import com.pranksounds.funny.haircut.sound.models.SoundPrank
import com.pranksounds.funny.haircut.sound.utils.Const
import com.pranksounds.funny.haircut.sound.utils.LocaleHelper
import com.pranksounds.funny.haircut.sound.viewmodel.MyViewModel
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModel
import com.pranksounds.funny.haircut.sound.viewmodel.SoundViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private var manager: ReviewManager? = null

    private var reviewInfo: ReviewInfo? = null

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundPrank = intent.getSerializableExtra("sound_prank") as SoundPrank

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))


        viewModel.sounds.observe(this) {
            Log.d("ntt", it.toString())
            val list = arrayListOf<Sound>()

            for (sound: Sound in it) {
                if (sound.folder == soundPrank.path) {
                    list.add(sound)
                }
            }
            binding.tvPrankSound.text = soundPrank.name
            adapter = SoundAdapter(this, list, this)
            binding.rvSoundPrank.adapter = adapter
            binding.rvSoundPrank.layoutManager = GridLayoutManager(this, 2)

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        editTor = sharedPreferences.edit()

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

    }

    override fun onStart() {
        super.onStart()
        localeHelper.setLanguage(this)
    }

    override fun onResume() {
        super.onResume()

        AppOpenManager.getInstance().enableAppResumeWithActivity(SoundActivity::class.java)

        if (DetailPrankSoundActivity.check) {

            DetailPrankSoundActivity.check = false

            val numShowRating = sharedPreferences.getInt(Const.NUM_SHOW_RATING, 1)

            val checkIsRating = sharedPreferences.getBoolean(Const.CHECK_IS_RATING, false)

            if (!checkIsRating) {
                if (numShowRating % 2 == 1) {
                    openRatingDialog()
                }
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

    @SuppressLint("IntentReset")
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
                    getString(R.string.string_please_feedback),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                when (ratingBar.rating.toString()) {
                    "1.0", "2.0", "3.0" -> {

                        val uriText =
                            "mailto:bingooteam@gmail.com?subject=Review for Sound Prank &body=----Mail content----\nSoundPrank\nRate: ${ratingBar.rating.toString()}\nContent: "

                        val uri = Uri.parse(uriText)

                        val sendIntent = Intent(Intent.ACTION_SENDTO)

                        sendIntent.type = "text/html"

                        sendIntent.putExtra(Intent.EXTRA_EMAIL, "mailto:bingooteam@gmail.com")

                        sendIntent.data = uri

                        AppOpenManager.getInstance()
                            .disableAppResumeWithActivity(SoundActivity::class.java)

                        startActivity(Intent.createChooser(sendIntent, "Send Email"))


                        dialog.dismiss()


                        editTor.putBoolean(Const.CHECK_IS_RATING, true)

                        editTor.apply()
                    }

                    "4.0", "5.0" -> {

                        manager = ReviewManagerFactory.create(this@SoundActivity)
                        val request: Task<ReviewInfo> =
                            manager?.requestReviewFlow() as Task<ReviewInfo>
                        request.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                reviewInfo = task.result
                                Log.e("ReviewInfo", "" + reviewInfo.toString())
                                val flow: Task<Void> =
                                    manager?.launchReviewFlow(
                                        this@SoundActivity,
                                        reviewInfo!!
                                    ) as Task<Void>
                                flow.addOnSuccessListener {

                                    dialog.dismiss()

                                    editTor.putBoolean(Const.CHECK_IS_RATING, true)

                                    editTor.apply()
                                }
                            } else {

                                dialog.dismiss()

                            }
                        }
                    }
                }
            }
        }

        btnExit.setOnClickListener {
            dialog.dismiss()
            editTor.putBoolean(Const.CHECK_IS_RATING, false)
            editTor.apply()
        }

        dialog.show()
    }

}