package com.example.telutask

import android.app.Application
import androidx.activity.ComponentActivity
import com.example.telutask.task.DeleteTaskDialog069
import com.example.telutask.task.DeleteTaskHost077

/**
 * Application class utama TeluTask yang dikelola oleh Marta (069)
 * untuk mengintegrasikan DeleteTaskHost077 dengan DeleteTaskDialog069.
 */
class TeluTaskApplication069 : Application(), DeleteTaskHost077 {

    override fun showDeleteTaskDialog077(
        activity077: ComponentActivity,
        taskId077: Int,
        userId077: Int,
        onDeleted077: () -> Unit
    ) {
        val dialog069 = DeleteTaskDialog069()
        dialog069.showDeleteTaskDialog069(activity077, taskId077, userId077, onDeleted077)
    }
}
