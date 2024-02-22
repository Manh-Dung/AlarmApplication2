package com.example.alarmapplication2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.AlarmDatabase
import com.example.alarmapplication2.models.Alarm
import com.example.alarmapplication2.repositories.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlarmRepository
    val getAllAlarms: LiveData<MutableList<Alarm>>
    val countCheckedAlarms: LiveData<Int>

    init {
        val alarmDAO = AlarmDatabase.getDatabase(application).alarmDAO()
        repository = AlarmRepository(alarmDAO)
        getAllAlarms = repository.getAllAlarms
        countCheckedAlarms = repository.countCheckedAlarms
    }

    fun insertAlarm(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm?) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAlarm(alarm)
    }

    fun deleteAlarm(deleteCheck: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(deleteCheck)
        }

    fun setDeleteCheckAll(deleteCheck: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.setDeleteCheckAll(deleteCheck)
        }
}