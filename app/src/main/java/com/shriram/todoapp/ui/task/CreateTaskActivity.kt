package com.shriram.todoapp.ui.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.shriram.todoapp.R
import com.shriram.todoapp.data.db.Task
import com.shriram.todoapp.utils.Constants
import kotlinx.android.synthetic.main.activity_create_task.*

class CreateTaskActivity : AppCompatActivity() {

    private var taskToEdit: Task? = null
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        val intent = intent
        if (intent != null && intent.hasExtra(Constants.TASK)) {
            val task = intent.getParcelableExtra(Constants.TASK) as Task
            this.taskToEdit = task
            prePopulateData(task)
        }

        title =
            if (taskToEdit != null) getString(R.string.view_edit_task) else getString(R.string.create_task)

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            saveOrUpdateTask()
        }
    }

    private fun prePopulateData(task: Task) {
        et_task_title.setText(task.title)
        et_task_description.setText(task.description)
    }

    private fun saveOrUpdateTask() {
        if (validateFields()) {
            val task = Task(
                id = taskToEdit?.id,
                title = et_task_title.text.toString(),
                description = et_task_description.text.toString(),
                completed = taskToEdit?.completed ?: false
            )
            if (taskToEdit != null) {
                taskViewModel.updateTask(task)
            } else {
                taskViewModel.saveTask(task)
            }
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (et_task_title.text.isEmpty()) {
            til_task_title.error = getString(R.string.error_empty_title)
            et_task_title.requestFocus()
            return false
        }
        if (et_task_description.text.isEmpty()) {
            til_task_description.error = getString(R.string.error_empty_description)
            et_task_description.requestFocus()
            return false
        }
        return true
    }
}
