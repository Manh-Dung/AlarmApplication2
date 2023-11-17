package com.example.alarmapplication2.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "StopClock")
data class StopClock (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var preTime: String,
    var time: String
) : Parcelable