package com.shriram.todoapp.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shriram.todoapp.data.TaskRepository
import com.shriram.todoapp.data.db.Task

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository = TaskRepository(application)
    private val taskList: LiveData<List<Task>> = repository.getTaskList()

    fun saveTask(task: Task) {
        repository.saveTask(task)
    }

    fun updateTask(task: Task){
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

    fun getTaskList(): LiveData<List<Task>> {
        return taskList
    }
}
