package com.example.alarmapplication2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmapplication2.models.CountDownClock

@Dao
interface CountDownClockDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClock(countDownClock: CountDownClock)

    @Update
    suspend fun updateClock(countDownClock: CountDownClock?)

    @Delete
    suspend fun deleteClock(countDownClock: CountDownClock?)

    @Query("SELECT * FROM CountDownClock ORDER BY id DESC")
    fun getAllClocks(): LiveData<MutableList<CountDownClock>>
}