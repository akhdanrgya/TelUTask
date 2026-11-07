package com.example.telutask.task

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
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

    private var selectedDeadlineEpoch075: Long? = null
    private val prioritasOptions075 = arrayOf("High", "Medium", "Low")

    override fun onCreate(savedInstanceState075: Bundle?) {
        super.onCreate(savedInstanceState075)
        binding075 = ActivityAddTask075Binding.inflate(layoutInflater)
        setContentView(binding075.root)

        sessionManager075 = SessionManager048(this)
        database075 = TeluTaskDatabase068.getInstance068(this)

        setupSpinner075()
        setupListeners075()
    }

    private fun setupSpinner075() {
        val adapter075 = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            prioritasOptions075
        )
        adapter075.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
                binding075.tvSelectedDeadline075.setTextColor(getColor(R.color.black))
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
        val userId075 = sessionManager075.getUserId048()
        if (userId075 == null) {
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

        lifecycleScope.launch(Dispatchers.IO) {
            database075.taskDao068().insertTask068(newTask075)
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
