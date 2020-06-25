package com.shriram.todoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: TasksDatabase? = null

        fun getInstance(context: Context): TasksDatabase? {
            if (INSTANCE == null) {
                synchronized(TasksDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                            TasksDatabase::class.java,
                            "tasks_db")
                            .build()
                }
            }
            return INSTANCE
        }
    }
}
