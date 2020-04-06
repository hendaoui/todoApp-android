package com.hendaoui.tasktodo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class TaskListActivity : AppCompatActivity() {
    private val sharedPrefFile = "TasksSharedPreference"

    companion object {
        var tasksList: ArrayList<TaskModel> = ArrayList()
        val sharedPrefFile = "TasksSharedPreference"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Remove Shadow
        supportActionBar?.elevation = 0f
        // Set Activity's Title
        supportActionBar?.title = "Tasks To Do"

        loadTasks()

        // RecyclerView Config
        rv_tasks.layoutManager = LinearLayoutManager(this)
        rv_tasks.adapter = TasksAdapter(tasksList, this)

        //
        fab.setOnClickListener { view ->
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("action", "add")
            }
            startActivity(intent)
        }
    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", null)
        val type = object : TypeToken<ArrayList<TaskModel>>() {}.type
        if (json !== null) {
            tasksList = gson.fromJson(json, type)
        }
        if (tasksList == null) {
            tasksList = ArrayList()
        }
    }
}
