package com.example.durater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}