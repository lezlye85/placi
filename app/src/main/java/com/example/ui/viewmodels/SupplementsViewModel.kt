package com.example.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SupplementsData
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.model.ProteinCalculationResult
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementGoal
import com.example.data.model.SupplementItem
import com.example.data.model.SupplementTiming
import com.example.data.model.UserDailySupplement
import com.example.data.model.UserProfile
import com.example.data.repository.NutritionRepository
import com.example.util.SupplementReminderManager
import com.example.util.TimingScheduleInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class SupplementsUiState(
    val allSupplements: List<SupplementItem> = SupplementsData.SUPPLEMENTS,
    val searchQuery: String = "",
    val selectedCategory: SupplementCategory = SupplementCategory.ALL,
    val selectedGoal: SupplementGoal = SupplementGoal.ALL,
    val selectedBrand: String = "Összes",
    val selectedSupplementForDetail: SupplementItem? = null,
    val userDailyStack: List<UserDailySupplement> = SupplementsData.DEFAULT_USER_DAILY_STACK,
    val activeTab: SupplementTab = SupplementTab.DATABASE,
    val messageSnackbar: String? = null,
    val isRemindersMasterEnabled: Boolean = true,
    val isReminderSettingsOpen: Boolean = false,
    val timingSchedules: List<TimingScheduleInfo> = emptyList(),
    val selectedTimingForEdit: SupplementTiming? = null
)

enum class SupplementTab(val titleHu: String, val iconEmoji: String) {
    DATABASE("Katalógus & Kereső", "💊"),
    PROTEIN_CALCULATOR("Súly & Fehérje Kalkulátor", "🧮"),
    MY_STACK("Napi Rutinom", "📋"),
    TIMING_GUIDE("Időzítési Kisokos", "⏱️"),
    GOAL_RECOMMENDER("Mit Szedjek?", "🎯")
}

class SupplementsViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = nutritionRepository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    private val _uiState = MutableStateFlow(SupplementsUiState())
    val uiState: StateFlow<SupplementsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            nutritionRepository.allSupplements.collect { entities ->
                if (entities.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        allSupplements = entities.map { it.toSupplementItem() }
                    )
                }
            }
        }
    }

    val availableBrands: List<String> = listOf("Összes", "BioTechUSA", "Scitec Nutrition", "OstroVit")

    fun selectTab(tab: SupplementTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun applyCalculatedTargetToProfile(result: ProteinCalculationResult) {
        viewModelScope.launch {
            try {
                val current = nutritionRepository.userProfile.firstOrNull() ?: UserProfile()
                val updated = current.copy(
                    currentWeightKg = result.weightKg,
                    gender = result.gender,
                    age = result.age,
                    heightCm = result.heightCm,
                    customProteinGrams = result.proteinGramsRecommended,
                    customTargetCalories = result.targetCalories,
                    customFatGrams = result.fatGrams,
                    customCarbsGrams = result.carbsGrams,
                    customWaterMl = result.waterMl
                )
                nutritionRepository.updateProfile(updated)
                _uiState.value = _uiState.value.copy(
                    messageSnackbar = "🎯 Célok sikeresen mentve: ${result.proteinGramsRecommended}g fehérje, ${result.targetCalories} kcal a napi naplódban!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    messageSnackbar = "Nem sikerült menteni a profilt: ${e.message}"
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectCategory(category: SupplementCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectGoal(goal: SupplementGoal) {
        _uiState.value = _uiState.value.copy(selectedGoal = goal)
    }

    fun selectBrand(brand: String) {
        _uiState.value = _uiState.value.copy(selectedBrand = brand)
    }

    fun selectSupplementForDetail(item: SupplementItem?) {
        _uiState.value = _uiState.value.copy(selectedSupplementForDetail = item)
    }

    fun clearSnackbarMessage() {
        _uiState.value = _uiState.value.copy(messageSnackbar = null)
    }

    fun toggleSupplementTaken(userSupplementId: String) {
        val currentStack = _uiState.value.userDailyStack.toMutableList()
        val index = currentStack.indexOfFirst { it.id == userSupplementId }
        if (index != -1) {
            val item = currentStack[index]
            val newStatus = !item.isTakenToday
            val timeString = if (newStatus) SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) else null
            currentStack[index] = item.copy(isTakenToday = newStatus, takenTimeHu = timeString)
            _uiState.value = _uiState.value.copy(
                userDailyStack = currentStack,
                messageSnackbar = if (newStatus) "✅ ${item.supplementName} bevéve ($timeString)!" else "Visszavonva."
            )
        }
    }

    fun addToDailyStack(supplement: SupplementItem) {
        val currentStack = _uiState.value.userDailyStack.toMutableList()
        if (currentStack.any { it.supplementId == supplement.id }) {
            _uiState.value = _uiState.value.copy(messageSnackbar = "${supplement.name} már a napi rutinodban van!")
            return
        }
        val newItem = UserDailySupplement(
            id = UUID.randomUUID().toString(),
            supplementId = supplement.id,
            supplementName = supplement.name,
            brand = supplement.brand,
            timing = supplement.primaryTiming,
            dosageText = supplement.recommendedDosageHu,
            proteinGrams = supplement.proteinPerServing,
            calories = supplement.caloriesPerServing,
            isTakenToday = false,
            takenTimeHu = null
        )
        currentStack.add(newItem)
        _uiState.value = _uiState.value.copy(
            userDailyStack = currentStack,
            messageSnackbar = "➕ ${supplement.name} hozzáadva a napi kiegészítő rutinodhoz!"
        )
    }

    fun removeFromDailyStack(userSupplementId: String) {
        val updated = _uiState.value.userDailyStack.filterNot { it.id == userSupplementId }
        _uiState.value = _uiState.value.copy(
            userDailyStack = updated,
            messageSnackbar = "Eltávolítva a rutinból."
        )
    }

    fun logSupplementToDiary(supplement: SupplementItem, mealType: MealType = MealType.SNACK) {
        viewModelScope.launch {
            try {
                val foodItem = SupplementsData.toFoodItem(supplement)
                val insertedFoodId = nutritionRepository.insertFood(foodItem)
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val mealEntry = MealEntry(
                    date = todayStr,
                    mealType = mealType,
                    foodId = insertedFoodId,
                    foodName = "${supplement.brand} ${supplement.name}",
                    brand = supplement.brand,
                    amountGrams = supplement.servingGrams,
                    calories = supplement.caloriesPerServing,
                    protein = supplement.proteinPerServing,
                    carbs = supplement.carbsPerServing,
                    fat = supplement.fatPerServing,
                    fiber = 0.0
                )
                nutritionRepository.insertMeal(mealEntry)
                _uiState.value = _uiState.value.copy(
                    messageSnackbar = "📝 ${supplement.name} hozzáadva a mai étkezési naplóhoz (${mealType.displayNameHu})!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(messageSnackbar = "Hiba a naplózás során: ${e.message}")
            }
        }
    }

    fun handleScannedBarcode(barcode: String): SupplementItem? {
        val found = _uiState.value.allSupplements.find { it.barcode.trim() == barcode.trim() }
            ?: SupplementsData.SUPPLEMENTS.find { it.barcode.trim() == barcode.trim() }
        if (found != null) {
            _uiState.value = _uiState.value.copy(
                selectedSupplementForDetail = found,
                messageSnackbar = "🔍 Megtalálva: ${found.brand} ${found.name}!"
            )
        }
        return found
    }

    fun setReminderSettingsOpen(isOpen: Boolean) {
        _uiState.value = _uiState.value.copy(isReminderSettingsOpen = isOpen)
    }

    fun setSelectedTimingForEdit(timing: SupplementTiming?) {
        _uiState.value = _uiState.value.copy(selectedTimingForEdit = timing)
    }

    fun loadReminderSchedules(context: Context) {
        val masterEnabled = SupplementReminderManager.isMasterEnabled(context)
        val schedules = SupplementReminderManager.getFullSchedule(context, _uiState.value.userDailyStack)
        _uiState.value = _uiState.value.copy(
            isRemindersMasterEnabled = masterEnabled,
            timingSchedules = schedules
        )
    }

    fun toggleMasterReminders(context: Context, enabled: Boolean) {
        SupplementReminderManager.setMasterEnabled(context, enabled, _uiState.value.userDailyStack)
        loadReminderSchedules(context)
        _uiState.value = _uiState.value.copy(
            messageSnackbar = if (enabled) "⏰ Kiegészítő bevétele emlékeztetők bekapcsolva az adagolási időpontok szerint!" else "🔕 Emlékeztetők kikapcsolva."
        )
    }

    fun toggleTimingSlot(context: Context, timing: SupplementTiming, enabled: Boolean) {
        SupplementReminderManager.setTimingEnabled(context, timing, enabled, _uiState.value.userDailyStack)
        loadReminderSchedules(context)
        _uiState.value = _uiState.value.copy(
            messageSnackbar = if (enabled) "⏰ ${timing.displayNameHu} emlékeztető aktiválva!" else "🔕 ${timing.displayNameHu} emlékeztető kikapcsolva."
        )
    }

    fun updateTimingSlotTime(context: Context, timing: SupplementTiming, hour: Int, minute: Int) {
        SupplementReminderManager.setTimingTime(context, timing, hour, minute, _uiState.value.userDailyStack)
        loadReminderSchedules(context)
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        _uiState.value = _uiState.value.copy(
            selectedTimingForEdit = null,
            messageSnackbar = "⏰ ${timing.displayNameHu} új időpontja: $timeFormatted (Adagolási emlékeztető frissítve)"
        )
    }

    fun triggerTestReminderNotification(context: Context, timing: SupplementTiming? = null) {
        SupplementReminderManager.triggerTestNotification(context, timing, _uiState.value.userDailyStack)
        _uiState.value = _uiState.value.copy(
            messageSnackbar = "🔔 Teszt emlékeztető kiküldve az értesítési sávba!"
        )
    }

    fun getFilteredSupplements(): List<SupplementItem> {
        val state = _uiState.value
        val query = state.searchQuery.trim().lowercase(Locale.getDefault())

        return state.allSupplements.filter { item ->
            val matchesQuery = query.isEmpty() ||
                    item.name.lowercase(Locale.getDefault()).contains(query) ||
                    item.brand.lowercase(Locale.getDefault()).contains(query) ||
                    item.barcode.contains(query) ||
                    item.activeIngredientsHu.lowercase(Locale.getDefault()).contains(query) ||
                    item.category.displayNameHu.lowercase(Locale.getDefault()).contains(query)

            val matchesCategory = state.selectedCategory == SupplementCategory.ALL || item.category == state.selectedCategory
            val matchesGoal = state.selectedGoal == SupplementGoal.ALL || item.targetGoals.contains(state.selectedGoal)
            val matchesBrand = state.selectedBrand == "Összes" || item.brand.equals(state.selectedBrand, ignoreCase = true)

            matchesQuery && matchesCategory && matchesGoal && matchesBrand
        }
    }
}
