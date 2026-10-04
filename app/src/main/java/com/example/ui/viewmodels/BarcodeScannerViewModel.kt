package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.data.remote.MockApiResponse
import com.example.data.remote.MockNutritionalApiService
import com.example.data.remote.NutritionalItemDetail
import com.example.data.repository.NutritionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed interface BarcodeScanUiState {
    object Idle : BarcodeScanUiState
    data class Searching(val barcode: String) : BarcodeScanUiState
    data class Success(
        val item: NutritionalItemDetail,
        val isSavedInDb: Boolean,
        val latencyMs: Long
    ) : BarcodeScanUiState
    data class NotFound(val barcode: String, val message: String) : BarcodeScanUiState
    data class Error(val barcode: String, val error: String) : BarcodeScanUiState
}

class BarcodeScannerViewModel(
    private val nutritionRepository: NutritionRepository,
    private val mockNutritionalApiService: MockNutritionalApiService = MockNutritionalApiService()
) : ViewModel() {

    private val _uiState = MutableStateFlow<BarcodeScanUiState>(BarcodeScanUiState.Idle)
    val uiState: StateFlow<BarcodeScanUiState> = _uiState.asStateFlow()

    private val _isTorchOn = MutableStateFlow(false)
    val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

    private val _isBackCamera = MutableStateFlow(true)
    val isBackCamera: StateFlow<Boolean> = _isBackCamera.asStateFlow()

    private val _isHapticEnabled = MutableStateFlow(true)
    val isHapticEnabled: StateFlow<Boolean> = _isHapticEnabled.asStateFlow()

    private val _customServingGrams = MutableStateFlow(100.0)
    val customServingGrams: StateFlow<Double> = _customServingGrams.asStateFlow()

    private val _recentScans = MutableStateFlow<List<NutritionalItemDetail>>(emptyList())
    val recentScans: StateFlow<List<NutritionalItemDetail>> = _recentScans.asStateFlow()

    val sampleBarcodes: List<Pair<String, String>> = mockNutritionalApiService.getSampleBarcodes()

    /**
     * Processes barcode captured via CameraX or entered manually.
     * Fetches details from Mock Nutritional API and saves to Room.
     */
    fun onBarcodeScanned(barcode: String) {
        val cleanBarcode = barcode.trim()
        if (cleanBarcode.isBlank()) return

        // Prevent redundant triggers if already searching this exact code
        val current = _uiState.value
        if (current is BarcodeScanUiState.Searching && current.barcode == cleanBarcode) {
            return
        }

        viewModelScope.launch {
            _uiState.value = BarcodeScanUiState.Searching(cleanBarcode)

            when (val response = mockNutritionalApiService.fetchFoodDetails(cleanBarcode)) {
                is MockApiResponse.Success -> {
                    val item = response.data
                    _customServingGrams.value = item.servingSizeGrams

                    // Persist item to Room local food database for offline access & autocomplete
                    val foodEntity = item.toFoodItem()
                    nutritionRepository.insertFood(foodEntity)

                    // Update recent scans list (keep latest 8)
                    val updated = listOf(item) + _recentScans.value.filter { it.barcode != item.barcode }
                    _recentScans.value = updated.take(8)

                    _uiState.value = BarcodeScanUiState.Success(
                        item = item,
                        isSavedInDb = true,
                        latencyMs = response.latencyMs
                    )
                }
                is MockApiResponse.Error -> {
                    // Check if exists locally in Room DB as fallback
                    val localFood = nutritionRepository.findFoodByBarcode(cleanBarcode)
                    if (localFood != null) {
                        val converted = NutritionalItemDetail(
                            barcode = localFood.barcode,
                            name = localFood.name,
                            brand = localFood.brand,
                            category = localFood.category,
                            caloriesPer100g = localFood.caloriesPer100g,
                            proteinPer100g = localFood.proteinPer100g,
                            carbsPer100g = localFood.carbsPer100g,
                            fatPer100g = localFood.fatPer100g,
                            fiberPer100g = localFood.fiberPer100g,
                            servingSizeGrams = localFood.defaultServingGrams,
                            servingUnitName = localFood.defaultServingUnit,
                            nutriScore = "B",
                            apiSource = "Helyi Room Adatbázis"
                        )
                        _customServingGrams.value = converted.servingSizeGrams
                        _uiState.value = BarcodeScanUiState.Success(
                            item = converted,
                            isSavedInDb = true,
                            latencyMs = 10
                        )
                    } else {
                        _uiState.value = BarcodeScanUiState.NotFound(
                            barcode = cleanBarcode,
                            message = response.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Directly logs the scanned food item to the daily meal diary in Room.
     */
    fun logScannedItemToDiary(
        item: NutritionalItemDetail,
        mealType: MealType,
        amountGrams: Double,
        date: String = LocalDate.now().toString()
    ) {
        viewModelScope.launch {
            val ratio = amountGrams / 100.0
            val entry = MealEntry(
                date = date,
                mealType = mealType,
                foodName = item.name,
                brand = item.brand,
                amountGrams = amountGrams,
                calories = item.caloriesPer100g * ratio,
                protein = item.proteinPer100g * ratio,
                carbs = item.carbsPer100g * ratio,
                fat = item.fatPer100g * ratio,
                fiber = item.fiberPer100g * ratio
            )
            nutritionRepository.insertMeal(entry)
        }
    }

    fun setCustomServingGrams(grams: Double) {
        if (grams > 0) {
            _customServingGrams.value = grams
        }
    }

    fun toggleTorch() {
        _isTorchOn.value = !_isTorchOn.value
    }

    fun toggleCameraFacing() {
        _isBackCamera.value = !_isBackCamera.value
    }

    fun toggleHaptic() {
        _isHapticEnabled.value = !_isHapticEnabled.value
    }

    fun resetScanState() {
        _uiState.value = BarcodeScanUiState.Idle
    }
}
