package com.example.typecle

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ScoresFragment: Fragment() {

    private val db = FirebaseFirestore.getInstance()

    //opens score fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_scores, container, false)

        //check for internet connection
        if (!isOnline(root.context)) {
            Toast.makeText(root.context, "No Internet", Toast.LENGTH_SHORT).show()
        }

        getScores(root)
        return root
    }

    //gets scores from database and displays it
    private fun getScores(v: View) {
        val layout: LinearLayout = v.findViewById(R.id.scores_layout)

        //check for logged in user
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            //database query for user scores
            val ref = "users/$uid/scores"
            db.collection(ref)
                .get().addOnCompleteListener{
                    if (it.isSuccessful) {
                        for (document in it.result!!) {
                            //displays the scores in the fragment

                            val time = document.get("time")
                            val title = document.get("title")
                            val mistakes = document.get("mistakes")
                            val wpm = document.get("wpm")
                            val mode = document.get("gameMode")

                            val timeString = "Time: $time"
                            val modeString = "Mode: $time"
                            val mistakesString = "Mistakes: $time"
                            val titleString = "\nTitle: $title"
                            val wpmString = "WPM: $time"

                            val textView = TextView(activity)
                            val textView2 = TextView(activity)
                            val textView3 = TextView(activity)
                            val textView4 = TextView(activity)
                            val textView5 = TextView(activity)

                            textView.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView2.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView3.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView4.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView5.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)

                            textView.text = titleString
                            textView2.text = modeString
                            textView3.text = timeString
                            textView4.text = mistakesString
                            textView5.text = wpmString

                            layout.addView(textView)
                            layout.addView(textView2)
                            layout.addView(textView3)
                            layout.addView(textView4)
                            layout.addView(textView5)
                        }
                    }
                }.addOnFailureListener{
                    val textView = TextView(activity)
                    val string = "Error. No scores can be shown"
                    textView.text = string
                    layout.addView(textView)
                }
        }
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