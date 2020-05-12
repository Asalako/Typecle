package com.example.typecle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.username)
        password = findViewById(R.id.password)

        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val mFirebaseUser = mFirebaseAuth.currentUser
            if (mFirebaseUser != null) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
            }
            else {
                Toast.makeText(this, "Please Log in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signIn(v: View){
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
            mFirebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener{
                if (!it.isSuccessful) {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
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

    fun signUpScreen(v: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener { mAuthStateListener }
    }
}
