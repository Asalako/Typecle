package com.example.typecle

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App: Application() {

    companion object {
        const val CHANNEL_1_ID = "channel1"
        const val CHANNEL_2_ID = "channel2"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    //Creates the notification channels
    private fun createNotificationChannels() {
        //Check to see if the build version is met
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 =NotificationChannel(CHANNEL_1_ID, "Channel 1",
            NotificationManager.IMPORTANCE_HIGH)
            channel1.description = "Important"

            val channel2 =NotificationChannel(CHANNEL_2_ID, "Channel 2",
                NotificationManager.IMPORTANCE_LOW)
            channel1.description = "General"

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel1)
            manager?.createNotificationChannel(channel2)
        }
    }
}