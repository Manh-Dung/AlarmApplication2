package com.example.alarmapplication2

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Timer
import java.util.TimerTask

class AlarmReceiver : BroadcastReceiver() {
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)


        // Tạo một Intent cho hành động "Dismiss"
        val dismissIntent = Intent(context, DismissReceiver::class.java)
        val dismissPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)

// Tạo một Intent cho hành động "Snooze"
        val snoozeIntent = Intent(context, SnoozeReceiver::class.java)
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context!!, "alarm")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm Manager")
            .setContentText("Alarm!!!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(0, "Dismiss", dismissPendingIntent) // Thêm hành động "Dismiss"
            .addAction(0, "Snooze", snoozePendingIntent) // Thêm hành động "Snooze"

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Yêu cầu quyền POST_NOTIFICATIONS nếu chưa được cấp
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS // Mã yêu cầu tùy chỉnh
            )
        } else {
            // Gửi thông báo nếu đã có quyền
            notificationManager.notify(123, builder.build())
        }

        val url = Uri.parse("android.resource://${context.packageName}/${R.raw.victory}")
        var ringtone = RingtoneManager.getRingtone(context, url)

        val startRingtone = Timer()
        ringtone.play()

        startRingtone.schedule(object : TimerTask() {
            override fun run() {
                ringtone.stop()
                ringtone = null
            }
        }, 10000) // Âm nhạc sẽ phát trong 10 giây. 1 giây = 1000 ms
    }
}

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Hủy báo thức khi người dùng nhấn "Dismiss"
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)

        val url = Uri.parse("android.resource://${context.packageName}/${R.raw.victory}")
        var ringtone = RingtoneManager.getRingtone(context, url)
        ringtone.stop()

        Toast.makeText(context, "Alarm dismissed", Toast.LENGTH_SHORT).show()
    }
}

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Đặt lại báo thức sau 10 phút khi người dùng nhấn "Snooze"
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        // Đặt báo thức để kích hoạt sau 10 phút
        val triggerTime = System.currentTimeMillis() + 10 * 60 * 1000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        val url = Uri.parse("android.resource://${context.packageName}/${R.raw.victory}")
        var ringtone = RingtoneManager.getRingtone(context, url)
        ringtone.stop()
        val startRingtone = Timer()

        startRingtone.schedule(object : TimerTask() {
            override fun run() {
                ringtone.stop()
                ringtone = null
            }
        }, triggerTime) // Âm nhạc sẽ phát trong 10 giây. 1 giây = 1000 ms

        Toast.makeText(context, "Alarm snoozed for 10 minutes", Toast.LENGTH_SHORT).show()
    }
}