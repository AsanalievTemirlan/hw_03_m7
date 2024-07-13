package com.example.hw_03_m7.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.hw_03_m7.ui.notification.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificationUtils {

    fun convertTimeToMillis(time: String): Long {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = sdf.parse(time)
        val calendar = Calendar.getInstance().apply {
            this.time = date
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (this.timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return calendar.timeInMillis
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, title: String, message: String, timeInMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}
