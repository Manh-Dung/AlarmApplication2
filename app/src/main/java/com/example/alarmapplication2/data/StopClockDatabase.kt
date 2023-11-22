package com.example.alarmapplication2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alarmapplication2.domain.StopClock

@Database(entities = [StopClock::class], version = 3, exportSchema = false)
abstract class StopClockDatabase : RoomDatabase() {
    abstract fun stopClockDAO(): StopClockDAO

    companion object {
        @Volatile
        var INSTANCE: StopClockDatabase? = null

        fun getDatabase(context: Context): StopClockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    StopClockDatabase::class.java,
                    "stopClock.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}