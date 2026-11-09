package com.example.telutask.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.telutask.data.entity.User068

@Dao
interface UserDao068 {
    @Insert
    suspend fun insertUser068(user068: User068): Long

    @Query("SELECT * FROM users068 WHERE username = :username068 COLLATE NOCASE LIMIT 1")
    suspend fun getUserByUsername068(username068: String): User068?

    @Query("SELECT * FROM users068 WHERE id = :userId068 LIMIT 1")
    suspend fun getUserById068(userId068: Int): User068?
}
