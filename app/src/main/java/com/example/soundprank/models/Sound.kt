package com.example.soundprank.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sound")
data class Sound(
    @ColumnInfo(name = "_name")
    var name: String = "",
    @PrimaryKey
    @ColumnInfo(name = "_path")
    var path: String = "",
    @ColumnInfo(name = "_folder")
    var folder: String = "",
    @ColumnInfo(name = "_image")
    var image: Int,
    @ColumnInfo(name = "_favourite")
    var favourite: Boolean,
    @Ignore
    var isSelected: Boolean = false
) : Serializable {
    constructor() : this("", "", "", 0, false, false)
}