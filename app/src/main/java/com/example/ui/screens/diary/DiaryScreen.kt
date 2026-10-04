package com.example.ui.screens.diary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FoodItem
import com.example.data.model.MealEntry
import com.example.data.model.MealType
import com.example.ui.components.AddFoodBottomSheet
import com.example.ui.components.CalorieSummaryCard
import com.example.ui.components.DateNavigationBar
import com.example.ui.components.MacroBreakdownCard
import com.example.ui.components.MealSectionCard
import com.example.ui.components.WaterTrackerCard
import com.example.ui.components.FullWaterTrackerModuleCard
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishDarkNavy
import com.example.ui.theme.PolishOnPrimaryContainer
import com.example.ui.theme.PolishPrimary
import com.example.ui.theme.PolishPrimaryContainer
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary
import com.example.ui.viewmodels.DiaryViewModel
import com.example.ui.viewmodels.FoodDatabaseViewModel

@Composable
fun DiaryScreen(
    diaryViewModel: DiaryViewModel,
    foodDatabaseViewModel: FoodDatabaseViewModel,
    onNavigateToFoodSearch: (MealType) -> Unit,
    onNavigateToBarcodeScanner: () -> Unit,
    onNavigateToAiMealScan: () -> Unit,
    onNavigateToWorkout: () -> Unit,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val summary by diaryViewModel.daySummary.collectAsStateWithLifecycle()
    val selectedDate by diaryViewModel.selectedDate.collectAsStateWithLifecycle()

    var showQuickCalorieDialog by remember { mutableStateOf(false) }
    var quickCalorieMealType by remember { mutableStateOf(MealType.LUNCH) }

    var foodForSheet by remember { mutableStateOf<FoodItem?>(null) }
    var targetMealTypeForSheet by remember { mutableStateOf(MealType.BREAKFAST) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Professional Polish Action Bar
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onNavigateToFoodSearch(MealType.LUNCH) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("action_log_meal"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Naplózás",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    // AI Meal Vision Button
                    IconButton(
                        onClick = onNavigateToAiMealScan,
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            )
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .testTag("action_ai_meal_scan")
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "AI Étel Fotózás",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Barcode Scanner Button
                    IconButton(
                        onClick = onNavigateToBarcodeScanner,
                        modifier = Modifier
                            .size(52.dp)
                            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp))
                            .testTag("action_barcode_scan")
                    ) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "Vonalkód olvasó",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Professional Polish Header (Greeting + Avatar & Quick Actions)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "SZIA, SPORTOLÓ!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Mai Nap",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // AI Meal Scan Quick Action
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                .clickable { onNavigateToAiMealScan() }
                                .testTag("quick_ai_meal_scan_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "AI Étel Fotózás",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Quick Theme Toggle
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                .clickable { onToggleTheme() }
                                .testTag("quick_theme_toggle_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkTheme) "Váltás világos módra" else "Váltás sötét módra",
                                tint = if (isDarkTheme) Color(0xFFFFC107) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Quick Calories
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                .clickable { showQuickCalorieDialog = true }
                                .testTag("quick_calories_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = "Gyors kalória",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 2. AI Meal Scanner Hero Card Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onNavigateToAiMealScan() }
                        .testTag("ai_meal_banner_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AI Étel Fotózás & Makrók",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "ÚJ",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Text(
                                text = "Fotózd le a fogásodat és a Gemini megbecsüli a kalóriákat!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 2. Date Navigation Bar
            item {
                DateNavigationBar(
                    currentDateString = selectedDate,
                    onPreviousDay = { diaryViewModel.changeDateByDays(-1) },
                    onNextDay = { diaryViewModel.changeDateByDays(1) },
                    onTodayClick = { diaryViewModel.setToday() }
                )
            }

            // 3. Calorie Summary Card (Circular Hero Ring)
            item {
                CalorieSummaryCard(
                    targetCalories = summary.targetCalories,
                    consumedCalories = summary.consumedCalories,
                    burnedCalories = summary.burnedCalories,
                    remainingCalories = summary.remainingCalories
                )
            }

            // 4. Macro Breakdown Card (Mini Macro Cards)
            item {
                MacroBreakdownCard(
                    totalProtein = summary.totalProtein,
                    targetProtein = summary.targetProtein,
                    totalCarbs = summary.totalCarbs,
                    targetCarbs = summary.targetCarbs,
                    totalFat = summary.totalFat,
                    targetFat = summary.targetFat,
                    totalFiber = summary.totalFiber
                )
            }

            // 5. Daily Water Intake Tracking Widget (Room DB Persisted)
            item {
                FullWaterTrackerModuleCard(
                    currentMl = summary.waterMl,
                    targetMl = summary.targetWaterMl,
                    waterEntries = summary.waterEntries,
                    onAddWater = { amount -> diaryViewModel.addWater(amount) },
                    onRemoveLast = { diaryViewModel.removeLastWater() },
                    onDeleteEntry = { id -> diaryViewModel.deleteWaterEntry(id) },
                    onClearToday = { diaryViewModel.clearAllWaterToday() },
                    onUpdateTarget = { target -> diaryViewModel.setCustomWaterTarget(target) }
                )
            }

            // 6. Calisthenics & Workout Plan Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onNavigateToWorkout() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, PolishBorder.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Kalisztenika Edzésterv",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                            Text(
                                text = if (summary.workoutsToday.isNotEmpty()) "+${summary.burnedCalories} kcal kész" else "Kezdés →",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = PolishPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sample Routine Quick Preview Rows
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PolishSurfaceVariant, RoundedCornerShape(14.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💪", fontSize = 18.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Saját Testsúlyos Edzés",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = PolishTextPrimary
                                )
                                Text(
                                    text = "Fekvőtámasz, Húzódzkodás, Plank & HIIT",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = PolishTextSecondary
                                )
                            }
                            if (summary.workoutsToday.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(PolishPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .border(2.dp, PolishBorder, CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            // 7. Meal Sections
            MealType.values().forEach { mealType ->
                val entries = summary.mealsByType[mealType] ?: emptyList()
                item(key = mealType.name) {
                    MealSectionCard(
                        mealType = mealType,
                        entries = entries,
                        onAddClick = { onNavigateToFoodSearch(mealType) },
                        onDeleteEntry = { entry -> diaryViewModel.deleteMeal(entry) }
                    )
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Quick Calorie Dialog
    if (showQuickCalorieDialog) {
        QuickCalorieDialog(
            initialMealType = quickCalorieMealType,
            onDismiss = { showQuickCalorieDialog = false },
            onConfirm = { type, name, cal, p, c, f ->
                diaryViewModel.addQuickCalories(type, name, cal, p, c, f)
                showQuickCalorieDialog = false
            }
        )
    }

    // Add Food Bottom Sheet
    foodForSheet?.let { food ->
        AddFoodBottomSheet(
            food = food,
            targetMealType = targetMealTypeForSheet,
            onDismiss = { foodForSheet = null },
            onConfirmAdd = { mealType, amount ->
                diaryViewModel.addMeal(mealType, food, amount)
                foodForSheet = null
            }
        )
    }
}

@Composable
fun QuickCalorieDialog(
    initialMealType: MealType,
    onDismiss: () -> Unit,
    onConfirm: (MealType, String, Double, Double, Double, Double) -> Unit
) {
    var mealType by remember { mutableStateOf(initialMealType) }
    var name by remember { mutableStateOf("") }
    var calInput by remember { mutableStateOf("") }
    var protInput by remember { mutableStateOf("") }
    var carbInput by remember { mutableStateOf("") }
    var fatInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gyors kalória hozzáadása", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Meal Type selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MealType.values().forEach { type ->
                        val sel = type == mealType
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) PolishPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            onClick = { mealType = type }
                        ) {
                            Text(
                                text = type.displayNameHu.split(" ")[0],
                                modifier = Modifier.padding(vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (sel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Megnevezés (pl. Kávé tejjel, Sütemény)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = calInput,
                    onValueChange = { calInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Kalória (kcal) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = protInput,
                        onValueChange = { protInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Fehérje (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = carbInput,
                        onValueChange = { carbInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Szénhidrát (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = fatInput,
                        onValueChange = { fatInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Zsír (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cal = calInput.toDoubleOrNull() ?: 0.0
                    if (cal > 0) {
                        val p = protInput.toDoubleOrNull() ?: 0.0
                        val c = carbInput.toDoubleOrNull() ?: 0.0
                        val f = fatInput.toDoubleOrNull() ?: 0.0
                        onConfirm(mealType, name, cal, p, c, f)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PolishPrimary)
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Mégse")
            }
        }
    )
}

