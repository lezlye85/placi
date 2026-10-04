package com.example.data.repository

import android.util.Log
import com.example.data.local.FoodDao
import com.example.data.local.InitialData
import com.example.data.local.MealDao
import com.example.data.local.SupplementDao
import com.example.data.local.UserDao
import com.example.data.local.WaterDao
import com.example.data.local.WeightDao
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementEntity
import com.example.data.model.UserProfile
import com.example.data.model.WaterEntry
import com.example.data.model.WeightEntry
import com.example.data.model.toEntity
import com.example.data.remote.MockApiResponse
import com.example.data.remote.MockNutritionalApiService
import com.example.data.remote.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class NutritionRepository(
    private val foodDao: FoodDao,
    private val mealDao: MealDao,
    private val waterDao: WaterDao,
    private val weightDao: WeightDao,
    private val userDao: UserDao,
    private val supplementDao: SupplementDao? = null,
    private val mockNutritionalApiService: MockNutritionalApiService = MockNutritionalApiService()
) {
    val allFoods: Flow<List<FoodItem>> = foodDao.getAllFoods()
    val favoriteFoods: Flow<List<FoodItem>> = foodDao.getFavoriteFoods()
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
    val allWeights: Flow<List<WeightEntry>> = weightDao.getAllWeights()
    val latestWeight: Flow<WeightEntry?> = weightDao.getLatestWeight()

    val allSupplements: Flow<List<SupplementEntity>> =
        supplementDao?.getAllSupplements() ?: flowOf(emptyList())

    fun getSupplementsByBrand(brand: String): Flow<List<SupplementEntity>> =
        if (brand == "Összes" || brand.isBlank()) {
            supplementDao?.getAllSupplements() ?: flowOf(emptyList())
        } else {
            supplementDao?.getSupplementsByBrand(brand) ?: flowOf(emptyList())
        }

    fun getSupplementsByCategory(category: SupplementCategory): Flow<List<SupplementEntity>> =
        if (category == SupplementCategory.ALL) {
            supplementDao?.getAllSupplements() ?: flowOf(emptyList())
        } else {
            supplementDao?.getSupplementsByCategory(category) ?: flowOf(emptyList())
        }

    fun searchSupplements(query: String): Flow<List<SupplementEntity>> =
        if (query.isBlank()) {
            supplementDao?.getAllSupplements() ?: flowOf(emptyList())
        } else {
            supplementDao?.searchSupplements(query) ?: flowOf(emptyList())
        }

    suspend fun getSupplementByBarcode(barcode: String): SupplementEntity? = withContext(Dispatchers.IO) {
        supplementDao?.getSupplementByBarcode(barcode.trim())
    }

    suspend fun insertSupplement(supplement: SupplementEntity) = withContext(Dispatchers.IO) {
        supplementDao?.insertSupplement(supplement)
    }

    suspend fun insertAllSupplements(supplements: List<SupplementEntity>) = withContext(Dispatchers.IO) {
        supplementDao?.insertAllSupplements(supplements)
    }

    suspend fun deleteSupplement(supplement: SupplementEntity) = withContext(Dispatchers.IO) {
        supplementDao?.deleteSupplement(supplement)
    }

    suspend fun deleteSupplementById(id: String) = withContext(Dispatchers.IO) {
        supplementDao?.deleteSupplementById(id)
    }

    fun searchFoods(query: String): Flow<List<FoodItem>> {
        return if (query.isBlank()) foodDao.getAllFoods() else foodDao.searchFoods(query)
    }

    fun getFoodsByCategory(category: String): Flow<List<FoodItem>> {
        return if (category == FoodCategory.ALL.name) foodDao.getAllFoods() else foodDao.getFoodsByCategory(category)
    }

    fun getMealsForDate(date: String): Flow<List<MealEntry>> = mealDao.getMealsForDate(date)

    fun getMealsBetweenDates(startDate: String, endDate: String): Flow<List<MealEntry>> =
        mealDao.getMealsBetweenDates(startDate, endDate)

    fun getWaterForDate(date: String): Flow<List<WaterEntry>> = waterDao.getWaterForDate(date)

    fun getTotalWaterForDate(date: String): Flow<Int?> = waterDao.getTotalWaterForDate(date)

    fun getWaterBetweenDates(startDate: String, endDate: String): Flow<List<WaterEntry>> =
        waterDao.getWaterBetweenDates(startDate, endDate)

    suspend fun findFoodByBarcode(barcode: String): FoodItem? = withContext(Dispatchers.IO) {
        val trimmed = barcode.trim()
        if (trimmed.isEmpty()) return@withContext null

        // 1. Check local Room database
        val local = foodDao.getFoodByBarcode(trimmed)
        if (local != null) return@withContext local

        // 1.5 Check pre-populated supplements and foods catalog
        val supplementFood = com.example.data.local.SupplementsData.SUPPLEMENT_FOOD_ITEMS.find { it.barcode == trimmed }
            ?: InitialData.PREPOPULATED_FOODS.find { it.barcode == trimmed }
        if (supplementFood != null) {
            val newId = foodDao.insertFood(supplementFood)
            return@withContext supplementFood.copy(id = newId)
        }

        // 1.8 Check Mock Nutritional Cloud API
        try {
            val mockResponse = mockNutritionalApiService.fetchFoodDetails(trimmed)
            if (mockResponse is MockApiResponse.Success) {
                val foodItem = mockResponse.data.toFoodItem()
                val newId = foodDao.insertFood(foodItem)
                return@withContext foodItem.copy(id = newId)
            }
        } catch (e: Exception) {
            Log.w("NutritionRepo", "Mock Nutritional API lookup failed: ${e.message}")
        }

        // 2. Fallback to Open Food Facts API
        try {
            val response = NetworkClient.apiService.getProductByBarcode(trimmed)
            if (response.status == 1 && response.product != null) {
                val product = response.product
                val nutriments = product.nutriments
                val name = product.productNameHu ?: product.productName ?: "Ismeretlen termék"
                val calories = nutriments?.energyKcal100g ?: nutriments?.energyKcal ?: 0.0
                val protein = nutriments?.proteins100g ?: 0.0
                val carbs = nutriments?.carbohydrates100g ?: 0.0
                val fat = nutriments?.fat100g ?: 0.0
                val fiber = nutriments?.fiber100g ?: 0.0

                val newFood = FoodItem(
                    name = name,
                    brand = product.brands ?: "",
                    barcode = trimmed,
                    category = FoodCategory.ALL.name,
                    caloriesPer100g = calories,
                    proteinPer100g = protein,
                    carbsPer100g = carbs,
                    fatPer100g = fat,
                    fiberPer100g = fiber,
                    defaultServingUnit = "g",
                    defaultServingGrams = 100.0,
                    isCustom = true
                )
                // Cache it into local DB
                val newId = foodDao.insertFood(newFood)
                return@withContext newFood.copy(id = newId)
            }
        } catch (e: Exception) {
            Log.e("NutritionRepo", "Open Food Facts lookup failed: ${e.message}")
        }
        return@withContext null
    }

    suspend fun insertFood(food: FoodItem): Long = withContext(Dispatchers.IO) {
        foodDao.insertFood(food)
    }

    suspend fun updateFood(food: FoodItem) = withContext(Dispatchers.IO) {
        foodDao.updateFood(food)
    }

    suspend fun deleteFood(food: FoodItem) = withContext(Dispatchers.IO) {
        foodDao.deleteFood(food)
    }

    suspend fun insertMeal(meal: MealEntry): Long = withContext(Dispatchers.IO) {
        mealDao.insertMeal(meal)
    }

    suspend fun deleteMeal(meal: MealEntry) = withContext(Dispatchers.IO) {
        mealDao.deleteMeal(meal)
    }

    suspend fun addWater(date: String, amountMl: Int) = withContext(Dispatchers.IO) {
        waterDao.insertWater(WaterEntry(date = date, amountMl = amountMl))
    }

    suspend fun removeLastWater(date: String) = withContext(Dispatchers.IO) {
        waterDao.removeLastWaterEntryForDate(date)
    }

    suspend fun deleteWaterEntry(id: Long) = withContext(Dispatchers.IO) {
        waterDao.deleteWaterById(id)
    }

    suspend fun clearWaterForDate(date: String) = withContext(Dispatchers.IO) {
        waterDao.clearWaterForDate(date)
    }

    suspend fun logWeight(date: String, weightKg: Double, note: String = "") = withContext(Dispatchers.IO) {
        weightDao.insertWeight(WeightEntry(date = date, weightKg = weightKg, note = note))
        // Also update currentWeight in user profile
        val currentProfile = userDao.getUserProfile().firstOrNull() ?: UserProfile()
        userDao.insertOrUpdateProfile(currentProfile.copy(currentWeightKg = weightKg))
    }

    suspend fun deleteWeight(weight: WeightEntry) = withContext(Dispatchers.IO) {
        weightDao.deleteWeight(weight)
    }

    suspend fun updateProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdateProfile(profile)
    }

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        if (foodDao.getFoodCount() == 0) {
            foodDao.insertAllFoods(InitialData.PREPOPULATED_FOODS)
        }
        val profile = userDao.getUserProfile().firstOrNull()
        if (profile == null) {
            userDao.insertOrUpdateProfile(UserProfile())
        }
        if (supplementDao != null && supplementDao.getSupplementCount() == 0) {
            val curated = com.example.data.local.SupplementsData.SUPPLEMENTS.map { it.toEntity() }
            supplementDao.insertAllSupplements(curated)
        }
    }
}
