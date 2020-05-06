package com.example.typecle.newsapi;

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class NewsApi(context: Context, category: String, country:String) {
    private val apiKey = "9f959a18ab254fb381f031560e3d7e41"
    private var totalResult = 0
    private lateinit var status: String
    private lateinit var articles: MutableList<Article>
    private var request: RequestQueue
    private var url: String

    init {
        url = "https://newsapi.org/v2/top-headlines?page=1&pagesize=100&country=$country" +
                "&category=$category&apiKey=$apiKey"
        request = Volley.newRequestQueue(context)
        Log.d("url", url)
        getResponse()
        Log.d("News", url)
    }

    private fun getResponse() {

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                status = it.getString("status")
                totalResult = it.getInt("totalResults")
                url = it.toString()
                Log.d("results", totalResult.toString())
                val jsonArr = it.getJSONArray("articles")
//                for (i in 0 until jsonArr.length() ) {
//                    val data = jsonArr.getJSONObject(i)
//                    val currentArticle = Article(
//                        data.getJSONObject("source").getString("name"),
//                        data.getString("author"),
//                        data.getString("title"),
//                        data.getString("description"),
//                        data.getString("url"),
//                        data.getString("urlToImage"),
//                        data.getString("publishedAt"),
//                        data.getString("content")
//                    )
//                    articles.add(currentArticle)
//                }
            },
            Response.ErrorListener { it.printStackTrace() })

        request.add(jsonRequest)
    }

    fun getResult(): Int {
        return totalResult
    }

}
