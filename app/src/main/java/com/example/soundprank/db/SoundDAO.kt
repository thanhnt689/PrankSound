package com.example.soundprank.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.soundprank.models.Sound

@Dao
interface SoundDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSound(sound: Sound)

    @Query("DELETE FROM sound WHERE _path=:path")
    suspend fun deleteSoundByPath(path: String)

    @Update
    suspend fun updateSound(sound: Sound)

    @Query("SELECT * FROM sound")
    fun getAllSound(): LiveData<List<Sound>>

    @Query("SELECT EXISTS (SELECT 1 FROM sound WHERE _path=:path)")
    suspend fun exists(path: String): Boolean

    @Query("SELECT * FROM sound WHERE _path=:path")
    suspend fun getSoundByPath(path: String): Sound

    @Query("SELECT * FROM sound WHERE _favourite = '1'")
    fun getListSoundFavourite(): LiveData<List<Sound>>

}