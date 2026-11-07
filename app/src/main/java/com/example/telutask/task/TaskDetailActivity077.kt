package com.example.telutask.task

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.telutask.databinding.ActivityTaskDetail077Binding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDetailActivity077 : TaskScreen077() {
    private lateinit var binding077: ActivityTaskDetail077Binding

    override fun onCreate(savedInstanceState077: Bundle?) {
        super.onCreate(savedInstanceState077)
        binding077 = ActivityTaskDetail077Binding.inflate(layoutInflater)
        setContentView(binding077.root)
        applyInsets077(binding077.root)
        binding077.btnBack077.setOnClickListener { finish() }
        binding077.btnEdit077.setOnClickListener {
            requireOwner077() ?: return@setOnClickListener
            startActivity(Intent(this, EditTaskActivity077::class.java).putExtra(EXTRA_TASK_ID077, taskId077))
        }
        binding077.btnDelete077.setOnClickListener {
            val userId077 = requireOwner077() ?: return@setOnClickListener
            (application as? DeleteTaskHost077)?.showDeleteTaskDialog077(this, taskId077, userId077) {
                setResult(RESULT_OK)
                navigate077("com.example.telutask.dashboard.DashboardActivity072", false)
            }
        }
        binding077.btnRetry077.setOnClickListener { loadTask077() }
    }

    override fun onResume() {
        super.onResume()
        loadTask077()
    }

    override fun onPause() {
        binding077.taskContent077.visibility = View.INVISIBLE
        super.onPause()
    }

    private fun loadTask077() {
        binding077.taskContent077.visibility = View.INVISIBLE
        binding077.btnRetry077.visibility = View.GONE
        val userId077 = requireOwner077() ?: return
        if (taskId077 <= 0) { missingTask077(); return }
        lifecycleScope.launch {
            try {
                val task077 = dao077.getTaskById068(taskId077, userId077)
                if (requireOwner077() == null) return@launch
                if (task077 == null) { missingTask077(); return@launch }
                binding077.tvName077.text = task077.namaTugas068
                binding077.tvPriority077.text = task077.prioritas068
                binding077.tvDeadline077.text = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(task077.deadline068)
                binding077.tvStatus077.setText(if (task077.isDone068) com.example.telutask.R.string.done077 else com.example.telutask.R.string.pending077)
                binding077.btnDelete077.isEnabled = application is DeleteTaskHost077
                binding077.tvDeleteIntegration077.visibility = if (application is DeleteTaskHost077) View.GONE else View.VISIBLE
                binding077.taskContent077.visibility = View.VISIBLE
            } catch (error077: CancellationException) {
                throw error077
            } catch (error077: Exception) {
                message077("Gagal memuat tugas. Silakan coba lagi.")
                binding077.btnRetry077.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val EXTRA_TASK_ID077 = "extra_task_id"
    }
}
