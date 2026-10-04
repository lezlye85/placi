package com.example

import com.example.data.remote.MockApiResponse
import com.example.data.remote.MockNutritionalApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BarcodeScannerTest {

    private val apiService = MockNutritionalApiService()

    @Test
    fun testFetchKnownBarcodeDetails() = runTest {
        // BioTech 100% Pure Whey barcode
        val response = apiService.fetchFoodDetails("5999076228300")
        assertTrue(response is MockApiResponse.Success)

        val item = (response as MockApiResponse.Success).data
        assertEquals("5999076228300", item.barcode)
        assertTrue(item.name.contains("Pure Whey"))
        assertEquals("BioTechUSA", item.brand)
        assertEquals(78.0, item.proteinPer100g, 0.1)

        // Custom serving calculation: 28g scoop
        val caloriesForScoop = item.caloriesForAmount(28.0)
        assertTrue(caloriesForScoop > 100.0 && caloriesForScoop < 110.0)

        val proteinForScoop = item.proteinForAmount(28.0)
        assertTrue(proteinForScoop > 21.0 && proteinForScoop < 22.0)
    }

    @Test
    fun testFetchUnknownNumericBarcodeSynthesizesRealisticItem() = runTest {
        // Arbitrary valid 13-digit EAN code
        val response = apiService.fetchFoodDetails("5991122334455")
        assertTrue(response is MockApiResponse.Success)

        val item = (response as MockApiResponse.Success).data
        assertEquals("5991122334455", item.barcode)
        assertNotNull(item.name)
        assertTrue(item.caloriesPer100g > 0.0)
        assertTrue(item.proteinPer100g >= 0.0)
        assertTrue(item.carbsPer100g >= 0.0)
    }

    @Test
    fun testEmptyBarcodeReturnsError() = runTest {
        val response = apiService.fetchFoodDetails("   ")
        assertTrue(response is MockApiResponse.Error)
    }
}
