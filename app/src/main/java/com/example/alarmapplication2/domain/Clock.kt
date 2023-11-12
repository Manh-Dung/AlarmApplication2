package com.example.alarmapplication2.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@Entity(tableName = "Clock")
data class Clock (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val label: String,
    val time: String,
    val isRepeat: Boolean,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday : Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean,
    val isEnable: Boolean
) : Parcelable