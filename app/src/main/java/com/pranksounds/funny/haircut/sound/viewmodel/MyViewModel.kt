package com.pranksounds.funny.haircut.sound.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pranksounds.funny.haircut.sound.models.Sound

class MyViewModel : ViewModel() {
    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    private val _num = MutableLiveData<Int>()
    val num: LiveData<Int>
        get() = _num

    init {
        _time.postValue("Off")
        _num.postValue(0)
    }

    fun setValueTime(time: String) {
        _time.postValue(time)
    }

    fun setValueNum(num: Int) {
        _num.postValue(num)
    }


}