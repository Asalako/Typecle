package com.example.typecle

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.typecle.notifications.NotificationReceiver

class CategoriesActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout

    //All categories in newsapi
    private val categories = arrayOf(
        "business","entertainment", "general", "health", "science", "sports", "technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        layout = findViewById<LinearLayout>(R.id.categoryLayout)

        //Generating category form based on the set category list
        for (i in categories.indices) { generateButton(categories[i], i) }
        
    }

    //creates buttons with onclick listener
    private fun generateButton(category: String, id: Int) {
        val btn = Button(this)
        //conversion between dpi to pixels
        val heightInPixels = (resources.displayMetrics.density * 75).toInt()

        //setting layout params and onclick listener which holds category string
        btn.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels)
        btn.text = category
        btn.id = id
        layout.addView(btn)
        btn.setOnClickListener{ openArticleActivity(category) }
    }

    //opens article activity and sends selected category string
    private fun openArticleActivity(category: String) {
        val categoryIntent = Intent(this, ArticleActivity::class.java)
        categoryIntent.putExtra("category", category)
        startActivity(categoryIntent)
        finish()
    }

}
