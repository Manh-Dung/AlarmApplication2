package com.example.alarmapplication2.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "CountDownClock")
data class CountDownClock (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var time: String,
    var tag: String
) : Parcelable