package com.example.typecle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.username)
        password = findViewById(R.id.password)
    }

    fun signUp(v: View) {
        val user = email.text.toString()
        val pass = password.text.toString()

        if (user.isEmpty()) {
            email.error = "Please Enter Email"
            email.requestFocus()
        }
        else if (pass.isEmpty()) {
            password.error = "Enter Password"
            password.requestFocus()
        }
        else if (user.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Fields Are Empty", Toast.LENGTH_SHORT).show()
        }
        else if (!(user.isEmpty() && pass.isEmpty())) {
            mFirebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener{
                task ->  if (!task.isSuccessful) {
                    Toast.makeText(this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show()
                }
                else {
                    startActivity(Intent(this, MenuActivity::class.java))
                }
            }
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun signInScreen(v: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }
}
