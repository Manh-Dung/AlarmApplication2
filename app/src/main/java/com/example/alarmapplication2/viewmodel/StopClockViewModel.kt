package com.example.alarmapplication2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapplication2.data.StopClockDAO
import com.example.alarmapplication2.data.StopClockDatabase
import com.example.alarmapplication2.domain.StopClock
import com.example.alarmapplication2.repository.AlarmRepository
import com.example.alarmapplication2.repository.StopClockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopClockViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StopClockRepository
    val getAllClocks: LiveData<List<StopClock>>

    private val _lastTime = MutableLiveData<String>()
    val lastTime: LiveData<String> get() = _lastTime

    init {
        val stopClockDAO = StopClockDatabase.getDatabase(application).stopClockDAO()
        repository = StopClockRepository(stopClockDAO)
        getAllClocks = repository.getAllClocks
    }

    fun insertClock(stopClock: StopClock) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertClock(stopClock)
        }
    }

    fun deleteClock() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteClocks()
        }
    }

    fun getLastTime(): String? {
        return _lastTime.value
    }

    fun setLastTime(time: String) {
        _lastTime.value = time
    }
}