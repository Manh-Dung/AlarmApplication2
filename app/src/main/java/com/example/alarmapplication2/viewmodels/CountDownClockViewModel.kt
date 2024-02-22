package com.example.alarmapplication2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.CountDownClockDatabase
import com.example.alarmapplication2.models.CountDownClock
import com.example.alarmapplication2.repositories.CountDownClockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownClockViewModel(application: Application) :
    AndroidViewModel(application) {
    val repository: CountDownClockRepository
    val getAllAlarms: LiveData<MutableList<CountDownClock>>

    init {
        val countDownClockDAO =
            CountDownClockDatabase.getDatabase(application).countDownClockDao()
        repository = CountDownClockRepository(countDownClockDAO)
        getAllAlarms = repository.getAllClocks
    }

    fun insertAlarm(clock: CountDownClock) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertClock(clock)
        }

    fun updateAlarm(clock: CountDownClock?) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateClock(clock)
        }

    fun deleteAlarm(clock: CountDownClock?) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteClock(clock)
        }
}