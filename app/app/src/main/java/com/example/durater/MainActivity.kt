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
    lateinit var messageView : TextView
    lateinit var supportButton : Button
    var quotes = arrayOf("Do not scroll too much reddit, it will harm your health.\n   - Tonald Drump",
    "Human's attention span has become shorter than a gold fish's Attention span, research suggests",
    "TikTok is cringe\n   - Sane People",
    "You should try reading a Book in your free time.",
    "Having an 30 minutes exercise routine daily is an anti aging remedy.",
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
        messageView = findViewById(R.id.bottomText)
        supportButton = findViewById(R.id.support)
        messageView.setText(quotes.random())

        //open buy me a coffee page
        supportButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.buymeacoffee.com/uday101")
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

    }

    override fun onResume() {
        super.onResume()
        messageView.setText(quotes.random())
    }
}