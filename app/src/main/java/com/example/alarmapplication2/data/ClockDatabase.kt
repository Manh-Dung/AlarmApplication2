package com.example.alarmapplication2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alarmapplication2.domain.Clock

@Database(entities = [Clock::class], version = 1, exportSchema = false)
abstract class ClockDatabase : RoomDatabase() {
    abstract fun clockDAO() : ClockDAO

    companion object {
        @Volatile
        var INSTANCE: ClockDatabase? = null

        fun getDatabase(context: Context): ClockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ClockDatabase::class.java,
                    "clock.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}