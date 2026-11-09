package com.example.telutask

import android.graphics.Bitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.telutask.auth.LoginActivity048
import com.example.telutask.auth.SessionManager048
import com.example.telutask.data.database.TeluTaskDatabase068
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Real UI smoke test; database suites separately cover hostile owner IDs. */
@RunWith(AndroidJUnit4::class)
class Finishing069Test {
    private fun settle069() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(700)
    }

    private fun screenshot069(name069: String) {
        settle069()
        val context069 = ApplicationProvider.getApplicationContext<android.content.Context>()
        val directory069 = File(context069.getExternalFilesDir(null), "qa069").apply { mkdirs() }
        val bitmap069 = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
        File(directory069, "$name069.png").outputStream().use { stream069 ->
            bitmap069.compress(Bitmap.CompressFormat.PNG, 100, stream069)
        }
        bitmap069.recycle()
    }

    @Test
    fun registerCreateEditCancelDeleteConfirmLogout069() {
        val context069 = ApplicationProvider.getApplicationContext<android.content.Context>()
        SessionManager048(context069).clearSession048()
        ActivityScenario.launch(LoginActivity048::class.java).use { scenario069 ->
            screenshot069("01-login")
            onView(withId(R.id.btnToRegister048)).perform(scrollTo(), click())
            screenshot069("02-register")
            onView(withId(R.id.etUsername048)).perform(replaceText("qa069_${System.nanoTime()}"))
            onView(withId(R.id.etPassword048)).perform(replaceText("demo069"))
            onView(withId(R.id.etConfirmPassword048)).perform(replaceText("demo069"), closeSoftKeyboard())
            onView(withId(R.id.btnRegister048)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.layoutEmptyState072)).check(matches(isDisplayed()))
            screenshot069("03-empty")
            onView(withId(R.id.fabAddTask072)).perform(click())
            onView(withId(R.id.etNamaTugas075)).perform(replaceText("Selesaikan laporan PAB"), closeSoftKeyboard())
            onView(withId(R.id.btnSelectDeadline075)).perform(scrollTo(), click())
            onView(withId(android.R.id.button1)).perform(click())
            screenshot069("04-add")
            onView(withId(R.id.btnSimpan075)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.cbIsDone072)).perform(click())
            settle069()
            onView(withId(R.id.cbIsDone072)).check(matches(isChecked()))
            onView(withId(R.id.btnSortPriority072)).perform(click())
            screenshot069("05-dashboard")
            onView(withId(R.id.tvNamaTugas072)).perform(click())
            settle069()
            screenshot069("06-detail")
            onView(withId(R.id.btnEdit077)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.etName077)).perform(replaceText("Laporan PAB final"), closeSoftKeyboard())
            screenshot069("07-edit")
            onView(withId(R.id.btnSave077)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.tvName077)).check(matches(withText("Laporan PAB final")))
            onView(withId(R.id.btnDelete077)).perform(scrollTo(), click())
            screenshot069("08-delete")
            onView(withId(android.R.id.button2)).perform(click())
            onView(withId(R.id.tvName077)).check(matches(withText("Laporan PAB final")))
            val owner069 = requireNotNull(SessionManager048(context069).getUserId048())
            runBlocking {
                val tasks069 = TeluTaskDatabase068.getInstance068(context069).taskDao068().getTasksByUser068(owner069)
                org.junit.Assert.assertEquals(1, tasks069.size)
                org.junit.Assert.assertTrue(tasks069.single().isDone068)
            }
            onView(withId(R.id.btnDelete077)).perform(scrollTo(), click())
            onView(withId(android.R.id.button1)).perform(click())
            settle069()
            onView(withId(R.id.layoutEmptyState072)).check(matches(isDisplayed()))
            onView(withId(R.id.btnLogout072)).perform(click())
            settle069()
            onView(withId(R.id.btnLogin048)).check(matches(isDisplayed()))
            org.junit.Assert.assertNull(SessionManager048(context069).getUserId048())
        }
    }
    @Test
    fun draftSurvivesRecreationAndRejectsInvalidInput069() {
        val context069 = ApplicationProvider.getApplicationContext<android.content.Context>()
        val database069 = TeluTaskDatabase068.getInstance068(context069)
        val owner069 = runBlocking {
            database069.userDao068().insertUser068(com.example.telutask.data.entity.User068(
                username068 = "rotation069_${System.nanoTime()}", password068 = "demo069"
            )).toInt()
        }
        SessionManager048(context069).saveUserId048(owner069)
        ActivityScenario.launch(com.example.telutask.task.AddTaskActivity075::class.java).use { scenario069 ->
            onView(withId(R.id.btnSimpan075)).perform(scrollTo(), click())
            onView(withId(R.id.etNamaTugas075)).check(matches(isDisplayed()))
            onView(withId(R.id.etNamaTugas075)).perform(replaceText("Draft rotasi"), closeSoftKeyboard())
            onView(withId(R.id.btnSimpan075)).perform(scrollTo(), click())
            settle069()
            runBlocking { org.junit.Assert.assertTrue(database069.taskDao068().getTasksByUser068(owner069).isEmpty()) }
            onView(withId(R.id.btnSelectDeadline075)).perform(scrollTo(), click())
            onView(withId(android.R.id.button1)).perform(click())
            scenario069.recreate()
            onView(withId(R.id.etNamaTugas075)).check(matches(withText("Draft rotasi")))
            onView(withId(R.id.btnSimpan075)).perform(scrollTo(), click())
            settle069()
            runBlocking { org.junit.Assert.assertEquals("Draft rotasi", database069.taskDao068().getTasksByUser068(owner069).single().namaTugas068) }
        }
        SessionManager048(context069).clearSession048()
    }

    @Test
    fun loginValidationSessionAndSecondAccount069() {
        val context069 = ApplicationProvider.getApplicationContext<android.content.Context>()
        val database069 = TeluTaskDatabase068.getInstance068(context069)
        val username069 = "login069_${System.nanoTime()}"
        val owner069 = runBlocking {
            val id069 = database069.userDao068().insertUser068(com.example.telutask.data.entity.User068(
                username068 = username069, password068 = "demo069"
            )).toInt()
            database069.taskDao068().insertTask068(com.example.telutask.data.entity.Task068(
                userId068 = id069, namaTugas068 = "Tugas privat A", prioritas068 = "High", deadline068 = 1794243600000L
            ))
            id069
        }
        SessionManager048(context069).clearSession048()
        ActivityScenario.launch(LoginActivity048::class.java).use { scenario069 ->
            onView(withId(R.id.etUsername048)).perform(replaceText(username069))
            onView(withId(R.id.etPassword048)).perform(replaceText("salah069"), closeSoftKeyboard())
            onView(withId(R.id.btnLogin048)).perform(scrollTo(), click())
            settle069()
            org.junit.Assert.assertNull(SessionManager048(context069).getUserId048())
            onView(withId(R.id.etPassword048)).perform(replaceText("demo069"), closeSoftKeyboard())
            onView(withId(R.id.btnLogin048)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.tvNamaTugas072)).check(matches(withText("Tugas privat A")))
        }
        ActivityScenario.launch(com.example.telutask.auth.LauncherActivity048::class.java).use { scenario069 ->
            settle069()
            onView(withId(R.id.tvNamaTugas072)).check(matches(withText("Tugas privat A")))
            onView(withId(R.id.btnLogout072)).perform(click())
            onView(withId(R.id.btnToRegister048)).perform(scrollTo(), click())
            onView(withId(R.id.btnRegister048)).perform(scrollTo(), click())
            org.junit.Assert.assertNull(SessionManager048(context069).getUserId048())
            onView(withId(R.id.etUsername048)).perform(replaceText(username069.uppercase()))
            onView(withId(R.id.etPassword048)).perform(replaceText("123"))
            onView(withId(R.id.etConfirmPassword048)).perform(replaceText("123"), closeSoftKeyboard())
            onView(withId(R.id.btnRegister048)).perform(scrollTo(), click())
            org.junit.Assert.assertNull(SessionManager048(context069).getUserId048())
            onView(withId(R.id.etPassword048)).perform(replaceText("demo069"))
            onView(withId(R.id.etConfirmPassword048)).perform(replaceText("demo069"), closeSoftKeyboard())
            onView(withId(R.id.btnRegister048)).perform(scrollTo(), click())
            settle069()
            org.junit.Assert.assertNull(SessionManager048(context069).getUserId048())
            onView(withId(R.id.etUsername048)).perform(replaceText("second069_${System.nanoTime()}"), closeSoftKeyboard())
            onView(withId(R.id.btnRegister048)).perform(scrollTo(), click())
            settle069()
            onView(withId(R.id.layoutEmptyState072)).check(matches(isDisplayed()))
        }
        val taskId069 = runBlocking { database069.taskDao068().getTasksByUser068(owner069).single().id068 }
        val intent069 = android.content.Intent(context069, com.example.telutask.task.TaskDetailActivity077::class.java)
            .putExtra(com.example.telutask.task.TaskDetailActivity077.EXTRA_TASK_ID077, taskId069)
        ActivityScenario.launch<com.example.telutask.task.TaskDetailActivity077>(intent069).use { scenario069 ->
            settle069()
            onView(withId(R.id.layoutEmptyState072)).check(matches(isDisplayed()))
        }
        SessionManager048(context069).clearSession048()
    }

}
