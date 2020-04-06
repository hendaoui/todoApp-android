package com.hendaoui.tasktodo

data class TaskModel(
    val title: String,
    val description: String,
    val duration: Float,
    val createdAt: String,
    val isDone: Boolean
)