package com.project.mvvmkotlin.todoRecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.mvvmkotlin.R

class Adapter : RecyclerView.Adapter<Adapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.Viewholder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_todo_item,
                parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Adapter.Viewholder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo = itemView.findViewById<TextView>(R.id.tvTodo)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
    }
}