package com.example.soundprank.callback

import com.example.soundprank.models.SoundPrank

interface OnClickItemSoundPrank {
    fun onClickItemSoundPrank(soundPrank: SoundPrank, position: Int)
}