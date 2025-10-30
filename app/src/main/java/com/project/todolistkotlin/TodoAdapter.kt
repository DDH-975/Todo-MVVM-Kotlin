package com.project.todolistkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(val testData : List<String>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(
           R.layout.activity_todo_items,
           parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val data = testData
        holder.tvTodo.text = data[position]
    }

    override fun getItemCount(): Int = testData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTodo = itemView.findViewById<TextView>(R.id.tvTodo)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
    }

}