package com.pranksounds.funny.haircut.sound.application

import android.app.Application
import android.content.Context
import com.amazic.ads.util.AdsApplication
import com.amazic.ads.util.AppOpenManager
import com.pranksounds.funny.haircut.sound.R
import com.pranksounds.funny.haircut.sound.ui.activities.SplashScreenActivity

class App : AdsApplication() {

    override fun onCreate() {
        super.onCreate()

        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashScreenActivity::class.java)
    }

    override fun enableAdsResume(): Boolean {
        return true
    }

    override fun getListTestDeviceId(): MutableList<String>? {
        return null
    }

    override fun getResumeAdId(): String {
        return getString(R.string.ads_resume)
    }

    override fun buildDebug(): Boolean? {
        return null
    }


}