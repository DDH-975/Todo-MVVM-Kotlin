package com.project.todolistkotlin.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoData")
    fun getAllData(): LiveData<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun setInsertTodo(todo: TodoEntity)

    @Query("DELETE FROM TodoData")
    fun deleteAllTodo()

    @Query("DELETE FROM TodoData WHERE id = :id")
    fun deleteDataWhereId(id: Int)
}