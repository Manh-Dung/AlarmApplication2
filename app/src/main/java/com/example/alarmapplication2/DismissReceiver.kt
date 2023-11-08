package com.example.alarmapplication2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.widget.Toast

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
        context?.stopService(serviceIntent)

        Toast.makeText(context, "Alarm dismissed", Toast.LENGTH_SHORT).show()
    }
}