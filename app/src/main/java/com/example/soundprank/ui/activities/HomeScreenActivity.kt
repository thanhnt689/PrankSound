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
import android.view.MenuItem
import android.view.View
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admob
import com.example.soundprank.R
import com.example.soundprank.adapters.PrankSoundAdapter
import com.example.soundprank.callback.OnClickItemSoundPrank
import com.example.soundprank.databinding.ActivityHomeScreenBinding
import com.example.soundprank.models.SoundPrank
import com.example.soundprank.utils.AdsInter
import com.example.soundprank.utils.Const
import com.example.soundprank.utils.LocaleHelper
import com.example.soundprank.viewmodel.MyViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.navigation.NavigationView

class HomeScreenActivity : AppCompatActivity(), OnClickItemSoundPrank,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var adapter: PrankSoundAdapter

    private lateinit var mRefreshReceiver: BroadcastReceiver

    private var mInterstitialAd: InterstitialAd? = null

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editTor: SharedPreferences.Editor

    private lateinit var myViewModel: MyViewModel

    private val localeHelper = LocaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        setReloadDataChangeLanguage()

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        binding.btnMore.setOnClickListener {
            onClickButtonMore()
        }

    }

    private fun init() {
        binding.navHome.setNavigationItemSelectedListener(this)

        localeHelper.setLanguage(this@HomeScreenActivity)

        sharedPreferences = this.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);

        editTor = sharedPreferences.edit()

        adapter = PrankSoundAdapter(getDataListPrankSound(), this)
        binding.rvListSoundPrank.adapter = adapter
        binding.rvListSoundPrank.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Admob.getInstance().loadBanner(this, getString(R.string.id_ads_banner))

        // loadInter()

    }

    private fun setReloadDataChangeLanguage() {
        val filter = IntentFilter()
        filter.addAction("My Broadcast")
        mRefreshReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "My Broadcast") {
                    Log.d("ntt", "broadcast")

                    localeHelper.setLanguage(this@HomeScreenActivity)

                    binding.tvPrankSound.text = getString(R.string.app_name)

                    Log.d("ntt", getString(R.string.app_name))

                    val favouriteTitle = binding.navHome.menu.findItem(R.id.menu_favourite)
                    favouriteTitle.title = getString(R.string.string_favourite)

                    Log.d("ntt", getString(R.string.string_favourite))

                    val languageTitle = binding.navHome.menu.findItem(R.id.menu_language)
                    languageTitle.title = getString(R.string.string_language)

                    Log.d("ntt", getString(R.string.string_language))

                    val rateTitle = binding.navHome.menu.findItem(R.id.menu_rate)
                    rateTitle.title = getString(R.string.string_rate)

                    Log.d("ntt", getString(R.string.string_rate))

                    val aboutTitle = binding.navHome.menu.findItem(R.id.menu_about)
                    aboutTitle.title = getString(R.string.string_about)

                    Log.d("ntt", getString(R.string.string_about))

                    val policyTitle = binding.navHome.menu.findItem(R.id.menu_policy)
                    policyTitle.title = getString(R.string.string_policy)

                    Log.d("ntt", getString(R.string.string_policy))

                    init()
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshReceiver, filter);
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
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

//        if (num % 2 == 1) {
//            try {
//                Log.d("ntt", "Load inter ads true")
//                Admob.getInstance()
//                    .showInterAds(
//                        this@HomeScreenActivity,
//                        AdsInter.inter_intro,
//                        object : InterCallback() {
//                            override fun onNextAction() {
//                                super.onNextAction()
//                                val intent =
//                                    Intent(this@HomeScreenActivity, SoundActivity::class.java)
//                                val bundle = Bundle()
//                                bundle.putSerializable("sound_prank", soundPrank)
//                                intent.putExtras(bundle)
//                                intent.putExtra("position", position)
//                                startActivity(intent)
//                            }
//                        })
//            } catch (exception: Exception) {
//                Log.d("ntt", "Load inter ads error: $exception")
//            }
//        } else {
            val intent =
                Intent(this@HomeScreenActivity, SoundActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("sound_prank", soundPrank)
            intent.putExtras(bundle)
            intent.putExtra("position", position)
            startActivity(intent)
//        }

        editTor.putInt(Const.NUM_SHOW_INTER, num + 1)

        editTor.apply()
    }

    override fun onStart() {
        super.onStart()
        Log.d("ntt", "onStart")
//        loadInter()
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
                    Toast.makeText(this, "You have rated the app!", Toast.LENGTH_SHORT).show()
                }

                true
            }

            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_policy -> {
                true
            }

            else -> throw IllegalArgumentException()
        }
    }

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
                    "Please feedback",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Thank for the rate: ${ratingBar.rating}",
                    Toast.LENGTH_SHORT
                ).show()

                if (start == "BackPress") {
                    dialog.dismiss()
                    finish()
                } else if (start == "NavigationView") {
                    dialog.dismiss()
                }

                editTor.putBoolean(Const.CHECK_IS_RATING, true)

                editTor.apply()
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

//    private fun loadInter() {
//        if (AdsInter.inter_intro == null) {
//            Admob.getInstance()
//                .loadInterAds(this, getString(R.string.id_ads_inter), object : InterCallback() {
//                    override fun onInterstitialLoad(interstitialAd2: InterstitialAd) {
//                        super.onInterstitialLoad(interstitialAd2)
//                        AdsInter.inter_intro = interstitialAd2
//                        Log.d("ntt", "Load true")
//                        // Show button
//                    }
//
//                    override fun onAdFailedToLoad(i: LoadAdError?) {
//                        super.onAdFailedToLoad(i)
//                        Log.d("ntt", "Load false to load")
//                        // Show button
//                    }
//
//                    override fun onAdFailedToShow(adError: AdError?) {
//                        super.onAdFailedToShow(adError)
//                        Log.d("ntt", "Load false to show")
//                        // Show button
//                    }
//                })
//        }
//    }

}