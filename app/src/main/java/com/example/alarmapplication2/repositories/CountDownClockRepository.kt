package com.example.alarmapplication2.repositories

import androidx.lifecycle.LiveData
import com.example.alarmapplication2.data.CountDownClockDAO
import com.example.alarmapplication2.models.CountDownClock

class CountDownClockRepository(private val countDownClockDAO: CountDownClockDAO) {
    val getAllClocks: LiveData<MutableList<CountDownClock>> =
        countDownClockDAO.getAllClocks()

    suspend fun insertClock(clock: CountDownClock) {
        countDownClockDAO.insertClock(clock)
    }

    suspend fun updateClock(clock: CountDownClock?) {
        countDownClockDAO.updateClock(clock)
    }

    suspend fun deleteClock(clock: CountDownClock?) {
        countDownClockDAO.deleteClock(clock)
    }
}