package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WaterEntry
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishBorderSubtle
import com.example.ui.theme.PolishDarkNavy
import com.example.ui.theme.PolishOnPrimaryContainer
import com.example.ui.theme.PolishPrimary
import com.example.ui.theme.PolishPrimaryContainer
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary
import com.example.ui.theme.WaterColor
import com.example.util.WaterReminderManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val HydrationDeepBlue = Color(0xFF006699)
val HydrationAqua = Color(0xFF00A3E0)
val HydrationSoftBg = Color(0xFFE6F4FA)
val HydrationCardBorder = Color(0xFFB3E0F2)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FullWaterTrackerModuleCard(
    currentMl: Int,
    targetMl: Int,
    waterEntries: List<WaterEntry> = emptyList(),
    onAddWater: (Int) -> Unit,
    onRemoveLast: () -> Unit,
    onDeleteEntry: (Long) -> Unit = {},
    onClearToday: () -> Unit = {},
    onUpdateTarget: (Int?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCustomAmountDialog by remember { mutableStateOf(false) }
    var showTargetGoalDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showHistoryExpanded by remember { mutableStateOf(false) }

    val progress = (currentMl.toFloat() / targetMl.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1.5f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceAtMost(1f),
        animationSpec = tween(durationMillis = 600),
        label = "water_progress_anim"
    )

    val percent = (progress * 100).toInt()
    val glassesCount = (currentMl / 250.0)
    val remainingMl = (targetMl - currentMl).coerceAtLeast(0)

    val reminderActive = remember { mutableStateOf(WaterReminderManager.isReminderEnabled(context)) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("water_tracker_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, HydrationCardBorder.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // 1. Top Header (Icon + Title + Action Badges: Goal & Reminders)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Brush.linearGradient(listOf(HydrationAqua, HydrationDeepBlue)),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Opacity,
                            contentDescription = "Vízfogyasztás",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Vízfogyasztás Követő",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (percent >= 100) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Emerald500.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "CÉL ELÉRVE! 🎉",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = Emerald500
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (remainingMl > 0) "Még ${remainingMl} ml a mai cél eléréséig" else "Kiváló hidratáltság a mai napra!",
                            style = MaterialTheme.typography.bodySmall,
                            color = PolishTextSecondary
                        )
                    }
                }

                // Quick buttons: Reminder & Goal Edit
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { showReminderDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (reminderActive.value) HydrationAqua.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                            .testTag("water_reminder_button")
                    ) {
                        Icon(
                            imageVector = if (reminderActive.value) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Emlékeztetők beállítása",
                            tint = if (reminderActive.value) HydrationDeepBlue else PolishTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { showTargetGoalDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .testTag("water_target_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Napi cél módosítása",
                            tint = PolishTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Central Hydration Meter & Circular Gauge Visual
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = HydrationSoftBg.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, HydrationCardBorder.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Circular Progress Dial
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 8.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2
                            val center = Offset(size.width / 2, size.height / 2)

                            // Track
                            drawCircle(
                                color = Color.White.copy(alpha = 0.8f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth)
                            )

                            // Progress Arc
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(HydrationAqua, HydrationDeepBlue, HydrationAqua)
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                size = Size(radius * 2, radius * 2),
                                topLeft = Offset(center.x - radius, center.y - radius)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$percent%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp
                                ),
                                color = HydrationDeepBlue
                            )
                            Text(
                                text = "kész",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = PolishTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Stats Details Column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "$currentMl",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                ),
                                color = HydrationDeepBlue
                            )
                            Text(
                                text = "/ $targetMl ml",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = PolishTextSecondary,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🥤 %.1f pohár (2.5 dl)".format(glassesCount),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = PolishTextPrimary
                            )
                            if (percent >= 100) {
                                Text(
                                    text = "✨ Teljesítve",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Emerald500
                                )
                            }
                        }

                        // Linear progress bar
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = HydrationDeepBlue,
                            trackColor = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Quick Intake Logging Buttons Grid (2.5 dl, 5 dl, 7.5 dl, 10 dl + Egyéni)
            Text(
                text = "GYORS VÍZBEVITEL HOZZÁADÁSA",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = PolishTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // +250 ml (Pohár)
                QuickWaterButton(
                    label = "+2.5 dl",
                    sublabel = "1 pohár",
                    amount = 250,
                    icon = "🥛",
                    onClick = { onAddWater(250) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_water_250"
                )

                // +500 ml (Palack)
                QuickWaterButton(
                    label = "+5.0 dl",
                    sublabel = "Fél liter",
                    amount = 500,
                    icon = "💧",
                    onClick = { onAddWater(500) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_water_500"
                )

                // +750 ml (Kulacs)
                QuickWaterButton(
                    label = "+7.5 dl",
                    sublabel = "Kulacs",
                    amount = 750,
                    icon = "🍶",
                    onClick = { onAddWater(750) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_water_750"
                )

                // +1000 ml (Kancsó)
                QuickWaterButton(
                    label = "+1.0 L",
                    sublabel = "Nagy kancsó",
                    amount = 1000,
                    icon = "🫗",
                    onClick = { onAddWater(1000) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_water_1000"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Secondary Action Row: Custom Amount + Remove Last + Clear
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showCustomAmountDialog = true },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("custom_water_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HydrationDeepBlue.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = HydrationDeepBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Egyéni (ml)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HydrationDeepBlue
                    )
                }

                if (currentMl > 0) {
                    OutlinedButton(
                        onClick = onRemoveLast,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("undo_water_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, PolishBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = PolishTextSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Visszavon",
                            fontSize = 12.sp,
                            color = PolishTextSecondary
                        )
                    }
                }

                // Toggle History Logs
                if (waterEntries.isNotEmpty()) {
                    IconButton(
                        onClick = { showHistoryExpanded = !showHistoryExpanded },
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                            .testTag("toggle_water_history_button")
                    ) {
                        Icon(
                            imageVector = if (showHistoryExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Mai bejegyzések listája",
                            tint = PolishTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 5. Expandable History Log List
            AnimatedVisibility(visible = showHistoryExpanded && waterEntries.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mai bejegyzések (${waterEntries.size} db)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = PolishTextPrimary
                        )
                        TextButton(
                            onClick = onClearToday,
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = "Összes törlése",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    waterEntries.forEach { entry ->
                        val timeStr = formatTimestamp(entry.timestamp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(HydrationAqua.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.WaterDrop,
                                        contentDescription = null,
                                        tint = HydrationDeepBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "+${entry.amountMl} ml",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = timeStr,
                                        fontSize = 10.sp,
                                        color = PolishTextSecondary
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDeleteEntry(entry.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Törlés",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 2.dp),
                            color = PolishBorder.copy(alpha = 0.25f)
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showCustomAmountDialog) {
        CustomWaterAmountDialog(
            onDismiss = { showCustomAmountDialog = false },
            onConfirm = { amount ->
                onAddWater(amount)
                showCustomAmountDialog = false
            }
        )
    }

    if (showTargetGoalDialog) {
        WaterTargetGoalDialog(
            currentTargetMl = targetMl,
            onDismiss = { showTargetGoalDialog = false },
            onSaveTarget = { newTarget ->
                onUpdateTarget(newTarget)
                showTargetGoalDialog = false
            }
        )
    }

    if (showReminderDialog) {
        WaterReminderSettingsDialog(
            context = context,
            onDismiss = {
                reminderActive.value = WaterReminderManager.isReminderEnabled(context)
                showReminderDialog = false
            }
        )
    }
}

@Composable
private fun QuickWaterButton(
    label: String,
    sublabel: String,
    amount: Int,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = HydrationSoftBg,
        border = BorderStroke(1.dp, HydrationCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HydrationDeepBlue
            )
            Text(
                text = sublabel,
                fontSize = 9.sp,
                color = PolishTextSecondary
            )
        }
    }
}

@Composable
fun CustomWaterAmountDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var inputAmount by remember { mutableStateOf("300") }
    val presetAmounts = listOf(150, 200, 300, 400, 600, 800)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = HydrationDeepBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Egyéni folyadékbevitel", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Add meg a pontos mennyiséget milliliterben (ml):",
                    style = MaterialTheme.typography.bodySmall,
                    color = PolishTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = inputAmount,
                    onValueChange = { inputAmount = it.filter { char -> char.isDigit() } },
                    label = { Text("Mennyiség (ml)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Text("ml", modifier = Modifier.padding(end = 12.dp)) }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Gyors választás:",
                    style = MaterialTheme.typography.labelSmall,
                    color = PolishTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAmounts.take(3).forEach { p ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (inputAmount == p.toString()) HydrationDeepBlue else HydrationSoftBg,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { inputAmount = p.toString() }
                        ) {
                            Text(
                                text = "$p ml",
                                modifier = Modifier.padding(vertical = 6.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (inputAmount == p.toString()) Color.White else HydrationDeepBlue,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAmounts.drop(3).forEach { p ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (inputAmount == p.toString()) HydrationDeepBlue else HydrationSoftBg,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { inputAmount = p.toString() }
                        ) {
                            Text(
                                text = "$p ml",
                                modifier = Modifier.padding(vertical = 6.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (inputAmount == p.toString()) Color.White else HydrationDeepBlue,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = inputAmount.toIntOrNull() ?: 250
                    if (amount > 0) onConfirm(amount)
                },
                colors = ButtonDefaults.buttonColors(containerColor = HydrationDeepBlue)
            ) {
                Text("Hozzáadás")
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
fun WaterTargetGoalDialog(
    currentTargetMl: Int,
    onDismiss: () -> Unit,
    onSaveTarget: (Int?) -> Unit
) {
    var useCustomTarget by remember { mutableStateOf(true) }
    var targetInput by remember { mutableStateOf(currentTargetMl.toString()) }
    val recommendedPresets = listOf(2000, 2500, 3000, 3500)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = HydrationDeepBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Napi Folyadékcél Beállítása", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Állítsd be a személyre szabott napi vízbeviteli célodat, vagy válassz az ajánlott mennyiségek közül.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PolishTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = targetInput,
                    onValueChange = { targetInput = it.filter { char -> char.isDigit() } },
                    label = { Text("Napi Cél (ml / nap)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Text("ml", modifier = Modifier.padding(end = 12.dp)) }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ajánlott sztenderdek testsúly és aktivitás szerint:",
                    style = MaterialTheme.typography.labelSmall,
                    color = PolishTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    recommendedPresets.forEach { p ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (targetInput == p.toString()) HydrationDeepBlue else HydrationSoftBg,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { targetInput = p.toString() }
                        ) {
                            Text(
                                text = "%.1f L".format(p / 1000.0),
                                modifier = Modifier.padding(vertical = 8.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (targetInput == p.toString()) Color.White else HydrationDeepBlue,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "💡 Tipp: Testsúly-kilogrammonként ~35 ml víz javasolt, sportnapokon +500-1000 ml.",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = PolishTextSecondary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = targetInput.toIntOrNull()
                    onSaveTarget(amount)
                },
                colors = ButtonDefaults.buttonColors(containerColor = HydrationDeepBlue)
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Reset to auto calculation
                    onSaveTarget(null)
                }
            ) {
                Text("Automatikus számítás")
            }
        }
    )
}

@Composable
fun WaterReminderSettingsDialog(
    context: android.content.Context,
    onDismiss: () -> Unit
) {
    var enabled by remember { mutableStateOf(WaterReminderManager.isReminderEnabled(context)) }
    var intervalHours by remember { mutableIntStateOf(WaterReminderManager.getIntervalHours(context)) }
    var startHour by remember { mutableIntStateOf(WaterReminderManager.getStartHour(context)) }
    var endHour by remember { mutableIntStateOf(WaterReminderManager.getEndHour(context)) }
    var notificationSentFeedback by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null,
                    tint = HydrationDeepBlue
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Vízivási Emlékeztetők", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Állíts be rendszeres értesítéseket, hogy ne felejts el inni a nap folyamán és elérd a kitűzött célt.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PolishTextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Toggle Switch
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = if (enabled) HydrationSoftBg else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Emlékeztetők bekapcsolása",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (enabled) "Aktív ütemezés" else "Kikapcsolva",
                                fontSize = 11.sp,
                                color = PolishTextSecondary
                            )
                        }
                        Switch(
                            checked = enabled,
                            onCheckedChange = { enabled = it }
                        )
                    }
                }

                if (enabled) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Emlékeztető gyakorisága:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = PolishTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1 to "1 óránként", 2 to "2 óránként", 3 to "3 óránként", 4 to "4 óránként").forEach { (h, label) ->
                            val isSel = intervalHours == h
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) HydrationDeepBlue else HydrationSoftBg,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { intervalHours = h }
                            ) {
                                Text(
                                    text = label,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else HydrationDeepBlue,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Aktív időablak (nap közben):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = PolishTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kezdés: ${startHour}:00  •  Befejezés: ${endHour}:00",
                        style = MaterialTheme.typography.bodySmall,
                        color = HydrationDeepBlue,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Test notification trigger button
                    OutlinedButton(
                        onClick = {
                            WaterReminderManager.triggerTestNotification(context)
                            notificationSentFeedback = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Teszt értesítés küldése most", fontSize = 12.sp)
                    }

                    if (notificationSentFeedback) {
                        Text(
                            text = "✓ Teszt értesítés elküldve a telefonra!",
                            color = Emerald500,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    WaterReminderManager.saveSettings(
                        context = context,
                        enabled = enabled,
                        intervalHours = intervalHours,
                        startHour = startHour,
                        endHour = endHour
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = HydrationDeepBlue)
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bezárás")
            }
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    return try {
        val dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        dt.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        ""
    }
}
