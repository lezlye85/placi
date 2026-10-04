package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.WorkoutCategory
import com.example.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CalisthenicsWorkoutTrackingTest {

    private lateinit var database: AppDatabase
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutRepository = WorkoutRepository(
            workoutDao = database.workoutDao(),
            exerciseProgressDao = database.exerciseProgressDao(),
            calisthenicsSetDao = database.calisthenicsSetDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrievePrisonSetLogs() = runTest {
        val setLog = CalisthenicsSetLog(
            date = "2026-10-04",
            routineId = "routine_prison_big_six",
            routineTitle = "Fegyencedzés: A Mester Hatos (The Big 6)",
            category = WorkoutCategory.PRISON,
            exerciseId = "ex_prison_pullup",
            exerciseName = "Börtön húzódzkodás szűk fogással",
            setNumber = 1,
            targetReps = 10,
            repsCompleted = 10,
            weightAddedKg = 0.0,
            rpe = 8,
            isPersonalRecord = true,
            notes = "Teljes mozgástartomány rácson"
        )

        val id = workoutRepository.logCalisthenicsSet(setLog)
        assertTrue(id > 0)

        val prisonHistory = workoutRepository.getPrisonPerformanceHistory().first()
        assertEquals(1, prisonHistory.size)
        val retrieved = prisonHistory.first()
        assertEquals("Börtön húzódzkodás szűk fogással", retrieved.exerciseName)
        assertEquals(10, retrieved.repsCompleted)
        assertTrue(retrieved.isPersonalRecord)
        assertEquals(WorkoutCategory.PRISON, retrieved.category)
    }

    @Test
    fun testInsertAndRetrieveMilitaryPerformanceHistory() = runTest {
        val set1 = CalisthenicsSetLog(
            date = "2026-10-04",
            routineId = "routine_military_combat_conditioning",
            routineTitle = "Katonai Rohamosztagos Erőnléti Tréning",
            category = WorkoutCategory.MILITARY,
            exerciseId = "ex_military_burpee",
            exerciseName = "Kommandós burpee távolugrással",
            setNumber = 1,
            targetReps = 15,
            repsCompleted = 15,
            rpe = 9,
            isPersonalRecord = true
        )

        val set2 = CalisthenicsSetLog(
            date = "2026-10-04",
            routineId = "routine_military_combat_conditioning",
            routineTitle = "Katonai Rohamosztagos Erőnléti Tréning",
            category = WorkoutCategory.MILITARY,
            exerciseId = "ex_military_burpee",
            exerciseName = "Kommandós burpee távolugrással",
            setNumber = 2,
            targetReps = 15,
            repsCompleted = 12,
            rpe = 10,
            isPersonalRecord = false
        )

        workoutRepository.logCalisthenicsSet(set1)
        workoutRepository.logCalisthenicsSet(set2)

        val militaryHistory = workoutRepository.getMilitaryPerformanceHistory().first()
        assertEquals(2, militaryHistory.size)

        val totalReps = database.calisthenicsSetDao().getTotalRepsForExerciseAndDate("ex_military_burpee", "2026-10-04").first()
        assertEquals(27, totalReps)

        val maxReps = database.calisthenicsSetDao().getMaxRepsForExercise("ex_military_burpee").first()
        assertEquals(15, maxReps)

        val prs = database.calisthenicsSetDao().getAllPersonalRecords().first()
        assertEquals(1, prs.size)
        assertEquals(1, prs.first().setNumber)
    }

    @Test
    fun testDeleteCalisthenicsSet() = runTest {
        val setLog = CalisthenicsSetLog(
            date = "2026-10-04",
            routineId = "routine_prison_big_six",
            routineTitle = "Fegyencedzés: A Mester Hatos",
            category = WorkoutCategory.PRISON,
            exerciseId = "ex_prison_squat",
            exerciseName = "Börtön egylábas guggolás",
            setNumber = 1,
            targetReps = 8,
            repsCompleted = 8,
            rpe = 9
        )

        val id = workoutRepository.logCalisthenicsSet(setLog)
        val setsBefore = workoutRepository.getAllCalisthenicsSets().first()
        assertEquals(1, setsBefore.size)

        workoutRepository.deleteCalisthenicsSetById(id)
        val setsAfter = workoutRepository.getAllCalisthenicsSets().first()
        assertTrue(setsAfter.isEmpty())
    }
}
