package com.example.soundprank.models

import java.io.Serializable

data class Sound(
    var name: String = "",
    var path: String = "",
    var image: Int,
    var favourite: Boolean,
) : Serializable