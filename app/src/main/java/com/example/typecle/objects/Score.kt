package com.example.typecle.objects

class Score(
    private var articleId: String,
    private var gameMode: String,
    private var title: String,
    private var mistakes: Int = 0,
    private var time: Int = 0,
    private var wpm: Double = 0.0
) {

    init {

    }

    fun getArticleId(): String {
        return articleId
    }

    fun getTitle(): String {
        return title
    }

    fun getGameMode(): String {
        return gameMode
    }

    fun getMistakes(): Int {
        return mistakes
    }

    fun getTime(): Int {
        return time
    }

    fun getWpm(): Double {
        return wpm
    }



}