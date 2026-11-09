package com.example.telutask.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks068",
    foreignKeys = [
        ForeignKey(
            entity = User068::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class Task068(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id068: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId068: Int,

    @ColumnInfo(name = "nama_tugas")
    val namaTugas068: String,

    @ColumnInfo(name = "prioritas")
    val prioritas068: String,

    @ColumnInfo(name = "deadline")
    val deadline068: Long,

    @ColumnInfo(name = "is_done")
    val isDone068: Boolean = false
)
