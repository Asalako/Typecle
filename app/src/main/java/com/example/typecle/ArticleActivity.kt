package com.example.typecle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.typecle.newsapi.NewsApi


class ArticleActivity : AppCompatActivity() {

    private lateinit var category: String
    private lateinit var api: NewsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        category = intent.getStringExtra("category") ?: ""
        Log.d("intentSent",category)

        api = NewsApi(applicationContext,category, "gb")
    }

    fun test(v: View) {
        Toast.makeText(this,api.getResult().toString(),Toast.LENGTH_LONG).show()
    }
}
