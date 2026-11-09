package com.example.telutask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.Task068
import com.example.telutask.data.entity.User068
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskOwnership077Test {
    @Test
    fun editCannotChangeAnotherOwnersTask077() = runBlocking {
        val context077 = ApplicationProvider.getApplicationContext<Context>()
        val database077 = Room.inMemoryDatabaseBuilder(context077, TeluTaskDatabase068::class.java).build()
        try {
            val users077 = database077.userDao068()
            val tasks077 = database077.taskDao068()
            val owner077 = users077.insertUser068(User068(username068 = "owner077", password068 = "123456")).toInt()
            val other077 = users077.insertUser068(User068(username068 = "other077", password068 = "123456")).toInt()
            val id077 = tasks077.insertTask068(Task068(userId068 = owner077, namaTugas068 = "Awal", prioritas068 = "High", deadline068 = 1000L, isDone068 = true)).toInt()
            assertNull(tasks077.getTaskById068(id077, other077))
            assertEquals(0, tasks077.updateTask068(id077, other077, "Terlarang", "Low", 2000L))
            assertEquals("Awal", tasks077.getTaskById068(id077, owner077)?.namaTugas068)
            assertEquals(1, tasks077.updateTask068(id077, owner077, "Revisi", "Medium", 3000L))
            val updated077 = requireNotNull(tasks077.getTaskById068(id077, owner077))
            assertEquals("Revisi", updated077.namaTugas068)
            assertEquals("Medium", updated077.prioritas068)
            assertEquals(3000L, updated077.deadline068)
            assertEquals(owner077, updated077.userId068)
            assertTrue(updated077.isDone068)
            tasks077.deleteTask068(id077, owner077)
            assertEquals(0, tasks077.updateTask068(id077, owner077, "Hilang", "Low", 4000L))
        } finally {
            database077.close()
        }
    }
}
