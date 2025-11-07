package com.project.todolistkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.todolistkotlin.roomDB.TodoEntity
import com.project.todolistkotlin.roomDB.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val repo: TodoRepository) : ViewModel() {
    val allData: LiveData<List<TodoEntity>> = repo.allData

    fun insertData(tododata: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(tododata)
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteDataById(id)
        }
    }

}

class TodoViewModelFactory(private val repo: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}