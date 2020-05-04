package com.example.typecle

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

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
        btn.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        btn.text = category
        btn.id = id
        layout.addView(btn)
        btn.setOnClickListener{ openArticleActivity(category) }

    }

    private fun openArticleActivity(category: String) {
        val categoryIntent = Intent(this, ArticleActivity::class.java)
        categoryIntent.putExtra("category", category)
        startActivity(categoryIntent)
    }
}
