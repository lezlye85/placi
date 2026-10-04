package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.remote.GeminiVisionClient
import com.example.data.remote.MealAiAnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class GeminiMealAnalysisRepository(
    private val nutritionRepository: NutritionRepository
) {
    private val _lastAnalysisResult = MutableStateFlow<MealAiAnalysisResult?>(null)
    val lastAnalysisResult: StateFlow<MealAiAnalysisResult?> = _lastAnalysisResult.asStateFlow()

    suspend fun analyzeMealPhoto(bitmap: Bitmap, userNotes: String = ""): Result<MealAiAnalysisResult> {
        val result = GeminiVisionClient.analyzeMealImage(bitmap, userNotes)
        if (result.isSuccess) {
            _lastAnalysisResult.value = result.getOrNull()
        }
        return result
    }

    suspend fun logAiMealToDiary(
        analysis: MealAiAnalysisResult,
        mealType: MealType,
        portionScale: Float = 1.0f,
        date: String = LocalDate.now().toString(),
        saveAsCustomFood: Boolean = true,
        overrideCalories: Double? = null,
        overrideProtein: Double? = null,
        overrideCarbs: Double? = null,
        overrideFat: Double? = null,
        overrideFiber: Double? = null,
        logSeparateIngredients: Boolean = false
    ): List<MealEntry> {
        val scaledWeight = analysis.estimatedWeightGrams * portionScale
        val scaledCalories = overrideCalories ?: (analysis.calories * portionScale)
        val scaledProtein = overrideProtein ?: (analysis.protein * portionScale)
        val scaledCarbs = overrideCarbs ?: (analysis.carbs * portionScale)
        val scaledFat = overrideFat ?: (analysis.fat * portionScale)
        val scaledFiber = overrideFiber ?: (analysis.fiber * portionScale)

        val insertedEntries = mutableListOf<MealEntry>()

        if (logSeparateIngredients && analysis.ingredients.isNotEmpty()) {
            for (ing in analysis.ingredients) {
                val ingWeight = ing.weightGrams * portionScale
                val ingKcal = ing.calories * portionScale
                val ingProt = ing.protein * portionScale
                val ingCarb = ing.carbs * portionScale
                val ingFat = ing.fat * portionScale

                var ingFoodId: Long? = null
                if (saveAsCustomFood && ingWeight > 0) {
                    val multiplier = 100.0 / ingWeight
                    val foodItem = FoodItem(
                        name = ing.name,
                        brand = "AI Vision",
                        category = "MEALS",
                        caloriesPer100g = ingKcal * multiplier,
                        proteinPer100g = ingProt * multiplier,
                        carbsPer100g = ingCarb * multiplier,
                        fatPer100g = ingFat * multiplier,
                        fiberPer100g = 0.0,
                        defaultServingGrams = ingWeight,
                        defaultServingUnit = "g",
                        isCustom = true
                    )
                    ingFoodId = nutritionRepository.insertFood(foodItem)
                }

                val mealEntry = MealEntry(
                    date = date,
                    mealType = mealType,
                    foodId = ingFoodId,
                    foodName = ing.name,
                    brand = "AI Vision",
                    amountGrams = ingWeight,
                    calories = ingKcal,
                    protein = ingProt,
                    carbs = ingCarb,
                    fat = ingFat,
                    fiber = 0.0
                )
                val id = nutritionRepository.insertMeal(mealEntry)
                insertedEntries.add(mealEntry.copy(id = id))
            }
        } else {
            var savedFoodId: Long? = null
            if (saveAsCustomFood && scaledWeight > 0) {
                val per100Multiplier = 100.0 / scaledWeight
                val foodItem = FoodItem(
                    name = analysis.mealName,
                    brand = "AI Vision Felismerés",
                    category = "MEALS",
                    caloriesPer100g = scaledCalories * per100Multiplier,
                    proteinPer100g = scaledProtein * per100Multiplier,
                    carbsPer100g = scaledCarbs * per100Multiplier,
                    fatPer100g = scaledFat * per100Multiplier,
                    fiberPer100g = scaledFiber * per100Multiplier,
                    defaultServingGrams = scaledWeight,
                    defaultServingUnit = "adag",
                    isCustom = true
                )
                savedFoodId = nutritionRepository.insertFood(foodItem)
            }

            val mealEntry = MealEntry(
                date = date,
                mealType = mealType,
                foodId = savedFoodId,
                foodName = analysis.mealName,
                brand = "AI Vision",
                amountGrams = scaledWeight,
                calories = scaledCalories,
                protein = scaledProtein,
                carbs = scaledCarbs,
                fat = scaledFat,
                fiber = scaledFiber
            )

            val insertedId = nutritionRepository.insertMeal(mealEntry)
            insertedEntries.add(mealEntry.copy(id = insertedId))
        }

        return insertedEntries
    }

    fun clearLastAnalysis() {
        _lastAnalysisResult.value = null
    }
}
