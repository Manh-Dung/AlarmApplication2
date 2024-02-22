package com.example.alarmapplication2.repositories

import androidx.lifecycle.LiveData
import com.example.alarmapplication2.data.StopClockDAO
import com.example.alarmapplication2.models.StopClock

class StopClockRepository(private val stopClockDAO: StopClockDAO) {

    val getAllClocks: LiveData<List<StopClock>> = stopClockDAO.getAllClocks()

    suspend fun insertClock(stopClock: StopClock) {
        stopClockDAO.insertClock(stopClock)
    }

    suspend fun deleteClocks() {
        stopClockDAO.deleteClocks()
    }

    suspend fun deleteAll() {
        stopClockDAO.deleteAll()
    }
}