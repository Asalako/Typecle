package com.example.typecle.newsapi

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Article(
    private var source: String?,
    private var author: String?,
    private var title: String?,
    private var description: String?,
    private var url: String?,
    private var image: String?,
    private var publishDate: String?,
    private var content: String?
): Parcelable {

    init {
        Log.d("working","here")
        Log.d("Test",toString())
        content = content?.replace(Regex("[\n\r]"), " ")
            ?.replace("  ", " ")?.trim()

    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "Source:$source Author:$author Title:$title URL:$url " +
                "Date:$publishDate Content:$content"
    }

    fun getSource(): String? {
        return source
    }

    fun getAuthor(): String? {
        return author
    }

    fun getTitle(): String? {
        return title
    }

    fun getDescription(): String? {
        return description
    }

    fun getUrl(): String? {
        return url
    }

    fun getImageUrl(): String? {
        return image
    }

    fun getDate(): String? {
        return publishDate
    }

    fun getContent(): String? {
        return content
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(image)
        parcel.writeString(publishDate)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }


}