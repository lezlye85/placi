package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MealType
import com.example.ui.screens.barcode.BarcodeScannerScreen
import com.example.ui.screens.camera.AiMealScannerScreen
import com.example.ui.screens.community.CommunityScreen
import com.example.ui.screens.diary.DiaryScreen
import com.example.ui.screens.foods.FoodDatabaseScreen
import com.example.ui.screens.health.HealthMetricsCalculatorScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.recipes.RecipeScreen
import com.example.ui.screens.stats.StatsScreen
import com.example.ui.screens.supplements.SupplementsScreen
import com.example.ui.screens.workout.WorkoutScreen
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

enum class FitScreen(val titleHu: String, val icon: ImageVector) {
    DIARY("Napló", Icons.Default.MenuBook),
    SUPPLEMENTS("Kiegészítők", Icons.Default.Favorite),
    WORKOUT("Edzés", Icons.Default.FitnessCenter),
    RECIPES("Receptek", Icons.Default.RestaurantMenu),
    STATS("Statisztika", Icons.Default.CalendarMonth),
    COMMUNITY("Közösség", Icons.Default.People),
    PROFILE("Profil", Icons.Default.Person)
}

const val ROUTE_FOOD_DATABASE = "food_database"
const val ROUTE_BARCODE_SCANNER = "barcode_scanner"
const val ROUTE_AI_MEAL_SCANNER = "ai_meal_scanner"
const val ROUTE_HEALTH_CALCULATOR = "health_calculator"

@Composable
fun FitMainNavigation(
    diaryViewModel: DiaryViewModel,
    foodDatabaseViewModel: FoodDatabaseViewModel,
    workoutViewModel: WorkoutViewModel,
    statsViewModel: StatsViewModel,
    profileViewModel: ProfileViewModel,
    recipeViewModel: RecipeViewModel,
    communityViewModel: CommunityViewModel,
    supplementsViewModel: SupplementsViewModel,
    aiMealScannerViewModel: AiMealScannerViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var foodSearchInitialMealType by remember { mutableStateOf(MealType.BREAKFAST) }

    val isTopLevelDestination = FitScreen.values().any { it.name == currentRoute }

    Scaffold(
        bottomBar = {
            if (isTopLevelDestination) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp
                    ) {
                        FitScreen.values().forEach { screen ->
                            val selected = currentRoute == screen.name
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.titleHu,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = screen.titleHu,
                                        fontSize = 10.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_tab_${screen.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FitScreen.DIARY.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(FitScreen.DIARY.name) {
                DiaryScreen(
                    diaryViewModel = diaryViewModel,
                    foodDatabaseViewModel = foodDatabaseViewModel,
                    onNavigateToFoodSearch = { mealType ->
                        foodSearchInitialMealType = mealType
                        navController.navigate(ROUTE_FOOD_DATABASE)
                    },
                    onNavigateToBarcodeScanner = {
                        navController.navigate(ROUTE_BARCODE_SCANNER)
                    },
                    onNavigateToAiMealScan = {
                        navController.navigate(ROUTE_AI_MEAL_SCANNER)
                    },
                    onNavigateToWorkout = {
                        navController.navigate(FitScreen.WORKOUT.name)
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme
                )
            }

            composable(FitScreen.SUPPLEMENTS.name) {
                SupplementsScreen(
                    supplementsViewModel = supplementsViewModel,
                    onNavigateToBarcodeScanner = {
                        navController.navigate(ROUTE_BARCODE_SCANNER)
                    }
                )
            }

            composable(FitScreen.RECIPES.name) {
                RecipeScreen(
                    recipeViewModel = recipeViewModel
                )
            }

            composable(FitScreen.COMMUNITY.name) {
                CommunityScreen(
                    communityViewModel = communityViewModel
                )
            }

            composable(FitScreen.WORKOUT.name) {
                WorkoutScreen(
                    workoutViewModel = workoutViewModel
                )
            }

            composable(FitScreen.STATS.name) {
                StatsScreen(
                    statsViewModel = statsViewModel,
                    onNavigateToHealthCalculator = {
                        navController.navigate(ROUTE_HEALTH_CALCULATOR)
                    }
                )
            }

            composable(FitScreen.PROFILE.name) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onNavigateToHealthCalculator = {
                        navController.navigate(ROUTE_HEALTH_CALCULATOR)
                    }
                )
            }

            composable(ROUTE_FOOD_DATABASE) {
                FoodDatabaseScreen(
                    foodDatabaseViewModel = foodDatabaseViewModel,
                    diaryViewModel = diaryViewModel,
                    initialMealType = foodSearchInitialMealType,
                    onNavigateToBarcodeScanner = {
                        navController.navigate(ROUTE_BARCODE_SCANNER)
                    },
                    onNavigateToAiMealScan = {
                        navController.navigate(ROUTE_AI_MEAL_SCANNER)
                    }
                )
            }

            composable(ROUTE_BARCODE_SCANNER) {
                BarcodeScannerScreen(
                    barcodeScannerViewModel = barcodeScannerViewModel,
                    foodDatabaseViewModel = foodDatabaseViewModel,
                    diaryViewModel = diaryViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ROUTE_AI_MEAL_SCANNER) {
                AiMealScannerScreen(
                    scannerViewModel = aiMealScannerViewModel,
                    diaryViewModel = diaryViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ROUTE_HEALTH_CALCULATOR) {
                val userProfile by profileViewModel.profile.collectAsStateWithLifecycle()
                HealthMetricsCalculatorScreen(
                    userProfile = userProfile,
                    onSaveMetricsToProfile = { h, w ->
                        profileViewModel.updateHeightAndWeight(h, w)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
