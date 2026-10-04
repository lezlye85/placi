package com.example.data.remote

import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import kotlinx.coroutines.delay
import kotlin.math.abs

/**
 * Data model returned by the Mock Nutritional API.
 * Includes complete macro & micronutrients, serving sizes, Nutri-Score, allergens, and ingredients.
 */
data class NutritionalItemDetail(
    val barcode: String,
    val name: String,
    val brand: String,
    val category: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val fiberPer100g: Double = 0.0,
    val sugarPer100g: Double = 0.0,
    val saltPer100g: Double = 0.0,
    val servingSizeGrams: Double = 100.0,
    val servingUnitName: String = "adag (100g)",
    val nutriScore: String = "A", // A, B, C, D, E
    val novaGroup: Int = 1, // 1 to 4
    val allergens: List<String> = emptyList(),
    val ingredients: String = "",
    val apiSource: String = "Mock Nutritional Cloud API v2"
) {
    /**
     * Calculates calories for a specific amount in grams
     */
    fun caloriesForAmount(grams: Double): Double = (caloriesPer100g * grams) / 100.0

    /**
     * Calculates protein for a specific amount in grams
     */
    fun proteinForAmount(grams: Double): Double = (proteinPer100g * grams) / 100.0

    /**
     * Calculates carbs for a specific amount in grams
     */
    fun carbsForAmount(grams: Double): Double = (carbsPer100g * grams) / 100.0

    /**
     * Calculates fat for a specific amount in grams
     */
    fun fatForAmount(grams: Double): Double = (fatPer100g * grams) / 100.0

    /**
     * Converts the API response into the Room database FoodItem entity
     */
    fun toFoodItem(): FoodItem {
        return FoodItem(
            name = name,
            brand = brand,
            barcode = barcode,
            category = category,
            caloriesPer100g = caloriesPer100g,
            proteinPer100g = proteinPer100g,
            carbsPer100g = carbsPer100g,
            fatPer100g = fatPer100g,
            fiberPer100g = fiberPer100g,
            defaultServingUnit = servingUnitName,
            defaultServingGrams = servingSizeGrams,
            isCustom = false
        )
    }
}

sealed interface MockApiResponse<out T> {
    data class Success<T>(val data: T, val latencyMs: Long) : MockApiResponse<T>
    data class Error(val message: String, val statusCode: Int = 404) : MockApiResponse<Nothing>
}

/**
 * Service simulating a real high-performance Nutritional Cloud API.
 * Provides realistic responses, latency, and comprehensive food databases
 * (including fitness supplements and everyday grocery items).
 */
class MockNutritionalApiService {

