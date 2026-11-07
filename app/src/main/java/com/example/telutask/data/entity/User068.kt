package com.example.telutask.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users068",
    indices = [Index(value = ["username"], unique = true)]
)
data class User068(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id068: Int = 0,

    @ColumnInfo(name = "username")
    val username068: String,

    @ColumnInfo(name = "password")
    val password068: String
)
