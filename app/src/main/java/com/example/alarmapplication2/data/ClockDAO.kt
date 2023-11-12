package com.example.alarmapplication2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmapplication2.domain.Clock
@Dao
interface ClockDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClock(clock: Clock)

    @Update
    suspend fun updateClock(clock: Clock?)

    @Delete
    suspend fun deleteClock(clock: Clock?)

    @Query("SELECT * FROM Clock ORDER BY id DESC")
    fun getAllClocks(): LiveData<List<Clock>>
}