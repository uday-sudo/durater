package com.example.durater

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Todo : AppCompatActivity() {

    lateinit var textBox : EditText
    lateinit var add : Button
    lateinit var view : RecyclerView
    lateinit var supportButton : Button

    lateinit var adapter : todoAdapter

    var todoList = ArrayList<String>()
    var datahelper = DataHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        textBox = findViewById(R.id.editText)
        add = findViewById(R.id.add)
        view = findViewById(R.id.recyclerView)
        supportButton = findViewById(R.id.support)
        view.layoutManager = LinearLayoutManager(this@Todo) //Since we are going to show top to bottom we set LinearLayout

        todoList = datahelper.readTodo(this@Todo)// Retrieve the data into this container

        adapter = todoAdapter(todoList, this@Todo)
        view.adapter = adapter

        add.setOnClickListener {
            var itemName : String = textBox.text.toString()
            todoList.add(itemName)
            textBox.setText("")
            datahelper.writeTodo(todoList,applicationContext)
            addEntry()
        }

        //open buy me a coffee page
        supportButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.buymeacoffee.com/uday101")
            startActivity(openURL)
        }

    }
    fun addEntry() {
        adapter.notifyDataSetChanged()
    }
}