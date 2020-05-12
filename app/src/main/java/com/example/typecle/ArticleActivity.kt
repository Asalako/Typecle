package com.example.typecle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.typecle.newsapi.Article


class ArticleActivity : AppCompatActivity() {

    private val apiKey = "9f959a18ab254fb381f031560e3d7e41"
    private var totalResult = 0
    private var articles = mutableListOf<Article>()
    private lateinit var request: RequestQueue
    private lateinit var url: String

    private lateinit var category: String
    private lateinit var textView: TextView
    private lateinit var layout: LinearLayout
    private lateinit var layout_fav: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        category = intent.getStringExtra("category") ?: ""
        Log.d("intentSent",category)
        val country = "gb"

        //textView = findViewById<TextView>(R.id.textView2)
        layout = findViewById(R.id.article_layout)
        layout_fav = findViewById(R.id.article_layout_fav)

        url = "https://newsapi.org/v2/top-headlines?page=1&pagesize=100&country=$country" +
                "&category=$category&apiKey=$apiKey"
        request = Volley.newRequestQueue(this)
        getResponse()

    }

    fun test(v: View) {
        Toast.makeText(this,"works",Toast.LENGTH_LONG).show()
    }

    private fun getResponse() {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val status = it.getString("status")
                totalResult = it.getInt("totalResults")
                url = it.toString()
                val jsonArr = it.getJSONArray("articles")
                for (i in 0 until jsonArr.length() ) {
                    val data = jsonArr.getJSONObject(i)
                    val currentArticle = Article(
                        data.getJSONObject("source").getString("name"),
                        data.getString("author"),
                        data.getString("title"),
                        data.getString("description"),
                        data.getString("url"),
                        data.getString("urlToImage"),
                        data.getString("publishedAt"),
                        data.getString("content")

                    )
                    articles.add(currentArticle)
                }
                if (status == "ok") {
                    var id = 10
                    for (article in articles) {
                        generateArticleButtons(article, id)
                        id++
                    }
                }
                Log.d("ere",articles.size.toString())

            },
            Response.ErrorListener { it.printStackTrace() })
        request.add(jsonRequest)
    }

    private fun generateArticleButtons(article: Article, id: Int) {
        val btn = Button(this)
        val btnFav = Button(this)

        val widthInPixels = (resources.displayMetrics.density * 75).toInt()

        btn.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, widthInPixels)
        btnFav.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, widthInPixels)

        btn.text = article.getTitle()
        btnFav.text = id.toString()
        btn.id = id
        btnFav.id = id + 100
        layout.addView(btn)
        layout_fav.addView(btnFav)
        btn.setOnClickListener{ openArticleActivity(article) }
        btnFav.setOnClickListener{ saveArticleActivity(article) }

    }

    private fun openArticleActivity(article: Article) {
        val categoryIntent = Intent(this, GameActivity::class.java)
        categoryIntent.putExtra("chosenArticle", article)
        startActivity(categoryIntent)
    }

    private fun saveArticleActivity(article: Article) {
        
    }
}
