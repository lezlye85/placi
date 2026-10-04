package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.model.WaterEntry
import com.example.data.repository.NutritionRepository
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
class DailyWaterTrackerRoomTest {

    private lateinit var database: AppDatabase
    private lateinit var nutritionRepository: NutritionRepository

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
            userDao = database.userDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testLogWaterEntriesAndCalculateTotalInRoom() = runTest {
        val date = "2026-10-04"

        // Log 250 ml (1 glass)
        nutritionRepository.addWater(date, 250)
        // Log 500 ml (1 bottle)
        nutritionRepository.addWater(date, 500)
        // Log 300 ml (custom amount)
        nutritionRepository.addWater(date, 300)

        val entries = nutritionRepository.getWaterForDate(date).first()
        assertEquals(3, entries.size)
        assertEquals(250, entries[0].amountMl)
        assertEquals(500, entries[1].amountMl)
        assertEquals(300, entries[2].amountMl)

        val totalWater = nutritionRepository.getTotalWaterForDate(date).first()
        assertNotNull(totalWater)
        assertEquals(1050, totalWater)
    }

    @Test
    fun testRemoveLastWaterEntry() = runTest {
        val date = "2026-10-04"

        nutritionRepository.addWater(date, 250)
        nutritionRepository.addWater(date, 500)

        var total = nutritionRepository.getTotalWaterForDate(date).first()
        assertEquals(750, total)

        nutritionRepository.removeLastWater(date)

        total = nutritionRepository.getTotalWaterForDate(date).first()
        assertEquals(250, total)

        val entries = nutritionRepository.getWaterForDate(date).first()
        assertEquals(1, entries.size)
        assertEquals(250, entries.first().amountMl)
    }

    @Test
    fun testDeleteSpecificWaterEntryById() = runTest {
        val date = "2026-10-04"

        nutritionRepository.addWater(date, 250)
        nutritionRepository.addWater(date, 500)

        val entries = nutritionRepository.getWaterForDate(date).first()
        assertEquals(2, entries.size)

        val firstEntryId = entries[0].id
        nutritionRepository.deleteWaterEntry(firstEntryId)

        val remainingEntries = nutritionRepository.getWaterForDate(date).first()
        assertEquals(1, remainingEntries.size)
        assertEquals(500, remainingEntries.first().amountMl)

        val total = nutritionRepository.getTotalWaterForDate(date).first()
        assertEquals(500, total)
    }

    @Test
    fun testClearAllWaterForDate() = runTest {
        val date = "2026-10-04"

        nutritionRepository.addWater(date, 250)
        nutritionRepository.addWater(date, 500)
        nutritionRepository.addWater(date, 750)

        nutritionRepository.clearWaterForDate(date)

        val entries = nutritionRepository.getWaterForDate(date).first()
        assertTrue(entries.isEmpty())

        val total = nutritionRepository.getTotalWaterForDate(date).first()
        assertEquals(null, total)
    }

    @Test
    fun testDateIsolationConsistentWithCalorieLogging() = runTest {
        val day1 = "2026-10-04"
        val day2 = "2026-10-05"

        // Day 1: Log water and meal
        nutritionRepository.addWater(day1, 1500)
        nutritionRepository.insertMeal(
            MealEntry(
                date = day1,
                mealType = MealType.LUNCH,
                foodName = "Csirkemell rizzsel",
                amountGrams = 200.0,
                calories = 330.0,
                protein = 45.0,
                carbs = 30.0,
                fat = 4.0,
                fiber = 2.0
            )
        )

        // Day 2: Log water and meal
        nutritionRepository.addWater(day2, 2250)
        nutritionRepository.insertMeal(
            MealEntry(
                date = day2,
                mealType = MealType.BREAKFAST,
                foodName = "Zabkása fehérjével",
                amountGrams = 150.0,
                calories = 420.0,
                protein = 35.0,
                carbs = 50.0,
                fat = 8.0,
                fiber = 6.0
            )
        )

        // Check Day 1
        val day1Water = nutritionRepository.getTotalWaterForDate(day1).first()
        val day1Meals = nutritionRepository.getMealsForDate(day1).first()
        assertEquals(1500, day1Water)
        assertEquals(1, day1Meals.size)
        assertEquals(330.0, day1Meals.first().calories, 0.01)

        // Check Day 2
        val day2Water = nutritionRepository.getTotalWaterForDate(day2).first()
        val day2Meals = nutritionRepository.getMealsForDate(day2).first()
        assertEquals(2250, day2Water)
        assertEquals(1, day2Meals.size)
        assertEquals(420.0, day2Meals.first().calories, 0.01)
    }
}
