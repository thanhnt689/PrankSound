package com.example.soundprank.ui.activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundprank.R
import com.example.soundprank.adapters.PrankSoundAdapter
import com.example.soundprank.callback.OnClickItemSoundPrank
import com.example.soundprank.databinding.ActivityHomeScreenBinding
import com.example.soundprank.models.SoundPrank
import com.example.soundprank.utils.LocaleHelper
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager

class HomeScreenActivity : AppCompatActivity(), OnClickItemSoundPrank,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var adapter: PrankSoundAdapter

    private var manager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMore.setOnClickListener {
            onClickButtonMore()
        }

        init()
    }

    private fun init() {
        binding.navHome.setNavigationItemSelectedListener(this)

        adapter = PrankSoundAdapter(getDataListPrankSound(), this)
        binding.rvListSoundPrank.adapter = adapter
        binding.rvListSoundPrank.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun onClickButtonMore() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            binding.drawer.openDrawer(GravityCompat.START)
        }
    }

    private fun getDataListPrankSound(): List<SoundPrank> {
        return listOf(
            SoundPrank(R.drawable.ic_fart, getString(R.string.string_fart), "fart"),
            SoundPrank(
                R.drawable.ic_hair_clipper,
                getString(R.string.string_hair_clipper),
                "hair clipper"
            ),
            SoundPrank(R.drawable.ic_air_horn, getString(R.string.string_air_horn), "air horn"),
            SoundPrank(R.drawable.ic_scary, getString(R.string.string_scary), "scary"),
            SoundPrank(R.drawable.ic_animals, getString(R.string.string_animals), "animals"),
            SoundPrank(R.drawable.ic_alarm, getString(R.string.string_alarm), "alarm"),
            SoundPrank(R.drawable.ic_cough, getString(R.string.string_cough), "cough"),
            SoundPrank(R.drawable.ic_burp, getString(R.string.string_burp), "burp"),
            SoundPrank(R.drawable.ic_breaking, getString(R.string.string_breaking), "breaking"),
            SoundPrank(
                R.drawable.ic_meme_sound,
                getString(R.string.string_meme_sound),
                "meme sound"
            ),
            SoundPrank(R.drawable.ic_toilet, getString(R.string.string_toilet), "toilet"),
            SoundPrank(R.drawable.ic_gun, getString(R.string.string_gun), "gun"),
            SoundPrank(R.drawable.ic_bomb, getString(R.string.string_bomb), "bomb"),
            SoundPrank(R.drawable.ic_snoring, getString(R.string.string_snoring), "snoring"),
            SoundPrank(R.drawable.ic_crying, getString(R.string.string_crying), "crying"),
            SoundPrank(R.drawable.ic_door_bell, getString(R.string.string_door_bell), "boor bell"),
            SoundPrank(R.drawable.ic_cat, getString(R.string.string_cat), "cat"),
            SoundPrank(R.drawable.ic_dog, getString(R.string.string_dog), "dog"),
            SoundPrank(R.drawable.ic_scissors, getString(R.string.string_scissors), "scissors"),
            SoundPrank(R.drawable.ic_sneezing, getString(R.string.string_sneezing), "sneezing"),
            SoundPrank(R.drawable.ic_car_horn, getString(R.string.string_car_horn), "car horn"),
        )
    }

    override fun onClickItemSoundPrank(soundPrank: SoundPrank, position: Int) {
        val intent = Intent(this, SoundActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("sound_prank", soundPrank)
        intent.putExtras(bundle)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawer.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.menu_favourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_language -> {
                val intent = Intent(this, LanguageActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_rate -> {
                openRatingDialog()
                true
            }
            R.id.menu_about -> {
//                val intent = Intent(this, MiniGameActivity::class.java)
//                startActivity(intent)
                true
            }
            R.id.menu_policy -> {
//                val intent = Intent(this, MiniGameActivity::class.java)
//                startActivity(intent)
                true
            }
            else -> throw IllegalArgumentException()
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
            if (ratingBar.rating.toString() == "0") {
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