    // Curated catalog of known barcodes and detailed nutritional profiles
    private val knownProducts: Map<String, NutritionalItemDetail> = listOf(
        NutritionalItemDetail(
            barcode = "5999076228300",
            name = "100% Pure Whey Fehérjepor (Csokoládé)",
            brand = "BioTechUSA",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 371.0,
            proteinPer100g = 78.0,
            carbsPer100g = 4.6,
            fatPer100g = 4.0,
            fiberPer100g = 1.2,
            sugarPer100g = 3.8,
            saltPer100g = 0.8,
            servingSizeGrams = 28.0,
            servingUnitName = "1 adagolókanál (28g)",
            nutriScore = "A",
            novaGroup = 4,
            allergens = listOf("Tej", "Szójalecitin"),
            ingredients = "Tejsavófehérje koncentrátum, tejsavófehérje izolátum, zsírszegény kakaópor, L-glutamin, aromák, édesítőszer (szukralóz)."
        ),
        NutritionalItemDetail(
            barcode = "5996655100018",
            name = "100% Whey Protein Professional (Vanília)",
            brand = "Scitec Nutrition",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 377.0,
            proteinPer100g = 73.0,
            carbsPer100g = 6.4,
            fatPer100g = 6.0,
            fiberPer100g = 0.5,
            sugarPer100g = 4.5,
            saltPer100g = 1.1,
            servingSizeGrams = 30.0,
            servingUnitName = "1 adag (30g)",
            nutriScore = "A",
            novaGroup = 4,
            allergens = listOf("Tej", "Szója"),
            ingredients = "Instant tejsavófehérjék (koncentrátum, izolátum), aromák, L-leucin, L-glutamin, papain és bromelain emésztőenzimek, édesítőszerek."
        ),
        NutritionalItemDetail(
            barcode = "5999076211104",
            name = "100% Micronized Creatine Monohydrate",
            brand = "BioTechUSA",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 0.0,
            proteinPer100g = 0.0,
            carbsPer100g = 0.0,
            fatPer100g = 0.0,
            fiberPer100g = 0.0,
            sugarPer100g = 0.0,
            saltPer100g = 0.0,
            servingSizeGrams = 3.4,
            servingUnitName = "1 adagolókanál (3.4g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = emptyList(),
            ingredients = "100% mikronizált kreatin-monohidrát (gyógyszerészeti tisztaságú, ízesítetlen)."
        ),
        NutritionalItemDetail(
            barcode = "5902560381001",
            name = "Creatine Monohydrate Pure",
            brand = "OstroVit",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 0.0,
            proteinPer100g = 0.0,
            carbsPer100g = 0.0,
            fatPer100g = 0.0,
            fiberPer100g = 0.0,
            sugarPer100g = 0.0,
            saltPer100g = 0.0,
            servingSizeGrams = 3.0,
            servingUnitName = "1 adag (3g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = emptyList(),
            ingredients = "Kreatin-monohidrát por."
        ),
        NutritionalItemDetail(
            barcode = "5997321400234",
            name = "Félzsíros Tehéntúró 250g",
            brand = "Mizo",
            category = FoodCategory.DAIRY_EGG.name,
            caloriesPer100g = 142.0,
            proteinPer100g = 16.2,
            carbsPer100g = 3.8,
            fatPer100g = 7.0,
            fiberPer100g = 0.0,
            sugarPer100g = 3.8,
            saltPer100g = 0.1,
            servingSizeGrams = 125.0,
            servingUnitName = "fél doboz (125g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = listOf("Tej / Laktóz"),
            ingredients = "Pasztőrözött tehéntej, tejsavbaktérium-színtenyészet, tejoltó enzim."
        ),
        NutritionalItemDetail(
            barcode = "5997123400567",
            name = "Finom Szemű Zabpehely",
            brand = "Bio Natúr",
            category = FoodCategory.BAKERY_GRAIN.name,
            caloriesPer100g = 368.0,
            proteinPer100g = 13.5,
            carbsPer100g = 58.7,
            fatPer100g = 7.0,
            fiberPer100g = 10.0,
            sugarPer100g = 1.2,
            saltPer100g = 0.02,
            servingSizeGrams = 50.0,
            servingUnitName = "1 tálka (50g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = listOf("Glutén"),
            ingredients = "100% teljes értékű finomszemű hengerelt zabpehely."
        ),
        NutritionalItemDetail(
            barcode = "5999076200207",
            name = "One-A-Day Multivitamin & Ásványi Anyag",
            brand = "BioTechUSA",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 0.0,
            proteinPer100g = 0.0,
            carbsPer100g = 0.0,
            fatPer100g = 0.0,
            fiberPer100g = 0.0,
            sugarPer100g = 0.0,
            saltPer100g = 0.0,
            servingSizeGrams = 1.5,
            servingUnitName = "1 tabletta (1.5g)",
            nutriScore = "A",
            novaGroup = 4,
            allergens = emptyList(),
            ingredients = "12 vitamin (A, C, D, E, B-komplex) és 10 alapvető ásványi anyag (cink, vas, magnézium, szelén)."
        ),
        NutritionalItemDetail(
            barcode = "5902560382008",
            name = "D3 4000 IU + K2 MK-7 cseppek/kapszula",
            brand = "OstroVit",
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = 0.0,
            proteinPer100g = 0.0,
            carbsPer100g = 0.0,
            fatPer100g = 0.0,
            fiberPer100g = 0.0,
            sugarPer100g = 0.0,
            saltPer100g = 0.0,
            servingSizeGrams = 0.5,
            servingUnitName = "1 kapszula (0.5g)",
            nutriScore = "A",
            novaGroup = 3,
            allergens = emptyList(),
            ingredients = "Kolekalciferol (D3-vitamin), Menakinon-7 (K2-vitamin nattóból), MCT olaj."
        ),
        NutritionalItemDetail(
            barcode = "7350083380123",
            name = "Protein Bar Salty Peanut",
            brand = "Barebells",
            category = FoodCategory.NUTS_SNACKS.name,
            caloriesPer100g = 373.0,
            proteinPer100g = 36.0,
            carbsPer100g = 27.0,
            fatPer100g = 15.0,
            fiberPer100g = 6.4,
            sugarPer100g = 2.8,
            saltPer100g = 0.7,
            servingSizeGrams = 55.0,
            servingUnitName = "1 szelet (55g)",
            nutriScore = "B",
            novaGroup = 4,
            allergens = listOf("Tej", "Mogyoró", "Szója"),
            ingredients = "Tejfehérje, tejcsokoládé édesítőszerrel, kollagén hidrolizátum, pörkölt földimogyoró darabok."
        ),
        NutritionalItemDetail(
            barcode = "5998711003456",
            name = "Friss Csirkemell Filé",
            brand = "Magyar Baromfi",
            category = FoodCategory.MEAT_FISH.name,
            caloriesPer100g = 120.0,
            proteinPer100g = 23.5,
            carbsPer100g = 0.0,
            fatPer100g = 2.1,
            fiberPer100g = 0.0,
            sugarPer100g = 0.0,
            saltPer100g = 0.15,
            servingSizeGrams = 150.0,
            servingUnitName = "1 szelet (150g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = emptyList(),
            ingredients = "100% friss bőr nélküli csirkemell filé."
        ),
        NutritionalItemDetail(
            barcode = "5998711007890",
            name = "Görög Natúr Joghurt 0% zsír",
            brand = "Mizo",
            category = FoodCategory.DAIRY_EGG.name,
            caloriesPer100g = 57.0,
            proteinPer100g = 10.3,
            carbsPer100g = 4.0,
            fatPer100g = 0.1,
            fiberPer100g = 0.0,
            sugarPer100g = 4.0,
            saltPer100g = 0.1,
            servingSizeGrams = 150.0,
            servingUnitName = "1 pohár (150g)",
            nutriScore = "A",
            novaGroup = 1,
            allergens = listOf("Tej / Laktóz"),
            ingredients = "Sovány tej, élő joghurtkultúra."
        ),
        NutritionalItemDetail(
            barcode = "5991234567890",
            name = "100% Crunchy Mogyoróvaj Só nélkül",
            brand = "GymBeam",
            category = FoodCategory.NUTS_SNACKS.name,
            caloriesPer100g = 588.0,
            proteinPer100g = 25.8,
            carbsPer100g = 20.0,
            fatPer100g = 49.0,
            fiberPer100g = 8.0,
            sugarPer100g = 4.2,
            saltPer100g = 0.01,
            servingSizeGrams = 25.0,
            servingUnitName = "1 evőkanál (25g)",
            nutriScore = "B",
            novaGroup = 1,
            allergens = listOf("Földimogyoró"),
            ingredients = "100% szárazon pirított ropogós földimogyoró (hozzáadott pálmaolaj és cukor nélkül)."
        )
    ).associateBy { it.barcode }

