package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.local.RecipeAndCommunityData
import com.example.data.model.CalisthenicsRoutine
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.CommunityPost
import com.example.data.model.CostLevel
import com.example.data.model.DayMealPlan
import com.example.data.model.FitnessChallenge
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.LeaderboardUser
import com.example.data.model.MacroTrendItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.model.MonthlyStatsSummary
import com.example.data.model.PostCategory
import com.example.data.model.Recipe
import com.example.data.model.RecipeDietaryTag
import com.example.data.model.ShoppingItem
import com.example.data.model.UserProfile
import com.example.data.model.VirtualBadge
import com.example.data.model.WaterEntry
import com.example.data.model.WeeklyMealPlan
import com.example.data.model.WeightEntry
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutFrequencyItem
import com.example.data.model.WorkoutLog
import com.example.data.repository.CommunityRepository
import com.example.data.repository.NutritionRepository
import com.example.data.repository.RecipeRepository
import com.example.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DiaryDaySummary(
    val date: String,
    val targetCalories: Int,
    val consumedCalories: Double,
    val burnedCalories: Int,
    val remainingCalories: Double,
    val totalProtein: Double,
    val targetProtein: Int,
    val totalCarbs: Double,
    val targetCarbs: Int,
    val totalFat: Double,
    val targetFat: Int,
    val totalFiber: Double,
    val waterMl: Int,
    val targetWaterMl: Int,
    val waterEntries: List<WaterEntry> = emptyList(),
    val mealsByType: Map<MealType, List<MealEntry>>,
    val workoutsToday: List<WorkoutLog>
)

