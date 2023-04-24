package com.example.soundprank.repository

import androidx.lifecycle.LiveData
import com.example.soundprank.db.SoundDAO
import com.example.soundprank.models.Sound

class SoundRepository(private val soundDAO: SoundDAO) {
    suspend fun insertSound(sound: Sound) {
        soundDAO.insertSound(sound)
    }

    suspend fun updateSound(sound: Sound) {
        soundDAO.updateSound(sound)
    }

    suspend fun deleteSoundByPath(path:String) {
        soundDAO.deleteSoundByPath(path)
    }

    fun getAllSound(): LiveData<List<Sound>> {
        return soundDAO.getAllSound()
    }

    fun checkExist(path: String): Boolean {
        return soundDAO.exists(path)
    }
}