package com.example.telutask.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.telutask.data.entity.Task068

@Dao
interface TaskDao068 {
    @Insert
    suspend fun insertTask068(task068: Task068): Long

    @Query("SELECT * FROM tasks068 WHERE user_id = :userId068 ORDER BY id DESC")
    suspend fun getTasksByUser068(userId068: Int): List<Task068>

    @Query("SELECT * FROM tasks068 WHERE id = :taskId068 AND user_id = :userId068 LIMIT 1")
    suspend fun getTaskById068(taskId068: Int, userId068: Int): Task068?

    @Query("""UPDATE tasks068 SET nama_tugas = :namaTugas068,
        prioritas = :prioritas068, deadline = :deadline068
        WHERE id = :taskId068 AND user_id = :userId068""")
    suspend fun updateTask068(
        taskId068: Int,
        userId068: Int,
        namaTugas068: String,
        prioritas068: String,
        deadline068: Long
    ): Int

    @Query("UPDATE tasks068 SET is_done = :isDone068 WHERE id = :taskId068 AND user_id = :userId068")
    suspend fun updateDone068(taskId068: Int, userId068: Int, isDone068: Boolean): Int

    @Query("DELETE FROM tasks068 WHERE id = :taskId068 AND user_id = :userId068")
    suspend fun deleteTask068(taskId068: Int, userId068: Int): Int

    @Query("""SELECT * FROM tasks068 WHERE user_id = :userId068
        ORDER BY CASE prioritas WHEN 'High' THEN 1
            WHEN 'Medium' THEN 2 WHEN 'Low' THEN 3 ELSE 4 END ASC, deadline ASC""")
    suspend fun getTasksSortedByPriority068(userId068: Int): List<Task068>
}
