package com.example.typecle.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.typecle.App
import com.example.typecle.R

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        //Setting the notification based on the channel
        val notificationManager = context?.let { NotificationManagerCompat.from(it) }

        val title = "Practice!"
        val message = "If you want to get better at typing, keep up the practice"

        //notification settings
        val notification = context?.let {
            NotificationCompat.Builder(it, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_keyboard_black_24dp).setContentTitle(title)
                .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).setColor(Color.BLUE).build()
        }
        //selecting the notification channel
        if (notification != null) {
            notificationManager?.notify(1, notification)
        }

    }
}