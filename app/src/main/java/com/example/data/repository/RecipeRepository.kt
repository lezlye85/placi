package com.example.data.repository

import com.example.data.local.RecipeAndCommunityData
import com.example.data.model.CostLevel
import com.example.data.model.MealType
import com.example.data.model.Recipe
import com.example.data.model.RecipeDietaryTag
import com.example.data.model.ShoppingItem
import com.example.data.model.WeeklyMealPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipeRepository(
    private val nutritionRepository: NutritionRepository
) {
    private val _recipes = MutableStateFlow(RecipeAndCommunityData.RECIPES)
    val recipes: Flow<List<Recipe>> = _recipes.asStateFlow()

    private val _weeklyMealPlan = MutableStateFlow(RecipeAndCommunityData.generateWeeklyMealPlan(2000))
    val weeklyMealPlan: Flow<WeeklyMealPlan> = _weeklyMealPlan.asStateFlow()

    private val _shoppingList = MutableStateFlow(RecipeAndCommunityData.INITIAL_SHOPPING_LIST)
    val shoppingList: Flow<List<ShoppingItem>> = _shoppingList.asStateFlow()

    fun filterRecipes(
        query: String = "",
        mealType: MealType? = null,
        tag: RecipeDietaryTag? = null,
        costLevel: CostLevel? = null
    ): List<Recipe> {
        return _recipes.value.filter { recipe ->
            val matchQuery = query.isBlank() || recipe.titleHu.contains(query, ignoreCase = true) ||
                    recipe.subtitleHu.contains(query, ignoreCase = true) ||
                    recipe.ingredients.any { it.name.contains(query, ignoreCase = true) }
            val matchType = mealType == null || recipe.mealType == mealType
            val matchTag = tag == null || recipe.tags.contains(tag)
            val matchCost = costLevel == null || recipe.costLevel == costLevel
            matchQuery && matchType && matchTag && matchCost
        }
    }

    fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }

    fun regenerateWeeklyPlan(targetCalories: Int, goalTag: String = "Költségkímélő Zsírégetés") {
        _weeklyMealPlan.value = RecipeAndCommunityData.generateWeeklyMealPlan(targetCalories, goalTag)
    }

    fun toggleShoppingItem(itemId: String) {
        _shoppingList.update { list ->
            list.map { item ->
                if (item.id == itemId) item.copy(isBought = !item.isBought) else item
            }
        }
    }

    fun addCustomShoppingItem(name: String, amount: String, category: String, costHuf: Int) {
        val newItem = ShoppingItem(
            id = "shop_${System.currentTimeMillis()}",
            name = name,
            amount = amount,
            category = category,
            estimatedCostHuf = costHuf,
            isBought = false
        )
        _shoppingList.update { it + newItem }
    }

    suspend fun logRecipeToDiary(date: String, recipe: Recipe, mealType: MealType = recipe.mealType) {
        nutritionRepository.insertMeal(
            com.example.data.model.MealEntry(
                date = date,
                mealType = mealType,
                foodName = recipe.titleHu,
                brand = "Recept (${recipe.estimatedCostHuf} Ft)",
                amountGrams = 250.0,
                calories = recipe.calories,
                protein = recipe.protein,
                carbs = recipe.carbs,
                fat = recipe.fat,
                fiber = recipe.fiber
            )
        )
    }
}
