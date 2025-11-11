package com.project.todolistkotlin.roomDB

import android.app.Application
import androidx.lifecycle.LiveData


class TodoRepository private constructor(private val dao: TodoDao) {
    val allData: LiveData<List<TodoEntity>> = dao.getAllData()

    suspend fun insertData(todoEntity: TodoEntity) {
        dao.setInsertTodo(todoEntity)
    }

    suspend fun deleteDataById(id: Int) {
        dao.deleteDataWhereId(id)
    }


    companion object {
        @Volatile
        private var INSTANCE: TodoRepository? = null
        fun getInstance(application: Application): TodoRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TodoRepository(
                    TodoDataBase.getInstance(application).dao()
                ).also { INSTANCE = it }
            }
    }
}


