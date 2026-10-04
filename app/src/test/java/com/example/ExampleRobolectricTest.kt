package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kalória & Fit", appName)
  }

    @Test
  fun `supplements catalog contains products`() {
    val supplements = com.example.data.local.SupplementsData.SUPPLEMENTS
    org.junit.Assert.assertTrue(supplements.isNotEmpty())
    org.junit.Assert.assertTrue(supplements.any { it.brand == "BioTechUSA" })
    org.junit.Assert.assertTrue(supplements.any { it.brand == "Scitec Nutrition" })
    org.junit.Assert.assertTrue(supplements.any { it.brand == "OstroVit" })
  }

  @Test
  fun `protein calculator calculates properly for fat loss, bulking and recomp`() {
    val weight = 80.0
    val fatLossResult = com.example.data.model.ProteinCalculatorEngine.calculate(
      weightKg = weight,
      goal = com.example.data.model.FitnessGoal.FAT_LOSS,
      trainingStyle = com.example.data.model.TrainingStyle.CALISTHENICS
    )
    // Fat loss should recommend high protein (>= 2.2 g/kg -> ~176g)
    org.junit.Assert.assertTrue("Fat loss protein should be >= 170g", fatLossResult.proteinGramsRecommended >= 170)
    org.junit.Assert.assertTrue("Deficit calories should be less than TDEE", fatLossResult.targetCalories < fatLossResult.tdeeCalories)

    val bulkingResult = com.example.data.model.ProteinCalculatorEngine.calculate(
      weightKg = weight,
      goal = com.example.data.model.FitnessGoal.BULKING,
      trainingStyle = com.example.data.model.TrainingStyle.GYM_WEIGHTS
    )
    // Bulking should have surplus calories
    org.junit.Assert.assertTrue("Bulking calories should exceed TDEE", bulkingResult.targetCalories > bulkingResult.tdeeCalories)
    org.junit.Assert.assertTrue("Protein for bulking 80kg should be >= 150g", bulkingResult.proteinGramsRecommended >= 150)

    val recompResult = com.example.data.model.ProteinCalculatorEngine.calculate(
      weightKg = weight,
      goal = com.example.data.model.FitnessGoal.RECOMPOSITION,
      trainingStyle = com.example.data.model.TrainingStyle.TACTICAL_MILITARY
    )
    org.junit.Assert.assertTrue("Recomp protein should be high", recompResult.proteinGramsRecommended >= 165)
    org.junit.Assert.assertTrue("Meal distribution perMeal4 should be calculated", recompResult.perMeal4 > 0)
    org.junit.Assert.assertTrue("Food examples should not be empty", recompResult.foodExamples.isNotEmpty())
  }
}
