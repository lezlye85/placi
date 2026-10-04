package com.example.ui.screens.stats

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import com.example.ui.screens.health.HealthMetricsCalculatorScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MacroTrendItem
import com.example.data.model.MonthlyStatsSummary
import com.example.data.model.WeightEntry
import com.example.data.model.WorkoutFrequencyItem
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodels.StatsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    statsViewModel: StatsViewModel,
    onNavigateToHealthCalculator: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val weights by statsViewModel.weights.collectAsStateWithLifecycle()
    val userProfile by statsViewModel.userProfile.collectAsStateWithLifecycle()
    val pastWeekMeals by statsViewModel.pastWeekMeals.collectAsStateWithLifecycle()
    val monthlySummaries by statsViewModel.monthlySummaries.collectAsStateWithLifecycle()
    val selectedMonthIndex by statsViewModel.selectedMonthIndex.collectAsStateWithLifecycle()
    val macroTrends by statsViewModel.macroTrends.collectAsStateWithLifecycle()
    val workoutTrends by statsViewModel.workoutFrequencyTrends.collectAsStateWithLifecycle()

    var showWeightDialog by remember { mutableStateOf(false) }
    var showHealthCalculatorSheet by remember { mutableStateOf(false) }
    var statsTab by remember { mutableIntStateOf(0) } // 0: Súly & Havi Összegzés, 1: Makró & Edzés Trendek

    val currentWeight = weights.lastOrNull()?.weightKg ?: userProfile.currentWeightKg
    val startWeight = weights.firstOrNull()?.weightKg ?: userProfile.currentWeightKg
    val goalWeight = userProfile.targetWeightKg

    val weightDiff = currentWeight - startWeight
    val remainingToGoal = (currentWeight - goalWeight).coerceAtLeast(0.0)

    val bmi = userProfile.getBMI()
    val bmiCategory = userProfile.getBMICategory()

    val activeSummary = monthlySummaries.getOrNull(selectedMonthIndex) ?: monthlySummaries.first()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))

                // Screen Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "HALADÁS & RÉSZLETES STATISZTIKA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Vizuális Elemzések & Trendek",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { showWeightDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("log_weight_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Új mérés", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Sub-Tabs
                TabRow(
                    selectedTabIndex = statsTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    divider = {}
                ) {
                    Tab(
                        selected = statsTab == 0,
                        onClick = { statsTab = 0 },
                        text = { Text("Súly & Havi Összefoglaló", fontWeight = if (statsTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_stats_weight_monthly")
                    )
                    Tab(
                        selected = statsTab == 1,
                        onClick = { statsTab = 1 },
                        text = { Text("Makrók & Edzés Trendek", fontWeight = if (statsTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_stats_trends")
                    )
                }
            }

            if (statsTab == 0) {
                // Súlyhaladás Kártya
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Súlyhaladás & BMI",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (bmi in 18.5..24.9) Emerald500.copy(alpha = 0.15f) else CarbsColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "BMI: %.1f (%s)".format(bmi, bmiCategory),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (bmi in 18.5..24.9) Emerald500 else CarbsColor,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatNumberBox("Kezdő súly", "%.1f kg".format(startWeight), MaterialTheme.colorScheme.onSurfaceVariant)
                                StatNumberBox("Jelenlegi", "%.1f kg".format(currentWeight), MaterialTheme.colorScheme.primary)
                                StatNumberBox("Cél súly", "%.1f kg".format(goalWeight), Emerald500)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action to open BMI & Health Metrics calculator
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (onNavigateToHealthCalculator != null) {
                                            onNavigateToHealthCalculator()
                                        } else {
                                            showHealthCalculatorSheet = true
                                        }
                                    }
                                    .testTag("stats_open_bmi_calc_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⚖️", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Részletes BMI & Egészségi Tartomány Kalkulátor",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text("Megnyitás ›", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Multi-month weight trend line chart
                            Text(
                                text = "Havi Súlyváltozás Trendje (Elmúlt hónapok):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            MultiMonthWeightTrendChart()

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (weightDiff <= 0) Emerald500.copy(alpha = 0.12f) else FatColor.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.TrendingDown,
                                            contentDescription = null,
                                            tint = if (weightDiff <= 0) Emerald500 else FatColor
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (weightDiff <= 0) "Teljes fogyás eddig:" else "Súlyváltozás:",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Text(
                                        text = "%+.1f kg".format(weightDiff),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (weightDiff <= 0) Emerald500 else FatColor
                                    )
                                }
                            }
                        }
                    }
                }

                // Havi Összefoglaló Szekció (Monthly Summary Card with Month Switcher)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "HAVI ÖSSZEFOGLALÓ JELENTÉS",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = activeSummary.monthYearHu,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Text(
                                        text = "⭐ ${activeSummary.consistencyScorePercent}% Sikeresség",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Month Selector Tabs
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(monthlySummaries.indices.toList()) { idx ->
                                    val summary = monthlySummaries[idx]
                                    val isSelected = idx == selectedMonthIndex
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { statsViewModel.selectMonthSummary(idx) },
                                        label = { Text(summary.monthYearHu, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Summary Grid 2x3
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    SummaryMetricBox(
                                        label = "Összes elégetett kalória",
                                        value = "${activeSummary.totalCaloriesBurned} kcal",
                                        icon = "🔥",
                                        modifier = Modifier.weight(1f)
                                    )
                                    SummaryMetricBox(
                                        label = "Napi átlagos kalória",
                                        value = "${activeSummary.avgDailyConsumedCalories} kcal",
                                        icon = "🥗",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    SummaryMetricBox(
                                        label = "Teljesített edzések",
                                        value = "${activeSummary.totalWorkoutsCount} alkalom (${activeSummary.totalWorkoutMinutes}p)",
                                        icon = "💪",
                                        modifier = Modifier.weight(1f)
                                    )
                                    SummaryMetricBox(
                                        label = "Napi átlag fehérje",
                                        value = "%.0f g / nap".format(activeSummary.avgDailyProteinGrams),
                                        icon = "🥩",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val weightChange = activeSummary.endingWeightKg - activeSummary.startingWeightKg
                                    SummaryMetricBox(
                                        label = "Súlyváltozás a hónapban",
                                        value = "%+.1f kg".format(weightChange),
                                        icon = "📉",
                                        modifier = Modifier.weight(1f)
                                    )
                                    SummaryMetricBox(
                                        label = "Átlagos vízfogyasztás",
                                        value = "%.1f L / nap".format(activeSummary.avgDailyWaterMl / 1000.0),
                                        icon = "💧",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Highlight notes
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏆", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Havi Csúcsteljesítmény:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("🔥 ${activeSummary.bestStreakDays} napos hibátlan sorozat és ${activeSummary.estimatedFatLostKg} kg tiszta zsírégetés!", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // 7-day Intake Chart
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Elmúlt 7 nap napi kalóriabevitele",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Napi cél: ${userProfile.getDailyCalorieTarget()} kcal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PastWeekCalorieChart(
                                pastWeekMeals = pastWeekMeals,
                                targetCalories = userProfile.getDailyCalorieTarget()
                            )
                        }
                    }
                }

                // Súlymérési napló lista
                item {
                    Text(
                        text = "Rögzített Súlymérések (${weights.size} db)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (weights.isEmpty()) {
                    item {
                        Text(
                            text = "Még nincsenek rögzített súlymérések.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(weights.reversed(), key = { it.id }) { weightEntry ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "%.1f kg".format(weightEntry.weightKg),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${weightEntry.date} ${if (weightEntry.note.isNotBlank()) "• " + weightEntry.note else ""}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { statsViewModel.deleteWeight(weightEntry) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Törlés",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Tab 1: Makró & Edzés Trendek az elmúlt hónapokban
                item {
                    // Multi-Month Macro Trends Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "MAKROTÁPANYAG TRENDEK",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Havi Átlagos Makrók (Elmúlt hónapok)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Legend
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(10.dp).background(ProteinColor, CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Fehérje", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(10.dp).background(CarbsColor, CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Szénhidrát", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(10.dp).background(FatColor, CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Zsír", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Macro comparative bars
                            macroTrends.forEach { trend ->
                                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(trend.label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(
                                            text = "F: %.0fg • Sz: %.0fg • Zs: %.0fg (%d kcal)".format(trend.avgProteinGrams, trend.avgCarbsGrams, trend.avgFatGrams, trend.avgTotalCalories.toInt()),
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))

                                    val totalGrams = (trend.avgProteinGrams + trend.avgCarbsGrams + trend.avgFatGrams).coerceAtLeast(1.0)
                                    val pRatio = (trend.avgProteinGrams / totalGrams).toFloat()
                                    val cRatio = (trend.avgCarbsGrams / totalGrams).toFloat()
                                    val fRatio = (trend.avgFatGrams / totalGrams).toFloat()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(14.dp)
                                            .clip(RoundedCornerShape(7.dp))
                                    ) {
                                        Box(modifier = Modifier.weight(pRatio.coerceAtLeast(0.01f)).fillMaxSize().background(ProteinColor))
                                        Box(modifier = Modifier.weight(cRatio.coerceAtLeast(0.01f)).fillMaxSize().background(CarbsColor))
                                        Box(modifier = Modifier.weight(fRatio.coerceAtLeast(0.01f)).fillMaxSize().background(FatColor))
                                    }
                                }
                            }
                        }
                    }
                }

                // Multi-Month Workout Frequency Trends Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "EDZÉS GYAKORISÁG & AKTIVITÁS",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Havi Kalisztenika Edzések Száma",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Bar chart of monthly workout counts
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                workoutTrends.forEach { w ->
                                    val maxScale = 24f
                                    val heightFraction = (w.sessionCount.toFloat() / maxScale).coerceIn(0.1f, 1f)
                                    val targetMet = w.sessionCount >= w.targetSessions

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text(
                                            text = "${w.sessionCount} db",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (targetMet) Emerald500 else MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(36.dp)
                                                .height((100 * heightFraction).dp)
                                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                                .background(
                                                    if (targetMet) Emerald500 else MaterialTheme.colorScheme.primary
                                                )
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = w.label.split(" ").firstOrNull() ?: w.label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE8F5E9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎯", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Havi cél: 16 edzés/hó (heti 4 edzés). Az elmúlt 2 hónapban 100%+ teljesítmény!",
                                        fontSize = 11.sp,
                                        color = Color(0xFF1B5E20),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    if (showWeightDialog) {
        AddWeightDialog(
            currentWeight = currentWeight,
            onDismiss = { showWeightDialog = false },
            onConfirm = { weight, note ->
                statsViewModel.logNewWeight(weight, note)
                showWeightDialog = false
            }
        )
    }

    if (showHealthCalculatorSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showHealthCalculatorSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            HealthMetricsCalculatorScreen(
                userProfile = userProfile,
                onSaveMetricsToProfile = { _, w ->
                    statsViewModel.logNewWeight(w, "BMI kalkulátorból rögzítve")
                    showHealthCalculatorSheet = false
                },
                onBack = { showHealthCalculatorSheet = false }
            )
        }
    }
}

@Composable
fun SummaryMetricBox(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun MultiMonthWeightTrendChart() {
    // 3 month points: June (87.6) -> July (86.0) -> August (82.0)
    val points = listOf(
        Pair("Június", 87.6f),
        Pair("Július", 86.0f),
        Pair("Augusztus", 82.0f)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            val width = size.width
            val height = size.height
            val minWeight = 80.0f
            val maxWeight = 89.0f
            val weightRange = maxWeight - minWeight

            val coords = points.mapIndexed { idx, point ->
                val x = (idx.toFloat() / (points.size - 1)) * (width - 60.dp.toPx()) + 30.dp.toPx()
                val y = height - ((point.second - minWeight) / weightRange) * height
                Offset(x, y.coerceIn(10f, height - 10f))
            }

            // Draw line
            val path = Path().apply {
                moveTo(coords[0].x, coords[0].y)
                for (i in 1 until coords.size) {
                    lineTo(coords[i].x, coords[i].y)
                }
            }

            drawPath(
                path = path,
                color = Color(0xFF2E7D32),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw dots & labels
            coords.forEachIndexed { idx, offset ->
                drawCircle(
                    color = Color(0xFF2E7D32),
                    radius = 6.dp.toPx(),
                    center = offset
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = offset
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            points.forEach { p ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(p.first, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("%.1f kg".format(p.second), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }
        }
    }
}

@Composable
private fun StatNumberBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun PastWeekCalorieChart(
    pastWeekMeals: List<com.example.data.model.MealEntry>,
    targetCalories: Int
) {
    val days = (6 downTo 0).map { LocalDate.now().minusDays(it.toLong()) }
    val grouped = pastWeekMeals.groupBy { it.date }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val maxScale = (targetCalories * 1.3f).coerceAtLeast(1000f)

        days.forEach { day ->
            val dateStr = day.toString()
            val dayMeals = grouped[dateStr] ?: emptyList()
            val cal = dayMeals.sumOf { it.calories }.toFloat()
            val heightFraction = (cal / maxScale).coerceIn(0.05f, 1f)
            val isOver = cal > targetCalories * 1.05f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                if (cal > 0) {
                    Text(
                        text = "${cal.toInt()}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height((90 * heightFraction).dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                            when {
                                cal == 0f -> MaterialTheme.colorScheme.surfaceVariant
                                isOver -> FatColor
                                else -> Emerald500
                            }
                        )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = day.format(DateTimeFormatter.ofPattern("E", Locale("hu"))),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (day == LocalDate.now()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (day == LocalDate.now()) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun AddWeightDialog(
    currentWeight: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit
) {
    var weightInput by remember { mutableStateOf("%.1f".format(currentWeight)) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Testsúly mérés rögzítése", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Testsúly (kg) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Megjegyzés (pl. Reggeli mérés éhgyomorra)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = weightInput.toDoubleOrNull() ?: 0.0
                    if (w > 0) {
                        onConfirm(w, note)
                    }
                }
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
