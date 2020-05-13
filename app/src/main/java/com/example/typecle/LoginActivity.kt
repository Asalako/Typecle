package com.example.typecle

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        //setting up firebase auth for login
        mFirebaseAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.username)
        password = findViewById(R.id.password)

        //Check for a logged in user
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

    //Function to sign in when button is pressed
    fun signIn(v: View){
        val user = email.text.toString()
        val pass = password.text.toString()

        //check for empty fields, not internet access and incorrect credentials
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
        else if (!isOnline(applicationContext)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
        }
        else if (!(user.isEmpty() && pass.isEmpty())) {
            mFirebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener{
                if (!it.isSuccessful) {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
                else {
                    //Starts activity if correct credentials are submitted
                    startActivity(Intent(this, MenuActivity::class.java))
                }
            }
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //Switches to sign up screen
    fun signUpScreen(v: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    //Add listener on start up
    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener { mAuthStateListener }
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
