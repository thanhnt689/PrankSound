package com.pranksounds.funny.haircut.sound.repository

import androidx.lifecycle.LiveData
import com.pranksounds.funny.haircut.sound.db.SoundDAO
import com.pranksounds.funny.haircut.sound.models.Sound

class SoundRepository(private val soundDAO: SoundDAO) {
    suspend fun insertSound(sound: Sound) {
        soundDAO.insertSound(sound)
    }

    suspend fun updateSound(sound: Sound) {
        soundDAO.updateSound(sound)
    }

    suspend fun deleteSoundByPath(path: String) {
        soundDAO.deleteSoundByPath(path)
    }

    fun getAllSound(): LiveData<List<Sound>> {
        return soundDAO.getAllSound()
    }

    fun getListSoundFavourite(): LiveData<List<Sound>> {
        return soundDAO.getListSoundFavourite()
    }

    suspend fun checkExist(path: String): Boolean {
        return soundDAO.exists(path)
    }

    suspend fun getSoundByPath(path: String): Sound {
        return soundDAO.getSoundByPath(path)
    }
}