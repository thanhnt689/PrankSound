package com.example.soundprank.callback

import android.view.View
import com.example.soundprank.models.Sound

interface OnClickCbSound {
    fun onClickCbSound(check:Boolean, sound: Sound)
}