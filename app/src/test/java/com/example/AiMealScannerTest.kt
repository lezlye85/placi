package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.MealType
import com.example.data.remote.GeminiVisionClient
import com.example.data.repository.GeminiMealAnalysisRepository
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
class AiMealScannerTest {

    private lateinit var database: AppDatabase
    private lateinit var nutritionRepository: NutritionRepository
    private lateinit var geminiRepository: GeminiMealAnalysisRepository

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
        geminiRepository = GeminiMealAnalysisRepository(nutritionRepository)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSmartFallbackAnalysisStructure() {
        val analysis = GeminiVisionClient.getSmartFallbackAnalysis("extra csirke")
        assertNotNull(analysis.mealName)
        assertTrue(analysis.calories > 0)
        assertTrue(analysis.protein > 0)
        assertTrue(analysis.carbs > 0)
        assertTrue(analysis.fat > 0)
        assertTrue(analysis.ingredients.isNotEmpty())
    }

    @Test
    fun testLogAiMealToDiarySingleEntry() = runTest {
        val analysis = GeminiVisionClient.samplePresets.first()
        val date = "2026-10-04"

        val entries = geminiRepository.logAiMealToDiary(
            analysis = analysis,
            mealType = MealType.LUNCH,
            portionScale = 1.0f,
            date = date,
            saveAsCustomFood = true,
            logSeparateIngredients = false
        )

        assertEquals(1, entries.size)
        val entry = entries.first()
        assertEquals(analysis.mealName, entry.foodName)
        assertEquals(analysis.calories, entry.calories, 0.1)
        assertEquals(analysis.protein, entry.protein, 0.1)

        // Verify it was persisted to Room database
        val mealsInDb = database.mealDao().getMealsForDate(date).first()
        assertTrue(mealsInDb.any { it.foodName == analysis.mealName })

        // Verify it was saved as custom food
        val customFoods = database.foodDao().getCustomFoods().first()
        assertTrue(customFoods.any { it.name == analysis.mealName })
    }

    @Test
    fun testLogAiMealToDiaryScaledPortion() = runTest {
        val analysis = GeminiVisionClient.samplePresets.first()
        val date = "2026-10-04"
        val scale = 1.5f

        val entries = geminiRepository.logAiMealToDiary(
            analysis = analysis,
            mealType = MealType.DINNER,
            portionScale = scale,
            date = date,
            saveAsCustomFood = false,
            logSeparateIngredients = false
        )

        assertEquals(1, entries.size)
        val entry = entries.first()
        assertEquals(analysis.calories * scale, entry.calories, 0.1)
        assertEquals(analysis.protein * scale, entry.protein, 0.1)
    }

    @Test
    fun testLogAiMealSeparateIngredients() = runTest {
        val analysis = GeminiVisionClient.samplePresets.first()
        val date = "2026-10-04"

        val entries = geminiRepository.logAiMealToDiary(
            analysis = analysis,
            mealType = MealType.LUNCH,
            portionScale = 1.0f,
            date = date,
            saveAsCustomFood = false,
            logSeparateIngredients = true
        )

        assertEquals(analysis.ingredients.size, entries.size)
        val firstIngredient = analysis.ingredients.first()
        assertTrue(entries.any { it.foodName == firstIngredient.name })

        val mealsInDb = database.mealDao().getMealsForDate(date).first()
        assertEquals(analysis.ingredients.size, mealsInDb.size)
    }
}
