package com.example.typecle

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private var content = "This is a test content"
    private var count = 0
    private var mistakes = 0
    private var mistakeFlag = false
    private var status = false

    private lateinit var contentView: TextView
    private lateinit var typeBox: EditText

    private var editor: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
            after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int,
            count: Int) {
            if (typeBox.text.isEmpty()) {
                return
            }
            var value = typeBox.text?.last()
            checkInput(value)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentView = findViewById(R.id.content_box)
        typeBox = findViewById(R.id.type_box)
        setTextView()

        //Opening keyboard on Activity creation
        //val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        typeBox.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        typeBox.addTextChangedListener(editor)
    }

    fun main() {

    }

    private fun setTextView() {
        val cView = this.contentView
        cView.text = this.content
    }

    fun checkInput(value: Char?) {
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        val correctChar = content[count]
        if (value == correctChar) {
            content = content.replace(count, '_')
            contentView.text = content
            if (count < content.length - 1) {
                count++
            }
            mistakeFlag = false

        }
        else {
            setMistakes()
        }
    }

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

    private fun runningTime() {
        
    }

    private fun String.replace(count: Int, value: Char): String {
        return this.substring(0,count) + value + this.substring(count+1)
    }
    



}


