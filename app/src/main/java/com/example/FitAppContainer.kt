package com.example

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.AppDatabase
import com.example.data.remote.MockNutritionalApiService
import com.example.data.repository.CommunityRepository
import com.example.data.repository.GeminiMealAnalysisRepository
import com.example.data.repository.NutritionRepository
import com.example.data.repository.RecipeRepository
import com.example.data.repository.WorkoutRepository
import com.example.ui.viewmodels.AiMealScannerViewModel
import com.example.ui.viewmodels.BarcodeScannerViewModel
import com.example.ui.viewmodels.CommunityViewModel
import com.example.ui.viewmodels.DiaryViewModel
import com.example.ui.viewmodels.FoodDatabaseViewModel
import com.example.ui.viewmodels.ProfileViewModel
import com.example.ui.viewmodels.RecipeViewModel
import com.example.ui.viewmodels.StatsViewModel
import com.example.ui.viewmodels.SupplementsViewModel
import com.example.ui.viewmodels.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class FitAppContainer(context: Context) {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val database = AppDatabase.getDatabase(context, appScope)

    val nutritionRepository = NutritionRepository(
        foodDao = database.foodDao(),
        mealDao = database.mealDao(),
        waterDao = database.waterDao(),
        weightDao = database.weightDao(),
        userDao = database.userDao(),
        supplementDao = database.supplementDao()
    )

    val workoutRepository = WorkoutRepository(
        workoutDao = database.workoutDao(),
        exerciseProgressDao = database.exerciseProgressDao(),
        calisthenicsSetDao = database.calisthenicsSetDao(),
        exercisePlanDao = database.exercisePlanDao()
    )

    val recipeRepository = RecipeRepository(
        nutritionRepository = nutritionRepository
    )

    val communityRepository = CommunityRepository()

    val geminiMealAnalysisRepository = GeminiMealAnalysisRepository(
        nutritionRepository = nutritionRepository
    )

    val mockNutritionalApiService = MockNutritionalApiService()
}

class FitViewModelFactory(private val container: FitAppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiaryViewModel::class.java) -> {
                DiaryViewModel(container.nutritionRepository, container.workoutRepository) as T
            }
            modelClass.isAssignableFrom(FoodDatabaseViewModel::class.java) -> {
                FoodDatabaseViewModel(container.nutritionRepository) as T
            }
            modelClass.isAssignableFrom(BarcodeScannerViewModel::class.java) -> {
                BarcodeScannerViewModel(
                    nutritionRepository = container.nutritionRepository,
                    mockNutritionalApiService = container.mockNutritionalApiService
                ) as T
            }
            modelClass.isAssignableFrom(WorkoutViewModel::class.java) -> {
                WorkoutViewModel(container.workoutRepository, container.nutritionRepository) as T
            }
            modelClass.isAssignableFrom(StatsViewModel::class.java) -> {
                StatsViewModel(container.nutritionRepository, container.workoutRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(container.nutritionRepository) as T
            }
            modelClass.isAssignableFrom(RecipeViewModel::class.java) -> {
                RecipeViewModel(container.recipeRepository, container.nutritionRepository) as T
            }
            modelClass.isAssignableFrom(CommunityViewModel::class.java) -> {
                CommunityViewModel(container.communityRepository) as T
            }
            modelClass.isAssignableFrom(SupplementsViewModel::class.java) -> {
                SupplementsViewModel(container.nutritionRepository) as T
            }
            modelClass.isAssignableFrom(AiMealScannerViewModel::class.java) -> {
                AiMealScannerViewModel(container.geminiMealAnalysisRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

