package com.example.telutask.dashboard

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telutask.R
import com.example.telutask.data.entity.Task068
import com.example.telutask.databinding.ItemTask072Binding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter072(
    private var taskList072: List<Task068>,
    private val onTaskClick072: (Task068) -> Unit,
    private val onDoneChanged072: (Task068, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter072.TaskViewHolder072>() {

    fun updateTasks072(newTasks072: List<Task068>) {
        taskList072 = newTasks072
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent072: ViewGroup, viewType072: Int): TaskViewHolder072 {
        val binding072 = ItemTask072Binding.inflate(
            LayoutInflater.from(parent072.context),
            parent072,
            false
        )
        return TaskViewHolder072(binding072)
    }

    override fun onBindViewHolder(holder072: TaskViewHolder072, position072: Int) {
        val task072 = taskList072[position072]
        holder072.binding072.apply {
            tvNamaTugas072.text = task072.namaTugas068

            val sdf072 = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
            val prefix072 = root.context.getString(R.string.deadline_prefix072)
            tvDeadline072.text = "$prefix072${sdf072.format(task072.deadline068)}"

            tvPrioritas072.text = task072.prioritas068
            when (task072.prioritas068) {
                "High" -> {
                    tvPrioritas072.setTextColor(root.context.getColor(R.color.priority_high_069))
                }
                "Medium" -> {
                    tvPrioritas072.setTextColor(root.context.getColor(R.color.priority_medium_069))
                }
                "Low" -> {
                    tvPrioritas072.setTextColor(root.context.getColor(R.color.priority_low_069))
                }
                else -> {
                    tvPrioritas072.setTextColor(root.context.getColor(R.color.gray_text_069))
                }
            }

            if (task072.isDone068) {
                tvNamaTugas072.paintFlags = tvNamaTugas072.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvNamaTugas072.paintFlags = tvNamaTugas072.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            cbIsDone072.setOnCheckedChangeListener(null)
            cbIsDone072.isChecked = task072.isDone068
            cbIsDone072.contentDescription = task072.namaTugas068
            cbIsDone072.setOnCheckedChangeListener { _, isChecked072 ->
                onDoneChanged072(task072, isChecked072)
            }

            root.setOnClickListener {
                onTaskClick072(task072)
            }
        }
    }

    override fun getItemCount(): Int = taskList072.size

    class TaskViewHolder072(val binding072: ItemTask072Binding) :
        RecyclerView.ViewHolder(binding072.root)
}
