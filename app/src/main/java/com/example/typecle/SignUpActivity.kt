package com.example.typecle

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.typecle.services.DbWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var mFirebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseAuth = FirebaseAuth.getInstance()
        emailField = findViewById(R.id.email)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
    }

    fun signUp(v: View) {

        val email = emailField.text.toString()
        val user = username.text.toString()
        val pass = password.text.toString()

        //checking for empty fields
        if (user.isEmpty()) {
            username.error = "Enter Username"
            username.requestFocus()
        }
        else if (email.isEmpty()) {
            emailField.error = "Please Enter Email"
            emailField.requestFocus()
        }
        else if (pass.isEmpty()) {
            password.error = "Enter Password"
            password.requestFocus()
        }
        else if (!isOnline(applicationContext)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()

        }
        else if (usernameCheck(user)) {
            Toast.makeText(this, "Username Taken", Toast.LENGTH_SHORT).show()
        }
        //firebase registration
        else if (!(email.isEmpty() && pass.isEmpty() && usernameCheck(user) )) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                task ->  if (!task.isSuccessful) {
                    Toast.makeText(this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show()
                }
            else {
                //if successful registration, store uid and email to db
                val id = task.result?.user?.uid

                if (id != null) {
                    val newUser = hashMapOf<String, Any>()
                    newUser["collection"] = "users"
                    newUser["function"] = 0
                    newUser["uid"] = id
                    newUser["username"] = user
                    newUser["email"] = email

                    val data = Data.Builder().putAll(newUser).build()
                    val request = OneTimeWorkRequest.Builder(DbWorker::class.java).setInputData(data).build()
                    WorkManager.getInstance(this).enqueue(request)

                    startActivity(Intent(this, MenuActivity::class.java))
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //Switch to sign in screen
    fun signInScreen(v: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    //Check to see if username exists or empty
    private fun usernameCheck(user: String): Boolean {
        if (user.isEmpty()) {
            return true
        }

        //query db to see if there is an existing user. if result found return true
        db.collection("users").whereEqualTo("username", user)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val query = task.result?.size() ?: 0
                    Log.d("dbRes", query.toString()) //testing
                    boolean = query > 0
                }
                else {
                    Log.d("dbErr",task.exception.toString()) //testing
                }
            }

        if (boolean) {
            boolean = false
            return true
        }
        return false
    }

    //check if any internet connection, returns false if there is none
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}
