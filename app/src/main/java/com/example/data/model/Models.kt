package com.example.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

enum class MealType(val displayNameHu: String) {
    BREAKFAST("Reggeli"),
    LUNCH("Ebéd"),
    DINNER("Vacsora"),
    SNACK("Uzsonna / Nasi")
}

enum class FoodCategory(val displayNameHu: String, val iconName: String) {
    ALL("Összes", "all"),
    MEAT_FISH("Hús & Hal", "meat"),
    DAIRY_EGG("Tejtermék & Tojás", "dairy"),
    VEG_FRUIT("Zöldség & Gyümölcs", "fruit"),
    BAKERY_GRAIN("Pékáru & Gabonák", "bread"),
    MEALS("Készételek", "soup"),
    NUTS_SNACKS("Magvak & Nasi", "cookie"),
    DRINKS("Italok", "cup"),
    SUPPLEMENTS("Kiegészítők", "fitness")
}

@Entity(tableName = "foods")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String = "",
    val barcode: String = "",
    val category: String = FoodCategory.ALL.name,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val fiberPer100g: Double = 0.0,
    val defaultServingUnit: String = "g", // "g", "ml", "db", "adag"
    val defaultServingGrams: Double = 100.0,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false
) {
    @get:Ignore
    val calories: Double get() = caloriesPer100g

    @get:Ignore
    val protein: Double get() = proteinPer100g

    @get:Ignore
    val carbs: Double get() = carbsPer100g

    @get:Ignore
    val fat: Double get() = fatPer100g

    @get:Ignore
    val fiber: Double get() = fiberPer100g
}

@Entity(tableName = "meal_entries")
data class MealEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // format "YYYY-MM-DD"
    val mealType: MealType,
    val foodId: Long? = null,
    val foodName: String,
    val brand: String = "",
    val amountGrams: Double,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "water_entries")
data class WaterEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "YYYY-MM-DD"
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "YYYY-MM-DD"
    val weightKg: Double,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class DifficultyLevel(val displayNameHu: String) {
    BEGINNER("Kezdő"),
    INTERMEDIATE("Haladó"),
    ADVANCED("Mester")
}

enum class WorkoutCategory(val displayNameHu: String, val iconEmoji: String) {
    ALL("Összes", "⚡"),
    FULL_BODY("Teljes Test", "🏋️"),
    PRISON("Börtön edzés (Convict)", "⛓️"),
    MILITARY("Katonai & Taktikai", "🪖"),
    FAT_BURN_HIIT("Zsírégető HIIT", "🔥"),
    UPPER_BODY("Felsőtest & Kar", "💪"),
    CORE_ABS("Hasizom & Core", "🎯"),
    LOWER_BODY("Láb & Farizom", "🦵"),
    POSTURE_BACK("Hát & Mobilitás", "🧘"),
    EXPRESS("Gyors (<15 perc)", "⏱️")
}

data class CalisthenicsExercise(
    val id: String,
    val nameHu: String,
    val targetMuscle: String,
    val difficulty: DifficultyLevel,
    val sets: Int,
    val repsOrSec: String,
    val isTimer: Boolean = false,
    val durationSeconds: Int = 0,
    val caloriesBurnEstimate: Int,
    val instructionsHu: String,
    val tipsHu: String,
    val progressionHu: String = "",
    val stepByStepStepsHu: List<String> = emptyList(),
    val commonMistakesHu: List<String> = emptyList(),
    val breathingTipHu: String = "",
    val equipmentHu: String = "Nincs eszköz (Testsúly)",
    val easierAlternativeHu: String = "",
    val harderAlternativeHu: String = ""
) {
    val reps: Int get() = repsOrSec.filter { it.isDigit() }.take(3).toIntOrNull() ?: 10
}

