package com.example.ui.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.WaterDrop
import com.example.data.model.ActivityLevel
import com.example.data.model.AppThemeMode
import com.example.data.model.Gender
import com.example.data.model.GoalType
import com.example.data.model.UserProfile
import com.example.ui.screens.health.HealthMetricsCalculatorScreen
import com.example.ui.screens.supplements.ProteinCalculatorTab
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.theme.WaterColor
import com.example.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateToHealthCalculator: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val currentProfile by profileViewModel.profile.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf(currentProfile.name) }
    var gender by remember { mutableStateOf(currentProfile.gender) }
    var ageInput by remember { mutableStateOf(currentProfile.age.toString()) }
    var heightInput by remember { mutableStateOf(currentProfile.heightCm.toInt().toString()) }
    var currentWeightInput by remember { mutableStateOf("%.1f".format(currentProfile.currentWeightKg)) }
    var targetWeightInput by remember { mutableStateOf("%.1f".format(currentProfile.targetWeightKg)) }
    var activityLevel by remember { mutableStateOf(currentProfile.activityLevel) }
    var goalType by remember { mutableStateOf(currentProfile.goalType) }
    var customTargetEnabled by remember { mutableStateOf(currentProfile.customTargetCalories != null) }
    var customCalInput by remember { mutableStateOf((currentProfile.customTargetCalories ?: 2000).toString()) }
    var customWaterEnabled by remember { mutableStateOf(currentProfile.customWaterMl != null) }
    var customWaterInput by remember { mutableStateOf((currentProfile.customWaterMl ?: 2500).toString()) }
    var themeMode by remember { mutableStateOf(currentProfile.themeMode) }
    var showProteinCalculatorSheet by remember { mutableStateOf(false) }
    var showHealthMetricsSheet by remember { mutableStateOf(false) }

    // Sync when currentProfile loads
    LaunchedEffect(currentProfile) {
        name = currentProfile.name
        gender = currentProfile.gender
        ageInput = currentProfile.age.toString()
        heightInput = currentProfile.heightCm.toInt().toString()
        currentWeightInput = "%.1f".format(currentProfile.currentWeightKg)
        targetWeightInput = "%.1f".format(currentProfile.targetWeightKg)
        activityLevel = currentProfile.activityLevel
        goalType = currentProfile.goalType
        themeMode = currentProfile.themeMode
        customTargetEnabled = currentProfile.customTargetCalories != null
        if (currentProfile.customTargetCalories != null) {
            customCalInput = currentProfile.customTargetCalories.toString()
        }
        customWaterEnabled = currentProfile.customWaterMl != null
        if (currentProfile.customWaterMl != null) {
            customWaterInput = currentProfile.customWaterMl.toString()
        }
    }

    // Temporary profile for live calculation preview
    val previewProfile = UserProfile(
        name = name,
        gender = gender,
        age = ageInput.toIntOrNull() ?: 28,
        heightCm = heightInput.toDoubleOrNull() ?: 175.0,
        currentWeightKg = currentWeightInput.toDoubleOrNull() ?: 80.0,
        targetWeightKg = targetWeightInput.toDoubleOrNull() ?: 72.0,
        activityLevel = activityLevel,
        goalType = goalType,
        themeMode = themeMode,
        customTargetCalories = if (customTargetEnabled) customCalInput.toIntOrNull() else null,
        customWaterMl = if (customWaterEnabled) customWaterInput.toIntOrNull() else null
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                // Header Title
                Text(
                    text = "Személyre szabott Napi Célok & Profil",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "A Mifflin-St Jeor egyenlet alapján pontosan kiszámoljuk a fogyási kalóriaszükségletedet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Calculation Results Card Preview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Számított Napi Céljaid:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Alapanyagcsere (BMR):", style = MaterialTheme.typography.labelSmall)
                                Text("${previewProfile.calculateBMR().toInt()} kcal", fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Szinten tartó (TDEE):", style = MaterialTheme.typography.labelSmall)
                                Text("${previewProfile.calculateTDEE().toInt()} kcal", fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Napi Fogyási Cél:", style = MaterialTheme.typography.labelSmall, color = Emerald500, fontWeight = FontWeight.Bold)
                                Text("${previewProfile.getDailyCalorieTarget()} kcal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Emerald500)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Macros preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Fehérje", style = MaterialTheme.typography.labelSmall, color = ProteinColor)
                                Text("${previewProfile.getProteinTargetGrams()}g", fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Szénhidrát", style = MaterialTheme.typography.labelSmall, color = CarbsColor)
                                Text("${previewProfile.getCarbsTargetGrams()}g", fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Zsír", style = MaterialTheme.typography.labelSmall, color = FatColor)
                                Text("${previewProfile.getFatTargetGrams()}g", fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Vízbevitel", style = MaterialTheme.typography.labelSmall, color = WaterColor)
                                Text("${previewProfile.getWaterTargetMl()} ml", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Quick Launcher Card for Súly- & Fehérjekalkulátor
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { showProteinCalculatorSheet = true }
                        .testTag("open_protein_calc_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ProteinColor.copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ProteinColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(ProteinColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧮", fontSize = 24.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Súly- & Fehérjeszükséglet Kalkulátor",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ProteinColor
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tudományos fehérje- és kalóriaszámítás fogyáshoz, tömegeléshez vagy test-rekompozícióhoz.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { showProteinCalculatorSheet = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ProteinColor),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Kiszámítás", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Quick Launcher Card for Egészségügyi Mutatók & BMI Kalkulátor
            item {
                val currentBmi = currentProfile.getBMI()
                val currentCategory = currentProfile.getBMICategory()
                val isNormalBmi = currentBmi in 18.5..24.9

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            if (onNavigateToHealthCalculator != null) {
                                onNavigateToHealthCalculator()
                            } else {
                                showHealthMetricsSheet = true
                            }
                        }
                        .testTag("open_bmi_calc_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = (if (isNormalBmi) Emerald500 else CarbsColor).copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, (if (isNormalBmi) Emerald500 else CarbsColor).copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isNormalBmi) Emerald500 else CarbsColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚖️", fontSize = 24.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Egészségügyi Mutatók & BMI",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isNormalBmi) Emerald600 else CarbsColor
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Jelenlegi BMI: %.1f • %s".format(currentBmi, currentCategory),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "WHO standard egészségügyi határok, optimális súlytartomány és cél-összegzés.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (onNavigateToHealthCalculator != null) {
                                    onNavigateToHealthCalculator()
                                } else {
                                    showHealthMetricsSheet = true
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (isNormalBmi) Emerald500 else CarbsColor),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Elemzés", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Gender Selector
            item {
                Text("Nem:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Gender.values().forEach { g ->
                        val selected = g == gender
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { gender = g }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (g == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                                    contentDescription = null,
                                    tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = g.displayNameHu,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Body Metrics Inputs
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ageInput,
                        onValueChange = { ageInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Életkor") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = heightInput,
                        onValueChange = { heightInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Magasság (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = currentWeightInput,
                        onValueChange = { currentWeightInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Jelenlegi súly (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = targetWeightInput,
                        onValueChange = { targetWeightInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Cél testsúly (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Activity Level
            item {
                Text("Napi aktivitási szint:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                ActivityLevel.values().forEach { level ->
                    val selected = level == activityLevel
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { activityLevel = level },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = level.displayNameHu,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = level.descriptionHu,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Goal Selector
            item {
                Text("Fő célkitűzés:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                GoalType.values().forEach { goal ->
                    val selected = goal == goalType
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { goalType = goal },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) Emerald500.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = goal.displayNameHu,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Emerald500 else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${goal.calorieAdjustment} kcal/nap deficit/többlet",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Custom Calorie Override Toggle
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Egyéni kalóriacél beállítása", fontWeight = FontWeight.Bold)
                        Text("Kézi felülbírálás a számított érték helyett", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = customTargetEnabled,
                        onCheckedChange = { customTargetEnabled = it }
                    )
                }
                if (customTargetEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customCalInput,
                        onValueChange = { customCalInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Egyéni napi kalóriakeret (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Custom Water Target & Hydration Settings Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("water_settings_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Vízfogyasztás & Hidratációs Célok",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Automatikus: testsúly alapján (35 ml / kg) = ${previewProfile.getWaterTargetMl()} ml",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Egyéni napi vízcél (ml)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Kézi érték megadása", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = customWaterEnabled,
                                onCheckedChange = { customWaterEnabled = it }
                            )
                        }

                        if (customWaterEnabled) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = customWaterInput,
                                onValueChange = { customWaterInput = it.filter { c -> c.isDigit() } },
                                label = { Text("Egyéni folyadékcél (ml / nap)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                trailingIcon = { Text("ml", modifier = Modifier.padding(end = 12.dp)) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Appearance & Theme Mode Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_settings_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Megjelenés & Sötét Mód",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Szemkímélő olvasás & AMOLED energiatakarékosság",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        AppThemeMode.values().forEach { mode ->
                            val isSelected = themeMode == mode
                            val icon = when (mode) {
                                AppThemeMode.LIGHT -> Icons.Default.LightMode
                                AppThemeMode.DARK -> Icons.Default.DarkMode
                                AppThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        themeMode = mode
                                        profileViewModel.setThemeMode(mode)
                                    }
                                    .testTag("theme_option_${mode.name.lowercase()}"),
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                border = if (isSelected) {
                                    androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                } else {
                                    androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = mode.displayNameHu,
                                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = mode.displayNameHu,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = mode.subtitleHu,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Kiválasztva",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Save Button
            item {
                Button(
                    onClick = {
                        profileViewModel.updateProfile(previewProfile)
                        scope.launch {
                            snackbarHostState.showSnackbar("Profil és napi célok sikeresen elmentve!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_profile_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Célok & Profil Mentése", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    if (showProteinCalculatorSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showProteinCalculatorSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                ProteinCalculatorTab(
                    initialWeightKg = currentProfile.currentWeightKg,
                    initialGender = currentProfile.gender,
                    initialAge = currentProfile.age,
                    initialHeightCm = currentProfile.heightCm,
                    onApplyTargetToProfile = { result ->
                        val updated = currentProfile.copy(
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
                        profileViewModel.updateProfile(updated)
                        currentWeightInput = "%.1f".format(result.weightKg)
                        customCalInput = result.targetCalories.toString()
                        customTargetEnabled = true
                        customWaterInput = result.waterMl.toString()
                        customWaterEnabled = true
                        gender = result.gender
                        ageInput = result.age.toString()
                        heightInput = result.heightCm.toInt().toString()

                        scope.launch {
                            snackbarHostState.showSnackbar("🎯 Új célok beállítva: ${result.proteinGramsRecommended}g fehérje, ${result.targetCalories} kcal!")
                        }
                    }
                )
            }
        }
    }

    if (showHealthMetricsSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showHealthMetricsSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            HealthMetricsCalculatorScreen(
                userProfile = currentProfile,
                onSaveMetricsToProfile = { h, w ->
                    profileViewModel.updateHeightAndWeight(h, w)
                    heightInput = h.toInt().toString()
                    currentWeightInput = "%.1f".format(w)
                    showHealthMetricsSheet = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Testméretek frissítve: ${h.toInt()} cm, %.1f kg!".format(w))
                    }
                },
                onBack = { showHealthMetricsSheet = false }
            )
        }
    }
}
