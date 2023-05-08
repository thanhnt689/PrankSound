package com.pranksounds.funny.haircut.sound.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    init {
        _time.postValue("Off")
    }

    fun setValueTime(time: String) {
        _time.postValue(time)
    }


}