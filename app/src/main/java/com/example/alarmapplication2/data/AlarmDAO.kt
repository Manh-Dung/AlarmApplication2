package com.example.alarmapplication2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmapplication2.domain.Alarm
@Dao
interface AlarmDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm?)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm?)

    @Query("DELETE FROM Alarm WHERE deleteCheck = true")
    suspend fun deleteCheckedAlarms()

    @Query("SELECT * FROM Alarm ORDER BY id DESC")
    fun getAllAlarms(): LiveData<List<Alarm>>
}