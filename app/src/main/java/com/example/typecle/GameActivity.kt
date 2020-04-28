package com.example.typecle

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GameActivity : AppCompatActivity() {

    var time = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
    var content = "This is a test content"
    private lateinit var contentView: TextView
    private lateinit var typeBox: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentView = findViewById(R.id.content_box)
        typeBox = findViewById(R.id.type_box)
        setTextView()

        //Opening keyboard on Activity creation
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        typeBox.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        typeBox.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                //typeBox.setText("")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


            }
        })
    }

    private fun setTextView() {
        val cView = this.contentView
        cView.text = this.content
    }

}
