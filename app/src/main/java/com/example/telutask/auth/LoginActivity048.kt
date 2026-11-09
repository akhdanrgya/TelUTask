package com.example.telutask.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.telutask.dashboard.DashboardActivity072
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.databinding.ActivityLogin048Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity048 : AppCompatActivity() {

    private lateinit var binding048: ActivityLogin048Binding
    private lateinit var sessionManager048: SessionManager048
    private lateinit var database048: TeluTaskDatabase068

    override fun onCreate(savedInstanceState048: Bundle?) {
        super.onCreate(savedInstanceState048)
        binding048 = ActivityLogin048Binding.inflate(layoutInflater)
        setContentView(binding048.root)

        applyInsets048(binding048.root)

        sessionManager048 = SessionManager048(this)
        database048 = TeluTaskDatabase068.getInstance068(this)

        setupListeners048()
    }

    private fun applyInsets048(view048: View) {
        val left048 = view048.paddingLeft
        val top048 = view048.paddingTop
        val right048 = view048.paddingRight
        val bottom048 = view048.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(view048) { target048, insets048 ->
            val bars048 = insets048.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            target048.setPadding(
                left048 + bars048.left,
                top048 + bars048.top,
                right048 + bars048.right,
                bottom048 + bars048.bottom
            )
            insets048
        }
    }

    private fun setupListeners048() {
        binding048.btnLogin048.setOnClickListener {
            performLogin048()
        }

        binding048.btnToRegister048.setOnClickListener {
            val intent048 = Intent(this, RegisterActivity048::class.java)
            startActivity(intent048)
            finish()
        }
    }

    private fun performLogin048() {
        val username048 = binding048.etUsername048.text?.toString()?.trim().orEmpty()
        val password048 = binding048.etPassword048.text?.toString()?.trim().orEmpty()

        if (username048.isEmpty() || password048.isEmpty()) {
            Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val userDao048 = database048.userDao068()
            val existingUser048 = userDao048.getUserByUsername068(username048)

            withContext(Dispatchers.Main) {
                if (existingUser048 != null && existingUser048.password068 == password048) {
                    sessionManager048.saveUserId048(existingUser048.id068)
                    Toast.makeText(this@LoginActivity048, "Login berhasil", Toast.LENGTH_SHORT).show()
                    val intent048 = Intent(this@LoginActivity048, DashboardActivity072::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent048)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity048, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
