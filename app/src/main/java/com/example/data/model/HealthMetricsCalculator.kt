package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import kotlin.math.pow
import kotlin.math.roundToInt

enum class BmiCategory(
    val titleHu: String,
    val subtitleHu: String,
    val emoji: String,
    val colorHex: Long,
    val healthRiskHu: String
) {
    SEVERELY_UNDERWEIGHT(
        titleHu = "Súlyos soványság",
        subtitleHu = "Jelentős alultápláltság (BMI < 16.0)",
        emoji = "⚠️",
        colorHex = 0xFF0288D1, // Deep Light Blue
        healthRiskHu = "Magas fertőzésveszély, csontritkulás és izomvesztés kockázat."
    ),
    MODERATELY_UNDERWEIGHT(
        titleHu = "Mérsékelt soványság",
        subtitleHu = "Alultápláltság (BMI 16.0 - 16.9)",
        emoji = "📉",
        colorHex = 0xFF03A9F4, // Light Blue
        healthRiskHu = "Csökkent energiaszint és immunfunkciók."
    ),
    MILDLY_UNDERWEIGHT(
        titleHu = "Enyhe soványság",
        subtitleHu = "Normál alatti testsúly (BMI 17.0 - 18.4)",
        emoji = "📉",
        colorHex = 0xFF4FC3F7, // Soft Blue
        healthRiskHu = "Enyhe tápanyaghiány, nehezebb izomtömeg-megtartás."
    ),
    NORMAL(
        titleHu = "Normál egészséges testsúly",
        subtitleHu = "Optimális tartomány (BMI 18.5 - 24.9)",
        emoji = "✅",
        colorHex = 0xFF10B981, // Emerald Green
        healthRiskHu = "Alacsony szív- és érrendszeri és anyagcsere kockázat."
    ),
    OVERWEIGHT(
        titleHu = "Túlsúly",
        subtitleHu = "Mérsékelt többlet (BMI 25.0 - 29.9)",
        emoji = "⚠️",
        colorHex = 0xFFF59E0B, // Amber / Orange
        healthRiskHu = "Mérsékelten emelkedett terhelés a szívre és az ízületekre."
    ),
    OBESE_CLASS_1(
        titleHu = "I. fokú elhízás",
        subtitleHu = "Elhízás kezdete (BMI 30.0 - 34.9)",
        emoji = "❗",
        colorHex = 0xFFEF4444, // Red
        healthRiskHu = "Fokozott kockázat magas vérnyomásra és 2-es típusú cukorbetegségre."
    ),
    OBESE_CLASS_2(
        titleHu = "II. fokú elhízás",
        subtitleHu = "Középsúlyos elhízás (BMI 35.0 - 39.9)",
        emoji = "🚨",
        colorHex = 0xFFDC2626, // Crimson Red
        healthRiskHu = "Jelentősen magas szív-, érrendszeri és ízületi terhelés."
    ),
    OBESE_CLASS_3(
        titleHu = "III. fokú (kóros) elhízás",
        subtitleHu = "Súlyos elhízás (BMI ≥ 40.0)",
        emoji = "🛑",
        colorHex = 0xFF991B1B, // Dark Red
        healthRiskHu = "Kritikus egészségügyi kockázat, orvosi konzultáció javasolt."
    )
}

data class HealthRangeBand(
    val category: BmiCategory,
    val rangeText: String,
    val minBmi: Double,
    val maxBmi: Double,
    val color: Color
)

data class HealthMetricsResult(
    val heightCm: Double,
    val weightKg: Double,
    val bmi: Double,
    val category: BmiCategory,
    val idealWeightMinKg: Double,
    val idealWeightMaxKg: Double,
    val differenceToNormalKg: Double, // >0 if overweight (kg to lose), <0 if underweight (kg to gain), 0.0 if normal
    val isHealthyNormal: Boolean,
    val progressGaugeRatio: Float, // 0.0f..1.0f mapped on 15.0 .. 40.0 BMI
    val ponderalIndex: Double,
    val progressSummaryHu: String,
    val targetRangeTextHu: String,
    val healthAdviceListHu: List<String>
)

object HealthMetricsEngine {

    fun getStandardRanges(): List<HealthRangeBand> = listOf(
        HealthRangeBand(
            category = BmiCategory.MILDLY_UNDERWEIGHT,
            rangeText = "< 18.5",
            minBmi = 0.0,
            maxBmi = 18.5,
            color = Color(0xFF0288D1)
        ),
        HealthRangeBand(
            category = BmiCategory.NORMAL,
            rangeText = "18.5 – 24.9",
            minBmi = 18.5,
            maxBmi = 24.9,
            color = Emerald500
        ),
        HealthRangeBand(
            category = BmiCategory.OVERWEIGHT,
            rangeText = "25.0 – 29.9",
            minBmi = 25.0,
            maxBmi = 29.9,
            color = CarbsColor
        ),
        HealthRangeBand(
            category = BmiCategory.OBESE_CLASS_1,
            rangeText = "30.0 – 34.9",
            minBmi = 30.0,
            maxBmi = 34.9,
            color = FatColor
        ),
        HealthRangeBand(
            category = BmiCategory.OBESE_CLASS_2,
            rangeText = "≥ 35.0",
            minBmi = 35.0,
            maxBmi = 60.0,
            color = Color(0xFF991B1B)
        )
    )

