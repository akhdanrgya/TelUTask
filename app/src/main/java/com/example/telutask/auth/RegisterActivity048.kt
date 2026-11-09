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
import com.example.telutask.data.entity.User068
import com.example.telutask.databinding.ActivityRegister048Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity048 : AppCompatActivity() {

    private lateinit var binding048: ActivityRegister048Binding
    private lateinit var sessionManager048: SessionManager048
    private lateinit var database048: TeluTaskDatabase068

    override fun onCreate(savedInstanceState048: Bundle?) {
        super.onCreate(savedInstanceState048)
        binding048 = ActivityRegister048Binding.inflate(layoutInflater)
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
        binding048.btnRegister048.setOnClickListener {
            performRegister048()
        }

        binding048.btnToLogin048.setOnClickListener {
            val intent048 = Intent(this, LoginActivity048::class.java)
            startActivity(intent048)
            finish()
        }
    }

    private fun performRegister048() {
        val username048 = binding048.etUsername048.text?.toString()?.trim().orEmpty()
        val password048 = binding048.etPassword048.text?.toString()?.trim().orEmpty()
        val confirmPassword048 = binding048.etConfirmPassword048.text?.toString()?.trim().orEmpty()

        if (username048.isEmpty()) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (password048.isEmpty() || password048.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (password048 != confirmPassword048) {
            Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val userDao048 = database048.userDao068()
            val existingUser048 = userDao048.getUserByUsername068(username048)

            if (existingUser048 != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RegisterActivity048,
                        "Username sudah terdaftar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            val newUser048 = User068(
                username068 = username048,
                password068 = password048
            )

            val newUserId048 = userDao048.insertUser068(newUser048).toInt()
            sessionManager048.saveUserId048(newUserId048)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterActivity048, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                val intent048 = Intent(this@RegisterActivity048, DashboardActivity072::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent048)
                finish()
            }
        }
    }
}
