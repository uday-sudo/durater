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
    var quotes = arrayOf("Do not scroll too much reddit, it will harm your health.\n - Tonald Drump",
    "Human's attention span has become shorter than a gold fish's Attention span research suggests",
    "TikTok is cringe\n -Sane People",
    "You should try reading a Book in your free time.",
    "Have an 15-30 minutes exercise rutine daily.",
    "Try to get through your day with no coffee. It is a challenge.",
    "Never Skip your breakfast.",
    "Why not drink a glass of water shall we.",
    "Sleep well, the memes aren't worth it to sleep less for, trust me.",
    "Your should stretch your legs been sitting too long?")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pomodoro = findViewById(R.id.openPomodoro)
        todoButton = findViewById(R.id.openTodo)
        reminderButton = findViewById(R.id.openReminder)
        messageView = findViewById(R.id.bottomText)
        githubLink = findViewById(R.id.rateUs)
        messageView.setText(quotes.random())

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

        //Open Reminders activity
        reminderButton.setOnClickListener {
            var intent = Intent(this@MainActivity,Reminder::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        messageView.setText(quotes.random())
    }
}