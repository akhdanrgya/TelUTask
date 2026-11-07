package com.example.telutask.task

import android.content.Intent
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.database.TeluTaskDatabase068

/** Shared session guard; the owner is never read from Intent extras. */
abstract class TaskScreen077 : ComponentActivity() {
    protected val session077 by lazy { SessionManager048(this) }
    protected val dao077 by lazy { TeluTaskDatabase068.getInstance068(this).taskDao068() }
    protected var ownerId077: Int? = null
    protected val taskId077: Int get() = intent.getIntExtra(TaskDetailActivity077.EXTRA_TASK_ID077, -1)

    override fun onCreate(savedInstanceState077: Bundle?) {
        super.onCreate(savedInstanceState077)
        ownerId077 = if (savedInstanceState077?.containsKey("owner077") == true) {
            savedInstanceState077.getInt("owner077").takeIf { id077 -> id077 > 0 }
        } else session077.getUserId048()
    }

    override fun onSaveInstanceState(outState077: Bundle) {
        outState077.putInt("owner077", ownerId077 ?: -1)
        super.onSaveInstanceState(outState077)
    }

    protected fun requireOwner077(): Int? {
        val current077 = session077.getUserId048()
        if (current077 == null || current077 <= 0 || current077 != ownerId077) {
            message077("Sesi berubah atau berakhir. Silakan masuk kembali.")
            navigate077("com.example.telutask.auth.LoginActivity048", true)
            return null
        }
        return current077
    }

    protected fun missingTask077() {
        message077("Tugas tidak ditemukan atau bukan milik akun aktif.")
        navigate077("com.example.telutask.dashboard.DashboardActivity072", false)
    }

    protected fun navigate077(className077: String, clear077: Boolean) {
        val intent077 = Intent().setClassName(this, className077).apply {
            flags = if (clear077) Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            else Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        try {
            startActivity(intent077)
            finish()
        } catch (error077: ActivityNotFoundException) {
            // Login/Dashboard are owned by other members and may not be merged yet.
            if (clear077) finishAffinity() else finish()
        }
    }

    protected fun applyInsets077(view077: android.view.View) {
        val left077 = view077.paddingLeft
        val top077 = view077.paddingTop
        val right077 = view077.paddingRight
        val bottom077 = view077.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(view077) { target077, insets077 ->
            val bars077 = insets077.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            target077.setPadding(left077 + bars077.left, top077 + bars077.top, right077 + bars077.right, bottom077 + bars077.bottom)
            insets077
        }
        ViewCompat.requestApplyInsets(view077)
    }

    protected fun message077(text077: String) = Toast.makeText(this, text077, Toast.LENGTH_SHORT).show()
}

/** Marta implements this on the Application, delegating to DeleteTaskDialog069.
 * The dialog must recheck the session before its scoped DAO delete and call onDeleted077
 * only after a successful deletion. No Activity reference may be retained after dismissal.
 */
interface DeleteTaskHost077 {
    fun showDeleteTaskDialog077(activity077: ComponentActivity, taskId077: Int, userId077: Int, onDeleted077: () -> Unit)
}
