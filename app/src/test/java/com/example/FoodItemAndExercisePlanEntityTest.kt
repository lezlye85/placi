package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.ExercisePlan
import com.example.data.model.FoodItem
import com.example.data.repository.NutritionRepository
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
class FoodItemAndExercisePlanEntityTest {

    private lateinit var database: AppDatabase
    private lateinit var nutritionRepository: NutritionRepository
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        nutritionRepository = NutritionRepository(
            foodDao = database.foodDao(),
            mealDao = database.mealDao(),
            waterDao = database.waterDao(),
            weightDao = database.weightDao(),
            userDao = database.userDao(),
            supplementDao = database.supplementDao()
        )

        workoutRepository = WorkoutRepository(
            workoutDao = database.workoutDao(),
            exerciseProgressDao = database.exerciseProgressDao(),
            calisthenicsSetDao = database.calisthenicsSetDao(),
            exercisePlanDao = database.exercisePlanDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testFoodItemEntityWithCaloriesAndMacros() = runTest {
        val food = FoodItem(
            name = "Csirkemell Filé",
            brand = "Bio Baromfi",
            barcode = "5991234567890",
            category = "MEAT_FISH",
            caloriesPer100g = 120.0,
            proteinPer100g = 23.5,
            carbsPer100g = 0.5,
            fatPer100g = 2.0,
            fiberPer100g = 0.0,
            defaultServingUnit = "g",
            defaultServingGrams = 150.0
        )

        // Verify entity properties: name, calories, macros
        assertEquals("Csirkemell Filé", food.name)
        assertEquals(120.0, food.calories, 0.001)
        assertEquals(23.5, food.protein, 0.001)
        assertEquals(0.5, food.carbs, 0.001)
        assertEquals(2.0, food.fat, 0.001)
        assertEquals(0.0, food.fiber, 0.001)

        // Insert into Room
        val insertedId = database.foodDao().insertFood(food)
        assertTrue(insertedId > 0)

        // Query back from Room
        val retrieved = database.foodDao().getFoodByIdDirect(insertedId)
        assertNotNull(retrieved)
        assertEquals("Csirkemell Filé", retrieved?.name)
        assertEquals(120.0, retrieved?.caloriesPer100g ?: 0.0, 0.001)
        assertEquals(120.0, retrieved?.calories ?: 0.0, 0.001)
        assertEquals(23.5, retrieved?.protein ?: 0.0, 0.001)
        assertEquals(0.5, retrieved?.carbs ?: 0.0, 0.001)
        assertEquals(2.0, retrieved?.fat ?: 0.0, 0.001)
    }

    @Test
    fun testExercisePlanEntityWithNameDurationDifficultyAndType() = runTest {
        val plan = ExercisePlan(
            name = "Otthoni Saját Testsúlyos Alapozás",
            duration = 30, // 30 perc
            difficulty = "Kezdő",
            type = "Kalisztenika",
            description = "Fekvőtámasz, plank és guggolás az alapoktól",
            caloriesBurnEstimate = 180
        )

        // Verify entity fields: name, duration, difficulty, type
        assertEquals("Otthoni Saját Testsúlyos Alapozás", plan.name)
        assertEquals(30, plan.duration)
        assertEquals(30, plan.durationMinutes)
        assertEquals("Kezdő", plan.difficulty)
        assertEquals("Kalisztenika", plan.type)

        // Insert into Room
        val insertedId = database.exercisePlanDao().insertExercisePlan(plan)
        assertTrue(insertedId > 0)

        // Query back directly
        val retrieved = database.exercisePlanDao().getExercisePlanByIdDirect(insertedId)
        assertNotNull(retrieved)
        assertEquals("Otthoni Saját Testsúlyos Alapozás", retrieved?.name)
        assertEquals(30, retrieved?.duration)
        assertEquals("Kezdő", retrieved?.difficulty)
        assertEquals("Kalisztenika", retrieved?.type)
        assertEquals(180, retrieved?.caloriesBurnEstimate)
    }

    @Test
    fun testExercisePlanFilteringAndRepositoryQueries() = runTest {
        val plans = listOf(
            ExercisePlan(
                name = "Kezdő Fegyencedzés",
                duration = 30,
                difficulty = "Kezdő",
                type = "Kalisztenika",
                caloriesBurnEstimate = 170
            ),
            ExercisePlan(
                name = "Haladó Katonai Taktikai HIIT",
                duration = 45,
                difficulty = "Haladó",
                type = "HIIT",
                caloriesBurnEstimate = 320
            ),
            ExercisePlan(
                name = "Mester Tabata Pulzusfokozó",
                duration = 20,
                difficulty = "Mester",
                type = "HIIT",
                caloriesBurnEstimate = 220
            )
        )

        workoutRepository.insertAllExercisePlans(plans)

        // Test getAllExercisePlans
        val all = workoutRepository.allExercisePlans.first()
        assertEquals(3, all.size)

        // Test filtering by type
        val hiitPlans = workoutRepository.getExercisePlansByType("HIIT").first()
        assertEquals(2, hiitPlans.size)
        assertTrue(hiitPlans.all { it.type == "HIIT" })

        // Test filtering by difficulty
        val beginnerPlans = workoutRepository.getExercisePlansByDifficulty("Kezdő").first()
        assertEquals(1, beginnerPlans.size)
        assertEquals("Kezdő Fegyencedzés", beginnerPlans.first().name)
    }

    @Test
    fun testDeleteExercisePlanInRoom() = runTest {
        val plan = ExercisePlan(
            name = "Törlendő Terv",
            duration = 15,
            difficulty = "Könnyű",
            type = "Kardió"
        )

        val id = database.exercisePlanDao().insertExercisePlan(plan)
        assertEquals(1, database.exercisePlanDao().getExercisePlanCount())

        database.exercisePlanDao().deleteExercisePlanById(id)
        assertEquals(0, database.exercisePlanDao().getExercisePlanCount())
    }
}
