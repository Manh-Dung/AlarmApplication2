package com.example.alarmapplication2.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.alarmapplication2.R
import com.example.alarmapplication2.receiver.Constants
import com.example.alarmapplication2.receiver.DismissReceiver
import com.example.alarmapplication2.receiver.SnoozeReceiver

class AlarmService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val alarmId = intent.getIntExtra("alarm_id", -1)
        val dismissIntent = Intent(this, DismissReceiver::class.java)
        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            alarmId,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val snoozeIntent = Intent(this, SnoozeReceiver::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            alarmId,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_ic)
                .setContentTitle("Alarm Manager")
                .setContentText("Alarm!!!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.alarm_off, "Dismiss", dismissPendingIntent)
                .addAction(R.drawable.alarm_pause, "Snooze", snoozePendingIntent)
                .build()

        when (intent.action) {
            Constants.ACTION.START_FOREGROUND_ACTION -> {
                startForeground(123, notification)
            }

            Constants.ACTION.STOP_FOREGROUND_ACTION -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}