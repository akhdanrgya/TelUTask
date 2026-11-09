package com.example.telutask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.dao.TaskDao068
import com.example.telutask.data.dao.UserDao068
import com.example.telutask.data.database.TeluTaskDatabase068
import com.example.telutask.data.entity.Task068
import com.example.telutask.data.entity.User068
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteTask069Test {

    private lateinit var db069: TeluTaskDatabase068
    private lateinit var userDao069: UserDao068
    private lateinit var taskDao069: TaskDao068
    private lateinit var sessionManager069: SessionManager048
    private lateinit var context069: Context

    @Before
    fun setUp069() {
        context069 = ApplicationProvider.getApplicationContext<Context>()
        db069 = Room.inMemoryDatabaseBuilder(context069, TeluTaskDatabase068::class.java)
            .allowMainThreadQueries()
            .build()
        userDao069 = db069.userDao068()
        taskDao069 = db069.taskDao068()
        sessionManager069 = SessionManager048(context069)
        sessionManager069.clearSession048()
    }

    @After
    fun tearDown069() {
        db069.close()
        sessionManager069.clearSession048()
    }

    @Test
    fun testDeleteTaskSuccess069() = runBlocking {
        // Create user Marta
        val userIdMarta069 = userDao069.insertUser068(
            User068(username068 = "marta069", password068 = "123456")
        ).toInt()

        // Create task for Marta
        val taskId069 = taskDao069.insertTask068(
            Task068(
                userId068 = userIdMarta069,
                namaTugas068 = "Tugas Integrasi Marta",
                prioritas068 = "High",
                deadline068 = 1700000000000L
            )
        ).toInt()

        // Verify task exists
        val taskBefore069 = taskDao069.getTaskById068(taskId069, userIdMarta069)
        assertNotNull(taskBefore069)

        // Delete task as owner
        val rowsDeleted069 = taskDao069.deleteTask068(taskId069, userIdMarta069)
        assertEquals(1, rowsDeleted069)

        // Verify task is deleted
        val taskAfter069 = taskDao069.getTaskById068(taskId069, userIdMarta069)
        assertNull(taskAfter069)
    }

    @Test
    fun testDeleteTaskIsolationOtherUserCannotDelete069() = runBlocking {
        // Create user 1 and user 2
        val user1Id069 = userDao069.insertUser068(
            User068(username068 = "user1_069", password068 = "123456")
        ).toInt()

        val user2Id069 = userDao069.insertUser068(
            User068(username068 = "user2_069", password068 = "123456")
        ).toInt()

        // Task created by user 1
        val taskIdUser1_069 = taskDao069.insertTask068(
            Task068(
                userId068 = user1Id069,
                namaTugas068 = "Tugas User 1",
                prioritas068 = "Medium",
                deadline068 = 1700000000000L
            )
        ).toInt()

        // User 2 attempts to delete User 1's task -> should affect 0 rows
        val rowsDeleted069 = taskDao069.deleteTask068(taskIdUser1_069, user2Id069)
        assertEquals(0, rowsDeleted069)

        // Verify task still exists for User 1
        val taskStillExists069 = taskDao069.getTaskById068(taskIdUser1_069, user1Id069)
        assertNotNull(taskStillExists069)
        assertEquals("Tugas User 1", taskStillExists069?.namaTugas068)
    }

    @Test
    fun testDeleteNonExistentTask069() = runBlocking {
        val userId069 = userDao069.insertUser068(
            User068(username068 = "marta_test069", password068 = "123456")
        ).toInt()

        val rowsDeleted069 = taskDao069.deleteTask068(9999, userId069)
        assertEquals(0, rowsDeleted069)
    }
}
