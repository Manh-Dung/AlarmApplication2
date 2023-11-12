package com.example.alarmapplication2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.ClockDAO
import com.example.alarmapplication2.data.ClockDatabase
import com.example.alarmapplication2.domain.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClockViewModel(application: Application) : AndroidViewModel(application) {
    val getAllClocks: LiveData<List<Clock>>
    private val clockDAO: ClockDAO

    init {
        clockDAO = ClockDatabase.getDatabase(application).clockDAO()
        getAllClocks = clockDAO.getAllClocks()
    }

    fun insertClock(clock: Clock) {
        viewModelScope.launch(Dispatchers.IO) {
            clockDAO.insertClock(clock)
        }
    }

    fun updateClock(clock: Clock?) {
        viewModelScope.launch(Dispatchers.IO) {
            clockDAO.updateClock(clock)
        }
    }

    fun deleteClock(clock: Clock?) {
        viewModelScope.launch(Dispatchers.IO) {
            clockDAO.deleteClock(clock)
        }
    }
}