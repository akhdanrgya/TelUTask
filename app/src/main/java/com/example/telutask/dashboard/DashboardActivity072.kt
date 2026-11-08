package com.example.telutask.dashboard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.telutask.R
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.Task068
import com.example.telutask.databinding.ActivityDashboard072Binding
import com.example.telutask.task.AddTaskActivity075
import com.example.telutask.task.TaskDetailActivity077
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity072 : AppCompatActivity() {

    private lateinit var binding072: ActivityDashboard072Binding
    private lateinit var sessionManager072: SessionManager048
    private lateinit var database072: TeluTaskDatabase068
    private lateinit var adapter072: TaskAdapter072
    private val taskList072: MutableList<Task068> = mutableListOf()
    private var isSortedByPriority072: Boolean = false

    override fun onCreate(savedInstanceState072: Bundle?) {
        super.onCreate(savedInstanceState072)
        binding072 = ActivityDashboard072Binding.inflate(layoutInflater)
        setContentView(binding072.root)

        applyInsets072(binding072.root)

        sessionManager072 = SessionManager048(this)
        database072 = TeluTaskDatabase068.getInstance068(this)

        setupRecyclerView072()
        setupListeners072()
    }

    override fun onResume() {
        super.onResume()
        checkSessionAndLoadTasks072()
    }

    private fun applyInsets072(view072: View) {
        val left072 = view072.paddingLeft
        val top072 = view072.paddingTop
        val right072 = view072.paddingRight
        val bottom072 = view072.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(view072) { target072, insets072 ->
            val bars072 = insets072.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime(),
            )
            target072.setPadding(
                left072 + bars072.left,
                top072 + bars072.top,
                right072 + bars072.right,
                bottom072 + bars072.bottom,
            )
            insets072
        }
    }

    private fun setupRecyclerView072() {
        adapter072 = TaskAdapter072(
            taskList072 = taskList072,
            onTaskClick072 = { task072 ->
                onTaskClicked072(task072)
            },
            onDoneChanged072 = { task072, isDone072 ->
                onDoneToggled072(task072, isDone072)
            },
        )
        binding072.rvTasks072.layoutManager = LinearLayoutManager(this)
        binding072.rvTasks072.adapter = adapter072
    }

    private fun setupListeners072() {
        binding072.fabAddTask072.setOnClickListener {
            val intent072 = Intent(this, AddTaskActivity075::class.java)
            startActivity(intent072)
        }

        binding072.btnSortPriority072.setOnClickListener {
            isSortedByPriority072 = !isSortedByPriority072
            updateSortButtonUI072()
            loadTasks072()
        }

        binding072.btnLogout072.setOnClickListener {
            logout072()
        }
    }

    private fun updateSortButtonUI072() {
        if (isSortedByPriority072) {
            binding072.btnSortPriority072.text = getString(R.string.sort_priority_active072)
        } else {
            binding072.btnSortPriority072.text = getString(R.string.sort_priority072)
        }
    }

    private fun checkSessionAndLoadTasks072() {
        val userId072 = sessionManager072.getUserId048()
        if (userId072 == null || userId072 <= 0) {
            Toast.makeText(this, "Sesi berakhir. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            logout072()
            return
        }
        loadTasks072()
    }

    private fun loadTasks072() {
        val userId072 = sessionManager072.getUserId048() ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            val tasksFromDb072 = if (isSortedByPriority072) {
                database072.taskDao068().getTasksSortedByPriority068(userId072)
            } else {
                database072.taskDao068().getTasksByUser068(userId072)
            }
            withContext(Dispatchers.Main) {
                taskList072.clear()
                taskList072.addAll(tasksFromDb072)
                adapter072.updateTasks072(taskList072)
                if (taskList072.isEmpty()) {
                    binding072.rvTasks072.visibility = View.GONE
                    binding072.layoutEmptyState072.visibility = View.VISIBLE
                } else {
                    binding072.rvTasks072.visibility = View.VISIBLE
                    binding072.layoutEmptyState072.visibility = View.GONE
                }
            }
        }
    }

    private fun onDoneToggled072(task072: Task068, isDone072: Boolean) {
        val userId072 = sessionManager072.getUserId048() ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            database072.taskDao068().updateDone068(task072.id068, userId072, isDone072)
            loadTasks072()
        }
    }

    private fun onTaskClicked072(task072: Task068) {
        val intent072 = Intent(this, TaskDetailActivity077::class.java).apply {
            putExtra(TaskDetailActivity077.EXTRA_TASK_ID077, task072.id068)
        }
        startActivity(intent072)
    }

    private fun logout072() {
        sessionManager072.clearSession048()
        val intent072 = Intent().apply {
            setClassName(this@DashboardActivity072, "com.example.telutask.auth.LoginActivity048")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        try {
            startActivity(intent072)
        } catch (_: ActivityNotFoundException) {
            finish()
        }
    }
}
