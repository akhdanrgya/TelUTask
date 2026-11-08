package com.example.telutask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telutask.data.dao.TaskDao068
import com.example.telutask.data.dao.UserDao068
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.Task068
import com.example.telutask.data.entity.User068
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Dashboard072Test {

    private lateinit var db072: TeluTaskDatabase068
    private lateinit var userDao072: UserDao068
    private lateinit var taskDao072: TaskDao068

    @Before
    fun createDb072() {
        val context072 = ApplicationProvider.getApplicationContext<Context>()
        db072 = Room.inMemoryDatabaseBuilder(context072, TeluTaskDatabase068::class.java)
            .allowMainThreadQueries()
            .build()
        userDao072 = db072.userDao068()
        taskDao072 = db072.taskDao068()
    }

    @After
    fun closeDb072() {
        db072.close()
    }

    @Test
    fun testDashboardTaskIsolationAndCheckbox072() = runBlocking {
        // Create User Faiz
        val faizUserId072 = userDao072.insertUser068(
            User068(username068 = "Faiz072", password068 = "pass072")
        ).toInt()

        // Create another User
        val otherUserId072 = userDao072.insertUser068(
            User068(username068 = "OtherUser072", password068 = "passOther")
        ).toInt()

        // Empty state check for Faiz
        val emptyTasks072 = taskDao072.getTasksByUser068(faizUserId072)
        assertTrue(emptyTasks072.isEmpty())

        // Insert tasks for Faiz
        val task1Id072 = taskDao072.insertTask068(
            Task068(
                userId068 = faizUserId072,
                namaTugas068 = "Tugas Dashboard 1",
                prioritas068 = "Low",
                deadline068 = 1000L,
                isDone068 = false
            )
        ).toInt()

        taskDao072.insertTask068(
            Task068(
                userId068 = faizUserId072,
                namaTugas068 = "Tugas Dashboard 2",
                prioritas068 = "High",
                deadline068 = 2000L,
                isDone068 = false
            )
        )

        // Insert task for other user
        taskDao072.insertTask068(
            Task068(
                userId068 = otherUserId072,
                namaTugas068 = "Tugas User Lain",
                prioritas068 = "Medium",
                deadline068 = 1500L,
                isDone068 = false
            )
        )

        // Verify Faiz only gets 2 tasks
        val faizTasks072 = taskDao072.getTasksByUser068(faizUserId072)
        assertEquals(2, faizTasks072.size)

        // Test priority sorting (High -> Medium -> Low)
        val sortedTasks072 = taskDao072.getTasksSortedByPriority068(faizUserId072)
        assertEquals(2, sortedTasks072.size)
        assertEquals("High", sortedTasks072[0].prioritas068)
        assertEquals("Low", sortedTasks072[1].prioritas068)

        // Test checkbox update (toggle is_done to true)
        val updateCount072 = taskDao072.updateDone068(task1Id072, faizUserId072, true)
        assertEquals(1, updateCount072)

        val updatedTask072 = taskDao072.getTaskById068(task1Id072, faizUserId072)
        assertTrue(updatedTask072?.isDone068 == true)

        // Test unchecking (toggle is_done to false)
        taskDao072.updateDone068(task1Id072, faizUserId072, false)
        val uncheckedTask072 = taskDao072.getTaskById068(task1Id072, faizUserId072)
        assertFalse(uncheckedTask072?.isDone068 == true)
    }
}
