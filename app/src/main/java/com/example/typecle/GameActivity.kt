package com.example.typecle

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.typecle.newsapi.Article
import com.example.typecle.services.DbWorker
import com.google.firebase.auth.FirebaseAuth


class GameActivity : AppCompatActivity() {

    private var count = 0
    private var wordCount = 0
    private var wpm = 0.0
    private var mistakes = 0
    private var mistakeFlag = false
    private var running = false
    private var pauseOffset: Long = 0
    private val mode = "time trial"

    private lateinit var contentView: TextView
    private lateinit var content: String
    private lateinit var typeBox: EditText
    private lateinit var stopWatch: Chronometer
    private lateinit var pauseDialog: Dialog
    private lateinit var resultDialog: Dialog
    private lateinit var article: Article;

    private lateinit var resultTime: TextView
    private lateinit var resultWpm: TextView
    private lateinit var resultMistakes: TextView

    //listener for edit text view that activates when a new char is entered
    private var editor: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (typeBox.text.isEmpty()) {
                return
            }

            //calculating word per minute after every 2 words entered
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
            //prevents error if input is empty else checks the give value
            if (typeBox.text.isEmpty()) {
                return
            }
            val value = typeBox.text.last()
            checkInput(value)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //retrieving the selected article and display views
        article = intent.getParcelableExtra<Article>("chosenArticle") as Article

        contentView = findViewById(R.id.content_box)
        contentView.text = article.getContent()
        content = article.getContent().toString()
//        contentView.text = "Test Content"               //testing
//        content = "Test Content"
        Log.d("gameTestContent", content)

        typeBox = findViewById(R.id.type_box)
        stopWatch = findViewById(R.id.time_view)

        typeBox.requestFocus(); //Opening keyboard on Activity creation
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        typeBox.addTextChangedListener(editor)
        addStartListener()
    }

    //starts the clock for first input
    fun startClock(listener: TextWatcher) {
        startStopWatch()
        typeBox.removeTextChangedListener(listener)
    }

    //checks if the input is correct
    fun checkInput(value: Char) {

        val correctChar = content[count]
        if (value == correctChar) {

            //replacing the correct char with an underscore
            content = content.replace(count, '_')
            Log.d("gameTestCorrect", "$value = $correctChar") //testing
            contentView.text = content

            //selecting the next char
            if (count < content.length - 1) {
                count++
                Log.d("gameTestCount", "$count") //testing
            }
            //end is reached, ends game
            else {
                endGame()
                stopStopWatch()
            }
            //resetting flag to indicating a mistake can be counted
            //preventing adding multiple mistakes in a row
            mistakeFlag = false

        }

        //if wrong add to mistakes
        else {
            setMistakes()
            Log.d("gameTestIncorrect", "$value != $correctChar") //testing
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

    //calculates words per minute
    private fun calculateWPM() {
        //finds the total minutes spent
        val minutes: Double =
            ((SystemClock.elapsedRealtime() - stopWatch.base) / 1000).toDouble()/ 60

        //displays the calculated wpm
        val wpm = wordCount.toDouble() / minutes
        val wpmView = findViewById<TextView>(R.id.wpm_view)
        wpmView.text = String.format("%.2f",wpm)
        Toast.makeText(this, minutes.toString(), Toast.LENGTH_SHORT).show()

    }

    //starts the watch and sets flag for currently running to true
    private fun startStopWatch() {
        if (!running) {
            stopWatch.base = SystemClock.elapsedRealtime() - pauseOffset
            stopWatch.start()
            running = true
        }
    }

    //stops the watch and sets flag for currently running to true
    private fun stopStopWatch() {
        if (running) {
            stopWatch.stop()
            pauseOffset = SystemClock.elapsedRealtime() - stopWatch.base
            running = false
        }
    }

    //Pauses the time and displays dialog with options
    fun pause(v: View) {
        stopStopWatch()
        //val playIcon = resources.getDrawable(R.drawable.ic_play_foreground)
        pauseDialog = Dialog(this)
        pauseDialog.setContentView(R.layout.pause_dialog)
        //pauseDialog.setCancelable(false)
        pauseDialog.show()
    }

    //resumes game from paused state
    fun resume(v: View) {
        startStopWatch()
        pauseDialog.dismiss()
    }

    //Resets the games from pause menu
    fun pauseReset(v: View) {
        reset()
        pauseDialog.dismiss()
    }

    //Resets the game from end screen
    fun endGameReset(v: View) {
        reset()
        resultDialog.dismiss()
    }

    //End the activity and returns to menu screen
    fun killGame(v: View) {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    //resets all the values back to default
    private fun reset() {
        stopWatch.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        count = 0
        wordCount = 0
        wpm = 0.0
        mistakes = 0
        mistakeFlag = false
        running = false
        contentView.text = article.getContent()
        content = article.getContent().toString()
        stopStopWatch()
        addStartListener()
    }

    //replaces given character to an underscore
    private fun String.replace(count: Int, value: Char): String {
        return this.substring(0,count) + value + this.substring(count+1)
    }


    //lister for edit text to start the clock on first input, is removed after that
    private fun addStartListener() {
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

    //displays end game and uploads results
    private fun endGame() {
        //time in seconds
        val time = ((SystemClock.elapsedRealtime() - stopWatch.base) / 1000).toDouble()

        //check for a logged in user
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {

            //retrieving data to be stored
            val score: MutableMap<String, Any> = HashMap()
            score["time"] = time
            score["wpm"] = wpm
            score["articleId"] = "${article.getSource()}|${article.getTitle()}"
            score["title"] = article.getTitle().toString()
            score["mistakes"] = mistakes
            score["gameMode"] = mode
            score["uid"] = uid
            score["function"] = 1

            //sending data and performing database operation in the back ground
            val data = Data.Builder().putAll(score).build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val request = OneTimeWorkRequest.Builder(DbWorker::class.java)
                .setConstraints(constraints).setInputData(data).build()
            WorkManager.getInstance(this).enqueue(request)
        }

        resultDialog = Dialog(this)
        resultDialog.setContentView(R.layout.end_results_dialog)
        resultDialog.show()

    }

    //opens browser with link to the full article
    fun openLink(v: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()))
        startActivity(browserIntent)
    }


}