data class CalisthenicsRoutine(
    val id: String,
    val titleHu: String,
    val subtitleHu: String,
    val difficulty: DifficultyLevel,
    val estimatedMinutes: Int,
    val totalCaloriesBurn: Int,
    val descriptionHu: String,
    val focusAreaHu: String,
    val exercises: List<CalisthenicsExercise>,
    val category: WorkoutCategory = WorkoutCategory.FULL_BODY,
    val recommendedScheduleHu: String = "Heti 3-4 alkalom (pl. Hétfő - Szerda - Péntek)",
    val requiredEquipmentHu: String = "100% Otthoni (Eszköz nélkül / Szék vagy Szőnyeg)",
    val warmUpHu: String = "3 perc karkörzés, csípőkörzés, láblendítés és helyben járás",
    val coolDownHu: String = "2 perc mellkas-, comb- és vádlinyújtás"
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "YYYY-MM-DD"
    val routineId: String,
    val routineTitle: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercise_plans")
data class ExercisePlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val duration: Int, // duration in minutes
    val difficulty: String, // e.g. "Kezdő", "Haladó", "Mester" or "BEGINNER", "INTERMEDIATE", "ADVANCED"
    val type: String, // e.g. "Kalisztenika", "HIIT", "Kardió", "Erőnléti"
    val description: String = "",
    val caloriesBurnEstimate: Int = 0,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    @get:Ignore
    val durationMinutes: Int get() = duration
}

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: String,
    val exerciseName: String,
    val date: String, // "YYYY-MM-DD"
    val setsCompleted: Int,
    val repsCompleted: Int,
    val weightAddedKg: Double = 0.0,
    val durationSeconds: Int = 0,
    val caloriesBurned: Int = 0,
    val isPersonalRecord: Boolean = false,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "calisthenics_set_logs")
data class CalisthenicsSetLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // format "YYYY-MM-DD"
    val routineId: String,
    val routineTitle: String,
    val category: WorkoutCategory, // e.g. PRISON, MILITARY
    val exerciseId: String,
    val exerciseName: String,
    val setNumber: Int,
    val targetReps: Int,
    val repsCompleted: Int,
    val weightAddedKg: Double = 0.0,
    val durationSeconds: Int = 0,
    val restSeconds: Int = 60,
    val rpe: Int = 8, // Rate of Perceived Exertion (1-10)
    val isPersonalRecord: Boolean = false,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyMacroTotals(
    val totalCalories: Double = 0.0,
    val totalProtein: Double = 0.0,
    val totalCarbs: Double = 0.0,
    val totalFat: Double = 0.0,
    val totalFiber: Double = 0.0
)

enum class Gender(val displayNameHu: String) {
    MALE("Férfi"),
    FEMALE("Nő")
}

enum class ActivityLevel(val displayNameHu: String, val multiplier: Double, val descriptionHu: String) {
    SEDENTARY("Ülő életmód", 1.2, "Kevés vagy semmi testmozgás"),
    LIGHT("Könnyű aktivitás", 1.375, "Heti 1-3 könnyű edzés/séta"),
    MODERATE("Közepes aktivitás", 1.55, "Heti 3-5 dinamikus edzés"),
    VERY_ACTIVE("Nagyon aktív", 1.725, "Heti 6-7 intenzív edzés / fizikai munka")
}

enum class GoalType(val displayNameHu: String, val calorieAdjustment: Int) {
    LOSE_INTENSE("Gyors fogyás (-0.75 kg/hét)", -750),
    LOSE_STEADY("Kiegyensúlyozott fogyás (-0.5 kg/hét)", -500),
    LOSE_MILD("Kíméletes fogyás (-0.25 kg/hét)", -250),
    MAINTAIN("Súlytartás", 0),
    GAIN_LEAN("Tiszta izomtömeg növelés (+0.25 kg/hét)", 300)
}

typealias WeightGoal = GoalType

