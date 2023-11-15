package com.example.alarmapplication2.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@Entity(tableName = "Alarm")
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var time: String,
    var isEnable: Boolean,
    var deleteCheck: Boolean
) : Parcelable