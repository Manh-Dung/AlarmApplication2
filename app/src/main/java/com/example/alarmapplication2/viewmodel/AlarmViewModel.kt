package com.example.alarmapplication2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.AlarmDAO
import com.example.alarmapplication2.data.AlarmDatabase
import com.example.alarmapplication2.domain.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    val getAllAlarms: LiveData<List<Alarm>>
    private val alarmDAO: AlarmDAO

    init {
        alarmDAO = AlarmDatabase.getDatabase(application).clockDAO()
        getAllAlarms = alarmDAO.getAllAlarms()
    }

    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDAO.insertAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm?) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDAO.updateAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm?) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDAO.deleteAlarm(alarm)
        }
    }

    fun deleteAlarm(deleteCheck: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDAO.deleteCheckedAlarms(deleteCheck)
        }
    }

    fun setDeleteCheckAll(deleteCheck: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDAO.setDeleteCheckAll(deleteCheck)
        }
    }
}