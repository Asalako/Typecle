package com.example.typecle.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DbWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val db = FirebaseFirestore.getInstance()
    private var data: MutableMap<String, Any> = HashMap()

    override fun doWork(): Result {
        data = inputData.keyValueMap

        when (data["function"]) {
            0 -> {
                dbAddUser()
            }
            1 -> {
                dbAddScore()
            }
            2 -> {
                dbAddArticle()
            }
            else -> {
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun dbAddUser() {

        val collection = data["collection"].toString()
        val newUser : MutableMap<String, Any> = HashMap()
        val fields = arrayOf("email","username")

        for (field in fields) {
            newUser[field] = data[field] as Any
        }

        db.collection(collection).document( data["uid"].toString() ).set(
            newUser
        ).addOnSuccessListener {
            Log.d("addSuc", "success")
        }.addOnFailureListener {
            Log.w("addErr", it.toString())
        }
    }

    private fun dbAddScore() {

        val fields = arrayOf("articleId","gameMode", "title", "mistakes", "time", "wpm")
        val score = convertToMap(fields)
        val ref = "users/${data["uid"].toString()}/scores"
        val scoreId = "${data["uid"].toString()}|${score["articleId"]}"

        db.collection(ref).document(scoreId).set(score, SetOptions.merge())

    }

    private fun dbAddArticle() {
        val fields = arrayOf("source","author", "title", "description", "url", "image",
            "publishDate", "content")
        val article = convertToMap(fields)

        val ref = "users/${data["uid"].toString()}/articles"
        val articleId = "${data["source"].toString()}|${data["title"]}"
        db.collection(ref).document(articleId).set(article, SetOptions.merge())
    }

    private fun convertToMap(fields: Array<String>): MutableMap<String, Any> {
        val map : MutableMap<String, Any> = HashMap()

        for (field in fields) {
            map[field] = data[field] as Any
        }
        return map
    }

    private fun dbTest() {

        // Add a new document with a generated ID
        //db.collection("users").document()
        val collection = data["collection"].toString()
        val updateMap : MutableMap<String, Any> = HashMap()
        val fields = arrayOf("born","first","last")

        updateMap[fields[0]] = data[fields[0]].toString()
        updateMap[fields[1]] = data[fields[1]].toString()
        updateMap[fields[2]] = data[fields[2]].toString()
        Log.d("strValue", data.toString())

        db.collection(collection)
            .add(updateMap)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "dbSuc",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("dbError", "Error adding document", e) }

    }

}