package com.project.todolistkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.todolistkotlin.roomDB.TodoEntity

class TodoAdapter(private var listener: OndeleteClickListener) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    private var todoData: List<TodoEntity> = emptyList()

    fun setData(data: List<TodoEntity>) {
        this.todoData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_todo_items,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val pos = holder.bindingAdapterPosition
        if (pos != RecyclerView.NO_POSITION) {
            holder.tvTodo.text = todoData.get(pos).todo

            holder.btnDelete.setOnClickListener { it ->
                listener.deleteClick(todoData.get(pos).id)
            }
        }
    }

    override fun getItemCount(): Int = todoData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo = itemView.findViewById<TextView>(R.id.tvTodo)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
    }

}