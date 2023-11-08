package com.example.alarmapplication2

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.Timer
import java.util.TimerTask

class AlarmService : Service() {
    private val NOTIFICATION_ID = 1

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val dismissIntent = Intent(this, DismissReceiver::class.java)
        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val snoozeIntent = Intent(this, SnoozeReceiver::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm Manager")
                .setContentText("Alarm!!!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "Dismiss", dismissPendingIntent)
                .addAction(0, "Snooze", snoozePendingIntent)
                .build()

        when (intent.action) {
            Constants.ACTION.START_FOREGROUND_ACTION -> {
                startForeground(NOTIFICATION_ID, notification)
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