enum class AppThemeMode(val displayNameHu: String, val subtitleHu: String) {
    SYSTEM("Rendszerkövető", "Igazodik a telefon általános beállításaihoz"),
    LIGHT("Világos téma", "Klasszikus, letisztult fehér háttér"),
    DARK("Sötét téma (OLED)", "Szemkímélő, energiatakarékos sötét felület")
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Felhasználó",
    val gender: Gender = Gender.MALE,
    val age: Int = 28,
    val heightCm: Double = 178.0,
    val currentWeightKg: Double = 82.0,
    val targetWeightKg: Double = 75.0,
    val activityLevel: ActivityLevel = ActivityLevel.LIGHT,
    val goalType: GoalType = GoalType.LOSE_STEADY,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val selectedWorkoutPlanId: String = "routine_beginner_fullbody",
    // Custom overrides if enabled (otherwise calculated via BMR & TDEE)
    val customTargetCalories: Int? = null,
    val customProteinGrams: Int? = null,
    val customCarbsGrams: Int? = null,
    val customFatGrams: Int? = null,
    val customWaterMl: Int? = null
) {
    // Mifflin - St Jeor Equation
    fun calculateBMR(): Double {
        return if (gender == Gender.MALE) {
            (10.0 * currentWeightKg) + (6.25 * heightCm) - (5.0 * age) + 5.0
        } else {
            (10.0 * currentWeightKg) + (6.25 * heightCm) - (5.0 * age) - 161.0
        }
    }

    fun calculateTDEE(): Double {
        return calculateBMR() * activityLevel.multiplier
    }

    fun getDailyCalorieTarget(): Int {
        if (customTargetCalories != null && customTargetCalories > 0) return customTargetCalories
        val target = (calculateTDEE() + goalType.calorieAdjustment).toInt()
        return target.coerceAtLeast(if (gender == Gender.MALE) 1500 else 1200)
    }

    fun getProteinTargetGrams(): Int {
        if (customProteinGrams != null && customProteinGrams > 0) return customProteinGrams
        // High protein for calisthenics & weight loss (~1.8g - 2.0g / kg)
        return (currentWeightKg * 1.9).toInt().coerceIn(60, 250)
    }

    fun getFatTargetGrams(): Int {
        if (customFatGrams != null && customFatGrams > 0) return customFatGrams
        // ~25% of calories
        val targetCal = getDailyCalorieTarget()
        return ((targetCal * 0.25) / 9.0).toInt().coerceIn(35, 120)
    }

    fun getCarbsTargetGrams(): Int {
        if (customCarbsGrams != null && customCarbsGrams > 0) return customCarbsGrams
        val targetCal = getDailyCalorieTarget()
        val proteinCal = getProteinTargetGrams() * 4
        val fatCal = getFatTargetGrams() * 9
        val remainingCal = (targetCal - proteinCal - fatCal).coerceAtLeast(200)
        return (remainingCal / 4.0).toInt().coerceIn(50, 400)
    }

    fun getWaterTargetMl(): Int {
        if (customWaterMl != null && customWaterMl > 0) return customWaterMl
        // ~35ml per kg + workout bonus
        return (currentWeightKg * 35).toInt().coerceIn(2000, 4000)
    }

    fun getBMI(): Double {
        val heightM = heightCm / 100.0
        return if (heightM > 0) currentWeightKg / (heightM * heightM) else 0.0
    }

    fun getBMICategory(): String {
        val bmi = getBMI()
        return when {
            bmi < 18.5 -> "Alultáplált"
            bmi < 25.0 -> "Normál testsúly"
            bmi < 30.0 -> "Túlsúly"
            bmi < 35.0 -> "I. fokú elhízás"
            else -> "Kifejezett elhízás"
        }
    }
}

// ==========================================
// 1. RECEPT & 1 HETES OLCSÓ ÉTLAP MODELLEK
// ==========================================

enum class CostLevel(val displayNameHu: String, val badgeText: String) {
    ULTRA_CHEAP("Ultra Olcsó (<500 Ft)", "💰 <500 Ft"),
    BUDGET("Pénztárcabarát (500-900 Ft)", "💰💰 500-900 Ft"),
    MODERATE("Közepes (900+ Ft)", "💰💰💰 900+ Ft")
}

