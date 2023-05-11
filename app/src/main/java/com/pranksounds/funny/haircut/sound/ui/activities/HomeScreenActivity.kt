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
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admob
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.adapters.PrankSoundAdapter
import com.pranksounds.funny.haircut.sound.callback.OnClickItemSoundPrank
import com.pranksounds.funny.haircut.sound.databinding.ActivityHomeScreenBinding
import com.pranksounds.funny.haircut.sound.models.SoundPrank
import com.pranksounds.funny.haircut.sound.utils.Const
import com.pranksounds.funny.haircut.sound.utils.LocaleHelper
import com.pranksounds.funny.haircut.sound.viewmodel.MyViewModel
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.pranksounds.funny.haircut.sound.utils.AdsInter


class HomeScreenActivity : AppCompatActivity(), OnClickItemSoundPrank,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var adapter: PrankSoundAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editTor: SharedPreferences.Editor

    private lateinit var myViewModel: MyViewModel

    private val localeHelper = LocaleHelper()

    private var manager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        binding.btnMore.setOnClickListener {
            onClickButtonMore()
        }

        AdsInter.inter_home = null

        loadInter()

        Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

    }

    private fun init() {
        binding.navHome.setNavigationItemSelectedListener(this)

        localeHelper.setLanguage(this@HomeScreenActivity)

        sharedPreferences = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        editTor = sharedPreferences.edit()

        adapter = PrankSoundAdapter(getDataListPrankSound(), this)
        binding.rvListSoundPrank.adapter = adapter
        binding.rvListSoundPrank.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onStart() {
        super.onStart()
        Log.d("ntt", "onStart")

        localeHelper.setLanguage(this@HomeScreenActivity)

        init()

        binding.tvPrankSound.text = getString(R.string.app_name)

        val favouriteTitle = binding.navHome.menu.findItem(R.id.menu_favourite)
        favouriteTitle.title = getString(R.string.string_favourite)

        val languageTitle = binding.navHome.menu.findItem(R.id.menu_language)
        languageTitle.title = getString(R.string.string_language)

        val rateTitle = binding.navHome.menu.findItem(R.id.menu_rate)
        rateTitle.title = getString(R.string.string_rate)

        val aboutTitle = binding.navHome.menu.findItem(R.id.menu_about)
        aboutTitle.title = getString(R.string.string_about)

        val policyTitle = binding.navHome.menu.findItem(R.id.menu_policy)
        policyTitle.title = getString(R.string.string_policy)

    }

    override fun onResume() {
        super.onResume()
        Log.d("ntt", "onResume")
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
            SoundPrank(
                R.drawable.ic_fart,
                getString(R.string.string_fart),
                "fart",
                "#FF9832",
                "#FFD59E"
            ),
            SoundPrank(
                R.drawable.ic_hair_clipper,
                getString(R.string.string_hair_clipper),
                "hair clipper", "#915EFF", "#CBB4FD"
            ),
            SoundPrank(
                R.drawable.ic_air_horn,
                getString(R.string.string_air_horn),
                "air horn",
                "#77B6FF",
                "#BADEFF"
            ),
            SoundPrank(
                R.drawable.ic_scary,
                getString(R.string.string_scary),
                "scary",
                "#E26CFF",
                "#F4C9FF"
            ),
            SoundPrank(
                R.drawable.ic_animals,
                getString(R.string.string_animals),
                "animals",
                "#6B53FF",
                "#BEB4FF"
            ),
            SoundPrank(
                R.drawable.ic_alarm,
                getString(R.string.string_alarm),
                "alarm",
                "#FF7A7A",
                "#FFC9C9"
            ),
            SoundPrank(
                R.drawable.ic_cough,
                getString(R.string.string_cough),
                "cough",
                "#59D45E",
                "#CBFDB4"
            ),
            SoundPrank(
                R.drawable.ic_burp,
                getString(R.string.string_burp),
                "burp",
                "#FFE300",
                "#FFF5A3"
            ),
            SoundPrank(
                R.drawable.ic_breaking,
                getString(R.string.string_breaking),
                "breaking",
                "#2260FF",
                "#C1D3FF"
            ),
            SoundPrank(
                R.drawable.ic_meme_sound,
                getString(R.string.string_meme_sound),
                "meme sound", "#49BEFF", "#C1E7FD"
            ),
            SoundPrank(
                R.drawable.ic_toilet,
                getString(R.string.string_toilet),
                "toilet",
                "#FF7B52",
                "#FFC5B2"
            ),
            SoundPrank(
                R.drawable.ic_gun,
                getString(R.string.string_gun),
                "gun",
                "#FFCE22",
                "#FFECA8"
            ),
            SoundPrank(
                R.drawable.ic_bomb,
                getString(R.string.string_bomb),
                "bomb",
                "#EC3F37",
                "#FFBFBC"
            ),
            SoundPrank(
                R.drawable.ic_snoring,
                getString(R.string.string_snoring),
                "snoring",
                "#ABEC42",
                "#E3FFB5"
            ),
            SoundPrank(
                R.drawable.ic_crying,
                getString(R.string.string_crying),
                "crying",
                "#B455FF",
                "#E1BBFF"
            ),
            SoundPrank(
                R.drawable.ic_door_bell,
                getString(R.string.string_door_bell),
                "door bell",
                "#FF9832",
                "#FFD59E"
            ),
            SoundPrank(
                R.drawable.ic_cat,
                getString(R.string.string_cat),
                "cat",
                "#77B6FF",
                "#BADEFF"
            ),
            SoundPrank(
                R.drawable.ic_dog,
                getString(R.string.string_dog),
                "dog",
                "#E26CFF",
                "#F4C9FF"
            ),
            SoundPrank(
                R.drawable.ic_scissors,
                getString(R.string.string_scissors),
                "scissors",
                "#6B53FF",
                "#BEB4FF"
            ),
            SoundPrank(
                R.drawable.ic_sneezing,
                getString(R.string.string_sneezing),
                "sneezing",
                "#FF7A7A",
                "#FFC9C9"
            ),
            SoundPrank(
                R.drawable.ic_car_horn,
                getString(R.string.string_car_horn),
                "car horn",
                "#59D45E",
                "#CBFDB4"
            ),
        )
    }

    override fun onClickItemSoundPrank(soundPrank: SoundPrank, position: Int) {
        val num = sharedPreferences.getInt(Const.NUM_SHOW_INTER, 1)
        Log.d("ntt", "$num")
        if (num % 2 == 1) {
            Log.d("ntt", "$num show intro")
            try {
                Log.d("ntt", "Load inter ads true")
                Admob.getInstance()
                    .showInterAds(
                        this@HomeScreenActivity,
                        AdsInter.inter_home,
                        object : InterCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                val intent =
                                    Intent(this@HomeScreenActivity, SoundActivity::class.java)
                                val bundle = Bundle()
                                bundle.putSerializable("sound_prank", soundPrank)
                                intent.putExtras(bundle)
                                intent.putExtra("position", position)
                                startActivity(intent)
                                AdsInter.inter_home = null
                                loadInter()
                            }
                        })
            } catch (exception: Exception) {
                Log.d("ntt", "Load inter ads error: $exception")
            }
        } else {
            Log.d("ntt", "$num show activity")
            val intent =
                Intent(this@HomeScreenActivity, SoundActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("sound_prank", soundPrank)
            intent.putExtras(bundle)
            intent.putExtra("position", position)

            startActivity(intent)
            AdsInter.inter_home = null
            loadInter()
        }

        editTor.putInt(Const.NUM_SHOW_INTER, num + 1)

        editTor.apply()
    }

    override fun onBackPressed() {

        val numberShowRate = sharedPreferences.getInt(Const.NUM_SHOW_RATING_EXIT_APP, 0)

        val checkIsRating = sharedPreferences.getBoolean(Const.CHECK_IS_RATING, false)

        Log.d("ntt", numberShowRate.toString())

        if (!checkIsRating) {
            if (numberShowRate % 2 == 0) {
                openRatingDialog("BackPress")
            } else {
                finish()
            }

            editTor.putInt(Const.NUM_SHOW_RATING_EXIT_APP, numberShowRate + 1)

            editTor.apply()
        } else {
            finish()
        }

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
                val intent = Intent(this, LanguageSettingActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_rate -> {
                val checkIsRating = sharedPreferences.getBoolean(Const.CHECK_IS_RATING, false)

                if (!checkIsRating) {
                    openRatingDialog("NavigationView")
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.string_you_have_rated_the_app),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                true
            }

            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_policy -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.LINK_POLICY))
                startActivity(browserIntent)
                true
            }

            else -> throw IllegalArgumentException()
        }
    }


    @SuppressLint("IntentReset")
    private fun openRatingDialog(start: String) {
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

                        startActivity(Intent.createChooser(sendIntent, "Send Email"))

                        if (start == "BackPress") {
                            dialog.dismiss()
                            finish()
                        } else if (start == "NavigationView") {
                            dialog.dismiss()
                        }

                        editTor.putBoolean(Const.CHECK_IS_RATING, true)

                        editTor.apply()
                    }

                    "4.0", "5.0" -> {

                        manager = ReviewManagerFactory.create(this@HomeScreenActivity)
                        val request: Task<ReviewInfo> =
                            manager?.requestReviewFlow() as Task<ReviewInfo>
                        request.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                reviewInfo = task.result
                                Log.e("ReviewInfo", "" + reviewInfo.toString())
                                val flow: Task<Void> =
                                    manager?.launchReviewFlow(
                                        this@HomeScreenActivity,
                                        reviewInfo!!
                                    ) as Task<Void>
                                flow.addOnSuccessListener {
                                    if (start == "BackPress") {
                                        dialog.dismiss()
                                        finish()
                                    } else if (start == "NavigationView") {
                                        dialog.dismiss()
                                    }

                                    editTor.putBoolean(Const.CHECK_IS_RATING, true)

                                    editTor.apply()
                                    // finish()
                                }
                            } else {
                                if (start == "BackPress") {
                                    dialog.dismiss()
                                    finish()
                                } else if (start == "NavigationView") {
                                    dialog.dismiss()
                                }
                                //finish()
                            }
                        }
                    }
                }

            }
        }

        btnExit.setOnClickListener {
            if (start == "NavigationView") {
                dialog.dismiss()
            } else if (start == "BackPress") {
                dialog.dismiss()
                finish()
            }

            editTor.putBoolean(Const.CHECK_IS_RATING, false)

            editTor.apply()
        }

        dialog.show()
    }

    private fun loadInter() {
        if (AdsInter.inter_home == null) {
            Admob.getInstance()
                .loadInterAds(
                    this,
                    getString(R.string.id_ads_inter_home),
                    object : InterCallback() {
                        override fun onInterstitialLoad(interstitialAd2: InterstitialAd) {
                            super.onInterstitialLoad(interstitialAd2)
                            AdsInter.inter_home = interstitialAd2
                            // Show button
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            // Show button
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
                            super.onAdFailedToShow(adError)
                            // Show button
                        }
                    })
        }
    }

}