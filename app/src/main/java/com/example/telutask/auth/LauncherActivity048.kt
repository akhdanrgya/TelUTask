package com.example.telutask.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.telutask.dashboard.DashboardActivity072
import com.example.telutask.databinding.ActivityLauncher048Binding

class LauncherActivity048 : AppCompatActivity() {

    private lateinit var binding048: ActivityLauncher048Binding
    private lateinit var sessionManager048: SessionManager048

    override fun onCreate(savedInstanceState048: Bundle?) {
        super.onCreate(savedInstanceState048)
        binding048 = ActivityLauncher048Binding.inflate(layoutInflater)
        setContentView(binding048.root)

        sessionManager048 = SessionManager048(this)
        checkSession048()
    }

    private fun checkSession048() {
        val userId048 = sessionManager048.getUserId048()
        if (userId048 != null && userId048 > 0) {
            val intent048 = Intent(this, DashboardActivity072::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent048)
        } else {
            val intent048 = Intent(this, LoginActivity048::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent048)
        }
        finish()
    }
}
