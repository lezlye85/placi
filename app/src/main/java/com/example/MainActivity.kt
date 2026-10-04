package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppThemeMode
import com.example.ui.navigation.FitMainNavigation
import com.example.ui.theme.KaloriaFitTheme
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

class MainActivity : ComponentActivity() {

    private val appContainer by lazy {
        FitAppContainer(applicationContext)
    }

    private val viewModelFactory by lazy {
        FitViewModelFactory(appContainer)
    }

    private val diaryViewModel: DiaryViewModel by viewModels { viewModelFactory }
    private val foodDatabaseViewModel: FoodDatabaseViewModel by viewModels { viewModelFactory }
    private val workoutViewModel: WorkoutViewModel by viewModels { viewModelFactory }
    private val statsViewModel: StatsViewModel by viewModels { viewModelFactory }
    private val profileViewModel: ProfileViewModel by viewModels { viewModelFactory }
    private val recipeViewModel: RecipeViewModel by viewModels { viewModelFactory }
    private val communityViewModel: CommunityViewModel by viewModels { viewModelFactory }
    private val supplementsViewModel: SupplementsViewModel by viewModels { viewModelFactory }
    private val aiMealScannerViewModel: AiMealScannerViewModel by viewModels { viewModelFactory }
    private val barcodeScannerViewModel: BarcodeScannerViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userProfile by profileViewModel.profile.collectAsStateWithLifecycle()
            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme = when (userProfile.themeMode) {
                AppThemeMode.SYSTEM -> isSystemDark
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }

            KaloriaFitTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FitMainNavigation(
                        diaryViewModel = diaryViewModel,
                        foodDatabaseViewModel = foodDatabaseViewModel,
                        workoutViewModel = workoutViewModel,
                        statsViewModel = statsViewModel,
                        profileViewModel = profileViewModel,
                        recipeViewModel = recipeViewModel,
                        communityViewModel = communityViewModel,
                        supplementsViewModel = supplementsViewModel,
                        aiMealScannerViewModel = aiMealScannerViewModel,
                        barcodeScannerViewModel = barcodeScannerViewModel,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = {
                            profileViewModel.toggleTheme(isSystemDark)
                        }
                    )
                }
            }
        }
    }
}
