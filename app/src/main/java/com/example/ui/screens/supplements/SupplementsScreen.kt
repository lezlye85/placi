package com.example.ui.screens.supplements

import java.util.Locale
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SupplementsData
import com.example.data.model.MealType
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementGoal
import com.example.data.model.SupplementItem
import com.example.data.model.SupplementTiming
import com.example.data.model.UserDailySupplement
import com.example.ui.viewmodels.SupplementTab
import com.example.ui.viewmodels.SupplementsViewModel
import com.example.util.SupplementReminderManager
import com.example.util.TimingScheduleInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplementsScreen(
    supplementsViewModel: SupplementsViewModel,
    onNavigateToBarcodeScanner: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by supplementsViewModel.uiState.collectAsState()
    val userProfile by supplementsViewModel.userProfile.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBarcodeManualDialog by remember { mutableStateOf(false) }
    var manualBarcodeInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        supplementsViewModel.loadReminderSchedules(context)
    }

    LaunchedEffect(uiState.messageSnackbar) {
        uiState.messageSnackbar?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            supplementsViewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Étrendkiegészítő Központ",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "BioTech, Scitec, OstroVit • Adagolás & Időzítés",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Supplement Reminders Quick Settings Action
                    IconButton(
                        onClick = { supplementsViewModel.setReminderSettingsOpen(true) },
                        modifier = Modifier.testTag("supplement_reminders_topbar_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (uiState.isRemindersMasterEnabled) {
                                    Badge(
                                        containerColor = Color(0xFF4CAF50),
                                        modifier = Modifier.size(8.dp)
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.isRemindersMasterEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Adagolási emlékeztetők beállítása",
                                tint = if (uiState.isRemindersMasterEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onNavigateToBarcodeScanner,
                        modifier = Modifier.testTag("scan_supplement_barcode_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Vonalkód beolvasása",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main Tabs
            ScrollableTabRow(
                selectedTabIndex = uiState.activeTab.ordinal,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) }
            ) {
                SupplementTab.values().forEach { tab ->
                    Tab(
                        selected = uiState.activeTab == tab,
                        onClick = { supplementsViewModel.selectTab(tab) },
                        text = {
                            Text(
                                text = "${tab.iconEmoji} ${tab.titleHu}",
                                fontWeight = if (uiState.activeTab == tab) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (uiState.activeTab) {
                SupplementTab.DATABASE -> {
                    SupplementsDatabaseTab(
                        supplementsViewModel = supplementsViewModel,
                        onScanClicked = onNavigateToBarcodeScanner,
                        onManualBarcodeClicked = { showBarcodeManualDialog = true }
                    )
                }
                SupplementTab.PROTEIN_CALCULATOR -> {
                    ProteinCalculatorTab(
                        initialWeightKg = userProfile.currentWeightKg,
                        initialGender = userProfile.gender,
                        initialAge = userProfile.age,
                        initialHeightCm = userProfile.heightCm,
                        onApplyTargetToProfile = { result ->
                            supplementsViewModel.applyCalculatedTargetToProfile(result)
                        }
                    )
                }
                SupplementTab.MY_STACK -> {
                    SupplementsMyStackTab(
                        supplementsViewModel = supplementsViewModel,
                        onExploreCatalogClicked = { supplementsViewModel.selectTab(SupplementTab.DATABASE) }
                    )
                }
                SupplementTab.TIMING_GUIDE -> {
                    SupplementsTimingGuideTab(
                        supplementsViewModel = supplementsViewModel,
                        onSelectSupplement = { supplement ->
                            supplementsViewModel.selectSupplementForDetail(supplement)
                        }
                    )
                }
                SupplementTab.GOAL_RECOMMENDER -> {
                    SupplementsGoalRecommenderTab(
                        supplementsViewModel = supplementsViewModel,
                        onSelectSupplement = { supplement ->
                            supplementsViewModel.selectSupplementForDetail(supplement)
                        }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet for Supplement Details
    if (uiState.selectedSupplementForDetail != null) {
        val selected = uiState.selectedSupplementForDetail!!
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { supplementsViewModel.selectSupplementForDetail(null) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            SupplementDetailModalContent(
                supplement = selected,
                timingSchedules = uiState.timingSchedules,
                onDismiss = { supplementsViewModel.selectSupplementForDetail(null) },
                onAddToStack = {
                    supplementsViewModel.addToDailyStack(selected)
                    supplementsViewModel.selectSupplementForDetail(null)
                },
                onLogToDiary = { mealType ->
                    supplementsViewModel.logSupplementToDiary(selected, mealType)
                    supplementsViewModel.selectSupplementForDetail(null)
                }
            )
        }
    }

    // Modal Bottom Sheet for Supplement Reminder Settings
    if (uiState.isReminderSettingsOpen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { supplementsViewModel.setReminderSettingsOpen(false) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            SupplementReminderSettingsSheet(
                supplementsViewModel = supplementsViewModel,
                onDismiss = { supplementsViewModel.setReminderSettingsOpen(false) }
            )
        }
    }

    // Interactive Time Picker Dialog for Timing Slot
    if (uiState.selectedTimingForEdit != null) {
        val timing = uiState.selectedTimingForEdit!!
        val currentSchedule = uiState.timingSchedules.find { it.timing == timing }
        val currentHour = currentSchedule?.hour ?: (SupplementReminderManager.DEFAULT_HOURS[timing]?.first ?: 8)
        val currentMinute = currentSchedule?.minute ?: (SupplementReminderManager.DEFAULT_HOURS[timing]?.second ?: 0)

        TimingTimePickerDialog(
            timing = timing,
            initialHour = currentHour,
            initialMinute = currentMinute,
            onDismiss = { supplementsViewModel.setSelectedTimingForEdit(null) },
            onConfirm = { newHour, newMinute ->
                supplementsViewModel.updateTimingSlotTime(context, timing, newHour, newMinute)
                supplementsViewModel.setSelectedTimingForEdit(null)
            }
        )
    }

    // Manual Barcode Input Dialog
    if (showBarcodeManualDialog) {
        AlertDialog(
            onDismissRequest = { showBarcodeManualDialog = false },
            title = { Text("Kiegészítő Vonalkód Keresése") },
            text = {
                Column {
                    Text(
                        "Írd be a termék vonalkódját (pl. BioTech 100% Pure Whey: 5999076228300 vagy Scitec Whey Pro: 5996655100018):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = manualBarcodeInput,
                        onValueChange = { manualBarcodeInput = it },
                        label = { Text("Vonalkód") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val result = supplementsViewModel.handleScannedBarcode(manualBarcodeInput)
                        showBarcodeManualDialog = false
                        manualBarcodeInput = ""
                    }
                ) {
                    Text("Keresés")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showBarcodeManualDialog = false }) {
                    Text("Mégse")
                }
            }
        )
    }
}

// =========================================================================
// 1. TAB: KATALÓGUS & KERESŐ (DATABASE TAB)
// =========================================================================
@Composable
fun SupplementsDatabaseTab(
    supplementsViewModel: SupplementsViewModel,
    onScanClicked: () -> Unit,
    onManualBarcodeClicked: () -> Unit
) {
    val uiState by supplementsViewModel.uiState.collectAsState()
    val filteredList = supplementsViewModel.getFilteredSupplements()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Scan & Barcode Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Megvan a dobozod? Csippantsd be!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Olvasd be a vonalkódot a pontos adagolásért és időzítésért.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onScanClicked,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Beolvasás", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { supplementsViewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("supplement_search_input"),
                placeholder = { Text("Keresés (pl. BioTech, Scitec, OstroVit, Kreatin, Whey...)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { supplementsViewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Törlés")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Brand Selector Chips
        item {
            Column {
                Text(
                    text = "Márkák:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(supplementsViewModel.availableBrands) { brand ->
                        val isSelected = uiState.selectedBrand == brand
                        FilterChip(
                            selected = isSelected,
                            onClick = { supplementsViewModel.selectBrand(brand) },
                            label = { Text(brand, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Category Selector Chips
        item {
            Column {
                Text(
                    text = "Kategóriák:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SupplementCategory.values()) { category ->
                        val isSelected = uiState.selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { supplementsViewModel.selectCategory(category) },
                            label = {
                                Text(
                                    "${category.iconEmoji} ${category.displayNameHu}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Section header with results count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Elérhető Kiegészítők (${filteredList.size} db)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kattints a részletekért",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // List of supplements
        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Nem található a keresési feltételeknek megfelelő kiegészítő.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = {
                            supplementsViewModel.setSearchQuery("")
                            supplementsViewModel.selectCategory(SupplementCategory.ALL)
                            supplementsViewModel.selectBrand("Összes")
                        }) {
                            Text("Szűrők törlése")
                        }
                    }
                }
            }
        } else {
            items(filteredList) { supplement ->
                SupplementCardItem(
                    supplement = supplement,
                    onClick = { supplementsViewModel.selectSupplementForDetail(supplement) },
                    onAddToStack = { supplementsViewModel.addToDailyStack(supplement) }
                )
            }
        }
    }
}

@Composable
fun SupplementCardItem(
    supplement: SupplementItem,
    onClick: () -> Unit,
    onAddToStack: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Brand & Badge header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (supplement.brand) {
                        "BioTechUSA" -> MaterialTheme.colorScheme.primaryContainer
                        "Scitec Nutrition" -> MaterialTheme.colorScheme.secondaryContainer
                        "OstroVit" -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = supplement.brand,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = supplement.badgeTagHu,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = supplement.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Timing Pill
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${supplement.primaryTiming.iconEmoji} ${supplement.primaryTiming.displayNameHu}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Recommended Dosage Summary
            Text(
                text = "📌 Adagolás: ${supplement.recommendedDosageHu}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Macros preview (if has protein/calories)
            if (supplement.proteinPerServing > 0 || supplement.caloriesPerServing > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (supplement.caloriesPerServing > 0) {
                        Text(
                            text = "🔥 ${supplement.caloriesPerServing.toInt()} kcal",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (supplement.proteinPerServing > 0) {
                        Text(
                            text = "🥛 ${supplement.proteinPerServing}g fehérje",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "⚖️ ${supplement.servingUnitHu}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mikor & Mennyit?", style = MaterialTheme.typography.labelMedium)
                }

                Button(
                    onClick = onAddToStack,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rutinba", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

// =========================================================================
// 2. TAB: SAJÁT NAPI KIEGÉSZÍTŐ RUTINOM (MY STACK TAB)
// =========================================================================
@Composable
fun SupplementsMyStackTab(
    supplementsViewModel: SupplementsViewModel,
    onExploreCatalogClicked: () -> Unit
) {
    val context = LocalContext.current
    val uiState by supplementsViewModel.uiState.collectAsState()
    val stack = uiState.userDailyStack
    val takenCount = stack.count { it.isTakenToday }
    val progress = if (stack.isNotEmpty()) takenCount.toFloat() / stack.size else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Daily Progress Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Napi Kiegészítő Haladás",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$takenCount / ${stack.size} adag bevéve ma",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = if (progress == 1f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    if (progress == 1f) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎉 Kiváló! Minden mai kiegészítődet sikeresen bevetted!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Supplement Dosage Reminder Status & Quick Settings Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (uiState.isRemindersMasterEnabled)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (uiState.isRemindersMasterEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Adagolási Emlékeztetők",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (uiState.isRemindersMasterEnabled)
                                        "Aktív az adagolási útmutató alapján"
                                    else
                                        "Emlékeztetők kikapcsolva",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (uiState.isRemindersMasterEnabled)
                                        Color(0xFF2E7D32)
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = uiState.isRemindersMasterEnabled,
                            onCheckedChange = { enabled ->
                                supplementsViewModel.toggleMasterReminders(context, enabled)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("supplement_reminders_master_switch")
                        )
                    }

                    if (uiState.isRemindersMasterEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Beállított értesítések:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val activeSchedules = uiState.timingSchedules.filter { it.isEnabled }
                            items(activeSchedules) { schedule ->
                                val countInStack = stack.count { it.timing == schedule.timing }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.clickable {
                                        supplementsViewModel.setSelectedTimingForEdit(schedule.timing)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(schedule.timing.iconEmoji, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = schedule.formattedTime,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (countInStack > 0) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Surface(
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                modifier = Modifier.size(16.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "$countInStack",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { supplementsViewModel.setReminderSettingsOpen(true) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Időzítések", style = MaterialTheme.typography.labelSmall)
                        }

                        Button(
                            onClick = { supplementsViewModel.triggerTestReminderNotification(context) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Teszt Értesítés", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mai Kiegészítőim Időrendben",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onExploreCatalogClicked,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hozzáadás", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // List of user supplements
        if (stack.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💊", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Még nincsenek kiegészítők a napi rutinodban.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Válassz a katalógusból vagy állíts össze egy ajánlott stack-et!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onExploreCatalogClicked) {
                            Text("Böngészés a katalógusban")
                        }
                    }
                }
            }
        } else {
            items(stack) { item ->
                val schedule = uiState.timingSchedules.find { it.timing == item.timing }
                val reminderTimeStr = schedule?.formattedTime

                UserSupplementDailyCard(
                    item = item,
                    reminderTime = reminderTimeStr,
                    isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                    onToggleTaken = { supplementsViewModel.toggleSupplementTaken(item.id) },
                    onEditTiming = { supplementsViewModel.setSelectedTimingForEdit(item.timing) },
                    onRemove = { supplementsViewModel.removeFromDailyStack(item.id) }
                )
            }
        }
    }
}

@Composable
fun UserSupplementDailyCard(
    item: UserDailySupplement,
    reminderTime: String?,
    isReminderEnabled: Boolean,
    onToggleTaken: () -> Unit,
    onEditTiming: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isTakenToday)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isTakenToday) 1.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isTakenToday,
                onCheckedChange = { onToggleTaken() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag("toggle_supplement_${item.id}")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.supplementName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "${item.brand} • ${item.timing.iconEmoji} ${item.timing.displayNameHu}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (reminderTime != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isReminderEnabled)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onEditTiming() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = if (isReminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = reminderTime,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isReminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Text(
                    text = item.dosageText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.isTakenToday && item.takenTimeHu != null) {
                    Text(
                        text = "✅ Bevéve: ${item.takenTimeHu}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Törlés",
                    tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// =========================================================================
// 3. TAB: IDŐZÍTÉSI KISOKOS (TIMING GUIDE TAB)
// =========================================================================
@Composable
fun SupplementsTimingGuideTab(
    supplementsViewModel: SupplementsViewModel,
    onSelectSupplement: (SupplementItem) -> Unit
) {
    val allSupps = SupplementsData.SUPPLEMENTS
    val uiState by supplementsViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⏱️ Napi Időzítési Útmutató: Mikor mit kell bevenni?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "A tápanyagok megfelelő időben történő bevitele jelentősen javítja a felszívódást, a sportteljesítményt és az éjszakai regenerációt. Az emlékeztető órákra koppintva módosíthatod az értesítési időpontokat.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Time Slot 1: Reggel ébredés után & Reggelivel
        item {
            val timing = SupplementTiming.WITH_BREAKFAST
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "1. Reggel ébredés után & Reggeli étkezéssel",
                iconEmoji = "🌅",
                subtitle = "Immunerő, hormonok & zsíroldékony vitaminok",
                guidelineHu = "A D3+K2 és az Omega-3 vitaminokat mindig zsírtartalmú étkezéssel (pl. tojás, zabpehely magvakkal) vedd be a tökéletes felszívódásért! A Multivitamin reggel biztosítja az egész napos energiaszintet.",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter {
                    it.primaryTiming == SupplementTiming.WITH_BREAKFAST || it.primaryTiming == SupplementTiming.MORNING_FASTED
                },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }

        // Time Slot 2: Edzés előtt 20-30 perccel
        item {
            val timing = SupplementTiming.PRE_WORKOUT
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "2. Edzés előtt 20-30 perccel",
                iconEmoji = "⚡",
                subtitle = "Fókusz, robbanékonyság & zsírmobilizáció",
                guidelineHu = "Pörgetők (Hot Blood, Black Blood) és L-Karnitin bevétele bő vízzel, fél órával a bemelegítés előtt. Ne fogyaszd közvetlenül nehéz étkezés után!",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter { it.primaryTiming == SupplementTiming.PRE_WORKOUT },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }

        // Time Slot 3: Edzés közben
        item {
            val timing = SupplementTiming.INTRA_WORKOUT
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "3. Edzés közben (Intra-Workout)",
                iconEmoji = "🥤",
                subtitle = "Izomvédelem, aminosavak & hidratáció",
                guidelineHu = "BCAA és Glutamin aminosavak a kulacsba keverve, folyamatosan kortyolgatva a hosszú vagy intenzív edzések alatt az izomleépülés megelőzésére.",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter { it.primaryTiming == SupplementTiming.INTRA_WORKOUT },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }

        // Time Slot 4: Edzés után azonnal (30 percen belül)
        item {
            val timing = SupplementTiming.POST_WORKOUT
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "4. Edzés után azonnal (Anabolikus ablak)",
                iconEmoji = "🏋️",
                subtitle = "Izomépítés, fehérjeszintézis & glikogén visszatöltés",
                guidelineHu = "1 adag gyors felszívódású tejsavó fehérje (Whey Protein) + 5g Kreatin monohidrát vízzel összerázva a legfontosabb kombináció az izomnövekedés azonnali beindításához.",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter { it.primaryTiming == SupplementTiming.POST_WORKOUT },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }

        // Time Slot 5: Napközben étkezésekkel
        item {
            val timing = SupplementTiming.WITH_MEAL
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "5. Napközben étkezésekkel",
                iconEmoji = "🍽️",
                subtitle = "Ízületvédelem & folyamatos aminosav ellátás",
                guidelineHu = "Komplex ízületvédők (Arthro Guard, Joint-X) és Omega-3 bevétele ebéd vagy vacsora közben.",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter { it.primaryTiming == SupplementTiming.WITH_MEAL || it.primaryTiming == SupplementTiming.DAILY_ANYTIME },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }

        // Time Slot 6: Lefekvés előtt 30-45 perccel
        item {
            val timing = SupplementTiming.BEFORE_BED
            val schedule = uiState.timingSchedules.find { it.timing == timing }
            TimingSlotSection(
                title = "6. Este, lefekvés előtt 30-45 perccel",
                iconEmoji = "🌙",
                subtitle = "Mély alvás, tesztoszteron & éjszakai anabolizmus",
                guidelineHu = "Magnézium-biszglicinát, Cink és Ashwagandha KSM-66 az idegrendszer megnyugtatására és a mély alvási fázisok támogatására. Kazein fehérje a lassú, 8 órás éjszakai aminosav ellátásért.",
                scheduleTime = schedule?.formattedTime,
                isReminderEnabled = uiState.isRemindersMasterEnabled && (schedule?.isEnabled ?: true),
                supplements = allSupps.filter { it.primaryTiming == SupplementTiming.BEFORE_BED },
                onEditTime = { supplementsViewModel.setSelectedTimingForEdit(timing) },
                onSelectSupplement = onSelectSupplement
            )
        }
    }
}

@Composable
fun TimingSlotSection(
    title: String,
    iconEmoji: String,
    subtitle: String,
    guidelineHu: String,
    scheduleTime: String?,
    isReminderEnabled: Boolean,
    supplements: List<SupplementItem>,
    onEditTime: () -> Unit,
    onSelectSupplement: (SupplementItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (scheduleTime != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isReminderEnabled)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { onEditTime() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Emlékeztető időpont",
                                tint = if (isReminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = scheduleTime,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isReminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Text(
                    text = "💡 $guidelineHu",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (supplements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ajánlott termékek ehhez az időponthoz:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    supplements.forEach { supp ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectSupplement(supp) },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${supp.brand} - ${supp.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = supp.recommendedDosageHu,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 4. TAB: "MIT ÉRDEMES SZEDNEM?" (GOAL RECOMMENDER TAB)
// =========================================================================
@Composable
fun SupplementsGoalRecommenderTab(
    supplementsViewModel: SupplementsViewModel,
    onSelectSupplement: (SupplementItem) -> Unit
) {
    var selectedRecommenderGoal by remember { mutableStateOf(SupplementGoal.MUSCLE_BUILDING) }
    val recommendedStack = SupplementsData.getRecommendedStack(selectedRecommenderGoal)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 Személyre Szabott Kiegészítő Ajánló",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Válaszd ki a jelenlegi fő célodat, és megmutatjuk a leghatékonyabb, tudományosan igazolt kiegészítő kombinációt!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Goal Selection Chips
        item {
            Column {
                Text(
                    text = "Válaszd ki a célodat:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SupplementGoal.values().filter { it != SupplementGoal.ALL }) { goal ->
                        val isSelected = selectedRecommenderGoal == goal
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedRecommenderGoal = goal },
                            label = {
                                Text(
                                    "${goal.iconEmoji} ${goal.displayNameHu}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Goal Description & Action Button
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedRecommenderGoal.iconEmoji} ${selectedRecommenderGoal.displayNameHu} Csomag",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = when (selectedRecommenderGoal) {
                            SupplementGoal.MUSCLE_BUILDING -> "A maximális izomépítéshez elengedhetetlen a megfelelő napi fehérjebevitel (1.8-2.2g/tskg), a sejtvolumenizáló kreatin az erőszinthez, és az alap multivitamin + magnézium a regenerációhoz."
                            SupplementGoal.FAT_LOSS -> "Diéta és szálkásítás során a kalóriamegtakarítás mellett védeni kell az izomtömeget tiszta izolátum fehérjével, miközben az L-Karnitin és zsírégető komplex fokozza a zsírsavak elégetését."
                            SupplementGoal.STRENGTH_ENDURANCE -> "Katonai és kalisztenika felkészüléshez robbanékony erőt ad a kreatin és az edzés előtti formula, miközben a BCAA és magnézium megelőzi az izomgörcsöket."
                            SupplementGoal.JOINTS_MOBILITY -> "Az intenzív saját testsúlyos edzés inakat és porcokat terhel. Az Arthro Guard, tiszta kollagén és C-vitamin szinergiája megvédi a könyököt, csuklót és térdet."
                            SupplementGoal.HEALTH_IMMUNITY -> "A sportoló szervezet alap immunvédelme: magas dózisú multivitamin, D3+K2 az optimális tesztoszteronszinthez, és EPA/DHA Omega-3 zsírsavak a gyulladáscsökkentéshez."
                            SupplementGoal.SLEEP_STRESS -> "Az izmok és idegrendszer alvás közben regenerálódnak. A prémium KSM-66 Ashwagandha és kelátos magnézium mélyíti a lassú hullámú alvást és csökkenti a kortizolt."
                            else -> "Teljes körű kiegyensúlyozott kiegészítő csomag."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            recommendedStack.forEach { supp ->
                                supplementsViewModel.addToDailyStack(supp)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("A teljes ajánlott stack hozzáadása a napi rutinomhoz")
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "A csomagban szereplő termékek (${recommendedStack.size} db):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Recommended items
        items(recommendedStack) { supplement ->
            SupplementCardItem(
                supplement = supplement,
                onClick = { onSelectSupplement(supplement) },
                onAddToStack = { supplementsViewModel.addToDailyStack(supplement) }
            )
        }
    }
}

// =========================================================================
// 5. DETAIL MODAL (Mikor és Mennyit kell bevenni?)
// =========================================================================
@Composable
fun SupplementDetailModalContent(
    supplement: SupplementItem,
    timingSchedules: List<TimingScheduleInfo> = emptyList(),
    onDismiss: () -> Unit,
    onAddToStack: () -> Unit,
    onLogToDiary: (MealType) -> Unit
) {
    var selectedMealForLog by remember { mutableStateOf(MealType.SNACK) }
    var showMealSelector by remember { mutableStateOf(false) }

    val matchedSchedule = timingSchedules.find { it.timing == supplement.primaryTiming }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${supplement.brand} • ${supplement.category.displayNameHu}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Bezárás")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = supplement.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 1: MIKOR KELL BEVENNI?
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(supplement.primaryTiming.iconEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mikor kell bevenni?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (matchedSchedule != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Emlékeztető: ${matchedSchedule.formattedTime}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = supplement.timingDescriptionHu,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SECTION 2: MENNYIT KELL BEVENNI?
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚖️", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mennyit kell bevenni? (Pontos Adagolás)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = supplement.recommendedDosageHu,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SECTION 3: MIÉRT ÉRDEMES SZEDNI?
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💡 Miért érdemes szedni? (Hatásmechanizmus)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = supplement.whyTakeItHu,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SECTION 4: HATÓANYAGOK & KOMBINÁLÁS (STACKING)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🧪 Főbb Összetevők:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = supplement.activeIngredientsHu,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🔗 Mivel érdemes kombinálni?",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = supplement.stackingTipsHu,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // SECTION 5: TÁPÉRTÉKEK (HA RELEVÁNS)
        if (supplement.caloriesPerServing > 0 || supplement.proteinPerServing > 0) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📊 Tápérték 1 adagban (${supplement.servingUnitHu}):",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MacroInfoColumn("Kalória", "${supplement.caloriesPerServing.toInt()} kcal", MaterialTheme.colorScheme.onSurface)
                        MacroInfoColumn("Fehérje", "${supplement.proteinPerServing}g", MaterialTheme.colorScheme.primary)
                        MacroInfoColumn("Szénhidrát", "${supplement.carbsPerServing}g", MaterialTheme.colorScheme.onSurfaceVariant)
                        MacroInfoColumn("Zsír", "${supplement.fatPerServing}g", MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Barcode info
        Text(
            text = "🏷️ Vonalkód: ${supplement.barcode}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { showMealSelector = !showMealSelector },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("📝 Naplózás étkezéshez", style = MaterialTheme.typography.labelMedium)
            }

            Button(
                onClick = onAddToStack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Napi Rutinba", style = MaterialTheme.typography.labelMedium)
            }
        }

        // Meal type selector dropdown/chips when "Naplózás étkezéshez" clicked
        AnimatedVisibility(visible = showMealSelector) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "Melyik étkezéshez adod hozzá?",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MealType.values().forEach { meal ->
                        FilterChip(
                            selected = selectedMealForLog == meal,
                            onClick = {
                                selectedMealForLog = meal
                                onLogToDiary(meal)
                            },
                            label = { Text(meal.displayNameHu, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// =========================================================================
// 6. EMLÉKEZTETŐ BEÁLLÍTÁSOK (REMINDER SETTINGS BOTTOM SHEET)
// =========================================================================
@Composable
fun SupplementReminderSettingsSheet(
    supplementsViewModel: SupplementsViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val uiState by supplementsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Étrendkiegészítő Emlékeztetők",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Adagolási útmutató szerinti ütemezés",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Bezárás")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Főkapcsoló Kártya
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.isRemindersMasterEnabled)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Emlékeztetők engedélyezése",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (uiState.isRemindersMasterEnabled)
                            "Értesítéseket kapsz a beállított időpontokban"
                        else
                            "Az összes kiegészítő értesítés szüneteltetve van",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = uiState.isRemindersMasterEnabled,
                    onCheckedChange = { enabled ->
                        supplementsViewModel.toggleMasterReminders(context, enabled)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Időzítési Résidők & Időpontok:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Koppints a ceruzára vagy az időpontra az értesítés pontos idejének átállításához!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            uiState.timingSchedules.forEach { schedule ->
                val countInStack = uiState.userDailyStack.count { it.timing == schedule.timing }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (schedule.isEnabled && uiState.isRemindersMasterEnabled)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(schedule.timing.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = schedule.timing.displayNameHu,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (countInStack > 0)
                                        "💊 $countInStack db kiegészítő a rutinodban"
                                    else
                                        "Nincs kiegészítő ebben az idősávban",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (countInStack > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.clickable {
                                    supplementsViewModel.setSelectedTimingForEdit(schedule.timing)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Módosítás",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = schedule.formattedTime,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Switch(
                                checked = schedule.isEnabled,
                                onCheckedChange = { enabled ->
                                    supplementsViewModel.toggleTimingSlot(context, schedule.timing, enabled)
                                },
                                enabled = uiState.isRemindersMasterEnabled
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Teszt Értesítés Gomb
        Button(
            onClick = { supplementsViewModel.triggerTestReminderNotification(context) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Teszt Értesítés Küldése Most")
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

// =========================================================================
// 7. TIME PICKER DIALOG (TOUCH FRIENDLY HOURS & MINUTES PICKER)
// =========================================================================
@Composable
fun TimingTimePickerDialog(
    timing: SupplementTiming,
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(timing.iconEmoji, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${timing.displayNameHu} Időpontja",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Értesítés ideje az adagolási útmutatóhoz",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Digital Clock Display
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "%02d : %02d", selectedHour, selectedMinute),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hour & Minute Steppers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hour selector
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Óra (0-23)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = { selectedHour = (selectedHour - 1 + 24) % 24 },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("-1h", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { selectedHour = (selectedHour + 1) % 24 },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("+1h", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Minute selector
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Perc (0-59)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = { selectedMinute = (selectedMinute - 5 + 60) % 60 },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("-5m", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { selectedMinute = (selectedMinute + 5) % 60 },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("+5m", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Presets
                Text(
                    text = "Gyors Választás:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        Pair(7, 0) to "07:00",
                        Pair(8, 0) to "08:00",
                        Pair(12, 30) to "12:30",
                        Pair(16, 30) to "16:30",
                        Pair(22, 0) to "22:00"
                    )
                    presets.forEach { (time, label) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedHour == time.first && selectedMinute == time.second)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedHour = time.first
                                    selectedMinute = time.second
                                }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = if (selectedHour == time.first && selectedMinute == time.second)
                                    Color.White
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedHour, selectedMinute)
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Mégse")
            }
        }
    )
}

@Composable
fun MacroInfoColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
    }
}