class DiaryViewModel(
    private val nutritionRepository: NutritionRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val userProfile: StateFlow<UserProfile> = nutritionRepository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val daySummary: StateFlow<DiaryDaySummary> = _selectedDate.flatMapLatest { date ->
        combine(
            nutritionRepository.getMealsForDate(date),
            nutritionRepository.getWaterForDate(date),
            workoutRepository.getLogsForDate(date),
            nutritionRepository.userProfile
        ) { meals, waterEntriesList, workouts, profileOpt ->
            val profile = profileOpt ?: UserProfile()
            val totalCal = meals.sumOf { it.calories }
            val totalProt = meals.sumOf { it.protein }
            val totalCarb = meals.sumOf { it.carbs }
            val totalFat = meals.sumOf { it.fat }
            val totalFib = meals.sumOf { it.fiber }
            val burned = workouts.sumOf { it.caloriesBurned }
            val targetCal = profile.getDailyCalorieTarget()
            val remaining = (targetCal + burned) - totalCal
            val totalWater = waterEntriesList.sumOf { it.amountMl }

            val grouped = MealType.values().associateWith { type ->
                meals.filter { it.mealType == type }
            }

            DiaryDaySummary(
                date = date,
                targetCalories = targetCal,
                consumedCalories = totalCal,
                burnedCalories = burned,
                remainingCalories = remaining,
                totalProtein = totalProt,
                targetProtein = profile.getProteinTargetGrams(),
                totalCarbs = totalCarb,
                targetCarbs = profile.getCarbsTargetGrams(),
                totalFat = totalFat,
                targetFat = profile.getFatTargetGrams(),
                totalFiber = totalFib,
                waterMl = totalWater,
                targetWaterMl = profile.getWaterTargetMl(),
                waterEntries = waterEntriesList,
                mealsByType = grouped,
                workoutsToday = workouts
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DiaryDaySummary(
            date = LocalDate.now().toString(),
            targetCalories = 2000,
            consumedCalories = 0.0,
            burnedCalories = 0,
            remainingCalories = 2000.0,
            totalProtein = 0.0,
            targetProtein = 140,
            totalCarbs = 0.0,
            targetCarbs = 200,
            totalFat = 0.0,
            targetFat = 60,
            totalFiber = 0.0,
            waterMl = 0,
            targetWaterMl = 2500,
            waterEntries = emptyList(),
            mealsByType = emptyMap(),
            workoutsToday = emptyList()
        )
    )

    init {
        viewModelScope.launch {
            nutritionRepository.ensureInitialized()
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun changeDateByDays(days: Long) {
        val current = LocalDate.parse(_selectedDate.value)
        _selectedDate.value = current.plusDays(days).toString()
    }

    fun setToday() {
        _selectedDate.value = LocalDate.now().toString()
    }

    fun addMeal(
        mealType: MealType,
        food: FoodItem,
        amountGrams: Double
    ) {
        viewModelScope.launch {
            val ratio = amountGrams / 100.0
            val entry = MealEntry(
                date = _selectedDate.value,
                mealType = mealType,
                foodId = food.id,
                foodName = food.name,
                brand = food.brand,
                amountGrams = amountGrams,
                calories = food.caloriesPer100g * ratio,
                protein = food.proteinPer100g * ratio,
                carbs = food.carbsPer100g * ratio,
                fat = food.fatPer100g * ratio,
                fiber = food.fiberPer100g * ratio
            )
            nutritionRepository.insertMeal(entry)
        }
    }

    fun addQuickCalories(
        mealType: MealType,
        name: String,
        calories: Double,
        protein: Double = 0.0,
        carbs: Double = 0.0,
        fat: Double = 0.0
    ) {
        viewModelScope.launch {
            val entry = MealEntry(
                date = _selectedDate.value,
                mealType = mealType,
                foodName = name.ifBlank { "Gyors kalória" },
                amountGrams = 100.0,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                fiber = 0.0
            )
            nutritionRepository.insertMeal(entry)
        }
    }

    fun deleteMeal(meal: MealEntry) {
        viewModelScope.launch {
            nutritionRepository.deleteMeal(meal)
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            nutritionRepository.addWater(_selectedDate.value, amountMl)
        }
    }

    fun removeLastWater() {
        viewModelScope.launch {
            nutritionRepository.removeLastWater(_selectedDate.value)
        }
    }

    fun deleteWaterEntry(id: Long) {
        viewModelScope.launch {
            nutritionRepository.deleteWaterEntry(id)
        }
    }

    fun clearAllWaterToday() {
        viewModelScope.launch {
            nutritionRepository.clearWaterForDate(_selectedDate.value)
        }
    }

    fun setCustomWaterTarget(targetMl: Int?) {
        viewModelScope.launch {
            val current = userProfile.value
            nutritionRepository.updateProfile(current.copy(customWaterMl = targetMl))
        }
    }
}

class FoodDatabaseViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(FoodCategory.ALL)
    val selectedCategory: StateFlow<FoodCategory> = _selectedCategory.asStateFlow()

    val foods: StateFlow<List<FoodItem>> = combine(
        _searchQuery,
        _selectedCategory,
        nutritionRepository.allFoods
    ) { query, category, allList ->
        var list = allList
        if (category != FoodCategory.ALL) {
            list = list.filter { it.category == category.name }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.brand.lowercase().contains(q) ||
                it.barcode.contains(q)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Barcode Search State
    private val _barcodeLookupState = MutableStateFlow<BarcodeSearchState>(BarcodeSearchState.Idle)
    val barcodeLookupState: StateFlow<BarcodeSearchState> = _barcodeLookupState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: FoodCategory) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(food: FoodItem) {
        viewModelScope.launch {
            nutritionRepository.updateFood(food.copy(isFavorite = !food.isFavorite))
        }
    }

    fun createCustomFood(
        name: String,
        brand: String,
        barcode: String,
        category: FoodCategory,
        calories: Double,
        protein: Double,
        carbs: Double,
        fat: Double,
        fiber: Double,
        servingUnit: String,
        servingGrams: Double
    ) {
        viewModelScope.launch {
            val item = FoodItem(
                name = name,
                brand = brand,
                barcode = barcode,
                category = category.name,
                caloriesPer100g = calories,
                proteinPer100g = protein,
                carbsPer100g = carbs,
                fatPer100g = fat,
                fiberPer100g = fiber,
                defaultServingUnit = servingUnit.ifBlank { "g" },
                defaultServingGrams = if (servingGrams > 0) servingGrams else 100.0,
                isCustom = true
            )
            nutritionRepository.insertFood(item)
        }
    }

    fun searchBarcode(barcode: String) {
        if (barcode.isBlank()) return
        viewModelScope.launch {
            _barcodeLookupState.value = BarcodeSearchState.Searching(barcode)
            val found = nutritionRepository.findFoodByBarcode(barcode)
            if (found != null) {
                _barcodeLookupState.value = BarcodeSearchState.Found(found)
            } else {
                _barcodeLookupState.value = BarcodeSearchState.NotFound(barcode)
            }
        }
    }

    fun clearBarcodeState() {
        _barcodeLookupState.value = BarcodeSearchState.Idle
    }
}

sealed interface BarcodeSearchState {
    object Idle : BarcodeSearchState
    data class Searching(val barcode: String) : BarcodeSearchState
    data class Found(val food: FoodItem) : BarcodeSearchState
    data class NotFound(val barcode: String) : BarcodeSearchState
}

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val nutritionRepository: NutritionRepository? = null
) : ViewModel() {

    val routines: List<CalisthenicsRoutine> = workoutRepository.routines
    val workoutLogs: StateFlow<List<WorkoutLog>> = workoutRepository.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prisonPerformanceHistory: StateFlow<List<CalisthenicsSetLog>> =
        workoutRepository.getPrisonPerformanceHistory()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val militaryPerformanceHistory: StateFlow<List<CalisthenicsSetLog>> =
        workoutRepository.getMilitaryPerformanceHistory()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCalisthenicsSets: StateFlow<List<CalisthenicsSetLog>> =
        workoutRepository.getAllCalisthenicsSets()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Category & Search Filtering
    private val _selectedCategory = MutableStateFlow(WorkoutCategory.ALL)
    val selectedCategory: StateFlow<WorkoutCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected plan for full detail / step-by-step instruction viewer
    private val _selectedPlanForDetail = MutableStateFlow<CalisthenicsRoutine?>(null)
    val selectedPlanForDetail: StateFlow<CalisthenicsRoutine?> = _selectedPlanForDetail.asStateFlow()

    // User's active primary chosen workout plan
    private val _activePlanId = MutableStateFlow("routine_beginner_fullbody")
    val activePlanId: StateFlow<String> = _activePlanId.asStateFlow()

    private val _planSelectToastMessage = MutableStateFlow<String?>(null)
    val planSelectToastMessage: StateFlow<String?> = _planSelectToastMessage.asStateFlow()

    init {
        if (nutritionRepository != null) {
            viewModelScope.launch {
                nutritionRepository.userProfile.collect { profile ->
                    if (profile != null && profile.selectedWorkoutPlanId.isNotBlank()) {
                        _activePlanId.value = profile.selectedWorkoutPlanId
                    }
                }
            }
        }
    }

    fun selectCategory(category: WorkoutCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPlanForDetail(routine: CalisthenicsRoutine?) {
        _selectedPlanForDetail.value = routine
    }

    fun setActivePrimaryPlan(routine: CalisthenicsRoutine) {
        _activePlanId.value = routine.id
        _planSelectToastMessage.value = "A(z) „${routine.titleHu}” beállítva elsődleges heti tervedként!"
        if (nutritionRepository != null) {
            viewModelScope.launch {
                val current = nutritionRepository.userProfile.firstOrNull() ?: UserProfile()
                nutritionRepository.updateProfile(current.copy(selectedWorkoutPlanId = routine.id))
            }
        }
        viewModelScope.launch {
            delay(3000)
            _planSelectToastMessage.value = null
        }
    }

    fun clearPlanSelectToast() {
        _planSelectToastMessage.value = null
    }

    // Active Workout Session state
    private val _activeRoutine = MutableStateFlow<CalisthenicsRoutine?>(null)
    val activeRoutine: StateFlow<CalisthenicsRoutine?> = _activeRoutine.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _currentSet = MutableStateFlow(1)
    val currentSet: StateFlow<Int> = _currentSet.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

    private val _timerSecondsLeft = MutableStateFlow(0)
    val timerSecondsLeft: StateFlow<Int> = _timerSecondsLeft.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _sessionCompleted = MutableStateFlow(false)
    val sessionCompleted: StateFlow<Boolean> = _sessionCompleted.asStateFlow()

    private var timerJob: Job? = null

    fun startRoutine(routine: CalisthenicsRoutine) {
        _activeRoutine.value = routine
        _selectedPlanForDetail.value = null
        _currentExerciseIndex.value = 0
        _currentSet.value = 1
        _isResting.value = false
        _sessionCompleted.value = false
        val firstEx = routine.exercises.firstOrNull()
        if (firstEx?.isTimer == true && firstEx.durationSeconds > 0) {
            _timerSecondsLeft.value = firstEx.durationSeconds
        } else {
            _timerSecondsLeft.value = 0
        }
    }

    fun toggleExerciseTimer() {
        if (_isTimerRunning.value) {
            pauseTimer()
        } else {
            resumeTimer()
        }
    }

    private fun resumeTimer() {
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSecondsLeft.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerSecondsLeft.value -= 1
            }
            _isTimerRunning.value = false
            if (_timerSecondsLeft.value == 0) {
                // If it was rest timer or exercise timer finished
                if (_isResting.value) {
                    _isResting.value = false
                }
            }
        }
    }

    private fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun completeCurrentSet(customReps: Int? = null, customRpe: Int? = null, notes: String = "") {
        val routine = _activeRoutine.value ?: return
        val currentEx = routine.exercises.getOrNull(_currentExerciseIndex.value) ?: return
        val currentSetNumber = _currentSet.value
        val actualReps = customReps ?: currentEx.reps

        // Log set to Room database for performance history
        viewModelScope.launch {
            workoutRepository.logCalisthenicsSet(
                CalisthenicsSetLog(
                    date = LocalDate.now().toString(),
                    routineId = routine.id,
                    routineTitle = routine.titleHu,
                    category = routine.category,
                    exerciseId = currentEx.id,
                    exerciseName = currentEx.nameHu,
                    setNumber = currentSetNumber,
                    targetReps = currentEx.reps,
                    repsCompleted = actualReps,
                    rpe = customRpe ?: 8,
                    notes = notes.ifBlank { "${currentEx.targetMuscle} - széria $currentSetNumber" }
                )
            )
        }

        if (_currentSet.value < currentEx.sets) {
            // Start Rest timer (45 seconds)
            _currentSet.value += 1
            startRestTimer(45)
        } else {
            // Next Exercise
            if (_currentExerciseIndex.value < routine.exercises.size - 1) {
                _currentExerciseIndex.value += 1
                _currentSet.value = 1
                val nextEx = routine.exercises[_currentExerciseIndex.value]
                if (nextEx.isTimer && nextEx.durationSeconds > 0) {
                    _timerSecondsLeft.value = nextEx.durationSeconds
                }
                startRestTimer(60) // 60s rest between exercises
            } else {
                // Complete Workout!
                finishWorkoutSession()
            }
        }
    }

    private fun startRestTimer(seconds: Int) {
        _isResting.value = true
        _timerSecondsLeft.value = seconds
        resumeTimer()
    }

    fun skipRest() {
        pauseTimer()
        _isResting.value = false
        val routine = _activeRoutine.value ?: return
        val currentEx = routine.exercises.getOrNull(_currentExerciseIndex.value) ?: return
        if (currentEx.isTimer && currentEx.durationSeconds > 0) {
            _timerSecondsLeft.value = currentEx.durationSeconds
        } else {
            _timerSecondsLeft.value = 0
        }
    }

    fun finishWorkoutSession() {
        val routine = _activeRoutine.value ?: return
        pauseTimer()
        _sessionCompleted.value = true
        viewModelScope.launch {
            workoutRepository.logWorkout(
                date = LocalDate.now().toString(),
                routineId = routine.id,
                routineTitle = routine.titleHu,
                durationMinutes = routine.estimatedMinutes,
                caloriesBurned = routine.totalCaloriesBurn,
                notes = "Teljesített kalisztenika edzés"
            )
        }
    }

    fun logCustomWorkout(
        title: String,
        durationMinutes: Int,
        caloriesBurned: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            workoutRepository.logWorkout(
                date = LocalDate.now().toString(),
                routineId = "custom_workout",
                routineTitle = title.ifBlank { "Egyéni edzés" },
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned,
                notes = notes
            )
        }
    }

    fun deleteLog(log: WorkoutLog) {
        viewModelScope.launch {
            workoutRepository.deleteWorkoutLog(log)
        }
    }

    fun logCalisthenicsSetManual(
        routineId: String,
        routineTitle: String,
        category: WorkoutCategory,
        exerciseId: String,
        exerciseName: String,
        setNumber: Int,
        targetReps: Int,
        repsCompleted: Int,
        weightAddedKg: Double = 0.0,
        rpe: Int = 8,
        isPersonalRecord: Boolean = false,
        notes: String = ""
    ) {
        viewModelScope.launch {
            workoutRepository.logCalisthenicsSet(
                CalisthenicsSetLog(
                    date = LocalDate.now().toString(),
                    routineId = routineId,
                    routineTitle = routineTitle,
                    category = category,
                    exerciseId = exerciseId,
                    exerciseName = exerciseName,
                    setNumber = setNumber,
                    targetReps = targetReps,
                    repsCompleted = repsCompleted,
                    weightAddedKg = weightAddedKg,
                    rpe = rpe,
                    isPersonalRecord = isPersonalRecord,
                    notes = notes
                )
            )
        }
    }

    fun deleteCalisthenicsSet(id: Long) {
        viewModelScope.launch {
            workoutRepository.deleteCalisthenicsSetById(id)
        }
    }

    fun exitRoutine() {
        pauseTimer()
        _activeRoutine.value = null
        _sessionCompleted.value = false
    }
}

class StatsViewModel(
    private val nutritionRepository: NutritionRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val weights: StateFlow<List<WeightEntry>> = nutritionRepository.allWeights
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfile> = nutritionRepository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val allWorkouts: StateFlow<List<WorkoutLog>> = workoutRepository.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 7-day intake history
    val pastWeekMeals: StateFlow<List<MealEntry>> = nutritionRepository
        .getMealsBetweenDates(
            LocalDate.now().minusDays(6).toString(),
            LocalDate.now().toString()
        )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Multi-month summary cards
    val monthlySummaries: StateFlow<List<MonthlyStatsSummary>> = MutableStateFlow(RecipeAndCommunityData.MONTHLY_SUMMARIES)
    private val _selectedMonthIndex = MutableStateFlow(0)
    val selectedMonthIndex: StateFlow<Int> = _selectedMonthIndex.asStateFlow()

    // Multi-month macro & workout frequency trends
    val macroTrends: StateFlow<List<MacroTrendItem>> = MutableStateFlow(RecipeAndCommunityData.MACRO_TRENDS)
    val workoutFrequencyTrends: StateFlow<List<WorkoutFrequencyItem>> = MutableStateFlow(RecipeAndCommunityData.WORKOUT_FREQUENCY_TRENDS)

    fun selectMonthSummary(index: Int) {
        if (index in 0..2) {
            _selectedMonthIndex.value = index
        }
    }

    fun logNewWeight(weightKg: Double, note: String = "") {
        viewModelScope.launch {
            nutritionRepository.logWeight(
                date = LocalDate.now().toString(),
                weightKg = weightKg,
                note = note
            )
        }
    }

    fun deleteWeight(entry: WeightEntry) {
        viewModelScope.launch {
            nutritionRepository.deleteWeight(entry)
        }
    }
}

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val recipes: StateFlow<List<Recipe>> = recipeRepository.recipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeAndCommunityData.RECIPES)

    val weeklyMealPlan: StateFlow<WeeklyMealPlan> = recipeRepository.weeklyMealPlan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeAndCommunityData.generateWeeklyMealPlan(2000))

    val shoppingList: StateFlow<List<ShoppingItem>> = recipeRepository.shoppingList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeAndCommunityData.INITIAL_SHOPPING_LIST)

    val userProfile: StateFlow<UserProfile> = nutritionRepository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMealTypeFilter = MutableStateFlow<MealType?>(null)
    val selectedMealTypeFilter: StateFlow<MealType?> = _selectedMealTypeFilter.asStateFlow()

    private val _selectedTagFilter = MutableStateFlow<RecipeDietaryTag?>(null)
    val selectedTagFilter: StateFlow<RecipeDietaryTag?> = _selectedTagFilter.asStateFlow()

    private val _selectedCostFilter = MutableStateFlow<CostLevel?>(null)
    val selectedCostFilter: StateFlow<CostLevel?> = _selectedCostFilter.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _selectedDayIndex = MutableStateFlow(0) // 0..6 (Hétfő..Vasárnap)
    val selectedDayIndex: StateFlow<Int> = _selectedDayIndex.asStateFlow()

    private val _logSuccessMessage = MutableStateFlow<String?>(null)
    val logSuccessMessage: StateFlow<String?> = _logSuccessMessage.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMealTypeFilter(type: MealType?) {
        _selectedMealTypeFilter.value = type
    }

    fun setTagFilter(tag: RecipeDietaryTag?) {
        _selectedTagFilter.value = tag
    }

    fun setCostFilter(cost: CostLevel?) {
        _selectedCostFilter.value = cost
    }

    fun selectRecipe(recipe: Recipe?) {
        _selectedRecipe.value = recipe
    }

    fun selectDayIndex(index: Int) {
        _selectedDayIndex.value = index.coerceIn(0, 6)
    }

    fun regeneratePlanForGoal(goalName: String) {
        val targetCal = userProfile.value.getDailyCalorieTarget()
        recipeRepository.regenerateWeeklyPlan(targetCal, goalName)
    }

    fun toggleShoppingItem(itemId: String) {
        recipeRepository.toggleShoppingItem(itemId)
    }

    fun addShoppingItem(name: String, amount: String, category: String, costHuf: Int) {
        recipeRepository.addCustomShoppingItem(name, amount, category, costHuf)
    }

    fun logRecipeToToday(recipe: Recipe, mealType: MealType = recipe.mealType) {
        viewModelScope.launch {
            recipeRepository.logRecipeToDiary(
                date = LocalDate.now().toString(),
                recipe = recipe,
                mealType = mealType
            )
            _logSuccessMessage.value = "${recipe.titleHu} hozzáadva a mai naplóhoz!"
            delay(3000)
            _logSuccessMessage.value = null
        }
    }

    fun clearLogMessage() {
        _logSuccessMessage.value = null
    }
}

