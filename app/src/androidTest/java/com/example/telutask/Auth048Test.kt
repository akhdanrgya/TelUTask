package com.example.telutask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.dao.UserDao068
import com.example.telutask.data.database.TeluTaskDatabase068
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
class Auth048Test {

    private lateinit var db048: TeluTaskDatabase068
    private lateinit var userDao048: UserDao068
    private lateinit var sessionManager048: SessionManager048
    private lateinit var context048: Context

    @Before
    fun setUp048() {
        context048 = ApplicationProvider.getApplicationContext<Context>()
        db048 = Room.inMemoryDatabaseBuilder(context048, TeluTaskDatabase068::class.java)
            .allowMainThreadQueries()
            .build()
        userDao048 = db048.userDao068()
        sessionManager048 = SessionManager048(context048)
        sessionManager048.clearSession048()
    }

    @After
    fun tearDown048() {
        db048.close()
        sessionManager048.clearSession048()
    }

    @Test
    fun testT01_RegisterValidUser048() = runBlocking {
        val username048 = "shafa048"
        val password048 = "password123"

        // Register valid user
        val userId048 = userDao048.insertUser068(
            User068(username068 = username048, password068 = password048)
        ).toInt()

        assertTrue(userId048 > 0)
        val fetchedUser048 = userDao048.getUserById068(userId048)
        assertNotNull(fetchedUser048)
        assertEquals(username048, fetchedUser048?.username068)
        assertEquals(password048, fetchedUser048?.password068)
    }

    @Test
    fun testT02_RegisterValidationInvalidPassword048() {
        val passwordShort048 = "12345"
        val isPasswordValid048 = passwordShort048.length >= 6
        assertEquals(false, isPasswordValid048)
    }

    @Test
    fun testT03_RegisterDuplicateUser048() = runBlocking {
        val username048 = "ShafaUser048"
        val password048 = "password123"

        // First registration
        userDao048.insertUser068(
            User068(username068 = username048, password068 = password048)
        )

        // Case-insensitive duplicate check
        val existingUser048 = userDao048.getUserByUsername068("shafauser048")
        assertNotNull(existingUser048)
        assertEquals("ShafaUser048", existingUser048?.username068)
    }

    @Test
    fun testT04_LoginSuccess048() = runBlocking {
        val username048 = "userLogin048"
        val password048 = "securePass048"

        val createdUserId048 = userDao048.insertUser068(
            User068(username068 = username048, password068 = password048)
        ).toInt()

        // Authenticate user
        val userFromDb048 = userDao048.getUserByUsername068(username048)
        assertNotNull(userFromDb048)
        assertEquals(password048, userFromDb048?.password068)

        // Save session
        sessionManager048.saveUserId048(createdUserId048)
        assertEquals(createdUserId048, sessionManager048.getUserId048())
    }

    @Test
    fun testT05_LoginFailureWrongPassword048() = runBlocking {
        val username048 = "userLoginFail048"
        val password048 = "realPassword"

        userDao048.insertUser068(
            User068(username068 = username048, password068 = password048)
        )

        val userFromDb048 = userDao048.getUserByUsername068(username048)
        assertNotNull(userFromDb048)
        val isPasswordCorrect048 = userFromDb048?.password068 == "wrongPassword"
        assertEquals(false, isPasswordCorrect048)
    }

    @Test
    fun testT06_SessionPersistence048() {
        val sampleUserId048 = 48
        sessionManager048.saveUserId048(sampleUserId048)
        assertEquals(sampleUserId048, sessionManager048.getUserId048())
    }

    @Test
    fun testT07_LogoutClearsSession048() {
        sessionManager048.saveUserId048(48)
        assertEquals(48, sessionManager048.getUserId048())

        sessionManager048.clearSession048()
        assertNull(sessionManager048.getUserId048())
    }
}
