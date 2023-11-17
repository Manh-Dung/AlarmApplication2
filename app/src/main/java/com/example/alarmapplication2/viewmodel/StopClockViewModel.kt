package com.example.alarmapplication2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.AlarmDAO
import com.example.alarmapplication2.data.AlarmDatabase
import com.example.alarmapplication2.data.StopClockDAO
import com.example.alarmapplication2.data.StopClockDatabase
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.domain.StopClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopClockViewModel(application: Application) : AndroidViewModel(application) {
    val getAllClocks: LiveData<List<StopClock>>
    private val stopClockDAO: StopClockDAO

    private val _lastTime = MutableLiveData<String>()
    val lastTime: LiveData<String> get() = _lastTime

    init {
        stopClockDAO = StopClockDatabase.getDatabase(application).stopClockDAO()
        getAllClocks = stopClockDAO.getAllClocks()
    }

    fun insertClock(stopClock: StopClock) {
        viewModelScope.launch(Dispatchers.IO) {
            stopClockDAO.insertClock(stopClock)
        }
    }

    fun deleteClock() {
        viewModelScope.launch(Dispatchers.IO) {
            stopClockDAO.deleteClocks()
        }
    }

    fun getLastTime(): String? {
        return _lastTime.value
    }

    fun setLastTime(time: String) {
        _lastTime.value = time
    }
}