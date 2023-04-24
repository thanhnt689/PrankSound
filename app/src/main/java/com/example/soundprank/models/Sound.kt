package com.example.soundprank.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sound")
data class Sound(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var path: String = "",
    var image: Int,
    var favourite: Boolean,
) : Serializable