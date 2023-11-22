package com.example.alarmapplication2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.alarmapplication2.service.AlarmService


class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getIntExtra("alarm_id", -1)
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = Constants.ACTION.START_FOREGROUND_ACTION
        serviceIntent.putExtra("alarm_id", alarmId)
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