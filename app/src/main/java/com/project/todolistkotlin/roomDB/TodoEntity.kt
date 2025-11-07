package com.project.todolistkotlin.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TodoData")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var todo: String
)
