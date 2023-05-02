package com.example.soundprank.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundprank.db.SoundDatabase
import com.example.soundprank.models.Sound
import com.example.soundprank.repository.SoundRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SoundViewModel(private val app: Application) : ViewModel() {
    private val soundRepository: SoundRepository = SoundRepository(
        SoundDatabase.getInstance(app.applicationContext).getSoundDao()
    )

    val sounds = soundRepository.getAllSound()

    val soundFavourite = soundRepository.getListSoundFavourite()

    fun insertSound(sound: Sound) {
        viewModelScope.launch(Dispatchers.IO) {
            soundRepository.insertSound(sound)
        }
    }

    fun updateSound(sound: Sound) {
        viewModelScope.launch(Dispatchers.IO) {
            soundRepository.updateSound(sound)
        }
    }

    fun deleteSoundByPath(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            soundRepository.deleteSoundByPath(path)
        }
    }

    suspend fun checkExist(path: String): Boolean {
        val exist = viewModelScope.async(Dispatchers.IO) {
            soundRepository.checkExist(path)
        }
        return exist.await()
    }

    suspend fun getSoundByPath(path: String): Sound {
        val sound = viewModelScope.async(Dispatchers.IO) {
            soundRepository.getSoundByPath(path)
        }

        return sound.await()
    }
}