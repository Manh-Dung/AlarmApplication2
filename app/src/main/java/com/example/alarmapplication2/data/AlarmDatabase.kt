package com.example.alarmapplication2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alarmapplication2.models.Alarm

@Database(entities = [Alarm::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDAO() : AlarmDAO

    companion object {
        @Volatile
        var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AlarmDatabase::class.java,
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