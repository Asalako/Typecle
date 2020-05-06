package com.example.typecle.newsapi

import android.util.Log

class Article(
    source: String,
    author: String,
    title: String,
    description: String,
    url: String,
    image: String,
    date: String,
    content: String
) {
    private var source: String = source
    private var author: String = author
    private var title: String = title
    private var description: String = description
    private var url: String = url
    private var urlImage: String = image
    private var publishDate: String = date
    private var content: String = content

    init {
        Log.d("working","here")
        Log.d("Test",toString())
    }

    override fun toString(): String {
        return "Source:$source Author:$author Title:$title URL:$url " +
                "Date:$publishDate Content:$content"
    }



}