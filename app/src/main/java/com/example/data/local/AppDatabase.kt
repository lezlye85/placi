package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.ExercisePlan
import com.example.data.model.ExerciseProgressEntry
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.SupplementEntity
import com.example.data.model.UserProfile
import com.example.data.model.WaterEntry
import com.example.data.model.WeightEntry
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutLog
import com.example.data.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FoodItem::class,
        MealEntry::class,
        WaterEntry::class,
        WeightEntry::class,
        WorkoutLog::class,
        ExerciseProgressEntry::class,
        CalisthenicsSetLog::class,
        UserProfile::class,
        SupplementEntity::class,
        ExercisePlan::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
    abstract fun waterDao(): WaterDao
    abstract fun weightDao(): WeightDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseProgressDao(): ExerciseProgressDao
    abstract fun calisthenicsSetDao(): CalisthenicsSetDao
    abstract fun userDao(): UserDao
    abstract fun supplementDao(): SupplementDao
    abstract fun exercisePlanDao(): ExercisePlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaloria_fit_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        // Prepopulate default foods
                        database.foodDao().insertAllFoods(InitialData.PREPOPULATED_FOODS)
                        // Prepopulate default user profile
                        database.userDao().insertOrUpdateProfile(UserProfile())
                        // Prepopulate an initial weight entry
                        val today = java.time.LocalDate.now().toString()
                        database.weightDao().insertWeight(
                            WeightEntry(date = today, weightKg = 82.0, note = "Kezdő súly")
                        )
                        // Prepopulate initial börtön and katonai performance history entries
                        database.calisthenicsSetDao().insertAllSetLogs(
                            listOf(
                                CalisthenicsSetLog(
                                    date = today,
                                    routineId = "routine_prison_big_six",
                                    routineTitle = "Fegyencedzés: A Mester Hatos (The Big 6)",
                                    category = WorkoutCategory.PRISON,
                                    exerciseId = "ex_prison_diamond_pushups",
                                    exerciseName = "Gyémánt börtön fekvőtámasz",
                                    setNumber = 1,
                                    targetReps = 15,
                                    repsCompleted = 15,
                                    rpe = 8,
                                    isPersonalRecord = true,
                                    notes = "Szigorú forma szűk cellatérben"
                                ),
                                CalisthenicsSetLog(
                                    date = today,
                                    routineId = "routine_prison_big_six",
                                    routineTitle = "Fegyencedzés: A Mester Hatos (The Big 6)",
                                    category = WorkoutCategory.PRISON,
                                    exerciseId = "ex_prison_diamond_pushups",
                                    exerciseName = "Gyémánt börtön fekvőtámasz",
                                    setNumber = 2,
                                    targetReps = 15,
                                    repsCompleted = 13,
                                    rpe = 9,
                                    isPersonalRecord = false,
                                    notes = "Tricepsz égés a végén"
                                ),
                                CalisthenicsSetLog(
                                    date = today,
                                    routineId = "routine_military_combat_conditioning",
                                    routineTitle = "Katonai Rohamosztagos Erőnléti Tréning",
                                    category = WorkoutCategory.MILITARY,
                                    exerciseId = "ex_military_burpee_broad_jump",
                                    exerciseName = "Katonai kommandós burpee távolugrással",
                                    setNumber = 1,
                                    targetReps = 12,
                                    repsCompleted = 12,
                                    rpe = 9,
                                    isPersonalRecord = true,
                                    notes = "Taktikai tempó, robbanékony ugrás"
                                ),
                                CalisthenicsSetLog(
                                    date = today,
                                    routineId = "routine_military_combat_conditioning",
                                    routineTitle = "Katonai Rohamosztagos Erőnléti Tréning",
                                    category = WorkoutCategory.MILITARY,
                                    exerciseId = "ex_military_burpee_broad_jump",
                                    exerciseName = "Katonai kommandós burpee távolugrással",
                                    setNumber = 2,
                                    targetReps = 12,
                                    repsCompleted = 10,
                                    rpe = 10,
                                    isPersonalRecord = false,
                                    notes = "Küzdelmes utolsó 2 ismétlés"
                                )
                            )
                        )
                        // Prepopulate curated BioTech, Scitec, and OstroVit supplements
                        val curatedSupplements = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
                        database.supplementDao().insertAllSupplements(curatedSupplements)

                        // Prepopulate default ExercisePlan entries
                        database.exercisePlanDao().insertAllExercisePlans(
                            listOf(
                                ExercisePlan(
                                    name = "Kezdő Teljes Test Áttörés",
                                    duration = 30,
                                    difficulty = "Kezdő",
                                    type = "Kalisztenika",
                                    description = "Fekvőtámasz, guggolás és plank alapozó otthoni edzés.",
                                    caloriesBurnEstimate = 180
                                ),
                                ExercisePlan(
                                    name = "Fegyencedzés: Mester Hatos",
                                    duration = 45,
                                    difficulty = "Haladó",
                                    type = "Kalisztenika",
                                    description = "Klasszikus Convict Conditioning testsúlyos erőfejlesztés.",
                                    caloriesBurnEstimate = 260
                                ),
                                ExercisePlan(
                                    name = "Katonai Rohamosztagos HIIT",
                                    duration = 35,
                                    difficulty = "Mester",
                                    type = "HIIT & Kardió",
                                    description = "Nagy intenzitású taktikai állóképességi tréning.",
                                    caloriesBurnEstimate = 320
                                ),
                                ExercisePlan(
                                    name = "Zsírégető Otthoni Tabata",
                                    duration = 20,
                                    difficulty = "Közepes",
                                    type = "HIIT",
                                    description = "Rövid, maximális pulzustartományú zsírégető etapok.",
                                    caloriesBurnEstimate = 210
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
