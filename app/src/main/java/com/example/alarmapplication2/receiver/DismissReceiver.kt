package com.example.alarmapplication2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alarmapplication2.service.AlarmService

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getIntExtra("alarm_id", -1)
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("alarm_id", alarmId)
        serviceIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
        context?.stopService(serviceIntent)

        Toast.makeText(context, "Alarm dismissed", Toast.LENGTH_SHORT).show()
    }
}