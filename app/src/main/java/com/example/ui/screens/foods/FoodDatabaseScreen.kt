package com.example.ui.screens.foods

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.MealType
import com.example.ui.components.AddFoodBottomSheet
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodels.DiaryViewModel
import com.example.ui.viewmodels.FoodDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDatabaseScreen(
    foodDatabaseViewModel: FoodDatabaseViewModel,
    diaryViewModel: DiaryViewModel,
    initialMealType: MealType = MealType.BREAKFAST,
    onNavigateToBarcodeScanner: () -> Unit,
    onNavigateToAiMealScan: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val foods by foodDatabaseViewModel.foods.collectAsStateWithLifecycle()
    val searchQuery by foodDatabaseViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by foodDatabaseViewModel.selectedCategory.collectAsStateWithLifecycle()

    var showCreateFoodDialog by remember { mutableStateOf(false) }
    var selectedFoodForAdd by remember { mutableStateOf<FoodItem?>(null) }
    var targetMealType by remember { mutableStateOf(initialMealType) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateFoodDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("create_custom_food_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Új étel", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input & Actions (AI Vision & Barcode)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { foodDatabaseViewModel.updateSearchQuery(it) },
                    placeholder = { Text("Étel vagy márka keresése...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Keresés")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { foodDatabaseViewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Törlés")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("food_search_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                // AI Meal Scan Button
                IconButton(
                    onClick = onNavigateToAiMealScan,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                        .size(52.dp)
                        .testTag("ai_meal_scan_button")
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "AI Étel Fotózás",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onNavigateToBarcodeScanner,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                        .size(52.dp)
                        .testTag("barcode_scanner_button")
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = "Vonalkódolvasó",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Category Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FoodCategory.values().forEach { category ->
                    val selected = category == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { foodDatabaseViewModel.selectCategory(category) }
                    ) {
                        Text(
                            text = category.displayNameHu,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Results count
            Text(
                text = "${foods.size} étel a kalóriabázisban",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Foods List
            if (foods.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nincs találat a keresésre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Próbálj más kifejezést, vagy hozz létre egyedi ételt a jobb alsó gombbal!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(foods, key = { it.id }) { food ->
                        FoodDatabaseItemCard(
                            food = food,
                            onToggleFavorite = { foodDatabaseViewModel.toggleFavorite(food) },
                            onSelect = {
                                selectedFoodForAdd = food
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Add Food Bottom Sheet
    selectedFoodForAdd?.let { food ->
        AddFoodBottomSheet(
            food = food,
            targetMealType = targetMealType,
            onDismiss = { selectedFoodForAdd = null },
            onConfirmAdd = { mealType, amount ->
                diaryViewModel.addMeal(mealType, food, amount)
                selectedFoodForAdd = null
            }
        )
    }

    // Create Custom Food Dialog
    if (showCreateFoodDialog) {
        CreateCustomFoodDialog(
            onDismiss = { showCreateFoodDialog = false },
            onSave = { name, brand, barcode, cat, cal, p, c, f, fib, unit, servGrams ->
                foodDatabaseViewModel.createCustomFood(
                    name, brand, barcode, cat, cal, p, c, f, fib, unit, servGrams
                )
                showCreateFoodDialog = false
            }
        )
    }
}

@Composable
fun FoodDatabaseItemCard(
    food: FoodItem,
    onToggleFavorite: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (food.brand.isNotBlank()) {
                    Text(
                        text = food.brand,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                // 100g Nutrition pills
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroBadge(text = "${food.caloriesPer100g.toInt()} kcal", color = MaterialTheme.colorScheme.primary)
                    MacroBadge(text = "F: ${food.proteinPer100g.toInt()}g", color = ProteinColor)
                    MacroBadge(text = "Sz: ${food.carbsPer100g.toInt()}g", color = CarbsColor)
                    MacroBadge(text = "Zs: ${food.fatPer100g.toInt()}g", color = FatColor)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (food.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Kedvenc",
                        tint = if (food.isFavorite) CarbsColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onSelect() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Hozzáadás",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomFoodDialog(
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        brand: String,
        barcode: String,
        category: FoodCategory,
        calories: Double,
        protein: Double,
        carbs: Double,
        fat: Double,
        fiber: Double,
        servingUnit: String,
        servingGrams: Double
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(FoodCategory.MEALS) }
    var caloriesInput by remember { mutableStateOf("") }
    var proteinInput by remember { mutableStateOf("") }
    var carbsInput by remember { mutableStateOf("") }
    var fatInput by remember { mutableStateOf("") }
    var fiberInput by remember { mutableStateOf("") }
    var servingUnit by remember { mutableStateOf("g") }
    var servingGramsInput by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Új egyedi étel rögzítése", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Étel megnevezése *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Márka / Gyártó (opcionális)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        label = { Text("Vonalkód (opcionális)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    Text("100 grammra vetített tápértékek:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = caloriesInput,
                        onValueChange = { caloriesInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Kalória (kcal / 100g) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = proteinInput,
                            onValueChange = { proteinInput = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Fehérje") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = carbsInput,
                            onValueChange = { carbsInput = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Szénhidrát") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = fatInput,
                            onValueChange = { fatInput = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Zsír") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = fiberInput,
                        onValueChange = { fiberInput = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Rost (g / 100g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cal = caloriesInput.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && cal > 0) {
                        onSave(
                            name,
                            brand,
                            barcode,
                            selectedCategory,
                            cal,
                            proteinInput.toDoubleOrNull() ?: 0.0,
                            carbsInput.toDoubleOrNull() ?: 0.0,
                            fatInput.toDoubleOrNull() ?: 0.0,
                            fiberInput.toDoubleOrNull() ?: 0.0,
                            servingUnit,
                            servingGramsInput.toDoubleOrNull() ?: 100.0
                        )
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
