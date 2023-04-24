package com.example.soundprank.db

import androidx.room.Dao

@Dao
interface SoundDAO {

    suspend fun insertSound()
}