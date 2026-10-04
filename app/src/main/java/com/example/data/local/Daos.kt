package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.ExercisePlan
import com.example.data.model.ExerciseProgressEntry
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementEntity
import com.example.data.model.UserProfile
import com.example.data.model.WaterEntry
import com.example.data.model.WeightEntry
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoods(): Flow<List<FoodItem>>

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFoods(query: String): Flow<List<FoodItem>>

    @Query("SELECT * FROM foods WHERE category = :category ORDER BY name ASC")
    fun getFoodsByCategory(category: String): Flow<List<FoodItem>>

    @Query("SELECT * FROM foods WHERE id = :id LIMIT 1")
    fun getFoodById(id: Long): Flow<FoodItem?>

    @Query("SELECT * FROM foods WHERE id = :id LIMIT 1")
    suspend fun getFoodByIdDirect(id: Long): FoodItem?

    @Query("SELECT * FROM foods WHERE barcode = :barcode LIMIT 1")
    suspend fun getFoodByBarcode(barcode: String): FoodItem?

    @Query("SELECT * FROM foods WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteFoods(): Flow<List<FoodItem>>

    @Query("SELECT * FROM foods WHERE isCustom = 1 ORDER BY name ASC")
    fun getCustomFoods(): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodItem): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllFoods(foods: List<FoodItem>)

    @Update
    suspend fun updateFood(food: FoodItem)

    @Delete
    suspend fun deleteFood(food: FoodItem)

    @Query("DELETE FROM foods WHERE id = :id")
    suspend fun deleteFoodById(id: Long)

    @Query("UPDATE foods SET isFavorite = :isFavorite WHERE id = :foodId")
    suspend fun setFavorite(foodId: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun getFoodCount(): Int
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_entries WHERE date = :date ORDER BY timestamp ASC")
    fun getMealsForDate(date: String): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries WHERE date = :date AND mealType = :mealType ORDER BY timestamp ASC")
    fun getMealsForDateAndType(date: String, mealType: MealType): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries ORDER BY timestamp DESC LIMIT 20")
    fun getRecentMeals(): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC, timestamp ASC")
    fun getMealsBetweenDates(startDate: String, endDate: String): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries WHERE id = :id LIMIT 1")
    fun getMealById(id: Long): Flow<MealEntry?>

    @Query("SELECT SUM(calories) FROM meal_entries WHERE date = :date")
    fun getTotalCaloriesForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(protein) FROM meal_entries WHERE date = :date")
    fun getTotalProteinForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(carbs) FROM meal_entries WHERE date = :date")
    fun getTotalCarbsForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(fat) FROM meal_entries WHERE date = :date")
    fun getTotalFatForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(fiber) FROM meal_entries WHERE date = :date")
    fun getTotalFiberForDate(date: String): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntry>): List<Long>

    @Update
    suspend fun updateMeal(meal: MealEntry)

    @Delete
    suspend fun deleteMeal(meal: MealEntry)

    @Query("DELETE FROM meal_entries WHERE id = :id")
    suspend fun deleteMealById(id: Long)

    @Query("DELETE FROM meal_entries WHERE date = :date")
    suspend fun clearMealsForDate(date: String)
}

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_entries WHERE date = :date ORDER BY timestamp ASC")
    fun getWaterForDate(date: String): Flow<List<WaterEntry>>

    @Query("SELECT SUM(amountMl) FROM water_entries WHERE date = :date")
    fun getTotalWaterForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM water_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC, timestamp ASC")
    fun getWaterBetweenDates(startDate: String, endDate: String): Flow<List<WaterEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(water: WaterEntry): Long

    @Query("DELETE FROM water_entries WHERE date = :date")
    suspend fun clearWaterForDate(date: String)

    @Query("DELETE FROM water_entries WHERE id = :id")
    suspend fun deleteWaterById(id: Long)

    @Query("DELETE FROM water_entries WHERE id = (SELECT id FROM water_entries WHERE date = :date ORDER BY id DESC LIMIT 1)")
    suspend fun removeLastWaterEntryForDate(date: String)
}

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight_entries ORDER BY date ASC, timestamp ASC")
    fun getAllWeights(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getLatestWeight(): Flow<WeightEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightEntry): Long

    @Delete
    suspend fun deleteWeight(weight: WeightEntry)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_logs ORDER BY date DESC, timestamp DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE date = :date ORDER BY timestamp ASC")
    fun getWorkoutsForDate(date: String): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC, timestamp ASC")
    fun getWorkoutsBetweenDates(startDate: String, endDate: String): Flow<List<WorkoutLog>>

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE date = :date")
    fun getTotalCaloriesBurnedForDate(date: String): Flow<Int?>

    @Query("SELECT SUM(durationMinutes) FROM workout_logs WHERE date = :date")
    fun getTotalWorkoutDurationForDate(date: String): Flow<Int?>

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE date >= :startDate AND date <= :endDate")
    fun getTotalCaloriesBurnedBetweenDates(startDate: String, endDate: String): Flow<Int?>

    @Query("SELECT COUNT(*) FROM workout_logs")
    fun getTotalWorkoutCount(): Flow<Int>

    @Query("SELECT * FROM workout_logs WHERE id = :id LIMIT 1")
    fun getWorkoutById(id: Long): Flow<WorkoutLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLog): Long

    @Update
    suspend fun updateWorkoutLog(log: WorkoutLog)

    @Delete
    suspend fun deleteWorkoutLog(log: WorkoutLog)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteWorkoutLogById(id: Long)
}

