package com.example.telutask

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.telutask.auth.SessionManager048
import com.example.telutask.dashboard.DashboardActivity072
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.User068
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState072: Bundle?) {
        super.onCreate(savedInstanceState072)

        val sessionManager072 = SessionManager048(this)
        val existingUserId072 = sessionManager072.getUserId048()

        if (existingUserId072 == null) {
            val database072 = TeluTaskDatabase068.getInstance068(this)
            lifecycleScope.launch(Dispatchers.IO) {
                val userDao072 = database072.userDao068()
                val existingUser072 = userDao072.getUserByUsername068("FaizUser072")
                val userId072 = if (existingUser072 != null) {
                    existingUser072.id068
                } else {
                    userDao072.insertUser068(
                        User068(username068 = "FaizUser072", password068 = "123456")
                    ).toInt()
                }

                sessionManager072.saveUserId048(userId072)

                withContext(Dispatchers.Main) {
                    openDashboard072()
                }
            }
        } else {
            openDashboard072()
        }
    }

    private fun openDashboard072() {
        val intent072 = Intent(this, DashboardActivity072::class.java)
        startActivity(intent072)
        finish()
    }
}
