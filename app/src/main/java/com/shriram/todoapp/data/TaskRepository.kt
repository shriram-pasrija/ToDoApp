package com.shriram.todoapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.shriram.todoapp.data.db.Task
import com.shriram.todoapp.data.db.TaskDao
import com.shriram.todoapp.data.db.TasksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskRepository(application: Application) {

    private val taskDao: TaskDao
    private val taskList: LiveData<List<Task>>

    init {
        val database = TasksDatabase.getInstance(application.applicationContext)
        taskDao = database!!.taskDao()
        taskList = taskDao.getTaskList()
    }

    fun saveTask(todo: Task) = runBlocking {
        this.launch(Dispatchers.IO) {
            taskDao.saveTask(todo)
        }
    }

    fun updateTask(todo: Task) = runBlocking {
        this.launch(Dispatchers.IO) {
            taskDao.updateTask(todo)
        }
    }


    fun deleteTask(todo: Task) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                taskDao.deleteTask(todo)
            }
        }
    }

    fun getTaskList(): LiveData<List<Task>> {
        return taskList
    }
}