@Dao
interface ExerciseProgressDao {
    @Query("SELECT * FROM exercise_progress ORDER BY date DESC, timestamp DESC")
    fun getAllExerciseProgress(): Flow<List<ExerciseProgressEntry>>

    @Query("SELECT * FROM exercise_progress WHERE exerciseId = :exerciseId ORDER BY date DESC, timestamp DESC")
    fun getProgressForExercise(exerciseId: String): Flow<List<ExerciseProgressEntry>>

    @Query("SELECT * FROM exercise_progress WHERE date = :date ORDER BY timestamp ASC")
    fun getProgressForDate(date: String): Flow<List<ExerciseProgressEntry>>

    @Query("SELECT * FROM exercise_progress WHERE exerciseId = :exerciseId ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getRecentProgressForExercise(exerciseId: String): Flow<ExerciseProgressEntry?>

    @Query("SELECT MAX(repsCompleted) FROM exercise_progress WHERE exerciseId = :exerciseId")
    fun getMaxRepsForExercise(exerciseId: String): Flow<Int?>

    @Query("SELECT MAX(weightAddedKg) FROM exercise_progress WHERE exerciseId = :exerciseId")
    fun getMaxWeightForExercise(exerciseId: String): Flow<Double?>

    @Query("SELECT * FROM exercise_progress WHERE isPersonalRecord = 1 ORDER BY date DESC")
    fun getPersonalRecords(): Flow<List<ExerciseProgressEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseProgress(entry: ExerciseProgressEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProgress(entries: List<ExerciseProgressEntry>): List<Long>

    @Update
    suspend fun updateExerciseProgress(entry: ExerciseProgressEntry)

    @Delete
    suspend fun deleteExerciseProgress(entry: ExerciseProgressEntry)

    @Query("DELETE FROM exercise_progress WHERE id = :id")
    suspend fun deleteProgressById(id: Long)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)
}

@Dao
interface CalisthenicsSetDao {
    @Query("SELECT * FROM calisthenics_set_logs ORDER BY date DESC, timestamp DESC")
    fun getAllSetLogs(): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE exerciseId = :exerciseId ORDER BY date DESC, setNumber ASC")
    fun getSetsForExercise(exerciseId: String): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE routineId = :routineId AND date = :date ORDER BY setNumber ASC, timestamp ASC")
    fun getSetsForRoutineAndDate(routineId: String, date: String): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE date = :date ORDER BY timestamp ASC")
    fun getSetsForDate(date: String): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE category = :category ORDER BY date DESC, timestamp DESC")
    fun getSetsByCategory(category: WorkoutCategory): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE category = 'PRISON' ORDER BY date DESC, timestamp DESC")
    fun getPrisonPerformanceHistory(): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE category = 'MILITARY' ORDER BY date DESC, timestamp DESC")
    fun getMilitaryPerformanceHistory(): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE exerciseId = :exerciseId AND isPersonalRecord = 1 ORDER BY date DESC")
    fun getPersonalRecordsForExercise(exerciseId: String): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT * FROM calisthenics_set_logs WHERE isPersonalRecord = 1 ORDER BY date DESC")
    fun getAllPersonalRecords(): Flow<List<CalisthenicsSetLog>>

    @Query("SELECT MAX(repsCompleted) FROM calisthenics_set_logs WHERE exerciseId = :exerciseId")
    fun getMaxRepsForExercise(exerciseId: String): Flow<Int?>

    @Query("SELECT MAX(weightAddedKg) FROM calisthenics_set_logs WHERE exerciseId = :exerciseId")
    fun getMaxWeightForExercise(exerciseId: String): Flow<Double?>

    @Query("SELECT SUM(repsCompleted) FROM calisthenics_set_logs WHERE exerciseId = :exerciseId AND date = :date")
    fun getTotalRepsForExerciseAndDate(exerciseId: String, date: String): Flow<Int?>

