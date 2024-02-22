package com.example.alarmapplication2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    private var _deleteLayoutOn = MutableLiveData<Boolean>(false)
    val deleteLayoutOn: LiveData<Boolean> get() = _deleteLayoutOn

    fun setDeleteLayoutOn(data: Boolean) {
        _deleteLayoutOn.value = data
    }

    private var _checkAll = MutableLiveData<Boolean>()
    val checkAll: LiveData<Boolean> get() = _checkAll

    fun setCheckAll(data: Boolean) {
        _checkAll.value = data
    }

    private var _countCheckedAlarms = MutableLiveData<Int>()
    val countCheckedAlarms: LiveData<Int> get() = _countCheckedAlarms

    fun setCountCheckedAlarms(data: Int) {
        _countCheckedAlarms.value = data
    }
}