package com.example.durater

import android.content.Context
import java.io.*

class DataHelper {

    val FILENAME = "todolist.dat"

    // To save the TodoList
    fun writeTodo(item : ArrayList<String>, context: Context) {
        var stream : FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)    //TO write data using file output stream

        var outputStream = ObjectOutputStream(stream)
        outputStream.writeObject(item)
        outputStream.close()
    }

    // Function to retrieve the TodoList from memory
    fun readTodo(context : Context) : ArrayList<String> {
        var tempList : ArrayList<String> //Container to store the array that we will read from file

        try {
            var stream : FileInputStream = context.openFileInput(FILENAME)
            var inputStream = ObjectInputStream(stream)
            tempList = inputStream.readObject() as ArrayList<String>
            inputStream.close()
        }catch (e : FileNotFoundException){
            tempList = ArrayList()
        }
        return tempList
    }
}