    @Query("SELECT SUM(repsCompleted) FROM calisthenics_set_logs WHERE category = :category")
    fun getTotalRepsForCategory(category: WorkoutCategory): Flow<Int?>

    @Query("SELECT COUNT(*) FROM calisthenics_set_logs WHERE category = :category")
    fun getTotalSetsCountForCategory(category: WorkoutCategory): Flow<Int>

    @Query("SELECT * FROM calisthenics_set_logs WHERE exerciseId = :exerciseId ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getLatestSetForExercise(exerciseId: String): Flow<CalisthenicsSetLog?>

    @Query("SELECT * FROM calisthenics_set_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentPerformanceHistory(limit: Int = 50): Flow<List<CalisthenicsSetLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLog(setLog: CalisthenicsSetLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSetLogs(setLogs: List<CalisthenicsSetLog>): List<Long>

    @Update
    suspend fun updateSetLog(setLog: CalisthenicsSetLog)

    @Delete
    suspend fun deleteSetLog(setLog: CalisthenicsSetLog)

    @Query("DELETE FROM calisthenics_set_logs WHERE id = :id")
    suspend fun deleteSetLogById(id: Long)

    @Query("DELETE FROM calisthenics_set_logs WHERE routineId = :routineId AND date = :date")
    suspend fun clearSetsForRoutineAndDate(routineId: String, date: String)
}

@Dao
interface SupplementDao {
    @Query("SELECT * FROM supplements ORDER BY brand ASC, name ASC")
    fun getAllSupplements(): Flow<List<SupplementEntity>>

    @Query("SELECT * FROM supplements WHERE brand = :brand ORDER BY name ASC")
    fun getSupplementsByBrand(brand: String): Flow<List<SupplementEntity>>

    @Query("SELECT * FROM supplements WHERE category = :category ORDER BY brand ASC, name ASC")
    fun getSupplementsByCategory(category: SupplementCategory): Flow<List<SupplementEntity>>

    @Query("SELECT * FROM supplements WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' OR activeIngredientsHu LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchSupplements(query: String): Flow<List<SupplementEntity>>

    @Query("SELECT * FROM supplements WHERE barcode = :barcode LIMIT 1")
    suspend fun getSupplementByBarcode(barcode: String): SupplementEntity?

    @Query("SELECT * FROM supplements WHERE id = :id LIMIT 1")
    fun getSupplementById(id: String): Flow<SupplementEntity?>

    @Query("SELECT * FROM supplements WHERE id = :id LIMIT 1")
    suspend fun getSupplementByIdDirect(id: String): SupplementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplement(supplement: SupplementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSupplements(supplements: List<SupplementEntity>)

    @Update
    suspend fun updateSupplement(supplement: SupplementEntity)

    @Delete
    suspend fun deleteSupplement(supplement: SupplementEntity)

    @Query("DELETE FROM supplements WHERE id = :id")
    suspend fun deleteSupplementById(id: String)

    @Query("SELECT COUNT(*) FROM supplements")
    suspend fun getSupplementCount(): Int

    @Query("SELECT DISTINCT brand FROM supplements ORDER BY brand ASC")
    fun getAllBrands(): Flow<List<String>>
}

@Dao
interface ExercisePlanDao {
    @Query("SELECT * FROM exercise_plans ORDER BY name ASC")
    fun getAllExercisePlans(): Flow<List<ExercisePlan>>

    @Query("SELECT * FROM exercise_plans WHERE id = :id LIMIT 1")
    fun getExercisePlanById(id: Long): Flow<ExercisePlan?>

    @Query("SELECT * FROM exercise_plans WHERE id = :id LIMIT 1")
    suspend fun getExercisePlanByIdDirect(id: Long): ExercisePlan?

    @Query("SELECT * FROM exercise_plans WHERE type = :type ORDER BY name ASC")
    fun getExercisePlansByType(type: String): Flow<List<ExercisePlan>>

    @Query("SELECT * FROM exercise_plans WHERE difficulty = :difficulty ORDER BY name ASC")
    fun getExercisePlansByDifficulty(difficulty: String): Flow<List<ExercisePlan>>

    @Query("SELECT * FROM exercise_plans WHERE name LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchExercisePlans(query: String): Flow<List<ExercisePlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercisePlan(plan: ExercisePlan): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExercisePlans(plans: List<ExercisePlan>): List<Long>

    @Update
    suspend fun updateExercisePlan(plan: ExercisePlan)

    @Delete
    suspend fun deleteExercisePlan(plan: ExercisePlan)

    @Query("DELETE FROM exercise_plans WHERE id = :id")
    suspend fun deleteExercisePlanById(id: Long)

    @Query("SELECT COUNT(*) FROM exercise_plans")
    suspend fun getExercisePlanCount(): Int
}
