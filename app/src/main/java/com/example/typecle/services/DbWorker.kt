package com.example.typecle.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore

class DbWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val db = FirebaseFirestore.getInstance()
    private var data: MutableMap<String, Any> = HashMap()

    override fun doWork(): Result {
        data = inputData.keyValueMap

        when (data["function"]) {
            0 -> {
                dbAddUser()
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