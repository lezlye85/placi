package com.example.ui.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CostLevel
import com.example.data.model.MealType
import com.example.data.model.Recipe
import com.example.data.model.RecipeDietaryTag
import com.example.data.model.ShoppingItem
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.PolishPrimary
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodels.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    recipeViewModel: RecipeViewModel,
    modifier: Modifier = Modifier
) {
    val recipes by recipeViewModel.recipes.collectAsStateWithLifecycle()
    val weeklyPlan by recipeViewModel.weeklyMealPlan.collectAsStateWithLifecycle()
    val shoppingList by recipeViewModel.shoppingList.collectAsStateWithLifecycle()
    val userProfile by recipeViewModel.userProfile.collectAsStateWithLifecycle()

    val searchQuery by recipeViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedMealType by recipeViewModel.selectedMealTypeFilter.collectAsStateWithLifecycle()
    val selectedTag by recipeViewModel.selectedTagFilter.collectAsStateWithLifecycle()
    val selectedCost by recipeViewModel.selectedCostFilter.collectAsStateWithLifecycle()
    val selectedRecipe by recipeViewModel.selectedRecipe.collectAsStateWithLifecycle()
    val selectedDayIndex by recipeViewModel.selectedDayIndex.collectAsStateWithLifecycle()
    val logMessage by recipeViewModel.logSuccessMessage.collectAsStateWithLifecycle()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Receptek, 1: 1 Hetes Étlap, 2: Bevásárlólista
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(logMessage) {
        logMessage?.let {
            snackbarHostState.showSnackbar(it)
            recipeViewModel.clearLogMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "RECEFTEK & ÉTLAP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Olcsó & Fehérjedús Gasztro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💰 ~${weeklyPlan.dailyAvgCostHuf} Ft/nap",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                ScrollableTabRow(
                    selectedTabIndex = activeTab,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    divider = {}
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Recepttár (${recipes.size})", fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_recipes")
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("1 Hetes Olcsó Étlap", fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_weekly_plan")
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        text = { Text("Bevásárlólista (${shoppingList.count { !it.isBought }})", fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("tab_shopping_list")
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> RecipeListTab(
                    recipes = recipes,
                    searchQuery = searchQuery,
                    selectedMealType = selectedMealType,
                    selectedTag = selectedTag,
                    selectedCost = selectedCost,
                    onSearchChange = { recipeViewModel.setSearchQuery(it) },
                    onMealTypeSelect = { recipeViewModel.setMealTypeFilter(it) },
                    onTagSelect = { recipeViewModel.setTagFilter(it) },
                    onCostSelect = { recipeViewModel.setCostFilter(it) },
                    onRecipeClick = { recipeViewModel.selectRecipe(it) },
                    onQuickLog = { recipe -> recipeViewModel.logRecipeToToday(recipe) }
                )
                1 -> WeeklyMealPlanTab(
                    weeklyPlan = weeklyPlan,
                    selectedDayIndex = selectedDayIndex,
                    onSelectDay = { recipeViewModel.selectDayIndex(it) },
                    onRegenerateGoal = { goal -> recipeViewModel.regeneratePlanForGoal(goal) },
                    onLogMeal = { recipe, type -> recipeViewModel.logRecipeToToday(recipe, type) },
                    onRecipeDetail = { recipeViewModel.selectRecipe(it) }
                )
                2 -> ShoppingListTab(
                    items = shoppingList,
                    onToggleItem = { recipeViewModel.toggleShoppingItem(it) },
                    onAddItem = { name, amt, cat, cost -> recipeViewModel.addShoppingItem(name, amt, cat, cost) }
                )
            }

            // Recipe Detail Dialog
            selectedRecipe?.let { recipe ->
                RecipeDetailDialog(
                    recipe = recipe,
                    onDismiss = { recipeViewModel.selectRecipe(null) },
                    onLogToDiary = { chosenType ->
                        recipeViewModel.logRecipeToToday(recipe, chosenType)
                        recipeViewModel.selectRecipe(null)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeListTab(
    recipes: List<Recipe>,
    searchQuery: String,
    selectedMealType: MealType?,
    selectedTag: RecipeDietaryTag?,
    selectedCost: CostLevel?,
    onSearchChange: (String) -> Unit,
    onMealTypeSelect: (MealType?) -> Unit,
    onTagSelect: (RecipeDietaryTag?) -> Unit,
    onCostSelect: (CostLevel?) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onQuickLog: (Recipe) -> Unit
) {
    val filteredRecipes = remember(recipes, searchQuery, selectedMealType, selectedTag, selectedCost) {
        recipes.filter { recipe ->
            val matchQuery = searchQuery.isBlank() ||
                    recipe.titleHu.contains(searchQuery, ignoreCase = true) ||
                    recipe.subtitleHu.contains(searchQuery, ignoreCase = true) ||
                    recipe.ingredients.any { it.name.contains(searchQuery, ignoreCase = true) }
            val matchType = selectedMealType == null || recipe.mealType == selectedMealType
            val matchTag = selectedTag == null || recipe.tags.contains(selectedTag)
            val matchCost = selectedCost == null || recipe.costLevel == selectedCost
            matchQuery && matchType && matchTag && matchCost
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipe_search_input"),
                placeholder = { Text("Keresés receptben, alapanyagban (pl. túró, zab, csirke)...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Törlés")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Meal Type Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedMealType == null,
                        onClick = { onMealTypeSelect(null) },
                        label = { Text("Összes étkezés") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                items(MealType.values()) { type ->
                    FilterChip(
                        selected = selectedMealType == type,
                        onClick = { onMealTypeSelect(if (selectedMealType == type) null else type) },
                        label = { Text(type.displayNameHu) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Dietary & Cost filter tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedCost == CostLevel.ULTRA_CHEAP,
                    onClick = { onCostSelect(if (selectedCost == CostLevel.ULTRA_CHEAP) null else CostLevel.ULTRA_CHEAP) },
                    label = { Text("💰 <500 Ft/adag", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFE8F5E9))
                )
                RecipeDietaryTag.values().forEach { tag ->
                    FilterChip(
                        selected = selectedTag == tag,
                        onClick = { onTagSelect(if (selectedTag == tag) null else tag) },
                        label = { Text(tag.displayNameHu, fontSize = 12.sp) }
                    )
                }
            }
        }

        if (filteredRecipes.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔍", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nincs találat a megadott szűrőkre",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Próbálj meg törölni a szűrőkből vagy keress más alapanyagra.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredRecipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) },
                    onQuickLog = { onQuickLog(recipe) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    onQuickLog: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = recipe.emoji, fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = recipe.titleHu,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = recipe.subtitleHu,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: Prep time + Cost estimate + Meal type
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = "💰 ~${recipe.estimatedCostHuf} Ft / adag",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${recipe.prepTimeMinutes} perc",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = recipe.mealType.displayNameHu,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nutrition numbers bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Kalória", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${recipe.calories.toInt()} kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Fehérje", fontSize = 10.sp, color = ProteinColor)
                    Text("%.1fg".format(recipe.protein), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ProteinColor)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Szénhidrát", fontSize = 10.sp, color = CarbsColor)
                    Text("%.1fg".format(recipe.carbs), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CarbsColor)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Zsír", fontSize = 10.sp, color = FatColor)
                    Text("%.1fg".format(recipe.fat), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FatColor)
                }

                Button(
                    onClick = onQuickLog,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.height(34.dp).testTag("quick_log_recipe_${recipe.id}")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Naplózom", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WeeklyMealPlanTab(
    weeklyPlan: com.example.data.model.WeeklyMealPlan,
    selectedDayIndex: Int,
    onSelectDay: (Int) -> Unit,
    onRegenerateGoal: (String) -> Unit,
    onLogMeal: (Recipe, MealType) -> Unit,
    onRecipeDetail: (Recipe) -> Unit
) {
    val currentDay = weeklyPlan.days.getOrNull(selectedDayIndex) ?: weeklyPlan.days.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Plan Header Card
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
                                text = "SZEMÉLYRE SZABOTT ÉTLAP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = weeklyPlan.titleHu,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Napi átlag ár", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("~${weeklyPlan.dailyAvgCostHuf} Ft", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32))
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Heti büdzsé", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${weeklyPlan.totalWeeklyCostHuf} Ft", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Napi kalória", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${weeklyPlan.dailyAvgCalories} kcal", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Goal selector chips
                    Text(
                        text = "Étlap cél & stílus:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val goals = listOf("Költségkímélő Zsírégetés", "Tiszta Izomépítés", "Diákbarát Spórolós")
                        goals.forEach { g ->
                            val isSelected = weeklyPlan.goalTagHu == g
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clickable { onRegenerateGoal(g) }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = g,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Day Selector Row (Hétfő .. Vasárnap)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(weeklyPlan.days.indices.toList()) { idx ->
                    val day = weeklyPlan.days[idx]
                    val isSelected = idx == selectedDayIndex
                    Card(
                        modifier = Modifier
                            .clickable { onSelectDay(idx) }
                            .testTag("day_tab_$idx"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day.dayNameHu,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${day.totalCostHuf} Ft",
                                fontSize = 11.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Daily meals for selected day
        item {
            Text(
                text = "${currentDay.dayNameHu}i Étrend (${currentDay.totalCalories.toInt()} kcal • ${currentDay.totalCostHuf} Ft)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            PlannedMealCard(
                mealTitle = "Reggeli",
                recipe = currentDay.breakfast,
                onRecipeClick = { onRecipeDetail(currentDay.breakfast) },
                onLog = { onLogMeal(currentDay.breakfast, MealType.BREAKFAST) }
            )
        }

        item {
            PlannedMealCard(
                mealTitle = "Ebéd",
                recipe = currentDay.lunch,
                onRecipeClick = { onRecipeDetail(currentDay.lunch) },
                onLog = { onLogMeal(currentDay.lunch, MealType.LUNCH) }
            )
        }

        item {
            PlannedMealCard(
                mealTitle = "Vacsora",
                recipe = currentDay.dinner,
                onRecipeClick = { onRecipeDetail(currentDay.dinner) },
                onLog = { onLogMeal(currentDay.dinner, MealType.DINNER) }
            )
        }

        item {
            PlannedMealCard(
                mealTitle = "Uzsonna / Nasi",
                recipe = currentDay.snack,
                onRecipeClick = { onRecipeDetail(currentDay.snack) },
                onLog = { onLogMeal(currentDay.snack, MealType.SNACK) }
            )
        }

        // Daily Totals Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Napi Tápanyag Összesítő",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("🔥 ${currentDay.totalCalories.toInt()} kcal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("🥩 Fehérje: %.0fg".format(currentDay.totalProtein), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ProteinColor)
                        Text("🌾 Szénhidrát: %.0fg".format(currentDay.totalCarbs), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CarbsColor)
                        Text("🥑 Zsír: %.0fg".format(currentDay.totalFat), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FatColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PlannedMealCard(
    mealTitle: String,
    recipe: Recipe,
    onRecipeClick: () -> Unit,
    onLog: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRecipeClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = recipe.emoji, fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mealTitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${recipe.estimatedCostHuf} Ft",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = recipe.titleHu,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${recipe.calories.toInt()} kcal • F: %.0fg • Sz: %.0fg • Zs: %.0fg".format(recipe.protein, recipe.carbs, recipe.fat),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onLog,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Hozzáadás a naplóhoz",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ShoppingListTab(
    items: List<ShoppingItem>,
    onToggleItem: (String) -> Unit,
    onAddItem: (String, String, String, Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemAmount by remember { mutableStateOf("") }
    var newItemCost by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("Egyéb") }

    val totalCost = items.sumOf { it.estimatedCostHuf }
    val boughtCost = items.filter { it.isBought }.sumOf { it.estimatedCostHuf }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Shopping Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
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
                                text = "HETI BEVÁSÁRLÓLISTA",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Becsült költségkeret",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Új tétel", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Megvásárolva: $boughtCost Ft / $totalCost Ft",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${items.count { it.isBought }} / ${items.size} db",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val progress = if (items.isNotEmpty()) items.count { it.isBought }.toFloat() / items.size.toFloat() else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF2E7D32),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        items(items, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleItem(item.id) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isBought) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (item.isBought) 0.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isBought,
                        onCheckedChange = { onToggleItem(item.id) },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2E7D32))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontWeight = if (item.isBought) FontWeight.Normal else FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (item.isBought) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${item.category} • ${item.amount}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (item.isBought) Color.Transparent else Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = "${item.estimatedCostHuf} Ft",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (item.isBought) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Új bevásárlólistás tétel", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Tétel neve (pl. Banán)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newItemAmount,
                        onValueChange = { newItemAmount = it },
                        label = { Text("Mennyiség (pl. 1 kg)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newItemCost,
                        onValueChange = { newItemCost = it },
                        label = { Text("Becsült ár Ft-ban (pl. 600)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            onAddItem(
                                newItemName,
                                newItemAmount.ifBlank { "1 adag" },
                                newItemCategory,
                                newItemCost.toIntOrNull() ?: 500
                            )
                            newItemName = ""
                            newItemAmount = ""
                            newItemCost = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Hozzáadás")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Mégse")
                }
            }
        )
    }
}

@Composable
fun RecipeDetailDialog(
    recipe: Recipe,
    onDismiss: () -> Unit,
    onLogToDiary: (MealType) -> Unit
) {
    var chosenMealType by remember { mutableStateOf(recipe.mealType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = recipe.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = recipe.titleHu,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "⏱️ ${recipe.prepTimeMinutes} perc • 💰 ~${recipe.estimatedCostHuf} Ft / adag",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Macro Summary Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Kalória", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${recipe.calories.toInt()} kcal", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Fehérje", fontSize = 11.sp, color = ProteinColor)
                                Text("%.1fg".format(recipe.protein), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProteinColor)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Szénhidrát", fontSize = 11.sp, color = CarbsColor)
                                Text("%.1fg".format(recipe.carbs), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CarbsColor)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Zsír", fontSize = 11.sp, color = FatColor)
                                Text("%.1fg".format(recipe.fat), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FatColor)
                            }
                        }
                    }
                }

                if (recipe.budgetTip.isNotBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("💡", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = recipe.budgetTip,
                                    fontSize = 12.sp,
                                    color = Color(0xFF1B5E20),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Hozzávalók (1 adag):",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                items(recipe.ingredients) { ing ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("• ${ing.name} (${ing.amountHu})", fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("~${ing.costHuf} Ft", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Elkészítés lépései:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                items(recipe.instructionsHu.indices.toList()) { idx ->
                    val step = recipe.instructionsHu[idx]
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("${idx + 1}. ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                        Text(step, fontSize = 13.sp)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Naplózás hova:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(MealType.values()) { type ->
                            FilterChip(
                                selected = chosenMealType == type,
                                onClick = { chosenMealType = type },
                                label = { Text(type.displayNameHu, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onLogToDiary(chosenMealType) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("dialog_log_recipe_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Naplózás ma")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bezárás")
            }
        }
    )
}
