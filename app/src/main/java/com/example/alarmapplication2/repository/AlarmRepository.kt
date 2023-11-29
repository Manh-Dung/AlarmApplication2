package com.example.alarmapplication2.repository

import androidx.lifecycle.LiveData
import com.example.alarmapplication2.data.AlarmDAO
import com.example.alarmapplication2.domain.Alarm

class AlarmRepository(private val alarmDAO: AlarmDAO) {

    val getAllAlarms: LiveData<List<Alarm>> = alarmDAO.getAllAlarms()

    suspend fun insertAlarm(alarm: Alarm) {
        alarmDAO.insertAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm?) {
        alarmDAO.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm?) {
        alarmDAO.deleteAlarm(alarm)
    }

    suspend fun deleteAlarm(deleteCheck: Boolean) {
        alarmDAO.deleteCheckedAlarms(deleteCheck)
    }

    suspend fun setDeleteCheckAll(deleteCheck: Boolean) {
        alarmDAO.setDeleteCheckAll(deleteCheck)
    }
}