package com.example.durater

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import org.w3c.dom.Text

class Pomodoro : AppCompatActivity() {

    // initialize all UI elements
    lateinit var plusFocus : Button
    lateinit var minusFocus : Button
    lateinit var plusBreak : Button
    lateinit var minusBreak : Button
    lateinit var start : Button
    lateinit var reset : Button
    lateinit var focusCount : TextView
    lateinit var breakCount : TextView
    lateinit var countdown : TextView
    lateinit var alerts : ToggleButton
    var focusTime = 0
    var breakTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        //Just Defining links to UI elements
        plusFocus = findViewById(R.id.plusFocus)
        minusFocus = findViewById(R.id.minusFocus)
        plusBreak = findViewById(R.id.plusBreak)
        minusBreak = findViewById(R.id.minusBreak)
        start = findViewById(R.id.start)
        reset = findViewById(R.id.reset)
        focusCount = findViewById(R.id.focusCount)
        breakCount = findViewById(R.id.breakCount)
        countdown = findViewById(R.id.countdown)
        alerts = findViewById(R.id.alertsToggle)

        // Just handling counter increment and decrement
        plusFocus.setOnClickListener {
            focusTime++
            focusCount.setText("" + focusTime)
        }
        minusFocus.setOnClickListener {
            if (focusTime>0) {
                focusTime--
                focusCount.setText("" + focusTime)
            }
        }
        plusBreak.setOnClickListener {
            breakTime++
            breakCount.setText("" + breakTime)
        }
        minusBreak.setOnClickListener {
            if (breakTime>0) {
                breakTime--
                breakCount.setText("" + breakTime)
            }
            playSound()
        }

        start.setOnClickListener {
            workTimer(focusTime.toLong())
        }
    }

    // From here starts the Definition of general functions
    // Worktimer
    fun workTimer(a : Long) {
        object : CountDownTimer(a*60000, 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                countdown.setText("" + millisUntilFinished / 1000)
            }
            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                breakTimer(breakTime.toLong())
            }
        }.start()
    }

    // Breaktimer
    fun breakTimer(a : Long) {
        object : CountDownTimer(a*60000, 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                countdown.setText("" + millisUntilFinished / 1000)
            }
            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                workTimer(focusTime.toLong())
            }
        }.start()
    }

    fun playSound() {
        var mediaPlayer = MediaPlayer.create(this, R.raw.notify1)
        mediaPlayer.start()
        Thread.sleep(1000)
        mediaPlayer.release()
    }
}