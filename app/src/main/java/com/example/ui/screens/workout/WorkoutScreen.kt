package com.example.ui.screens.workout

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CalisthenicsExercise
import com.example.data.model.CalisthenicsRoutine
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.DifficultyLevel
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutLog
import com.example.ui.theme.BurnedCalColor
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.viewmodels.WorkoutViewModel

@Composable
fun WorkoutScreen(
    workoutViewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val activeRoutine by workoutViewModel.activeRoutine.collectAsState()
    val sessionCompleted by workoutViewModel.sessionCompleted.collectAsState()
    val workoutLogs by workoutViewModel.workoutLogs.collectAsState()
    val allCalisthenicsSets by workoutViewModel.allCalisthenicsSets.collectAsState()
    val prisonHistory by workoutViewModel.prisonPerformanceHistory.collectAsState()
    val militaryHistory by workoutViewModel.militaryPerformanceHistory.collectAsState()

    var historyFilterCategory by remember { mutableStateOf<WorkoutCategory?>(null) }
    var showManualSetLogDialog by remember { mutableStateOf(false) }

    val selectedCategory by workoutViewModel.selectedCategory.collectAsState()
    val searchQuery by workoutViewModel.searchQuery.collectAsState()
    val selectedPlanForDetail by workoutViewModel.selectedPlanForDetail.collectAsState()
    val activePlanId by workoutViewModel.activePlanId.collectAsState()
    val toastMessage by workoutViewModel.planSelectToastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showCustomWorkoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            workoutViewModel.clearPlanSelectToast()
        }
    }

    // Filter routines by Category and Search Query
    val filteredRoutines = remember(workoutViewModel.routines, selectedCategory, searchQuery) {
        workoutViewModel.routines.filter { routine ->
            val matchesCategory = when (selectedCategory) {
                WorkoutCategory.ALL -> true
                else -> routine.category == selectedCategory
            }
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                val q = searchQuery.trim().lowercase()
                routine.titleHu.lowercase().contains(q) ||
                    routine.subtitleHu.lowercase().contains(q) ||
                    routine.descriptionHu.lowercase().contains(q) ||
                    routine.focusAreaHu.lowercase().contains(q) ||
                    routine.exercises.any { it.nameHu.lowercase().contains(q) || it.targetMuscle.lowercase().contains(q) }
            }
            matchesCategory && matchesSearch
        }
    }

    val activePrimaryPlan = remember(workoutViewModel.routines, activePlanId) {
        workoutViewModel.routines.find { it.id == activePlanId }
    }

    // If an active live workout session is running, display the Player!
    if (activeRoutine != null) {
        WorkoutPlayerScreen(
            workoutViewModel = workoutViewModel,
            routine = activeRoutine!!,
            sessionCompleted = sessionCompleted,
            onExit = { workoutViewModel.exitRoutine() }
        )
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCustomWorkoutDialog = true },
                    containerColor = BurnedCalColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("log_custom_workout_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DirectionsRun, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Egyéni edzés", fontWeight = FontWeight.Bold)
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Header Banner
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Otthoni Kalisztenika Edzéstervek",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Választható saját testsúlyos edzéstervek lépésről lépésre útmutatóval.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Active Chosen Plan Hero Card (if selected)
                if (activePrimaryPlan != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Emerald500.copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Emerald600,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Kiválasztott Fő Terved",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Emerald600
                                            )
                                        }
                                    }

                                    Text(
                                        text = "📅 ${activePrimaryPlan.recommendedScheduleHu}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = activePrimaryPlan.titleHu,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = activePrimaryPlan.focusAreaHu,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { workoutViewModel.selectPlanForDetail(activePrimaryPlan) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Útmutató", style = MaterialTheme.typography.labelMedium)
                                    }

                                    Button(
                                        onClick = { workoutViewModel.startRoutine(activePrimaryPlan) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Indítás", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { workoutViewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("workout_search_input"),
                        placeholder = { Text("Keresés gyakorlat, izom vagy terv szerint...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { workoutViewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Törlés")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Category Filter Chips
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(WorkoutCategory.values()) { category ->
                            val isSelected = category == selectedCategory
                            FilterChip(
                                selected = isSelected,
                                onClick = { workoutViewModel.selectCategory(category) },
                                label = {
                                    Text(
                                        text = "${category.iconEmoji} ${category.displayNameHu}",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Section Title: Edzéstervek száma
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Választható Edzéstervek (${filteredRoutines.size}):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (searchQuery.isNotBlank() || selectedCategory != WorkoutCategory.ALL) {
                            TextButton(
                                onClick = {
                                    workoutViewModel.selectCategory(WorkoutCategory.ALL)
                                    workoutViewModel.setSearchQuery("")
                                }
                            ) {
                                Text("Szűrők törlése", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (filteredRoutines.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Nincs találat a keresési feltételekre", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Próbálj más kategóriát vagy keresőszót.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                // Calisthenics Routines List
                items(filteredRoutines, key = { it.id }) { routine ->
                    val isPlanSelectedAsPrimary = routine.id == activePlanId
                    WorkoutRoutineCard(
                        routine = routine,
                        isPrimaryPlan = isPlanSelectedAsPrimary,
                        onViewDetails = { workoutViewModel.selectPlanForDetail(routine) },
                        onSelectPrimaryPlan = { workoutViewModel.setActivePrimaryPlan(routine) },
                        onStartWorkout = { workoutViewModel.startRoutine(routine) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Section: Börtön- és Katonai Teljesítmény Napló & Sorozat-követés
                item {
                    val displayedSets = when (historyFilterCategory) {
                        WorkoutCategory.PRISON -> prisonHistory
                        WorkoutCategory.MILITARY -> militaryHistory
                        else -> allCalisthenicsSets
                    }
                    val totalSets = displayedSets.size
                    val totalReps = displayedSets.sumOf { it.repsCompleted }
                    val prCount = displayedSets.count { it.isPersonalRecord }

                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Emerald500.copy(alpha = 0.15f),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.FitnessCenter,
                                                contentDescription = null,
                                                tint = Emerald600,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Börtön & Katonai Teljesítmény",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Sorozatok, ismétlések és rekordok",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                TextButton(
                                    onClick = { showManualSetLogDialog = true },
                                    modifier = Modifier.testTag("log_manual_set_button")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+ Sorozat", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Summary KPI Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                                    .padding(vertical = 10.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Szériák", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("$totalSets db", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Összes ismétlés", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("$totalReps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Emerald600)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Rekordok (PR)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("⭐ $prCount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = CarbsColor)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Category filter chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                FilterChip(
                                    selected = historyFilterCategory == null,
                                    onClick = { historyFilterCategory = null },
                                    label = { Text("Mind", style = MaterialTheme.typography.labelSmall) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                FilterChip(
                                    selected = historyFilterCategory == WorkoutCategory.PRISON,
                                    onClick = { historyFilterCategory = WorkoutCategory.PRISON },
                                    label = { Text("⛓️ Börtön", style = MaterialTheme.typography.labelSmall) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                FilterChip(
                                    selected = historyFilterCategory == WorkoutCategory.MILITARY,
                                    onClick = { historyFilterCategory = WorkoutCategory.MILITARY },
                                    label = { Text("🪖 Katonai", style = MaterialTheme.typography.labelSmall) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Render list of calisthenics set logs
                val displayedHistorySets = when (historyFilterCategory) {
                    WorkoutCategory.PRISON -> prisonHistory
                    WorkoutCategory.MILITARY -> militaryHistory
                    else -> allCalisthenicsSets
                }

                if (displayedHistorySets.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                text = "Még nincs rögzített sorozat ebben a kategóriában. Indíts el egy börtön vagy katonai edzést, vagy rögzíts egyet a + Sorozat gombbal!",
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    items(displayedHistorySets.take(15), key = { it.id }) { setLog ->
                        CalisthenicsSetItemCard(
                            setLog = setLog,
                            onDelete = { workoutViewModel.deleteCalisthenicsSet(setLog.id) }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                // Section Title: Edzés Előzmények
                if (workoutLogs.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Korábbi Edzések Naplója:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(workoutLogs.take(10), key = { it.id }) { log ->
                        WorkoutLogItemCard(
                            log = log,
                            onDelete = { workoutViewModel.deleteLog(log) }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Step-by-step Plan Detail Dialog
    if (selectedPlanForDetail != null) {
        val plan = selectedPlanForDetail!!
        val isPlanSelected = plan.id == activePlanId
        WorkoutPlanDetailDialog(
            routine = plan,
            isPrimaryPlan = isPlanSelected,
            onDismiss = { workoutViewModel.selectPlanForDetail(null) },
            onSetAsPrimaryPlan = {
                workoutViewModel.setActivePrimaryPlan(plan)
            },
            onStartWorkout = {
                workoutViewModel.startRoutine(plan)
            }
        )
    }

    // Custom External Workout Log Dialog
    if (showCustomWorkoutDialog) {
        CustomWorkoutLogDialog(
            onDismiss = { showCustomWorkoutDialog = false },
            onConfirm = { title, minutes, calories, notes ->
                workoutViewModel.logCustomWorkout(title, minutes, calories, notes)
                showCustomWorkoutDialog = false
            }
        )
    }

    // Manual Calisthenics Set Log Dialog (Börtön / Katonai / Kalisztenika)
    if (showManualSetLogDialog) {
        ManualCalisthenicsSetDialog(
            routines = workoutViewModel.routines,
            onDismiss = { showManualSetLogDialog = false },
            onConfirm = { routineId, routineTitle, category, exerciseId, exerciseName, setNumber, targetReps, repsCompleted, weightAdded, rpe, isPr, notes ->
                workoutViewModel.logCalisthenicsSetManual(
                    routineId = routineId,
                    routineTitle = routineTitle,
                    category = category,
                    exerciseId = exerciseId,
                    exerciseName = exerciseName,
                    setNumber = setNumber,
                    targetReps = targetReps,
                    repsCompleted = repsCompleted,
                    weightAddedKg = weightAdded,
                    rpe = rpe,
                    isPersonalRecord = isPr,
                    notes = notes
                )
                showManualSetLogDialog = false
            }
        )
    }
}

@Composable
fun WorkoutRoutineCard(
    routine: CalisthenicsRoutine,
    isPrimaryPlan: Boolean,
    onViewDetails: () -> Unit,
    onSelectPrimaryPlan: () -> Unit,
    onStartWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isQuickListExpanded by remember { mutableStateOf(false) }

    val diffColor = when (routine.difficulty) {
        DifficultyLevel.BEGINNER -> Emerald500
        DifficultyLevel.INTERMEDIATE -> CarbsColor
        DifficultyLevel.ADVANCED -> BurnedCalColor
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Badge + Difficulty + Calorie & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = diffColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = routine.difficulty.displayNameHu,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = diffColor
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "${routine.category.iconEmoji} ${routine.category.displayNameHu}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Bookmark / Star for active primary plan
                IconButton(
                    onClick = onSelectPrimaryPlan,
                    modifier = Modifier.size(32.dp).testTag("select_plan_star_${routine.id}")
                ) {
                    Icon(
                        imageVector = if (isPrimaryPlan) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = if (isPrimaryPlan) "Kiválasztott fő terv" else "Kiválasztás fő tervként",
                        tint = if (isPrimaryPlan) Emerald500 else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = routine.titleHu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = routine.subtitleHu,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = routine.descriptionHu,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Metadata Row (Calories, Duration, Equipment)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = BurnedCalColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "~${routine.totalCaloriesBurn} kcal",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BurnedCalColor
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${routine.estimatedMinutes} perc",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "🛠 ${routine.requiredEquipmentHu}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Cél: ${routine.focusAreaHu}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Exercise Accordion Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isQuickListExpanded = !isQuickListExpanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${routine.exercises.size} gyakorlat áttekintése",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isQuickListExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Exercise List in expandable section
            AnimatedVisibility(visible = isQuickListExpanded) {
                Column(modifier = Modifier.padding(top = 6.dp)) {
                    routine.exercises.forEachIndexed { index, ex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${index + 1}. ${ex.nameHu}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Izom: ${ex.targetMuscle}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${ex.sets} × ${ex.repsOrSec}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dual Action Buttons: "Részletes Útmutató" & "Edzés Indítása"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("view_details_${routine.id}"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Lépések & Tippek", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("start_workout_${routine.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edzés Indítása", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanDetailDialog(
    routine: CalisthenicsRoutine,
    isPrimaryPlan: Boolean,
    onDismiss: () -> Unit,
    onSetAsPrimaryPlan: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val diffColor = when (routine.difficulty) {
        DifficultyLevel.BEGINNER -> Emerald500
        DifficultyLevel.INTERMEDIATE -> CarbsColor
        DifficultyLevel.ADVANCED -> BurnedCalColor
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Edzésterv Részletei",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Bezárás")
                            }
                        },
                        actions = {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = diffColor.copy(alpha = 0.15f),
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                Text(
                                    text = routine.difficulty.displayNameHu,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = diffColor
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    Surface(
                        tonalElevation = 3.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onSetAsPrimaryPlan,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    if (isPrimaryPlan) Icons.Default.Check else Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = if (isPrimaryPlan) Emerald500 else MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isPrimaryPlan) "Kiválasztva" else "Fő Tervemnek",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    onDismiss()
                                    onStartWorkout()
                                },
                                modifier = Modifier.weight(1.2f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edzés Indítása", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = routine.titleHu,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = routine.subtitleHu,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⏱ Időtartam", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${routine.estimatedMinutes} perc", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔥 Kalória", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("~${routine.totalCaloriesBurn} kcal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = BurnedCalColor)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📅 Ajánlott", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(routine.recommendedScheduleHu, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Focus area & Equipment
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Célzott területek:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Text(routine.focusAreaHu, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Szükséges eszközök:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Text(routine.requiredEquipmentHu, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Warm-up guidance
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald500.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = Emerald600, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Bemelegítési javaslat (3-5 perc):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Emerald600)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(routine.warmUpHu, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Lépésről Lépésre Gyakorlatsor (${routine.exercises.size} gyakorlat):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Detailed exercise step cards
                    items(routine.exercises.mapIndexed { i, ex -> Pair(i + 1, ex) }, key = { it.second.id }) { (index, exercise) ->
                        DetailedExerciseStepCard(index = index, exercise = exercise)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Cooldown guidance
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Levezetés és nyújtás az edzés végén:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(routine.coolDownHu, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedExerciseStepCard(
    index: Int,
    exercise: CalisthenicsExercise,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Exercise Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$index",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = exercise.nameHu,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "🎯 ${exercise.targetMuscle}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${exercise.sets} × ${exercise.repsOrSec}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Step-by-step instructions
            if (exercise.stepByStepStepsHu.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Lépésről lépésre kivitelezés:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        exercise.stepByStepStepsHu.forEach { step ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = exercise.instructionsHu,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Breathing Technique Box
            if (exercise.breathingTipHu.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Emerald500.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Emerald600,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Légzés: ${exercise.breathingTipHu}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Emerald600,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Common Mistakes Box
            if (exercise.commonMistakesHu.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BurnedCalColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = BurnedCalColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Gyakori hibák elkerülése:",
                            style = MaterialTheme.typography.labelSmall,
                            color = BurnedCalColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    exercise.commonMistakesHu.forEach { mistake ->
                        Text(
                            text = "⚠️ $mistake",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Alternatives (Regression / Progression)
            if (exercise.easierAlternativeHu.isNotBlank() || exercise.harderAlternativeHu.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (exercise.easierAlternativeHu.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Könnyítés: ${exercise.easierAlternativeHu}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (exercise.harderAlternativeHu.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Nehezítés: ${exercise.harderAlternativeHu}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    workoutViewModel: WorkoutViewModel,
    routine: CalisthenicsRoutine,
    sessionCompleted: Boolean,
    onExit: () -> Unit
) {
    val currentExerciseIndex by workoutViewModel.currentExerciseIndex.collectAsState()
    val currentSet by workoutViewModel.currentSet.collectAsState()
    val isResting by workoutViewModel.isResting.collectAsState()
    val timerSecondsLeft by workoutViewModel.timerSecondsLeft.collectAsState()
    val isTimerRunning by workoutViewModel.isTimerRunning.collectAsState()

    val currentEx = routine.exercises.getOrNull(currentExerciseIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routine.titleHu, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.Close, contentDescription = "Kilépés")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BurnedCalColor.copy(alpha = 0.15f),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = "${currentExerciseIndex + 1} / ${routine.exercises.size}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BurnedCalColor
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (sessionCompleted) {
            // Completion View
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Emerald500,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Gratulálunk! Edzés Teljesítve!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Elégetett kalória: +${routine.totalCaloriesBurn} kcal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BurnedCalColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "A kalória automatikusan hozzáadódott a mai napi egyenlegedhez!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onExit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                    ) {
                        Text("Vissza az edzéstervekhez", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        } else if (currentEx != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Progress indicator
                val progress = (currentExerciseIndex.toFloat() / routine.exercises.size.toFloat()).coerceIn(0f, 1f)
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Emerald500
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Active State (Resting vs Exercising)
                if (isResting) {
                    Card(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "PIHENŐ IDŐ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "$timerSecondsLeft mp",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Következő: ${routine.exercises.getOrNull(currentExerciseIndex)?.nameHu ?: ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedButton(
                                onClick = { workoutViewModel.skipRest() },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.SkipNext, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pihenő kihagyása", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "$currentSet. / ${currentEx.sets} Sorozat",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = currentEx.nameHu,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "🎯 Cél: ${currentEx.repsOrSec}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Emerald500
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Timer display if it's a hold
                            if (currentEx.isTimer && timerSecondsLeft > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(90.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "$timerSecondsLeft",
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { workoutViewModel.toggleExerciseTimer() },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isTimerRunning) "Szünet" else "Időzítő Indítása")
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // Step-by-step in player
                            if (currentEx.stepByStepStepsHu.isNotEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Lépések:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        currentEx.stepByStepStepsHu.forEach { step ->
                                            Text(
                                                text = "• $step",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = currentEx.instructionsHu,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (currentEx.breathingTipHu.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "💡 Légzés: ${currentEx.breathingTipHu}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Emerald600,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (currentEx.commonMistakesHu.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "⚠️ Hiba: ${currentEx.commonMistakesHu.firstOrNull() ?: ""}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BurnedCalColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Button: Complete Set
                Button(
                    onClick = { workoutViewModel.completeCurrentSet() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("complete_set_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentSet < currentEx.sets) "Sorozat Kész (Pihenő)" else "Gyakorlat Kész",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutLogItemCard(
    log: WorkoutLog,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.routineTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${log.date} • ${log.durationMinutes} perc • ${log.caloriesBurned} kcal elégetve",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
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

@Composable
fun CustomWorkoutLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var minutesInput by remember { mutableStateOf("30") }
    var caloriesInput by remember { mutableStateOf("200") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Egyéni edzés rögzítése", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Edzés típusa (pl. Futás, Séta, Kerékpár)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = minutesInput,
                        onValueChange = { minutesInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Időtartam (perc)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = caloriesInput,
                        onValueChange = { caloriesInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Elégetett kalória (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Megjegyzés (opcionális)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val min = minutesInput.toIntOrNull() ?: 30
                    val cal = caloriesInput.toIntOrNull() ?: 200
                    onConfirm(title.ifBlank { "Egyéni edzés" }, min, cal, notes)
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

@Composable
fun CalisthenicsSetItemCard(
    setLog: CalisthenicsSetLog,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (setLog.category) {
                            WorkoutCategory.PRISON -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                            WorkoutCategory.MILITARY -> Emerald500.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        }
                    ) {
                        Text(
                            text = "${setLog.category.iconEmoji} ${setLog.setNumber}. széria",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when (setLog.category) {
                                WorkoutCategory.PRISON -> MaterialTheme.colorScheme.error
                                WorkoutCategory.MILITARY -> Emerald600
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = setLog.exerciseName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Törlés",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${setLog.repsCompleted} / ${setLog.targetReps} ism.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Emerald600
                    )

                    if (setLog.weightAddedKg > 0.0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(+${setLog.weightAddedKg} kg)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• RPE ${setLog.rpe}/10",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (setLog.isPersonalRecord) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CarbsColor.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = CarbsColor, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "PR Rekord",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = CarbsColor
                            )
                        }
                    }
                }
            }

            if (setLog.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = setLog.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${setLog.date} • ${setLog.routineTitle}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ManualCalisthenicsSetDialog(
    routines: List<CalisthenicsRoutine>,
    onDismiss: () -> Unit,
    onConfirm: (
        routineId: String,
        routineTitle: String,
        category: WorkoutCategory,
        exerciseId: String,
        exerciseName: String,
        setNumber: Int,
        targetReps: Int,
        repsCompleted: Int,
        weightAdded: Double,
        rpe: Int,
        isPr: Boolean,
        notes: String
    ) -> Unit
) {
    val calisthenicsRoutines = remember(routines) {
        routines.filter { it.category == WorkoutCategory.PRISON || it.category == WorkoutCategory.MILITARY || it.category == WorkoutCategory.FULL_BODY }
            .ifEmpty { routines }
    }

    var selectedRoutineIndex by remember { mutableStateOf(0) }
    val currentRoutine = calisthenicsRoutines.getOrNull(selectedRoutineIndex) ?: calisthenicsRoutines.firstOrNull()

    var selectedExerciseIndex by remember { mutableStateOf(0) }
    val currentExercise = currentRoutine?.exercises?.getOrNull(selectedExerciseIndex) ?: currentRoutine?.exercises?.firstOrNull()

    var setNumberInput by remember { mutableStateOf("1") }
    var repsInput by remember { mutableStateOf((currentExercise?.reps ?: 10).toString()) }
    var targetRepsInput by remember { mutableStateOf((currentExercise?.reps ?: 10).toString()) }
    var weightInput by remember { mutableStateOf("0") }
    var rpeInput by remember { mutableStateOf("8") }
    var isPr by remember { mutableStateOf(false) }
    var notesInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Börtön & Katonai Sorozat Rögzítése",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Válaszd ki az edzéstervet és a gyakorlatot a teljesítmény Room adatbázisba mentéséhez.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Routine Selector
                Text("Edzésterv:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(calisthenicsRoutines.size) { index ->
                        val routine = calisthenicsRoutines[index]
                        val isSelected = index == selectedRoutineIndex
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedRoutineIndex = index
                                selectedExerciseIndex = 0
                                currentRoutine?.exercises?.firstOrNull()?.let { ex ->
                                    repsInput = ex.reps.toString()
                                    targetRepsInput = ex.reps.toString()
                                }
                            },
                            label = { Text(routine.titleHu, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Exercise Selector
                if (currentRoutine != null && currentRoutine.exercises.isNotEmpty()) {
                    Text("Gyakorlat:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(currentRoutine.exercises.size) { exIdx ->
                            val ex = currentRoutine.exercises[exIdx]
                            val isSelected = exIdx == selectedExerciseIndex
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedExerciseIndex = exIdx
                                    repsInput = ex.reps.toString()
                                    targetRepsInput = ex.reps.toString()
                                },
                                label = { Text(ex.nameHu, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Set Number & Reps
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = setNumberInput,
                        onValueChange = { setNumberInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Széria #") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = repsInput,
                        onValueChange = { repsInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Teljesített ism.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = targetRepsInput,
                        onValueChange = { targetRepsInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Cél ism.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Weight added & RPE
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Extra súly (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = rpeInput,
                        onValueChange = { rpeInput = it.filter { c -> c.isDigit() } },
                        label = { Text("RPE (1-10)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // PR Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { isPr = !isPr }.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isPr) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = null,
                        tint = if (isPr) CarbsColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ez egy új egyéni rekord (PR)!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isPr) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = { notesInput = it },
                    label = { Text("Megjegyzés (pl. szűk fogás, szabályos forma)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val routine = currentRoutine ?: return@Button
                    val exercise = currentExercise ?: return@Button
                    val setNum = setNumberInput.toIntOrNull() ?: 1
                    val reps = repsInput.toIntOrNull() ?: exercise.reps
                    val target = targetRepsInput.toIntOrNull() ?: exercise.reps
                    val weight = weightInput.toDoubleOrNull() ?: 0.0
                    val rpeVal = (rpeInput.toIntOrNull() ?: 8).coerceIn(1, 10)

                    onConfirm(
                        routine.id,
                        routine.titleHu,
                        routine.category,
                        exercise.id,
                        exercise.nameHu,
                        setNum,
                        target,
                        reps,
                        weight,
                        rpeVal,
                        isPr,
                        notesInput.ifBlank { "${exercise.targetMuscle} forma" }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
            ) {
                Text("Mentés Room-ba", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Mégse")
            }
        }
    )
}
