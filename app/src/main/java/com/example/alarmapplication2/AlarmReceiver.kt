package com.example.alarmapplication2

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Timer
import java.util.TimerTask


class AlarmReceiver : BroadcastReceiver() {
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = Constants.ACTION.START_FOREGROUND_ACTION
        context?.startForegroundService(serviceIntent)



//        val i = Intent(context, MainActivity::class.java)
//        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(
//                context,
//                0,
//                i,
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            )

//        val dismissIntent = Intent(context, DismissReceiver::class.java)
//        val dismissPendingIntent: PendingIntent =
//            PendingIntent.getBroadcast(
//                context,
//                1,
//                dismissIntent,
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//        val snoozeIntent = Intent(context, SnoozeReceiver::class.java)
//        val snoozePendingIntent: PendingIntent =
//            PendingIntent.getBroadcast(
//                context,
//                2,
//                snoozeIntent,
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            )

//        val builder = NotificationCompat.Builder(context!!, Constants.NOTIFICATION_CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Alarm Manager")
//            .setContentText("Alarm!!!")
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .addAction(0, "Dismiss", dismissPendingIntent)
//            .addAction(0, "Snooze", snoozePendingIntent)

//        val notificationManager = NotificationManagerCompat.from(context)
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context as Activity,
//                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                REQUEST_CODE_POST_NOTIFICATIONS
//            )
//        } else {
//            notificationManager.notify(123, builder.build())
//        }
//
//        // Phat nhac
//        val url = Uri.parse("android.resource://${context.packageName}/${R.raw.victory}")
//        var ringtone = RingtoneManager.getRingtone(context, url)
//
//        val startRingtone = Timer()
//        ringtone.play()
//
//        startRingtone.schedule(object : TimerTask() {
//            override fun run() {
//                ringtone.stop()
//                ringtone = null
//            }
//        }, 10000)

        // Test phat nhac
//        val notification =
//            RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
//        val mp = MediaPlayer.create(context, notification)
//        mp.prepare()
//        mp.start()
//        mp.isLooping = true
//
//        val startRingtone = Timer()
//
//        startRingtone.schedule(object : TimerTask() {
//            override fun run() {
//                mp.stop()
//                mp.release()
//            }
//        }, 10000)
    }

}

object Constants {
    object ACTION {
        const val START_FOREGROUND_ACTION = "START_FOREGROUND"
        const val STOP_FOREGROUND_ACTION = "STOP_FOREGROUND"
    }

    const val NOTIFICATION_CHANNEL_ID = "alarm_channel"
}