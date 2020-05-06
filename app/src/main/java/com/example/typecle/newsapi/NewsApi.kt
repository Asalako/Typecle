package com.example.typecle.newsapi;

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NewsApi(context: Context, category: String, country:String) {
    private val apiKey = "9f959a18ab254fb381f031560e3d7e41"
    private var totalResult = 0
    private lateinit var status: String
    private lateinit var articles: MutableList<Article>
    private var request: RequestQueue
    private var url: String

    init {
        url = "#"
        request = Volley.newRequestQueue(context)
        getResponse()
        Log.d("afterGetResponse", url)
    }

    private fun getResponse() {
        val future = RequestFuture.newFuture<JSONObject>()
        val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                status = it.getJSONArray("articles").getJSONObject(0).getString("title")
                totalResult = it.getInt("totalResults")
                url = it.toString()
                Log.d("results", totalResult.toString())
                val jsonArr = it.getJSONArray("articles")
                val list = mutableListOf<Article>()
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
                    list.add(currentArticle)
                }
                articles = list
                Log.d("ere",articles.size.toString())

            },
            Response.ErrorListener { it.printStackTrace() })
        request.add(jsonRequest)
    }

    fun getResult(): String {
        return status
    }

}
