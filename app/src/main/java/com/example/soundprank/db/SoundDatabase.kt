package com.example.soundprank.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.soundprank.R
import com.example.soundprank.models.Sound
import com.example.soundprank.models.SoundPrank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Sound::class], version = 1, exportSchema = false)
abstract class SoundDatabase : RoomDatabase() {
    abstract fun getSoundDao(): SoundDAO

    companion object {
        private var INSTANCE: SoundDatabase? = null

        fun getInstance(ctx: Context, scope: CoroutineScope): SoundDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    ctx,
                    SoundDatabase::class.java,
                    "sound.db"
                ).fallbackToDestructiveMigration().addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                    }


                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                    }
                })

                    .build()
            }
            return INSTANCE!!
        }
    }
}