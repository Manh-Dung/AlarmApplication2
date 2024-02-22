package com.example.alarmapplication2.repositories

import androidx.lifecycle.LiveData
import com.example.alarmapplication2.data.AlarmDAO
import com.example.alarmapplication2.models.Alarm

class AlarmRepository(private val alarmDAO: AlarmDAO) {

    val getAllAlarms: LiveData<MutableList<Alarm>> = alarmDAO.getAllAlarms()
    val countCheckedAlarms: LiveData<Int> = alarmDAO.countCheckedAlarms()

    suspend fun insertAlarm(alarm: Alarm) {
        alarmDAO.insertAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm?) {
        alarmDAO.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(deleteCheck: Boolean) {
        alarmDAO.deleteCheckedAlarms(deleteCheck)
    }

    suspend fun setDeleteCheckAll(deleteCheck: Boolean) {
        alarmDAO.setDeleteCheckAll(deleteCheck)
    }
}