package com.example.hw_03_m7.ui.notification

import NotificationHelper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val message = intent?.getStringExtra("message")

        val notificationHelper = context?.let { NotificationHelper(it) }
        notificationHelper?.showNotification(title, message)
    }
}