    fun calculate(heightCm: Double, weightKg: Double): HealthMetricsResult {
        val safeHeightCm = heightCm.coerceIn(100.0, 250.0)
        val safeWeightKg = weightKg.coerceIn(30.0, 300.0)

        val heightM = safeHeightCm / 100.0
        val rawBmi = safeWeightKg / (heightM * heightM)
        val roundedBmi = (rawBmi * 10.0).roundToInt() / 10.0

        val category = when {
            rawBmi < 16.0 -> BmiCategory.SEVERELY_UNDERWEIGHT
            rawBmi < 17.0 -> BmiCategory.MODERATELY_UNDERWEIGHT
            rawBmi < 18.5 -> BmiCategory.MILDLY_UNDERWEIGHT
            rawBmi < 25.0 -> BmiCategory.NORMAL
            rawBmi < 30.0 -> BmiCategory.OVERWEIGHT
            rawBmi < 35.0 -> BmiCategory.OBESE_CLASS_1
            rawBmi < 40.0 -> BmiCategory.OBESE_CLASS_2
            else -> BmiCategory.OBESE_CLASS_3
        }

        // Standard WHO healthy normal range: 18.5 to 24.9
        val idealMinKg = ((18.5 * heightM * heightM) * 10.0).roundToInt() / 10.0
        val idealMaxKg = ((24.9 * heightM * heightM) * 10.0).roundToInt() / 10.0

        val diffToNormal = when {
            safeWeightKg > idealMaxKg -> ((safeWeightKg - idealMaxKg) * 10.0).roundToInt() / 10.0
            safeWeightKg < idealMinKg -> -(((idealMinKg - safeWeightKg) * 10.0).roundToInt() / 10.0)
            else -> 0.0
        }

        val isNormal = category == BmiCategory.NORMAL

        // Gauge ratio normalized between BMI 15.0 (0%) and 40.0 (100%)
        val gaugeRatio = ((rawBmi - 15.0) / (40.0 - 15.0)).toFloat().coerceIn(0.0f, 1.0f)

        // Ponderal Index (PI = weight / height^3) in kg/m^3 (Normal ~ 11 - 15 kg/m^3)
        val ponderal = (safeWeightKg / (heightM.pow(3)) * 10.0).roundToInt() / 10.0

        val targetRangeText = "${idealMinKg} kg – ${idealMaxKg} kg"

        val summary = when {
            isNormal -> {
                val distToMax = ((idealMaxKg - safeWeightKg) * 10.0).roundToInt() / 10.0
                val distToMin = ((safeWeightKg - idealMinKg) * 10.0).roundToInt() / 10.0
                "Kiváló! A testsúlyod az egészséges standard zónában van (${idealMinKg} – ${idealMaxKg} kg). A felső határtól még ${distToMax} kg-ra, az alsó határtól ${distToMin} kg-ra vagy."
            }
            diffToNormal > 0 -> {
                "Még ${diffToNormal} kg leadása szükséges a normál egészségügyi BMI zóna (${idealMinKg} – ${idealMaxKg} kg) eléréséhez."
            }
            else -> {
                val toGain = -diffToNormal
                "Még ${toGain} kg gyarapodás szükséges az optimális egészségügyi normál zóna (${idealMinKg} – ${idealMaxKg} kg) eléréséhez."
            }
        }

        val advice = when (category) {
            BmiCategory.SEVERELY_UNDERWEIGHT,
            BmiCategory.MODERATELY_UNDERWEIGHT,
            BmiCategory.MILDLY_UNDERWEIGHT -> listOf(
                "Növeld a napi energiabeviteled 300-500 kcal többlettel tápanyagdús ételekből (magvak, zab, tojás, húsok).",
                "Fogyassz napi legalább 1.6 - 2.0g fehérjét testtömeg-kilogrammonként az izomtömeg építéséhez.",
                "Végezz progresszív kalisztenika vagy saját testsúlyos saját edzést az izomstimulációhoz."
            )
            BmiCategory.NORMAL -> listOf(
                "Tartsd fenn a jelenlegi kiegyensúlyozott kalóriabevitelt a szinten tartó (TDEE) szinteden.",
                "Fókuszálj a funkcionális erőnlétre és állóképességre börtön- vagy katonai kalisztenika tervekkel.",
                "Ügyelj a napi minimum 2.5 - 3 liter tiszta folyadékbevitelre a hidratáltságért."
            )
            BmiCategory.OVERWEIGHT -> listOf(
                "Tervezz mérsékelt, fenntartható 300-500 kcal napi kalóriadeficitet az étrendedben.",
                "Kombináld a saját testsúlyos kalisztenikát napi 8000-10000 lépéssel és kardió mozgással.",
                "Növeld a rostbevitelt (zöldségek, zab, hüvelyesek) a jobb teltségérzet és vércukor-kontroll érdekében."
            )
            BmiCategory.OBESE_CLASS_1,
            BmiCategory.OBESE_CLASS_2,
            BmiCategory.OBESE_CLASS_3 -> listOf(
                "Fokozatos 500-750 kcal napi deficit javasolt kíméletes, ízületbarát mozgással (pl. tempós séta, könnyített fekvőtámasz).",
                "Kerüld a hozzáadott cukros üdítőket és ultrafeldolgozott élelmiszereket.",
                "Vezesd a napi étkezési és vízfogyasztási naplót a tudatosság megőrzéséhez."
            )
        }

        return HealthMetricsResult(
            heightCm = safeHeightCm,
            weightKg = safeWeightKg,
            bmi = roundedBmi,
            category = category,
            idealWeightMinKg = idealMinKg,
            idealWeightMaxKg = idealMaxKg,
            differenceToNormalKg = diffToNormal,
            isHealthyNormal = isNormal,
            progressGaugeRatio = gaugeRatio,
            ponderalIndex = ponderal,
            progressSummaryHu = summary,
            targetRangeTextHu = targetRangeText,
            healthAdviceListHu = advice
        )
    }
}
