package com.example.typecle.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.typecle.MenuActivity
import com.example.typecle.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val db = FirebaseFirestore.getInstance()
    private val tagd = "db"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = "Select Game Mode"
        })
        return root
    }

    //fun dbtest(v: View) {}
//    fun dbtest(v: View) {
//        // Create a new user with a first and last name
//        // Create a new user with a first and last name
//        val user: MutableMap<String, Any> = HashMap()
//        user["first"] = "Ada"
//        user["last"] = "Lovelace"
//        user["born"] = 1815
//
//        // Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(tagd,
//                    "DocumentSnapshot added with ID: " + documentReference.id);
//                Toast.makeText(requireContext(),"saved", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener(OnFailureListener { e -> Log.w(tagd, "Error adding document", e) })
//    }
}
