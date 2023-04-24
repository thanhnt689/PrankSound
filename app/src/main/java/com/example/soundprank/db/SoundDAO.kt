package com.example.soundprank.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.soundprank.models.Sound

@Dao
interface SoundDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSound(sound: Sound)

    @Query("DELETE FROM sound WHERE path = :path")
    suspend fun deleteSoundByPath(path: String)

    @Update
    suspend fun updateSound(sound: Sound)

    @Query("SELECT * FROM sound")
    fun getAllSound(): LiveData<List<Sound>>

    @Query("SELECT EXISTS (SELECT 1 FROM sound WHERE path = :path)")
    fun exists(path: String): Boolean
}