enum class RecipeDietaryTag(val displayNameHu: String) {
    HIGH_PROTEIN("Magas fehérje"),
    BUDGET_SAVER("Diákbarát & Olcsó"),
    QUICK_MEAL("Gyors (<15 perc)"),
    LOW_CARB("Szénhidrátszegény"),
    FIBER_RICH("Rostdús"),
    VEGETARIAN("Vegetáriánus")
}

data class RecipeIngredient(
    val name: String,
    val amountHu: String,
    val costHuf: Int,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)

data class Recipe(
    val id: String,
    val titleHu: String,
    val subtitleHu: String,
    val mealType: MealType,
    val prepTimeMinutes: Int,
    val servings: Int = 1,
    val costLevel: CostLevel = CostLevel.ULTRA_CHEAP,
    val estimatedCostHuf: Int,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double = 0.0,
    val tags: List<RecipeDietaryTag>,
    val ingredients: List<RecipeIngredient>,
    val instructionsHu: List<String>,
    val emoji: String = "🍲",
    val budgetTip: String = ""
)

data class DayMealPlan(
    val dayNameHu: String,
    val dayNumber: Int,
    val breakfast: Recipe,
    val lunch: Recipe,
    val dinner: Recipe,
    val snack: Recipe
) {
    val totalCalories: Double get() = breakfast.calories + lunch.calories + dinner.calories + snack.calories
    val totalProtein: Double get() = breakfast.protein + lunch.protein + dinner.protein + snack.protein
    val totalCarbs: Double get() = breakfast.carbs + lunch.carbs + dinner.carbs + snack.carbs
    val totalFat: Double get() = breakfast.fat + lunch.fat + dinner.fat + snack.fat
    val totalFiber: Double get() = breakfast.fiber + lunch.fiber + dinner.fiber + snack.fiber
    val totalCostHuf: Int get() = breakfast.estimatedCostHuf + lunch.estimatedCostHuf + dinner.estimatedCostHuf + snack.estimatedCostHuf
}

data class WeeklyMealPlan(
    val titleHu: String,
    val goalTagHu: String,
    val dailyAvgCalories: Int,
    val dailyAvgCostHuf: Int,
    val totalWeeklyCostHuf: Int,
    val days: List<DayMealPlan>
)

data class ShoppingItem(
    val id: String,
    val name: String,
    val amount: String,
    val category: String,
    val estimatedCostHuf: Int,
    val isBought: Boolean = false
)

// ==========================================
// 2. KÖZÖSSÉGI & KIHÍVÁS & DÍJAK MODELLEK
// ==========================================

enum class PostCategory(val displayNameHu: String, val iconEmoji: String) {
    ALL("Összes", "🌐"),
    MEAL_SHARE("Napi Étel & Recept", "🥗"),
    WORKOUT_WIN("Kalisztenika & Edzés", "💪"),
    TRANSFORMATION("Sikertörténet & Súly", "🏆"),
    TIPS_MOTIVATION("Tippek & Motiváció", "🔥")
}

