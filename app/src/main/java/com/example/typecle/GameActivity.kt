package com.example.typecle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

abstract class GameActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }

    abstract fun onCreate();
    abstract val time: Date;
    abstract val content: String;

}
