package com.example

import com.example.data.model.BmiCategory
import com.example.data.model.HealthMetricsEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HealthMetricsCalculatorTest {

    @Test
    fun testNormalBmiCalculation() {
        val heightCm = 178.0
        val weightKg = 70.0
        val result = HealthMetricsEngine.calculate(heightCm, weightKg)

        // Expected BMI: 70 / (1.78 * 1.78) = 70 / 3.1684 = 22.09 -> 22.1
        assertEquals(22.1, result.bmi, 0.1)
        assertEquals(BmiCategory.NORMAL, result.category)
        assertTrue(result.isHealthyNormal)
        assertEquals(0.0, result.differenceToNormalKg, 0.01)

        // Ideal weight bounds:
        // 18.5 * 1.78^2 = 58.6 kg
        // 24.9 * 1.78^2 = 78.9 kg
        assertEquals(58.6, result.idealWeightMinKg, 0.2)
        assertEquals(78.9, result.idealWeightMaxKg, 0.2)

        assertTrue(result.progressSummaryHu.contains("egészséges"))
        assertTrue(result.healthAdviceListHu.isNotEmpty())
    }

    @Test
    fun testOverweightBmiCalculationAndWeightLossNeeded() {
        val heightCm = 178.0
        val weightKg = 85.0
        val result = HealthMetricsEngine.calculate(heightCm, weightKg)

        // Expected BMI: 85 / (1.78 * 1.78) = 26.83 -> 26.8
        assertEquals(26.8, result.bmi, 0.1)
        assertEquals(BmiCategory.OVERWEIGHT, result.category)
        assertFalse(result.isHealthyNormal)

        // Weight loss needed to reach 78.9 kg (max normal)
        // 85.0 - 78.9 = 6.1 kg
        assertTrue(result.differenceToNormalKg > 0.0)
        assertEquals(6.1, result.differenceToNormalKg, 0.2)

        assertTrue(result.progressSummaryHu.contains("leadása szükséges"))
    }

    @Test
    fun testUnderweightBmiCalculationAndWeightGainNeeded() {
        val heightCm = 178.0
        val weightKg = 50.0
        val result = HealthMetricsEngine.calculate(heightCm, weightKg)

        // Expected BMI: 50 / (1.78 * 1.78) = 15.78 -> 15.8
        assertEquals(15.8, result.bmi, 0.1)
        assertEquals(BmiCategory.SEVERELY_UNDERWEIGHT, result.category)
        assertFalse(result.isHealthyNormal)

        // Weight gain needed to reach 58.6 kg (min normal)
        // 58.6 - 50.0 = 8.6 kg
        assertTrue(result.differenceToNormalKg < 0.0)
        assertEquals(-8.6, result.differenceToNormalKg, 0.2)

        assertTrue(result.progressSummaryHu.contains("gyarapodás szükséges"))
    }

    @Test
    fun testObeseClassOneBmiCalculation() {
        val heightCm = 175.0
        val weightKg = 100.0
        val result = HealthMetricsEngine.calculate(heightCm, weightKg)

        // Expected BMI: 100 / (1.75 * 1.75) = 100 / 3.0625 = 32.65 -> 32.7
        assertEquals(32.7, result.bmi, 0.1)
        assertEquals(BmiCategory.OBESE_CLASS_1, result.category)
        assertFalse(result.isHealthyNormal)
        assertTrue(result.differenceToNormalKg > 0)
    }

    @Test
    fun testGaugeRatioNormalization() {
        val minResult = HealthMetricsEngine.calculate(180.0, 45.0)
        assertTrue(minResult.progressGaugeRatio in 0.0f..1.0f)

        val maxResult = HealthMetricsEngine.calculate(160.0, 150.0)
        assertTrue(maxResult.progressGaugeRatio in 0.0f..1.0f)
    }

    @Test
    fun testStandardRangesList() {
        val ranges = HealthMetricsEngine.getStandardRanges()
        assertEquals(5, ranges.size)
        assertNotNull(ranges.find { it.category == BmiCategory.NORMAL })
    }
}
