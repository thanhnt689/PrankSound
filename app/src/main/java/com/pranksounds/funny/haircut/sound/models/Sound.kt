package com.pranksounds.funny.haircut.sound.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sound")
data class Sound(
    @ColumnInfo(name = "_num")
    var num: Int = 0,
    @PrimaryKey
    @ColumnInfo(name = "_path")
    var path: String = "",
    @ColumnInfo(name = "_folder")
    var folder: String = "",
    @ColumnInfo(name = "_image")
    var image: Int,
    @ColumnInfo(name = "_favourite")
    var favourite: Boolean,
    @ColumnInfo(name = "_idString")
    var idString: Int,
    @Ignore
    var isSelected: Boolean = false
) : Serializable {
    constructor() : this(0, "", "", 0, false, 0, false)
}