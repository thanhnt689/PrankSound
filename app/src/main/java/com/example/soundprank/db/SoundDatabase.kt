package com.example.soundprank.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soundprank.models.Sound

@Database(entities = [Sound::class], version = 1, exportSchema = false)
abstract class SoundDatabase : RoomDatabase() {
    abstract fun getSoundDao(): SoundDAO

    companion object {
        private var INSTANCE: SoundDatabase? = null

        fun getInstance(ctx: Context): SoundDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    ctx,
                    SoundDatabase::class.java,
                    "sound.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}