package com.example.alarmapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActFragViewModel : ViewModel() {
    private var _deleteLayoutOn = MutableLiveData<Boolean>()
    val deleteLayoutOn: LiveData<Boolean> get() = _deleteLayoutOn

    fun setDeleteLayoutOn(data: Boolean) {
        _deleteLayoutOn.value = data
    }

    private var _checkAll = MutableLiveData<Int>()
    val checkAll: LiveData<Int> get() = _checkAll

    fun setCheckAll(data: Int) {
        _checkAll.value = data
    }
}