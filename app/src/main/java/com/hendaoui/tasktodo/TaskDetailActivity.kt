package com.hendaoui.tasktodo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_task_detail.*
import java.text.SimpleDateFormat
import java.util.*


class TaskDetailActivity : AppCompatActivity() {

    private var action = "";
    private var taskPosition = -1;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        // Remove Shadow
        supportActionBar?.elevation = 0f

        action = intent.getStringExtra("action")
        taskPosition = intent.getIntExtra("taskID", -1)

        // Set Activity's Title
        when (action) {
            "add" -> supportActionBar?.title = "Create a New Task"
            "display" -> {
                supportActionBar?.title = "Task Details"
                retrieveTask(taskPosition)
            }
        }

        // Handle Save Button Click
        submit?.setOnClickListener {
            if (validateForm()) {
                when (action) {
                    "add" -> addTask()
                    "display" -> editTask()
                }
            }
        }

    }

    // getSelected Task and Fill Form
    private fun retrieveTask(taskPosition: Int) {
        titleField.setText(TaskListActivity.tasksList.get(taskPosition).title)
        durationField.setText(TaskListActivity.tasksList.get(taskPosition).duration.toString())
        descriptionField.setText(TaskListActivity.tasksList.get(taskPosition).description)
    }

    // Validate Form
    private fun validateForm(): Boolean {
        if (!titleField.text?.isNotEmpty()!!) {
            Toast.makeText(this, "Title shouldn't be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!durationField.text?.isNotEmpty()!!) {
            Toast.makeText(this, "Duration shouldn't be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!descriptionField.text?.isNotEmpty()!!) {
            Toast.makeText(this, "Description shouldn't be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTask() {
        val sdf = SimpleDateFormat("dd/M/yyyy kk:mm")
        val currentDate = sdf.format(Date())
        TaskListActivity.tasksList.add(
            TaskModel(
                titleField.text.toString(),
                descriptionField.text.toString(),
                durationField.text.toString().toFloat(),
                currentDate,
                false
            )
        )
        saveToSharedPrefs()
    }

    private fun editTask() {
        val currentTask = TaskListActivity.tasksList.get(taskPosition)
        val sdf = SimpleDateFormat("dd/M/yyyy kk:mm")
        val currentDate = sdf.format(Date())
        TaskListActivity.tasksList.set(
            taskPosition, TaskModel(
                titleField.text.toString(),
                descriptionField.text.toString(),
                durationField.text.toString().toFloat(),
                currentDate, // update Date
                false // reActivate task after update
            )
        )
        saveToSharedPrefs()
    }

    private fun saveToSharedPrefs() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(TaskListActivity.sharedPrefFile, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(TaskListActivity.tasksList)
        editor.putString("task list", json)
        editor.apply()
        editor.commit()
        val msg = if (action.equals("add")) "Saved!" else "Updated!";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // Back to MainScreen
        startActivity(Intent(this, TaskListActivity::class.java))
    }
}