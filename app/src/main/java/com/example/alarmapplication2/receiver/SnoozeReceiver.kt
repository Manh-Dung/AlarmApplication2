package com.example.alarmapplication2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.alarmapplication2.service.AlarmService
import java.util.Timer
import java.util.TimerTask

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
        context?.stopService(serviceIntent)

        val startRingtone = Timer()
        startRingtone.schedule(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                val startServiceIntent = Intent(context, AlarmService::class.java)
                startServiceIntent.action = Constants.ACTION.START_FOREGROUND_ACTION
                context?.startForegroundService(startServiceIntent)
            }
        }, 60000)

        Toast.makeText(context, "Alarm snoozed for 1 minute", Toast.LENGTH_SHORT).show()
    }
}