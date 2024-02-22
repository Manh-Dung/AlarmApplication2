package com.example.alarmapplication2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alarmapplication2.models.CountDownClock

@Database(entities = [CountDownClock::class], version = 1, exportSchema = false)
abstract class CountDownClockDatabase : RoomDatabase() {
    abstract fun countDownClockDao(): CountDownClockDAO

    companion object {
        @Volatile
        var INSTANCE: CountDownClockDatabase? = null

        fun getDatabase(context: Context): CountDownClockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CountDownClockDatabase::class.java,
                    "countDownClock.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}