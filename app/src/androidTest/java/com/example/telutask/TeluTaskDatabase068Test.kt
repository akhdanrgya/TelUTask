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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeluTaskDatabase068Test {

    private lateinit var db068: TeluTaskDatabase068
    private lateinit var userDao068: UserDao068
    private lateinit var taskDao068: TaskDao068

    @Before
    fun createDb068() {
        val context068 = ApplicationProvider.getApplicationContext<Context>()
        db068 = Room.inMemoryDatabaseBuilder(context068, TeluTaskDatabase068::class.java)
            .allowMainThreadQueries()
            .build()
        userDao068 = db068.userDao068()
        taskDao068 = db068.taskDao068()
    }

    @After
    fun closeDb068() {
        db068.close()
    }

    @Test
    fun testUserCrudAndLookup068() = runBlocking {
        val userA068 = User068(username068 = "ReyUser068", password068 = "password123")
        val userIdA068 = userDao068.insertUser068(userA068).toInt()

        assertTrue(userIdA068 > 0)

        // Lookup case-insensitive
        val foundUser068 = userDao068.getUserByUsername068("reyuser068")
        assertNotNull(foundUser068)
        assertEquals("ReyUser068", foundUser068?.username068)

        // Lookup by ID
        val userById068 = userDao068.getUserById068(userIdA068)
        assertNotNull(userById068)
        assertEquals(userIdA068, userById068?.id068)
    }

    @Test
    fun testTaskCrudAndUserIsolation068() = runBlocking {
        // Create User 1 & User 2
        val user1Id068 = userDao068.insertUser068(User068(username068 = "user1", password068 = "123456")).toInt()
        val user2Id068 = userDao068.insertUser068(User068(username068 = "user2", password068 = "123456")).toInt()

        // Insert tasks for User 1
        val task1Id068 = taskDao068.insertTask068(
            Task068(
                userId068 = user1Id068,
                namaTugas068 = "Tugas DAA",
                prioritas068 = "High",
                deadline068 = 1700000000000L
            )
        ).toInt()

        // Insert tasks for User 2
        taskDao068.insertTask068(
            Task068(
                userId068 = user2Id068,
                namaTugas068 = "Tugas PAB",
                prioritas068 = "Medium",
                deadline068 = 1700000000000L
            )
        )

        // Verify User 1 only sees User 1's task
        val user1Tasks068 = taskDao068.getTasksByUser068(user1Id068)
        assertEquals(1, user1Tasks068.size)
        assertEquals("Tugas DAA", user1Tasks068[0].namaTugas068)

        // Verify User 2 cannot access User 1's task via getTaskById
        val invalidAccess068 = taskDao068.getTaskById068(task1Id068, user2Id068)
        assertNull(invalidAccess068)

        // Update task for User 1
        val updatedRows068 = taskDao068.updateTask068(
            taskId068 = task1Id068,
            userId068 = user1Id068,
            namaTugas068 = "Tugas DAA Revisi",
            prioritas068 = "Low",
            deadline068 = 1710000000000L
        )
        assertEquals(1, updatedRows068)

        // Update done status
        taskDao068.updateDone068(task1Id068, user1Id068, true)
        val updatedTask068 = taskDao068.getTaskById068(task1Id068, user1Id068)
        assertTrue(updatedTask068?.isDone068 == true)

        // Delete task
        val deletedRows068 = taskDao068.deleteTask068(task1Id068, user1Id068)
        assertEquals(1, deletedRows068)
        assertNull(taskDao068.getTaskById068(task1Id068, user1Id068))
    }

    @Test
    fun testTaskPrioritySorting068() = runBlocking {
        val userId068 = userDao068.insertUser068(User068(username068 = "sortUser", password068 = "123456")).toInt()

        taskDao068.insertTask068(Task068(userId068 = userId068, namaTugas068 = "Low Task", prioritas068 = "Low", deadline068 = 1000L))
        taskDao068.insertTask068(Task068(userId068 = userId068, namaTugas068 = "High Task", prioritas068 = "High", deadline068 = 2000L))
        taskDao068.insertTask068(Task068(userId068 = userId068, namaTugas068 = "Medium Task", prioritas068 = "Medium", deadline068 = 1500L))

        val sortedTasks068 = taskDao068.getTasksSortedByPriority068(userId068)
        assertEquals(3, sortedTasks068.size)
        assertEquals("High Task", sortedTasks068[0].namaTugas068)
        assertEquals("Medium Task", sortedTasks068[1].namaTugas068)
        assertEquals("Low Task", sortedTasks068[2].namaTugas068)
    }
}
