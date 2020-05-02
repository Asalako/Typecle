package com.example.typecle

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.floor

class GameActivity : AppCompatActivity() {

    private var content = "This is a passage for testing Typecle application in it's development " +
            "stage, which is to be used within the game mode created."
    private var count = 0
    private var wordCount = 0
    private var wpm = 0.0
    private var mistakes = 0
    private var mistakeFlag = false
    private var running = false
    private var pauseOffset: Long = 0
    private var status = false

    private lateinit var contentView: TextView
    private lateinit var typeBox: EditText
    private lateinit var stopWatch: Chronometer
    //private lateinit var pauseButton: Button

    private var editor: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (typeBox.text.isEmpty()) {
                return
            }

            if (content[count].isWhitespace()) {
                wordCount++
                if (wordCount % 2 == 0) {
                    calculateWPM()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
            after: Int) {

        }

        //Activates when user inputs a character
        override fun onTextChanged(s: CharSequence, start: Int, before: Int,
            count: Int) {
            //prevents error if input is empty
            if (typeBox.text.isEmpty()) {
                return
            }
            var value = typeBox.text.last()
            checkInput(value)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentView = findViewById(R.id.content_box)
        setTextView()
        typeBox = findViewById(R.id.type_box)
        stopWatch = findViewById(R.id.time_view)
        //stopWatch.setOnChronometerTickListener {  } use to stop game after x amount of time 1hr
        typeBox.requestFocus(); //Opening keyboard on Activity creation
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        typeBox.addTextChangedListener(editor)

        val startTime: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                startClock(this)
            }

        }

        typeBox.addTextChangedListener(startTime)
    }

    fun startClock(listener: TextWatcher) {
        startStopWatch()
        typeBox.removeTextChangedListener(listener)
    }

    private fun setTextView() {
        val cView = this.contentView
        cView.text = this.content
    }

    //checks if the input is correct
    fun checkInput(value: Char) {
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        val correctChar = content[count]
        if (value == correctChar) {
            content = content.replace(count, '_')
            contentView.text = content
            if (count < content.length - 1) {
                count++
            }
            else {
                stopStopWatch()
            }
            mistakeFlag = false

        }
        else {
            setMistakes()
        }
    }

    //increments mistakes and sets a flag to stop incrementing.
    private fun setMistakes() {
        if (!mistakeFlag) {
            mistakes++
            val view = findViewById<TextView>(R.id.mistake_view)
            view.text = mistakes.toString()
            mistakeFlag = true
        }
        return

    }

    private fun calculateWPM() {
        var minutes: Double =
            ((SystemClock.elapsedRealtime() - stopWatch.base) / 1000).toDouble()/ 60

        val wpm = wordCount.toDouble() / minutes
        val wpmView = findViewById<TextView>(R.id.wpm_view)
        wpmView.text = String.format("%.2f",wpm)
        Toast.makeText(this, minutes.toString(), Toast.LENGTH_SHORT).show()

    }

    private fun startStopWatch() {
        if (!running) {
            stopWatch.base = SystemClock.elapsedRealtime()
            stopWatch.start()
            running = true
        }
    }

    private fun stopStopWatch() {
        if (running) {
            stopWatch.stop()
            pauseOffset = SystemClock.elapsedRealtime() - stopWatch.base
            running = false
        }
    }

    fun pause(v: View) {
        stopStopWatch()
    }

    fun resetStopWatch(v: View) {
        stopWatch.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
    }

    private fun String.replace(count: Int, value: Char): String {
        return this.substring(0,count) + value + this.substring(count+1)
    }
    



}


