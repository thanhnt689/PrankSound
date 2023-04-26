package com.example.soundprank.utils

import com.google.android.gms.ads.interstitial.InterstitialAd


class AdsInter {
    companion object {
        var inter_splash: InterstitialAd? = null
        var inter_intro: InterstitialAd? = null
        var inter_voice_lock: InterstitialAd? = null
        var inter_theme: InterstitialAd? = null
    }
}