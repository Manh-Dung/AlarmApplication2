package com.example.alarmapplication2

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Timer
import java.util.TimerTask

class AlarmService : Service() {
    private val NOTIFICATION_ID = 1

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.victory)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val dismissIntent = Intent(this, DismissReceiver::class.java)
        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm Manager")
                .setContentText("Alarm!!!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "Dismiss", dismissPendingIntent)
                .build()

        if (intent.action == Constants.ACTION.START_FOREGROUND_ACTION) {
            startForeground(NOTIFICATION_ID, notification)
            playMusic()
            isPlaying = true
        } else if (intent.action == Constants.ACTION.STOP_FOREGROUND_ACTION) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopMusic()
            isPlaying = false
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlaying) {
            stopMusic()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun playMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()

//            Thực hiện công việc trên luồng nền, giúp làm việc nặng
            val startRingtone = Timer()

            startRingtone.schedule(object : TimerTask() {
                override fun run() {
                    mediaPlayer.stop()
                    stopSelf()
                }
            }, 10000)

//            Thực hiện công việc trên luồng chính, giúp cập nhật giao diện
//            Handler(Looper.getMainLooper()).postDelayed({
//                mediaPlayer.stop()
//                stopSelf()
//            }, 10000)
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            stopSelf()
        }
    }
}