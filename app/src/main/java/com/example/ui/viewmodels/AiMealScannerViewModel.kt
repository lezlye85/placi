package com.example.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MealType
import com.example.data.remote.GeminiVisionClient
import com.example.data.remote.MealAiAnalysisResult
import com.example.data.repository.GeminiMealAnalysisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

sealed interface AiMealScannerUiState {
    object Idle : AiMealScannerUiState
    data class Analyzing(
        val capturedBitmap: Bitmap?,
        val statusMessage: String = "A Gemini Vision elemzi az ételfotót..."
    ) : AiMealScannerUiState
    data class Success(
        val analysis: MealAiAnalysisResult,
        val capturedBitmap: Bitmap?,
        val portionScale: Float = 1.0f,
        val selectedMealType: MealType = MealType.LUNCH,
        val customMealName: String = "",
        val customCalories: String = "",
        val customProtein: String = "",
        val customCarbs: String = "",
        val customFat: String = "",
        val saveAsCustomFood: Boolean = true,
        val logSeparateIngredients: Boolean = false,
        val isSaving: Boolean = false,
        val isSaved: Boolean = false
    ) : AiMealScannerUiState
    data class Error(val message: String, val lastBitmap: Bitmap? = null) : AiMealScannerUiState
}

class AiMealScannerViewModel(
    private val geminiRepository: GeminiMealAnalysisRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AiMealScannerUiState>(AiMealScannerUiState.Idle)
    val uiState: StateFlow<AiMealScannerUiState> = _uiState.asStateFlow()

    private val _userNotes = MutableStateFlow("")
    val userNotes: StateFlow<String> = _userNotes.asStateFlow()

    fun setUserNotes(notes: String) {
        _userNotes.value = notes
    }

    fun analyzeMealPhoto(bitmap: Bitmap, notes: String = _userNotes.value) {
        _uiState.value = AiMealScannerUiState.Analyzing(
            capturedBitmap = bitmap,
            statusMessage = "Gemini Vision tápanyagfelismerés folyamatban..."
        )

        viewModelScope.launch {
            val result = geminiRepository.analyzeMealPhoto(bitmap, notes)
            result.fold(
                onSuccess = { analysis ->
                    val defaultMealType = mapDishTypeToMealType(analysis.dishType)
                    _uiState.value = AiMealScannerUiState.Success(
                        analysis = analysis,
                        capturedBitmap = bitmap,
                        portionScale = 1.0f,
                        selectedMealType = defaultMealType,
                        customMealName = analysis.mealName,
                        customCalories = analysis.calories.toInt().toString(),
                        customProtein = analysis.protein.toInt().toString(),
                        customCarbs = analysis.carbs.toInt().toString(),
                        customFat = analysis.fat.toInt().toString(),
                        saveAsCustomFood = true,
                        logSeparateIngredients = false
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = AiMealScannerUiState.Error(
                        message = throwable.message ?: "Nem sikerült az étel elemzése.",
                        lastBitmap = bitmap
                    )
                }
            )
        }
    }

    fun loadSamplePreset(presetIndex: Int) {
        val presets = GeminiVisionClient.samplePresets
        val preset = presets.getOrElse(presetIndex) { presets.first() }

        _uiState.value = AiMealScannerUiState.Analyzing(
            capturedBitmap = null,
            statusMessage = "Minta ételfotó betöltése és elemzése..."
        )

        viewModelScope.launch {
            val defaultMealType = mapDishTypeToMealType(preset.dishType)
            _uiState.value = AiMealScannerUiState.Success(
                analysis = preset,
                capturedBitmap = null,
                portionScale = 1.0f,
                selectedMealType = defaultMealType,
                customMealName = preset.mealName,
                customCalories = preset.calories.toInt().toString(),
                customProtein = preset.protein.toInt().toString(),
                customCarbs = preset.carbs.toInt().toString(),
                customFat = preset.fat.toInt().toString(),
                saveAsCustomFood = true,
                logSeparateIngredients = false
            )
        }
    }

    fun updatePortionScale(scale: Float) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                val newScale = scale.coerceIn(0.25f, 3.0f)
                val newKcal = (current.analysis.calories * newScale).toInt().toString()
                val newProt = (current.analysis.protein * newScale).toInt().toString()
                val newCarb = (current.analysis.carbs * newScale).toInt().toString()
                val newFat = (current.analysis.fat * newScale).toInt().toString()
                current.copy(
                    portionScale = newScale,
                    customCalories = newKcal,
                    customProtein = newProt,
                    customCarbs = newCarb,
                    customFat = newFat
                )
            } else current
        }
    }

    fun updateSelectedMealType(mealType: MealType) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(selectedMealType = mealType)
            } else current
        }
    }

    fun updateCustomMealName(newName: String) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(customMealName = newName)
            } else current
        }
    }

    fun updateCalories(caloriesStr: String) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(customCalories = caloriesStr)
            } else current
        }
    }

    fun updateProtein(proteinStr: String) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(customProtein = proteinStr)
            } else current
        }
    }

    fun updateCarbs(carbsStr: String) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(customCarbs = carbsStr)
            } else current
        }
    }

    fun updateFat(fatStr: String) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(customFat = fatStr)
            } else current
        }
    }

    fun toggleSaveAsCustomFood(save: Boolean) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(saveAsCustomFood = save)
            } else current
        }
    }

    fun toggleLogSeparateIngredients(separate: Boolean) {
        _uiState.update { current ->
            if (current is AiMealScannerUiState.Success) {
                current.copy(logSeparateIngredients = separate)
            } else current
        }
    }

    fun saveAndLogMeal(date: String = LocalDate.now().toString(), onDone: () -> Unit) {
        val state = _uiState.value
        if (state !is AiMealScannerUiState.Success || state.isSaving) return

        _uiState.update { (it as AiMealScannerUiState.Success).copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val finalAnalysis = if (state.customMealName.isNotBlank() && state.customMealName != state.analysis.mealName) {
                    state.analysis.copy(mealName = state.customMealName)
                } else {
                    state.analysis
                }

                val overrideKcal = state.customCalories.toDoubleOrNull()
                val overrideProt = state.customProtein.toDoubleOrNull()
                val overrideCarb = state.customCarbs.toDoubleOrNull()
                val overrideFat = state.customFat.toDoubleOrNull()

                geminiRepository.logAiMealToDiary(
                    analysis = finalAnalysis,
                    mealType = state.selectedMealType,
                    portionScale = state.portionScale,
                    date = date,
                    saveAsCustomFood = state.saveAsCustomFood,
                    overrideCalories = overrideKcal,
                    overrideProtein = overrideProt,
                    overrideCarbs = overrideCarb,
                    overrideFat = overrideFat,
                    logSeparateIngredients = state.logSeparateIngredients
                )

                _uiState.update {
                    if (it is AiMealScannerUiState.Success) {
                        it.copy(isSaving = false, isSaved = true)
                    } else it
                }
                onDone()
            } catch (e: Exception) {
                _uiState.update {
                    if (it is AiMealScannerUiState.Success) {
                        it.copy(isSaving = false)
                    } else it
                }
            }
        }
    }

    fun reset() {
        _uiState.value = AiMealScannerUiState.Idle
        _userNotes.value = ""
    }

    private fun mapDishTypeToMealType(dishType: String): MealType {
        return when (dishType.uppercase()) {
            "BREAKFAST" -> MealType.BREAKFAST
            "LUNCH" -> MealType.LUNCH
            "DINNER" -> MealType.DINNER
            "SNACK" -> MealType.SNACK
            else -> {
                val hour = LocalTime.now().hour
                when {
                    hour in 5..10 -> MealType.BREAKFAST
                    hour in 11..15 -> MealType.LUNCH
                    hour in 16..17 -> MealType.SNACK
                    else -> MealType.DINNER
                }
            }
        }
    }
}
