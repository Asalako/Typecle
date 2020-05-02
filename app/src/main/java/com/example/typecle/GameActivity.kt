package com.example.typecle

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private var content = "This is a test content"
    private var count = 0
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
        stopWatch = findViewById(R.id.stopwatch)
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