    /**
     * Simulates fetching item details from a Mock Nutritional API.
     * Includes network latency simulation (400-800ms).
     * If the barcode is not in the curated list, it generates an intelligent,
     * realistic nutritional profile based on barcode digits so testing with any physical product works.
     */
    suspend fun fetchFoodDetails(barcode: String): MockApiResponse<NutritionalItemDetail> {
        val trimmed = barcode.trim()
        if (trimmed.isBlank()) {
            return MockApiResponse.Error("A vonalkód nem lehet üres", 400)
        }

        // Simulate network call latency
        val start = System.currentTimeMillis()
        delay(650)
        val latency = System.currentTimeMillis() - start

        // Check if barcode is in known products
        val known = knownProducts[trimmed]
        if (known != null) {
            return MockApiResponse.Success(known, latency)
        }

        // Fallback: If barcode is 8 to 14 digits (valid EAN/UPC), dynamically generate a realistic item
        if (trimmed.length in 8..14 && trimmed.all { it.isDigit() }) {
            val generated = synthesizeFoodFromBarcode(trimmed)
            return MockApiResponse.Success(generated, latency)
        }

        return MockApiResponse.Error("Nem található termék a következő vonalkóddal: $trimmed", 404)
    }

    private data class SyntheticPreset(
        val name: String,
        val category: String,
        val nutriScore: String
    )

