package com.example.telutask.task

import android.widget.Toast
import com.example.telutask.R
import kotlinx.coroutines.CancellationException
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.database.TeluTaskDatabase068
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Class dialog konfirmasi hapus tugas milik Marta (069).
 * Menampilkan AlertDialog konfirmasi, memeriksa sesi aktif, dan memanggil DAO
 * deleteTask068(taskId069, userId069) terikat user_id.
 */
class DeleteTaskDialog069 {

    fun showDeleteTaskDialog069(
        activity069: ComponentActivity,
        taskId069: Int,
        userId069: Int,
        onDeleted069: () -> Unit
    ) {
        val sessionManager069 = SessionManager048(activity069)
        val activeUserId069 = sessionManager069.getUserId048()

        if (activeUserId069 == null || activeUserId069 != userId069) {
            Toast.makeText(activity069, R.string.session_invalid_069, Toast.LENGTH_SHORT).show()
            return
        }

        val alertDialog069 = AlertDialog.Builder(activity069)
            .setTitle(R.string.delete_title_069)
            .setMessage(R.string.delete_message_069)
            .setPositiveButton(R.string.delete_confirm_069) { dialog069, _ ->
                dialog069.dismiss()
                performDeleteTask069(activity069, taskId069, userId069, onDeleted069)
            }
            .setNegativeButton(R.string.delete_cancel_069) { dialog069, _ ->
                dialog069.dismiss()
            }
            .create()

        alertDialog069.show()
    }

    private fun performDeleteTask069(
        activity069: ComponentActivity,
        taskId069: Int,
        userId069: Int,
        onDeleted069: () -> Unit
    ) {
        val sessionManager069 = SessionManager048(activity069)
        val currentUserId069 = sessionManager069.getUserId048()

        if (currentUserId069 == null || currentUserId069 != userId069) {
            Toast.makeText(activity069, R.string.session_invalid_069, Toast.LENGTH_SHORT).show()
            return
        }

        val database069 = TeluTaskDatabase068.getInstance068(activity069)
        val taskDao069 = database069.taskDao068()

        activity069.lifecycleScope.launch(Dispatchers.IO) {
            val rowsDeleted069 = try {
                taskDao069.deleteTask068(taskId069, currentUserId069)
            } catch (error069: CancellationException) {
                throw error069
            } catch (error069: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity069, R.string.delete_failed_069, Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            withContext(Dispatchers.Main) {
                if (rowsDeleted069 > 0) {
                    Toast.makeText(activity069, R.string.delete_success_069, Toast.LENGTH_SHORT).show()
                    onDeleted069()
                } else {
                    Toast.makeText(activity069, R.string.delete_failed_069, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