data class PostComment(
    val id: String,
    val authorName: String,
    val authorAvatarEmoji: String,
    val text: String,
    val timeAgoHu: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CommunityPost(
    val id: String,
    val authorName: String,
    val authorBadge: String,
    val authorAvatarEmoji: String,
    val timeAgoHu: String,
    val category: PostCategory,
    val title: String,
    val content: String,
    val statsTag: String = "",
    val likesCount: Int,
    val isLikedByMe: Boolean = false,
    val commentsCount: Int,
    val comments: List<PostComment> = emptyList(),
    val cheersCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class FitnessChallenge(
    val id: String,
    val titleHu: String,
    val monthHu: String,
    val descriptionHu: String,
    val targetDays: Int = 30,
    val completedDays: Int = 18,
    val rewardBadgeName: String,
    val rewardBadgeEmoji: String,
    val dailyTasksHu: List<String>,
    val pointsReward: Int = 500,
    val isJoined: Boolean = true
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val avatarEmoji: String,
    val badgeTitle: String,
    val weeklyPoints: Int,
    val streakDays: Int,
    val isCurrentUser: Boolean = false,
    val recentActivity: String
)

data class VirtualBadge(
    val id: String,
    val titleHu: String,
    val descriptionHu: String,
    val emoji: String,
    val isUnlocked: Boolean,
    val unlockedDateHu: String? = null,
    val progressCurrent: Int = 0,
    val progressMax: Int = 1,
    val rarityHu: String = "Közönséges"
)

// ==========================================
// 3. STATISZTIKAI TRENDEK & HAVI ÖSSZEFOGLALÓ
// ==========================================

data class MonthlyStatsSummary(
    val monthYearHu: String,
    val totalCaloriesBurned: Int,
    val avgDailyConsumedCalories: Int,
    val avgDailyDeficitCalories: Int,
    val totalWorkoutsCount: Int,
    val totalWorkoutMinutes: Int,
    val avgDailyProteinGrams: Double,
    val avgDailyWaterMl: Int,
    val estimatedFatLostKg: Double,
    val startingWeightKg: Double,
    val endingWeightKg: Double,
    val bestStreakDays: Int,
    val consistencyScorePercent: Int
)

data class MacroTrendItem(
    val label: String,
    val avgProteinGrams: Double,
    val avgCarbsGrams: Double,
    val avgFatGrams: Double,
    val avgTotalCalories: Double
)

data class WorkoutFrequencyItem(
    val label: String,
    val sessionCount: Int,
    val totalMinutes: Int,
    val targetSessions: Int = 16
)

// ==========================================
// 4. ÉTREND-KIEGÉSZÍTŐ (SUPPLEMENT) MODELLEK
// ==========================================

enum class SupplementCategory(val displayNameHu: String, val iconEmoji: String) {
    ALL("Összes", "💊"),
    PROTEIN("Fehérjék & Proteinek", "🥛"),
    CREATINE("Kreatin & Erőfokozók", "⚡"),
    VITAMINS_MINERALS("Vitaminok & Ásványi Anyagok", "🛡️"),
    JOINT_HEALTH("Ízületvédelem & Kollagén", "🦴"),
    PRE_WORKOUT("Edzés Előtti Pörgetők", "🔥"),
    AMINO_ACIDS("BCAA, EAA & Glutamin", "🧬"),
    FAT_BURNERS("Zsírégetők & L-Karnitin", "⚡"),
    RECOVERY_SLEEP("Alvás & Regeneráció", "🌙")
}

enum class SupplementTiming(val displayNameHu: String, val iconEmoji: String) {
    MORNING_FASTED("Reggel éhgyomorra", "🌅"),
    WITH_BREAKFAST("Reggeli étkezéssel", "🍳"),
    PRE_WORKOUT("Edzés előtt 20-30 perccel", "⚡"),
    INTRA_WORKOUT("Edzés közben kortyolgatva", "🥤"),
    POST_WORKOUT("Edzés után azonnal", "🏋️"),
    WITH_MEAL("Bármely főétkezéssel", "🍽️"),
    BEFORE_BED("Lefekvés előtt 30-45 perccel", "🌙"),
    DAILY_ANYTIME("Bármikor a nap folyamán", "🕒")
}

enum class SupplementGoal(val displayNameHu: String, val iconEmoji: String) {
    ALL("Összes cél", "🎯"),
    MUSCLE_BUILDING("Izomtömeg Növelés", "💪"),
    FAT_LOSS("Fogyás & Szálkásítás", "🔥"),
    STRENGTH_ENDURANCE("Erő & Állóképesség", "⚡"),
    HEALTH_IMMUNITY("Alap Egészség & Immunrendszer", "🛡️"),
    JOINTS_MOBILITY("Ízületvédelem & Porcok", "🦴"),
    SLEEP_STRESS("Pihentető Alvás & Stresszoldás", "🌙")
}

data class SupplementItem(
    val id: String,
    val name: String,
    val brand: String,
    val barcode: String,
    val category: SupplementCategory,
    val targetGoals: List<SupplementGoal>,
    val primaryTiming: SupplementTiming,
    val timingDescriptionHu: String,
    val recommendedDosageHu: String,
    val activeIngredientsHu: String,
    val whyTakeItHu: String,
    val stackingTipsHu: String,
    val caloriesPerServing: Double,
    val proteinPerServing: Double,
    val carbsPerServing: Double,
    val fatPerServing: Double,
    val servingGrams: Double,
    val servingUnitHu: String,
    val badgeTagHu: String = "⭐ Ajánlott"
)

@Entity(tableName = "supplements")
data class SupplementEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val brand: String, // "BioTechUSA", "Scitec Nutrition", "OstroVit"
    val barcode: String = "",
    val category: SupplementCategory,
    val targetGoals: String = "", // Comma-separated SupplementGoal names
    val primaryTiming: SupplementTiming,
    val timingDescriptionHu: String = "",
    val recommendedDosageHu: String = "",
    val activeIngredientsHu: String = "",
    val whyTakeItHu: String = "",
    val stackingTipsHu: String = "",
    val caloriesPerServing: Double = 0.0,
    val proteinPerServing: Double = 0.0,
    val carbsPerServing: Double = 0.0,
    val fatPerServing: Double = 0.0,
    val servingGrams: Double = 0.0,
    val servingUnitHu: String = "",
    val badgeTagHu: String = "⭐ Ajánlott"
) {
    fun toSupplementItem(): SupplementItem {
        val goals = if (targetGoals.isBlank()) {
            emptyList()
        } else {
            targetGoals.split(",")
                .mapNotNull { name ->
                    try {
                        SupplementGoal.valueOf(name.trim())
                    } catch (e: Exception) {
                        null
                    }
                }
        }
        return SupplementItem(
            id = id,
            name = name,
            brand = brand,
            barcode = barcode,
            category = category,
            targetGoals = goals,
            primaryTiming = primaryTiming,
            timingDescriptionHu = timingDescriptionHu,
            recommendedDosageHu = recommendedDosageHu,
            activeIngredientsHu = activeIngredientsHu,
            whyTakeItHu = whyTakeItHu,
            stackingTipsHu = stackingTipsHu,
            caloriesPerServing = caloriesPerServing,
            proteinPerServing = proteinPerServing,
            carbsPerServing = carbsPerServing,
            fatPerServing = fatPerServing,
            servingGrams = servingGrams,
            servingUnitHu = servingUnitHu,
            badgeTagHu = badgeTagHu
        )
    }
}

fun SupplementItem.toEntity(): SupplementEntity = SupplementEntity(
    id = id,
    name = name,
    brand = brand,
    barcode = barcode,
    category = category,
    targetGoals = targetGoals.joinToString(",") { it.name },
    primaryTiming = primaryTiming,
    timingDescriptionHu = timingDescriptionHu,
    recommendedDosageHu = recommendedDosageHu,
    activeIngredientsHu = activeIngredientsHu,
    whyTakeItHu = whyTakeItHu,
    stackingTipsHu = stackingTipsHu,
    caloriesPerServing = caloriesPerServing,
    proteinPerServing = proteinPerServing,
    carbsPerServing = carbsPerServing,
    fatPerServing = fatPerServing,
    servingGrams = servingGrams,
    servingUnitHu = servingUnitHu,
    badgeTagHu = badgeTagHu
)

data class UserDailySupplement(
    val id: String,
    val supplementId: String,
    val supplementName: String,
    val brand: String,
    val timing: SupplementTiming,
    val dosageText: String,
    val proteinGrams: Double = 0.0,
    val calories: Double = 0.0,
    val isTakenToday: Boolean = false,
    val takenTimeHu: String? = null
)


