package com.example.telutask.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CancellationException
import com.example.telutask.auth.LoginActivity048
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.Task068
import com.example.telutask.databinding.ActivityAddTask075Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity075 : AppCompatActivity() {

    private lateinit var binding075: ActivityAddTask075Binding
    private lateinit var sessionManager075: SessionManager048
    private lateinit var database075: TeluTaskDatabase068

    private var saving069 = false
    private var owner069: Int? = null
    private var selectedDeadlineEpoch075: Long? = null
    private val prioritasOptions075 = arrayOf("High", "Medium", "Low")

    override fun onCreate(savedInstanceState075: Bundle?) {
        super.onCreate(savedInstanceState075)
        binding075 = ActivityAddTask075Binding.inflate(layoutInflater)
        setContentView(binding075.root)

        sessionManager075 = SessionManager048(this)
        database075 = TeluTaskDatabase068.getInstance068(this)

        owner069 = savedInstanceState075?.getInt("owner069") ?: sessionManager075.getUserId048()
        ViewCompat.setOnApplyWindowInsetsListener(binding075.root) { view069, insets069 ->
            val bars069 = insets069.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            view069.setPadding(bars069.left, bars069.top, bars069.right, bars069.bottom)
            insets069
        }
        selectedDeadlineEpoch075 = savedInstanceState075?.getLong("deadline069")?.takeIf { value069 -> value069 > 0 }
        selectedDeadlineEpoch075?.let { deadline069 -> binding075.tvSelectedDeadline075.text = formatDate075(deadline069) }
        setupSpinner075()
        setupListeners075()
    }

    override fun onSaveInstanceState(outState069: Bundle) {
        outState069.putLong("deadline069", selectedDeadlineEpoch075 ?: 0)
        outState069.putInt("owner069", owner069 ?: -1)
        super.onSaveInstanceState(outState069)
    }

    override fun onResume() {
        super.onResume()
        if (owner069 == null || owner069 != sessionManager075.getUserId048()) {
            startActivity(Intent(this, LoginActivity048::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
    }

    private fun setupSpinner075() {
        val adapter075 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            prioritasOptions075
        )
        adapter075.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding075.spinnerPrioritas075.adapter = adapter075
    }

    private fun setupListeners075() {
        binding075.btnSelectDeadline075.setOnClickListener {
            showDatePicker075()
        }

        binding075.btnSimpan075.setOnClickListener {
            if (validateForm075()) {
                saveTask075()
            }
        }

        binding075.btnBatal075.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker075() {
        val calendar075 = Calendar.getInstance()

        // Maintain previous selection if already picked
        selectedDeadlineEpoch075?.let { epoch075 ->
            calendar075.timeInMillis = epoch075
        }

        val year075 = calendar075.get(Calendar.YEAR)
        val month075 = calendar075.get(Calendar.MONTH)
        val day075 = calendar075.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog075 = DatePickerDialog(
            this,
            { _, selectedYear075, selectedMonth075, selectedDay075 ->
                val pickedCalendar075 = Calendar.getInstance().apply {
                    set(Calendar.YEAR, selectedYear075)
                    set(Calendar.MONTH, selectedMonth075)
                    set(Calendar.DAY_OF_MONTH, selectedDay075)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val epoch075 = pickedCalendar075.timeInMillis
                selectedDeadlineEpoch075 = epoch075
                binding075.tvSelectedDeadline075.text = formatDate075(epoch075)
                binding075.tvSelectedDeadline075.setTextColor(getColor(com.example.telutask.R.color.dark_text_069))
            },
            year075,
            month075,
            day075
        )
        datePickerDialog075.show()
    }

    private fun validateForm075(): Boolean {
        val namaTugas075 = binding075.etNamaTugas075.text.toString().trim()
        if (namaTugas075.isEmpty()) {
            binding075.tilNamaTugas075.error = "Nama tugas wajib diisi"
            binding075.etNamaTugas075.requestFocus()
            return false
        } else {
            binding075.tilNamaTugas075.error = null
        }

        if (selectedDeadlineEpoch075 == null) {
            Toast.makeText(this, "Deadline wajib dipilih", Toast.LENGTH_SHORT).show()
            return false
        }

        val prioritasSelected075 = binding075.spinnerPrioritas075.selectedItem?.toString()
        if (prioritasSelected075.isNullOrEmpty() || prioritasSelected075 !in prioritasOptions075) {
            Toast.makeText(this, "Prioritas tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveTask075() {
        if (saving069) return
        val userId075 = sessionManager075.getUserId048()
        if (userId075 == null || userId075 != owner069) {
            Toast.makeText(this, "Sesi pengguna tidak valid. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val namaTugas075 = binding075.etNamaTugas075.text.toString().trim()
        val prioritas075 = binding075.spinnerPrioritas075.selectedItem.toString()
        val deadline075 = selectedDeadlineEpoch075 ?: return

        val newTask075 = Task068(
            userId068 = userId075,
            namaTugas068 = namaTugas075,
            prioritas068 = prioritas075,
            deadline068 = deadline075,
            isDone068 = false
        )

        saving069 = true
        binding075.btnSimpan075.isEnabled = false
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                database075.taskDao068().insertTask068(newTask075)
            } catch (error069: CancellationException) {
                throw error069
            } catch (error069: Exception) {
                withContext(Dispatchers.Main) {
                    saving069 = false
                    binding075.btnSimpan075.isEnabled = true
                    Toast.makeText(this@AddTaskActivity075, "Gagal menyimpan tugas. Silakan coba lagi.", Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddTaskActivity075, "Tugas berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun formatDate075(epoch075: Long): String {
        val sdf075 = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        return sdf075.format(epoch075)
    }
}
