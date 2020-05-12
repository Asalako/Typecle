package com.example.typecle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
                    val newUser = hashMapOf<String, String>()
                    newUser["username"] = user
                    newUser["email"] = email
                    val id = task.result?.user?.uid

                if (id != null) {
                    db.collection("users").document(id).set(
                        newUser
                    ).addOnSuccessListener {
                        Log.d("addSuc", "success")
                    }.addOnFailureListener {
                        Log.w("addErr", it.toString())
                    }

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
}
