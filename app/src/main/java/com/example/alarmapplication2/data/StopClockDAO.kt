package com.example.alarmapplication2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.domain.StopClock

@Dao
interface StopClockDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClock(stopClock: StopClock)

    @Query("DELETE FROM StopClock")
    suspend fun deleteClocks()

    @Query("SELECT * FROM StopClock ORDER BY id DESC")
    fun getAllClocks(): LiveData<List<StopClock>>
}