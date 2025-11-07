package com.project.todolistkotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.todolistkotlin.databinding.ActivityMainBinding
import com.project.todolistkotlin.roomDB.TodoEntity
import com.project.todolistkotlin.roomDB.TodoRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var managerLayout: LinearLayoutManager
    private lateinit var repo: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        repo = TodoRepository.getInstance(application)
        todoViewModel = ViewModelProvider(
            this,
            TodoViewModelFactory(repo)
        )[TodoViewModel::class.java]

        managerLayout = LinearLayoutManager(this)

        todoAdapter = TodoAdapter { id ->
            todoViewModel.deleteById(id)
        }


        binding.recyclerView.apply {
            layoutManager = managerLayout
            adapter = todoAdapter
        }

        todoViewModel.allData.observe(this) { todoData ->
            todoAdapter.setData(todoData)
        }



        binding.btnAdd.setOnClickListener {
            val text = binding.etTodo.text
            if (text.isEmpty()) {
                Toast.makeText(this, "할일을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                val entitiy = TodoEntity(todo = "$text")
                todoViewModel.insertData(entitiy)
            }
        }

    }
}