package com.hendaoui.tasktodo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.task.view.*
import kotlin.collections.ArrayList


class TasksAdapter(val items: ArrayList<TaskModel>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.task, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title?.text = items.get(position).title
        holder.createdAt?.text = items.get(position).createdAt
        holder.duration?.text = items.get(position).duration.toString() + " Days"
        // Initialize done checkbox
        holder.checkbox?.isChecked = items.get(position).isDone
        if (holder.checkbox?.isChecked!!) {
            holder.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.alpha = 0.4f
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("action", "display")
            intent.putExtra("taskID", position)
            context.startActivity(intent)
        }
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.itemView.alpha = 0.4f
                changeTaskStatus(true, position)
            } else {
                holder.title.setPaintFlags(holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.itemView.alpha = 1f
                changeTaskStatus(false, position)
            }
        }
    }

    private fun changeTaskStatus(isDone: Boolean, position: Int) {
        val currentTask = TaskListActivity.tasksList.get(position)
        TaskListActivity.tasksList.set(
            position, TaskModel(
                currentTask.title,
                currentTask.description,
                currentTask.duration,
                currentTask.createdAt,
                isDone // reActivate task after update
            )
        )
        // Save Changes in SharedPrefs
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                TaskListActivity.sharedPrefFile,
                AppCompatActivity.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(TaskListActivity.tasksList)
        editor.putString("task list", json)
        editor.apply()
        editor.commit()
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val checkbox = view.checkBox
    val title = view.Title
    val createdAt = view.createdAt
    val duration = view.duration
}