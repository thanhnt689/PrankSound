package com.example.soundprank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    private val _loop = MutableLiveData<Boolean>()
    val loop: LiveData<Boolean>
        get() = _loop


    init {
        _time.postValue("Off")
        _loop.postValue(false)
    }

    fun setValueTime(time: String) {
        _time.postValue(time)
    }

    fun setValueLoop(loop: Boolean) {
        _loop.postValue(loop)
    }
}