    /**
     * Synthesizes a plausible nutritional product from an unknown scanned barcode
     * to guarantee robust emulator/field testing.
     */
    private fun synthesizeFoodFromBarcode(barcode: String): NutritionalItemDetail {
        val hash = abs(barcode.hashCode())
        val categories = listOf(
            SyntheticPreset("Protein Snack Szelet", FoodCategory.NUTS_SNACKS.name, "A"),
            SyntheticPreset("Izotóniás Sportital", FoodCategory.DRINKS.name, "B"),
            SyntheticPreset("Magvas Teljes Kiőrlésű Kenyér", FoodCategory.BAKERY_GRAIN.name, "A"),
            SyntheticPreset("Érlelt Sovány Sajt", FoodCategory.DAIRY_EGG.name, "C"),
            SyntheticPreset("Granola Mazsolával és Mandulával", FoodCategory.BAKERY_GRAIN.name, "B"),
            SyntheticPreset("Tonhalfilé Natúr Lében", FoodCategory.MEAT_FISH.name, "A"),
            SyntheticPreset("Fehérjés Kakaós Tejital", FoodCategory.DAIRY_EGG.name, "A")
        )
        val selected = categories[hash % categories.size]

        val calories = 90.0 + (hash % 380)
        val protein = 2.0 + ((hash / 3) % 28)
        val carbs = 5.0 + ((hash / 7) % 55)
        val fat = 1.0 + ((hash / 11) % 22)
        val fiber = 1.0 + ((hash / 13) % 8)

        return NutritionalItemDetail(
            barcode = barcode,
            name = "${selected.name} (EAN: ${barcode.takeLast(4)})",
            brand = "NutriCloud Partner",
            category = selected.category,
            caloriesPer100g = Math.round(calories * 10.0) / 10.0,
            proteinPer100g = Math.round(protein * 10.0) / 10.0,
            carbsPer100g = Math.round(carbs * 10.0) / 10.0,
            fatPer100g = Math.round(fat * 10.0) / 10.0,
            fiberPer100g = Math.round(fiber * 10.0) / 10.0,
            sugarPer100g = Math.round((carbs * 0.25) * 10.0) / 10.0,
            saltPer100g = 0.45,
            servingSizeGrams = 100.0,
            servingUnitName = "adag (100g)",
            nutriScore = selected.nutriScore,
            novaGroup = if (calories > 300) 4 else 2,
            allergens = listOf("Nyomokban dióféléket tartalmazhat"),
            ingredients = "Természetes összetevők, mikrotápanyagok, ásványi anyagok.",
            apiSource = "Mock Nutritional Cloud API (Dinamikus felismerés)"
        )
    }

    /**
     * Returns curated sample barcodes for testing
     */
    fun getSampleBarcodes(): List<Pair<String, String>> {
        return knownProducts.values.map { item ->
            Pair("${item.brand} - ${item.name.take(24)}", item.barcode)
        }
    }
}
