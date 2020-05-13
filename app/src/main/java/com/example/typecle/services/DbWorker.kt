package com.example.typecle.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

//Class that performs write database operations in the background
class DbWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val db = FirebaseFirestore.getInstance()
    private var data: MutableMap<String, Any> = HashMap()

    override fun doWork(): Result {
        data = inputData.keyValueMap

        //selecting the type of function to perform based on the set number
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

    //Adds a new user to the database
    private fun dbAddUser() {

        //Taking the necessary data to be upload as not all are relevant to the user
        val collection = data["collection"].toString()
        val fields = arrayOf("email","username")
        val newUser = convertToMap(fields)

        //database which uploads the user details with a custom ID
        db.collection(collection).document( data["uid"].toString() ).set(
            newUser
        ).addOnSuccessListener {
            Log.d("addSuc", "success") //testing
        }.addOnFailureListener {
            Log.w("addErr", it.toString()) //testing
        }
    }

    //Adds/updates a new score after completing a game
    private fun dbAddScore() {

        //Taking the necessary data to be upload as not all are relevant to the user
        val fields = arrayOf("articleId","gameMode", "title", "mistakes", "time", "wpm")
        val score = convertToMap(fields)

        //reference in the db and creating an ID for score
        val ref = "users/${data["uid"].toString()}/scores"
        val scoreId = "${data["uid"].toString()}|${score["articleId"]}"

        //database operation to add/update score to the database
        db.collection(ref).document(scoreId).set(score, SetOptions.merge())

    }

    //Stores an article the user wants to save
    private fun dbAddArticle() {

        //Taking the necessary data to be upload as not all are relevant to the user
        val fields = arrayOf("source","author", "title", "description", "url", "image",
            "publishDate", "content")
        val article = convertToMap(fields)

        //reference in the db and creating an ID for
        val ref = "users/${data["uid"].toString()}/articles"
        val articleId = "${data["source"].toString()}|${data["title"]}"
        db.collection(ref).document(articleId).set(article, SetOptions.merge())
    }

    //takes the specified fields in the map and stores it into another map
    private fun convertToMap(fields: Array<String>): MutableMap<String, Any> {
        val map : MutableMap<String, Any> = HashMap()

        for (field in fields) {
            map[field] = data[field] as Any
        }
        return map
    }

}