package com.shriram.todoapp.ui.task

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shriram.todoapp.R
import com.shriram.todoapp.data.db.Task
import com.shriram.todoapp.utils.Constants
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity(), TaskListAdapter.InteractionListener {

    private lateinit var todoViewModel: TaskViewModel
    private lateinit var searchView: SearchView
    private lateinit var taskAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        //Setting up RecyclerView
        rvToDoList.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskListAdapter(this)
        rvToDoList.adapter = taskAdapter

        //Setting up ViewModel and LiveData
        todoViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        todoViewModel.getTaskList().observe(this, Observer {
            taskAdapter.setTasks(it)
        })
    }

    override fun onTaskCompletionStatusChanged(task: Task) {
        todoViewModel.updateTask(task)
    }

    override fun onTaskItemClick(task: Task) {
        resetSearchView()
        val intent = Intent(this@TaskListActivity, CreateTaskActivity::class.java)
        intent.putExtra(Constants.TASK, task)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search_todo)
                ?.actionView as SearchView
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                taskAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                taskAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_todo -> true
            R.id.add_todo -> {
                resetSearchView()
                val intent = Intent(this@TaskListActivity, CreateTaskActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        resetSearchView()
        super.onBackPressed()
    }

    private fun resetSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
    }
}
