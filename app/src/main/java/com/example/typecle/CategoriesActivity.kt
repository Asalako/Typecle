package com.example.typecle

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.typecle.notifications.NotificationReceiver
import com.example.typecle.services.DbService
import com.example.typecle.services.DbWorker

class CategoriesActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout


    private val categories = arrayOf(
        "business","entertainment", "general", "health", "science", "sports", "technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        layout = findViewById<LinearLayout>(R.id.categoryLayout)
        
        for (i in categories.indices) { generateButton(categories[i], i) }
        
    }

    private fun generateButton(category: String, id: Int) {
        val btn = Button(this)
        val heightInPixels = (resources.displayMetrics.density * 75).toInt()

        btn.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels)
        btn.text = category
        btn.id = id
        layout.addView(btn)
        btn.setOnClickListener{ openArticleActivity(category) }

    }

    private fun openArticleActivity(category: String) {
        val categoryIntent = Intent(this, ArticleActivity::class.java)
        categoryIntent.putExtra("category", category)
        startActivity(categoryIntent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        //startService(Intent(this, DbService::class.java))
        //notificationTest()

    }

    fun notificationTest() {
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        val buffer: Long = 1000 * 10
        alarmManager.set(AlarmManager.RTC_WAKEUP,time+buffer, actionIntent)
    }
}
