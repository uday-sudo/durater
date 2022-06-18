package com.example.durater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class todoAdapter(var todoList: ArrayList<String>, var context: Context) : RecyclerView.Adapter<todoAdapter.todoViewHolder>(){

    class todoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card : CardView = itemView.findViewById((R.id.card))
        var del : ImageButton = itemView.findViewById((R.id.delButton))
        var textViewTodo : TextView = itemView.findViewById(R.id.cardText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.card_design, parent, false)
        return todoViewHolder(view)
    }

    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        holder.textViewTodo.text = todoList.get(position)

        holder.del.setOnClickListener {
            todoList.removeAt(position)
            var datahelper = DataHelper()
            datahelper.writeTodo(todoList,context)
            super.notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

}