class CommunityViewModel(
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val posts: StateFlow<List<CommunityPost>> = communityRepository.posts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val challenge: StateFlow<FitnessChallenge> = communityRepository.challenge
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeAndCommunityData.CURRENT_CHALLENGE)

    val leaderboard: StateFlow<List<LeaderboardUser>> = communityRepository.leaderboard
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val badges: StateFlow<List<VirtualBadge>> = communityRepository.badges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow(PostCategory.ALL)
    val selectedCategory: StateFlow<PostCategory> = _selectedCategory.asStateFlow()

    private val _selectedBadge = MutableStateFlow<VirtualBadge?>(null)
    val selectedBadge: StateFlow<VirtualBadge?> = _selectedBadge.asStateFlow()

    fun selectCategory(category: PostCategory) {
        _selectedCategory.value = category
    }

    fun selectBadge(badge: VirtualBadge?) {
        _selectedBadge.value = badge
    }

    fun toggleLike(postId: String) {
        communityRepository.toggleLike(postId)
    }

    fun addCheer(postId: String) {
        communityRepository.addCheer(postId)
    }

    fun addComment(postId: String, text: String) {
        communityRepository.addComment(postId, text)
    }

    fun createPost(
        title: String,
        content: String,
        category: PostCategory,
        statsTag: String = ""
    ) {
        communityRepository.createPost(
            title = title,
            content = content,
            category = category,
            statsTag = statsTag
        )
    }

    fun toggleChallengeJoin() {
        communityRepository.joinOrLeaveChallenge()
    }

    fun completeTodayChallenge() {
        communityRepository.incrementChallengeDay()
    }
}

class ProfileViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val profile: StateFlow<UserProfile> = nutritionRepository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    fun updateProfile(updated: UserProfile) {
        viewModelScope.launch {
            nutritionRepository.updateProfile(updated)
        }
    }

    fun updateHeightAndWeight(heightCm: Double, weightKg: Double) {
        viewModelScope.launch {
            val current = profile.value
            nutritionRepository.updateProfile(current.copy(heightCm = heightCm, currentWeightKg = weightKg))
            nutritionRepository.logWeight(
                date = LocalDate.now().toString(),
                weightKg = weightKg,
                note = "BMI kalkulátorból frissítve"
            )
        }
    }

    fun setThemeMode(mode: com.example.data.model.AppThemeMode) {
        viewModelScope.launch {
            val current = profile.value
            nutritionRepository.updateProfile(current.copy(themeMode = mode))
        }
    }

    fun toggleTheme(isSystemDark: Boolean) {
        viewModelScope.launch {
            val current = profile.value
            val nextMode = when (current.themeMode) {
                com.example.data.model.AppThemeMode.LIGHT -> com.example.data.model.AppThemeMode.DARK
                com.example.data.model.AppThemeMode.DARK -> com.example.data.model.AppThemeMode.LIGHT
                com.example.data.model.AppThemeMode.SYSTEM -> if (isSystemDark) com.example.data.model.AppThemeMode.LIGHT else com.example.data.model.AppThemeMode.DARK
            }
            nutritionRepository.updateProfile(current.copy(themeMode = nextMode))
        }
    }
}
