package com.example.telutask.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.telutask.databinding.ActivityEditTask077Binding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskActivity077 : TaskScreen077() {
    private lateinit var binding077: ActivityEditTask077Binding
    private val priorities077 = arrayOf("High", "Medium", "Low")
    private var deadline077: Long? = null
    private var loaded077 = false
    private var saving077 = false
    private var restored077: Bundle? = null

    override fun onCreate(savedInstanceState077: Bundle?) {
        super.onCreate(savedInstanceState077)
        binding077 = ActivityEditTask077Binding.inflate(layoutInflater)
        setContentView(binding077.root)
        applyInsets077(binding077.root)
        restored077 = savedInstanceState077
        binding077.spinnerPriority077.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities077)
        binding077.btnCancel077.setOnClickListener { finish() }
        binding077.btnDeadline077.setOnClickListener { pickDate077() }
        binding077.btnSave077.setOnClickListener { save077() }
        binding077.btnRetry077.setOnClickListener { load077() }
    }

    override fun onResume() {
        super.onResume()
        if (requireOwner077() != null && !loaded077) load077()
    }

    override fun onSaveInstanceState(outState077: Bundle) {
        if (loaded077) {
            outState077.putString("name077", binding077.etName077.text.toString())
            outState077.putInt("priority077", binding077.spinnerPriority077.selectedItemPosition)
            deadline077?.let { value077 -> outState077.putLong("deadline077", value077) }
        }
        super.onSaveInstanceState(outState077)
    }

    private fun load077() {
        val userId077 = requireOwner077() ?: return
        binding077.form077.visibility = View.INVISIBLE
        binding077.btnRetry077.visibility = View.GONE
        if (taskId077 <= 0) { missingTask077(); return }
        lifecycleScope.launch {
            try {
                val task077 = dao077.getTaskById068(taskId077, userId077)
                if (requireOwner077() == null) return@launch
                if (task077 == null) { missingTask077(); return@launch }
                val draft077 = restored077
                binding077.etName077.setText(draft077?.getString("name077") ?: task077.namaTugas068)
                binding077.spinnerPriority077.setSelection(
                    if (draft077?.containsKey("priority077") == true) draft077.getInt("priority077").coerceIn(0, 2)
                    else priorities077.indexOf(task077.prioritas068).coerceAtLeast(0)
                )
                deadline077 = if (draft077?.containsKey("deadline077") == true) draft077.getLong("deadline077") else task077.deadline068
                renderDate077()
                restored077 = null
                loaded077 = true
                binding077.form077.visibility = View.VISIBLE
            } catch (error077: CancellationException) { throw error077
            } catch (error077: Exception) {
                message077("Gagal memuat tugas. Silakan coba lagi.")
                binding077.btnRetry077.visibility = View.VISIBLE
            }
        }
    }

    private fun pickDate077() {
        val calendar077 = Calendar.getInstance().apply { deadline077?.let { value077 -> timeInMillis = value077 } }
        DatePickerDialog(this, { _, year077, month077, day077 ->
            calendar077.set(year077, month077, day077, 0, 0, 0)
            calendar077.set(Calendar.MILLISECOND, 0)
            deadline077 = calendar077.timeInMillis
            renderDate077()
        }, calendar077.get(Calendar.YEAR), calendar077.get(Calendar.MONTH), calendar077.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun renderDate077() {
        binding077.btnDeadline077.text = deadline077?.let { value077 ->
            SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(value077)
        } ?: getString(com.example.telutask.R.string.choose_date077)
    }

    private fun save077() {
        if (!loaded077 || saving077) return
        val userId077 = requireOwner077() ?: return
        val name077 = binding077.etName077.text.toString().trim()
        if (name077.isEmpty()) {
            binding077.etName077.error = "Nama tugas wajib diisi"
            binding077.etName077.requestFocus()
            return
        }
        val priority077 = binding077.spinnerPriority077.selectedItem?.toString()
        if (priority077 !in priorities077) { message077("Prioritas tidak valid"); return }
        val date077 = deadline077 ?: run { message077("Deadline wajib dipilih"); return }
        saving077 = true
        binding077.btnSave077.isEnabled = false
        lifecycleScope.launch {
            try {
                val rows077 = dao077.updateTask068(taskId077, userId077, name077, requireNotNull(priority077), date077)
                if (requireOwner077() == null) return@launch
                if (rows077 == 0) { missingTask077(); return@launch }
                message077("Perubahan berhasil disimpan")
                setResult(RESULT_OK)
                finish()
            } catch (error077: CancellationException) { throw error077
            } catch (error077: Exception) {
                message077("Gagal menyimpan perubahan. Silakan coba lagi.")
            } finally {
                saving077 = false
                binding077.btnSave077.isEnabled = true
            }
        }
    }
}
