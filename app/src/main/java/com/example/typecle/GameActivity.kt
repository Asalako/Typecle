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

    private var editor: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (typeBox.text.isEmpty()) {
                return
            }

            if (content[count].isWhitespace()) {
                Log.d("gameTestWpm", content[count].isWhitespace().toString() + " $wordCount|") //testing

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
            val value = typeBox.text.last()
            checkInput(value)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        article = intent.getParcelableExtra<Article>("chosenArticle") as Article

        contentView = findViewById(R.id.content_box)
//        contentView.text = article.getContent()
//        content = article.getContent().toString()
        contentView.text = "Test Content"               //testing
        content = "Test Content"
        Log.d("gameTestContent", content)

        typeBox = findViewById(R.id.type_box)
        stopWatch = findViewById(R.id.time_view)
        //stopWatch.setOnChronometerTickListener {  } use to stop game after x amount of time 1hr
        typeBox.requestFocus(); //Opening keyboard on Activity creation
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        typeBox.addTextChangedListener(editor)
        addStartListener()
    }

    fun startClock(listener: TextWatcher) {
        startStopWatch()
        typeBox.removeTextChangedListener(listener)
    }

    //checks if the input is correct
    fun checkInput(value: Char) {
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        val correctChar = content[count]
        if (value == correctChar) {
            content = content.replace(count, '_')
            Log.d("gameTestCorrect", "$value = $correctChar") //testing
            contentView.text = content
            if (count < content.length - 1) {
                count++
                Log.d("gameTestCount", "$count") //testing
            }
            else {
                endGame()
                stopStopWatch()
            }
            mistakeFlag = false

        }
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

    private fun calculateWPM() {
        val minutes: Double =
            ((SystemClock.elapsedRealtime() - stopWatch.base) / 1000).toDouble()/ 60

        val wpm = wordCount.toDouble() / minutes
        val wpmView = findViewById<TextView>(R.id.wpm_view)
        wpmView.text = String.format("%.2f",wpm)
        Toast.makeText(this, minutes.toString(), Toast.LENGTH_SHORT).show()

    }

    private fun startStopWatch() {
        if (!running) {
            stopWatch.base = SystemClock.elapsedRealtime() - pauseOffset
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
        //val playIcon = resources.getDrawable(R.drawable.ic_play_foreground)
        pauseDialog = Dialog(this)
        pauseDialog.setContentView(R.layout.pause_dialog)
        //pauseDialog.setCancelable(false)
        pauseDialog.show()
    }

    fun resume(v: View) {
        startStopWatch()
        pauseDialog.dismiss()
    }

    fun pauseReset(v: View) {
        reset()
        pauseDialog.dismiss()
    }

    fun endGameReset(v: View) {
        reset()
        resultDialog.dismiss()
    }

    fun killGame(v: View) {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
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

    private fun String.replace(count: Int, value: Char): String {
        return this.substring(0,count) + value + this.substring(count+1)
    }

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

    private fun endGame() {
        val time = ((SystemClock.elapsedRealtime() - stopWatch.base) / 1000).toDouble()

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val score: MutableMap<String, Any> = HashMap()
            score["time"] = time
            score["wpm"] = wpm
            score["articleId"] = "${article.getSource()}|${article.getTitle()}"
            score["title"] = article.getTitle().toString()
            score["mistakes"] = mistakes
            score["gameMode"] = mode
            score["uid"] = uid
            score["function"] = 1

            val data = Data.Builder().putAll(score).build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val request = OneTimeWorkRequest.Builder(DbWorker::class.java)
                .setConstraints(constraints).setInputData(data).build()
            WorkManager.getInstance(this).enqueue(request)
        }

        val minutes = time / 1000 / 60
        val seconds = time / 1000 % 60
        val string = "Time: $minutes:$seconds"
        resultDialog = Dialog(this)
        resultDialog.setContentView(R.layout.end_results_dialog)
        resultDialog.show()

//        resultTime = findViewById(R.id.result_time)
//        resultMistakes = findViewById(R.id.result_mistake)
//        resultWpm = findViewById(R.id.result_wpm)
//        resultTime.text = string
//        resultMistakes.text = mistakes.toString()
//        resultWpm.text = wpm.toString()

    }

    fun openLink(v: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()))
        startActivity(browserIntent)
    }


}


