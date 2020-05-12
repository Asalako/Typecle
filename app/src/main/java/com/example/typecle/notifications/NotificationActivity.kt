package com.example.typecle.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.typecle.App
import com.example.typecle.MenuActivity
import com.example.typecle.R

class NotificationActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var editTextTitle: EditText
    private lateinit var editTextMessage: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        notificationManager = NotificationManagerCompat.from(this)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextMessage = findViewById(R.id.edit_text_message)

    }

    fun sendOnChannel1(v: View) {
        val title = editTextTitle.text.toString()
        val message = editTextMessage.text.toString()

        val actIntent = Intent(this, MenuActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, actIntent, 0)
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", message)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, App.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_keyboard_black_24dp).setContentTitle(title)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE).setColor(Color.BLUE)
            .setContentIntent(contentIntent).setAutoCancel(true).setOnlyAlertOnce(true)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent).build()

        notificationManager.notify(1, notification)
    }

    fun sendOnChannel2(v: View) {
        val title = editTextTitle.text.toString()
        val message = editTextMessage.text.toString()

        val notification = NotificationCompat.Builder(this, App.CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_library_add_black_24dp).setContentTitle(title)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(2, notification)
    }
}
