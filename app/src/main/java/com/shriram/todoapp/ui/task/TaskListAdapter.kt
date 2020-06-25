package com.shriram.todoapp.ui.task

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.shriram.todoapp.R
import com.shriram.todoapp.data.db.Task
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*

class TaskListAdapter(interactionListener: InteractionListener) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>(), Filterable {

    private var taskList: List<Task> = arrayListOf()
    private var filteredTaskList: List<Task> = arrayListOf()
    private val listener: InteractionListener = interactionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredTaskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredTaskList[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, listener: InteractionListener) {
            val color =
                if (task.completed) {
                    Color.parseColor("#D3D3D3")
                } else {
                    Color.WHITE
                }
            itemView.setBackgroundColor(color)
            itemView.tv_item_title.text = task.title
            itemView.tv_item_content.text = task.description
            itemView.cb_done.isChecked = task.completed
            itemView.cb_done.setOnClickListener {
                task.completed = itemView.cb_done.isChecked
                listener.onTaskCompletionStatusChanged(task)
            }

            itemView.setOnClickListener {
                listener.onTaskItemClick(task)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredTaskList = if (charString.isEmpty()) {
                    taskList
                } else {
                    val filteredTaskList = arrayListOf<Task>()
                    for (task in taskList) {
                        if (task.title.toLowerCase(Locale.getDefault())
                                .contains(charString.toLowerCase(Locale.getDefault()))
                            || task.description.contains(charString.toLowerCase(Locale.getDefault()))
                        ) {
                            filteredTaskList.add(task)
                        }
                    }
                    filteredTaskList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredTaskList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredTaskList = p1?.values as List<Task>
                notifyDataSetChanged()
            }

        }
    }

    fun setTasks(tasks: List<Task>) {
        this.taskList = tasks
        this.filteredTaskList = tasks
        notifyDataSetChanged()
    }

    interface InteractionListener {
        fun onTaskCompletionStatusChanged(task: Task)
        fun onTaskItemClick(task: Task)
    }
}
