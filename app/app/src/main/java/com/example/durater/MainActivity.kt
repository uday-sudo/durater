package com.example.durater

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var pomodoro : Button
    lateinit var todoButton : Button
    lateinit var reminderButton : Button
    lateinit var messageView : TextView
    lateinit var githubLink : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pomodoro = findViewById(R.id.openPomodoro)
        todoButton = findViewById(R.id.openTodo)
        reminderButton = findViewById(R.id.openReminder)
        messageView = findViewById(R.id.bottomText)
        githubLink = findViewById(R.id.rateUs)

        //open the github link
        githubLink.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://github.com/uday-sudo/durater")
            startActivity(openURL)
        }

        //Open Pomodoro activity
        pomodoro.setOnClickListener {
            var intent = Intent(this@MainActivity,Pomodoro::class.java)
            startActivity(intent)
        }

        //open todolist activity
        todoButton.setOnClickListener {
            var intent = Intent(this@MainActivity,Todo::class.java)
            startActivity(intent)
        }

        //Open Rminders activity
        reminderButton.setOnClickListener {
            var intent = Intent(this@MainActivity,Reminder::class.java)
            startActivity(intent)
        }

    }
}