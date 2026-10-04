package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.SupplementsData
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementGoal
import com.example.data.model.SupplementItem
import com.example.data.model.SupplementTiming
import com.example.data.model.toEntity
import com.example.data.repository.NutritionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CuratedSupplementsRoomTest {

    private lateinit var database: AppDatabase
    private lateinit var nutritionRepository: NutritionRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        nutritionRepository = NutritionRepository(
            foodDao = database.foodDao(),
            mealDao = database.mealDao(),
            waterDao = database.waterDao(),
            weightDao = database.weightDao(),
            userDao = database.userDao(),
            supplementDao = database.supplementDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testPrepopulateAndRetrieveCuratedSupplementsFromRoom() = runTest {
        // Prepopulate curated list from SupplementsData into Room
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        val allSupplements = database.supplementDao().getAllSupplements().first()
        assertEquals(SupplementsData.SUPPLEMENTS.size, allSupplements.size)
        assertTrue("Expected at least 25 curated supplements", allSupplements.size >= 25)

        // Verify count
        val count = database.supplementDao().getSupplementCount()
        assertEquals(allSupplements.size, count)
    }

    @Test
    fun testCuratedBrandsCoverageInRoom() = runTest {
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        // 1. BioTechUSA
        val biotechItems = database.supplementDao().getSupplementsByBrand("BioTechUSA").first()
        assertTrue("Expected BioTechUSA supplements in Room", biotechItems.isNotEmpty())
        assertTrue(biotechItems.all { it.brand == "BioTechUSA" })
        val biotechWhey = biotechItems.find { it.id == "supp_biotech_pure_whey" }
        assertNotNull(biotechWhey)
        assertEquals("100% Pure Whey", biotechWhey?.name)
        assertTrue(biotechWhey!!.recommendedDosageHu.isNotBlank())
        assertTrue(biotechWhey.timingDescriptionHu.isNotBlank())
        assertEquals(104.0, biotechWhey.caloriesPerServing, 0.1)
        assertEquals(21.0, biotechWhey.proteinPerServing, 0.1)
        assertEquals(28.0, biotechWhey.servingGrams, 0.1)

        // 2. Scitec Nutrition
        val scitecItems = database.supplementDao().getSupplementsByBrand("Scitec Nutrition").first()
        assertTrue("Expected Scitec Nutrition supplements in Room", scitecItems.isNotEmpty())
        assertTrue(scitecItems.all { it.brand == "Scitec Nutrition" })
        val scitecWhey = scitecItems.find { it.id == "supp_scitec_whey_pro" }
        assertNotNull(scitecWhey)
        assertEquals("100% Whey Protein Professional", scitecWhey?.name)
        assertEquals("5996655100018", scitecWhey?.barcode)
        assertTrue(scitecWhey!!.recommendedDosageHu.contains("30g"))
        assertEquals(112.0, scitecWhey.caloriesPerServing, 0.1)
        assertEquals(22.0, scitecWhey.proteinPerServing, 0.1)

        // 3. OstroVit
        val ostrovitItems = database.supplementDao().getSupplementsByBrand("OstroVit").first()
        assertTrue("Expected OstroVit supplements in Room", ostrovitItems.isNotEmpty())
        assertTrue(ostrovitItems.all { it.brand == "OstroVit" })
        val ostrovitWhey = ostrovitItems.find { it.id == "supp_ostrovit_whey" }
        assertNotNull(ostrovitWhey)
        assertEquals("OstroVit 100% Whey Protein", ostrovitWhey?.name)
        assertEquals(111.0, ostrovitWhey!!.caloriesPerServing, 0.1)
        assertEquals(21.0, ostrovitWhey.proteinPerServing, 0.1)
    }

    @Test
    fun testDosageGuidelinesAndNutritionalInfoIntegrity() = runTest {
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        val all = database.supplementDao().getAllSupplements().first()
        for (item in all) {
            // Standard dosage guidelines checks
            assertTrue("Supplement ${item.name} must have recommended dosage guidelines", item.recommendedDosageHu.isNotBlank())
            assertTrue("Supplement ${item.name} must have primary timing guidelines", item.timingDescriptionHu.isNotBlank())
            assertTrue("Supplement ${item.name} must have serving unit", item.servingUnitHu.isNotBlank())

            // Nutritional info checks
            assertTrue("Supplement ${item.name} servingGrams must be positive", item.servingGrams > 0.0)
            assertTrue("Calories must be >= 0", item.caloriesPerServing >= 0.0)
            assertTrue("Protein must be >= 0", item.proteinPerServing >= 0.0)
            assertTrue("Carbs must be >= 0", item.carbsPerServing >= 0.0)
            assertTrue("Fat must be >= 0", item.fatPerServing >= 0.0)
        }
    }

    @Test
    fun testFilterByCategoryInRoom() = runTest {
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        val proteins = database.supplementDao().getSupplementsByCategory(SupplementCategory.PROTEIN).first()
        assertTrue(proteins.isNotEmpty())
        assertTrue(proteins.all { it.category == SupplementCategory.PROTEIN })

        val creatines = database.supplementDao().getSupplementsByCategory(SupplementCategory.CREATINE).first()
        assertTrue(creatines.isNotEmpty())
        assertTrue(creatines.all { it.category == SupplementCategory.CREATINE })
        assertTrue(creatines.any { it.brand == "BioTechUSA" })
        assertTrue(creatines.any { it.brand == "Scitec Nutrition" })
        assertTrue(creatines.any { it.brand == "OstroVit" })
    }

    @Test
    fun testSearchSupplementsInRoom() = runTest {
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        val creatineResults = database.supplementDao().searchSupplements("Kreatin").first()
        assertTrue(creatineResults.isNotEmpty())

        val ostrovitResults = database.supplementDao().searchSupplements("OstroVit").first()
        assertTrue(ostrovitResults.isNotEmpty())
        assertTrue(ostrovitResults.all { it.brand == "OstroVit" })
    }

    @Test
    fun testBarcodeLookupInRoom() = runTest {
        val curatedEntities = SupplementsData.SUPPLEMENTS.map { it.toEntity() }
        database.supplementDao().insertAllSupplements(curatedEntities)

        val found = database.supplementDao().getSupplementByBarcode("5996655100018")
        assertNotNull(found)
        assertEquals("100% Whey Protein Professional", found?.name)
        assertEquals("Scitec Nutrition", found?.brand)
    }

    @Test
    fun testEntityDomainModelMapping() {
        val original = SupplementItem(
            id = "test_supp_1",
            name = "Test Kreatin Monohidrát",
            brand = "BioTechUSA",
            barcode = "1234567890123",
            category = SupplementCategory.CREATINE,
            targetGoals = listOf(SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.POST_WORKOUT,
            timingDescriptionHu = "Edzés előtt vagy után",
            recommendedDosageHu = "Napi 5g bőséges folyadékkal",
            activeIngredientsHu = "100% mikronizált kreatin-monohidrát",
            whyTakeItHu = "Növeli a fizikai teljesítményt",
            stackingTipsHu = "Kombináld gyors szénhidráttal",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 5.0,
            servingUnitHu = "1 adagolókanál (5g)"
        )

        val entity = original.toEntity()
        assertEquals(original.id, entity.id)
        assertEquals(original.name, entity.name)
        assertEquals(original.brand, entity.brand)
        assertEquals("STRENGTH_ENDURANCE,MUSCLE_BUILDING", entity.targetGoals)

        val domainItem = entity.toSupplementItem()
        assertEquals(original.id, domainItem.id)
        assertEquals(original.name, domainItem.name)
        assertEquals(original.brand, domainItem.brand)
        assertEquals(2, domainItem.targetGoals.size)
        assertTrue(domainItem.targetGoals.contains(SupplementGoal.STRENGTH_ENDURANCE))
        assertTrue(domainItem.targetGoals.contains(SupplementGoal.MUSCLE_BUILDING))
        assertEquals(original.recommendedDosageHu, domainItem.recommendedDosageHu)
        assertEquals(original.timingDescriptionHu, domainItem.timingDescriptionHu)
        assertEquals(original.servingGrams, domainItem.servingGrams, 0.001)
    }
}
