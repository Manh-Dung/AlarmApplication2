package com.example.alarmapplication2.repository

import androidx.lifecycle.LiveData
import com.example.alarmapplication2.data.AlarmDAO
import com.example.alarmapplication2.data.StopClockDAO
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.domain.StopClock

class StopClockRepository(private val stopClockDAO: StopClockDAO) {

    val getAllClocks: LiveData<List<StopClock>> = stopClockDAO.getAllClocks()

    suspend fun insertClock(stopClock: StopClock) {
        stopClockDAO.insertClock(stopClock)
    }

    suspend fun deleteClocks() {
        stopClockDAO.deleteClocks()
    }
}