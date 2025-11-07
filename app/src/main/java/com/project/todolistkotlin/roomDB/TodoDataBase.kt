package com.project.todolistkotlin.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDataBase : RoomDatabase() {
    abstract fun dao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDataBase? = null

        fun getInstance(context: Context): TodoDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDataBase::class.java,
                    "app_db"
                ).build().also { INSTANCE = it }
            }
    }
}
