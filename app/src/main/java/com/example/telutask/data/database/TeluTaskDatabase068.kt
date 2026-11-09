package com.example.telutask.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.telutask.data.dao.TaskDao068
import com.example.telutask.data.dao.UserDao068
import com.example.telutask.data.entity.Task068
import com.example.telutask.data.entity.User068

@Database(
    entities = [User068::class, Task068::class],
    version = 1,
    exportSchema = false
)
abstract class TeluTaskDatabase068 : RoomDatabase() {

    abstract fun userDao068(): UserDao068
    abstract fun taskDao068(): TaskDao068

    companion object {
        @Volatile
        private var INSTANCE068: TeluTaskDatabase068? = null

        fun getInstance068(context068: Context): TeluTaskDatabase068 {
            return INSTANCE068 ?: synchronized(this) {
                val instance068 = Room.databaseBuilder(
                    context068.applicationContext,
                    TeluTaskDatabase068::class.java,
                    "telutask_database_068"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE068 = instance068
                instance068
            }
        }
    }
}
