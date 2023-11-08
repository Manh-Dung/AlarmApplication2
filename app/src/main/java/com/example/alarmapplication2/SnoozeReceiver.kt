package com.example.alarmapplication2

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
        context?.stopService(serviceIntent)

        val startRingtone = Timer()
        startRingtone.schedule(object : TimerTask() {
            override fun run() {
                val serviceIntent = Intent(context, AlarmService::class.java)
                serviceIntent.action = Constants.ACTION.START_FOREGROUND_ACTION
                context?.startForegroundService(serviceIntent)
            }
        }, 60000)

        Toast.makeText(context, "Alarm snoozed for 1 minutes", Toast.LENGTH_SHORT).show()
    }
}