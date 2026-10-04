package com.example.ui.screens.health

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BmiCategory
import com.example.data.model.HealthMetricsEngine
import com.example.data.model.HealthMetricsResult
import com.example.data.model.UserProfile
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMetricsCalculatorScreen(
    userProfile: UserProfile? = null,
    onSaveMetricsToProfile: ((heightCm: Double, weightKg: Double) -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onBack != null) {
        BackHandler { onBack() }
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Initial values from userProfile if available
    var heightCm by remember {
        mutableDoubleStateOf(userProfile?.heightCm ?: 178.0)
    }
    var weightKg by remember {
        mutableDoubleStateOf(userProfile?.currentWeightKg ?: 82.0)
    }

    var heightInputText by remember(heightCm) {
        mutableStateOf(heightCm.roundToInt().toString())
    }
    var weightInputText by remember(weightKg) {
        mutableStateOf("%.1f".format(weightKg))
    }

    // Dynamic Calculation
    val metricsResult by remember(heightCm, weightKg) {
        derivedStateOf {
            HealthMetricsEngine.calculate(heightCm, weightKg)
        }
    }

    val categoryColor = Color(metricsResult.category.colorHex)
    val animatedGaugeRatio by animateFloatAsState(
        targetValue = metricsResult.progressGaugeRatio,
        animationSpec = tween(durationMillis = 400),
        label = "gaugeRatioAnimation"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Egészségügyi Mutatók & BMI",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Vissza"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hero Banner
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.MonitorWeight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "BMI & Egészségügyi Tartomány Kalkulátor",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Nemzetközi WHO standard egészségügyi határértékek és egyéni testsúly-célkövetés.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Interactive Body Metrics Inputs Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Testméretek Megadása",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (userProfile != null) {
                                OutlinedButton(
                                    onClick = {
                                        heightCm = userProfile.heightCm
                                        weightKg = userProfile.currentWeightKg
                                    },
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Profilból", fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // --- Height Input Section ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Height, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Magasság:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                text = "${heightCm.roundToInt()} cm",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Slider(
                            value = heightCm.toFloat(),
                            onValueChange = { heightCm = (it.roundToInt()).toDouble() },
                            valueRange = 120f..230f,
                            steps = 109,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("height_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        // Quick Height Preset Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(listOf(160, 165, 170, 175, 180, 185, 190, 195)) { preset ->
                                val isSelected = heightCm.roundToInt() == preset
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { heightCm = preset.toDouble() },
                                    label = { Text("${preset} cm", fontSize = 11.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // --- Weight Input Section ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonitorWeight, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Testsúly:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilledTonalIconButton(
                                    onClick = { weightKg = (weightKg - 0.5).coerceAtLeast(35.0) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Csökkentés", modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "%.1f kg".format(weightKg),
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Emerald600
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                FilledTonalIconButton(
                                    onClick = { weightKg = (weightKg + 0.5).coerceAtMost(250.0) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Növelés", modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Slider(
                            value = weightKg.toFloat(),
                            onValueChange = { weightKg = ((it * 10f).roundToInt() / 10f).toDouble() },
                            valueRange = 40f..160f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("weight_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = Emerald600,
                                activeTrackColor = Emerald600
                            )
                        )

                        // Quick Weight Preset Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(listOf(55.0, 65.0, 70.0, 75.0, 80.0, 85.0, 90.0, 100.0, 110.0)) { preset ->
                                val isSelected = (weightKg * 10).roundToInt() == (preset * 10).roundToInt()
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { weightKg = preset },
                                    label = { Text("${preset.toInt()} kg", fontSize = 11.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Primary BMI Result Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bmi_result_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = categoryColor.copy(alpha = 0.08f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, categoryColor.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TESTTÖMEG-INDEX (BMI)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Large BMI Value Display
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "%.1f".format(metricsResult.bmi),
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
                                fontWeight = FontWeight.ExtraBold,
                                color = categoryColor,
                                modifier = Modifier.testTag("bmi_value_text")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "kg/m²",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        // Category Pill Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = categoryColor.copy(alpha = 0.18f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(metricsResult.category.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = metricsResult.category.titleHu,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = categoryColor,
                                    modifier = Modifier.testTag("bmi_category_title")
                                )
                            }
                        }

                        Text(
                            text = metricsResult.category.subtitleHu,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Visual Health Range Segmented Gauge
                        BmiSegmentedGauge(
                            currentBmi = metricsResult.bmi,
                            progressRatio = animatedGaugeRatio,
                            activeColor = categoryColor
                        )
                    }
                }
            }

            // Progress Summary & Healthy Goal Distance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Emerald600,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Egészségi Zóna & Haladási Összegzés",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Target ideal range box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Egészséges normál tartomány:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${heightCm.roundToInt()} cm magassághoz",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = metricsResult.targetRangeTextHu,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress narrative
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (metricsResult.isHealthyNormal) Emerald500.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = if (metricsResult.isHealthyNormal) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (metricsResult.isHealthyNormal) Emerald600 else categoryColor,
                                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (metricsResult.isHealthyNormal) "Optimális állapot!" else "Célkitűzés az egészséges tartományért:",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (metricsResult.isHealthyNormal) Emerald600 else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = metricsResult.progressSummaryHu,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Health Risk text
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️ ", fontSize = 14.sp)
                            Text(
                                text = "Kockázati besorolás: ${metricsResult.category.healthRiskHu}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Standard Health Ranges Reference Legend
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "WHO Nemzetközi Egészségügyi Skála",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val ranges = listOf(
                            Triple("Soványság", "< 18.5", Color(0xFF0288D1)),
                            Triple("Normál testsúly", "18.5 – 24.9", Emerald500),
                            Triple("Túlsúly", "25.0 – 29.9", CarbsColor),
                            Triple("I. fokú elhízás", "30.0 – 34.9", FatColor),
                            Triple("II-III. fokú elhízás", "≥ 35.0", Color(0xFF991B1B))
                        )

                        ranges.forEach { (label, rangeStr, color) ->
                            val isCurrentTier = when (label) {
                                "Soványság" -> metricsResult.bmi < 18.5
                                "Normál testsúly" -> metricsResult.bmi in 18.5..24.9
                                "Túlsúly" -> metricsResult.bmi in 25.0..29.9
                                "I. fokú elhízás" -> metricsResult.bmi in 30.0..34.9
                                else -> metricsResult.bmi >= 35.0
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCurrentTier) color.copy(alpha = 0.15f) else Color.Transparent,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(color, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isCurrentTier) FontWeight.ExtraBold else FontWeight.Normal,
                                            color = if (isCurrentTier) color else MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = rangeStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (isCurrentTier) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCurrentTier) color else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        if (isCurrentTier) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Jelenlegi kategória",
                                                tint = color,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Actionable Lifestyle & Health Guidance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Emerald500.copy(alpha = 0.08f)
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Személyre szabott életmód tanácsok:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Emerald600
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        metricsResult.healthAdviceListHu.forEach { adviceItem ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", fontWeight = FontWeight.Bold, color = Emerald600)
                                Text(
                                    text = adviceItem,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Save to Profile & Weight Log Action
            if (onSaveMetricsToProfile != null) {
                item {
                    Button(
                        onClick = {
                            onSaveMetricsToProfile(heightCm, weightKg)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("A magasság és testsúly adatok elmentve a profilba és a súlynaplóba!")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_metrics_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mentés a Profilba & Súlynaplóba",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun BmiSegmentedGauge(
    currentBmi: Double,
    progressRatio: Float,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Needle / Pointer Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pointerX = size.width * progressRatio.coerceIn(0f, 1f)
                val trianglePath = Path().apply {
                    moveTo(pointerX, size.height)
                    lineTo(pointerX - 7.dp.toPx(), 0f)
                    lineTo(pointerX + 7.dp.toPx(), 0f)
                    close()
                }
                drawPath(path = trianglePath, color = activeColor)
            }
        }

        // Segmented Bar (Underweight, Normal, Overweight, Obese 1, Obese 2+)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            // Segment 1: Underweight (< 18.5) ~ 14%
            Box(
                modifier = Modifier
                    .weight(0.14f)
                    .background(Color(0xFF0288D1))
            )
            // Segment 2: Normal (18.5 - 24.9) ~ 26%
            Box(
                modifier = Modifier
                    .weight(0.26f)
                    .background(Emerald500)
            )
            // Segment 3: Overweight (25.0 - 29.9) ~ 20%
            Box(
                modifier = Modifier
                    .weight(0.20f)
                    .background(CarbsColor)
            )
            // Segment 4: Obese Class 1 (30.0 - 34.9) ~ 20%
            Box(
                modifier = Modifier
                    .weight(0.20f)
                    .background(FatColor)
            )
            // Segment 5: Obese Class 2+ (≥ 35.0) ~ 20%
            Box(
                modifier = Modifier
                    .weight(0.20f)
                    .background(Color(0xFF991B1B))
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Scale milestone numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("15", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("18.5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald600)
            Text("25.0", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CarbsColor)
            Text("30.0", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FatColor)
            Text("35.0+", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
