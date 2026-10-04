package com.example.data.model

enum class FitnessGoal(
    val titleHu: String,
    val subtitleHu: String,
    val emoji: String,
    val proteinPerKgMin: Double,
    val proteinPerKgMax: Double,
    val recommendedProteinPerKg: Double,
    val calorieAdjustmentPercent: Double,
    val descriptionHu: String,
    val whyProteinHu: String
) {
    FAT_LOSS(
        titleHu = "Fogyás / Szálkásítás",
        subtitleHu = "Kalóriadeficit & maximális izomvédelem",
        emoji = "📉",
        proteinPerKgMin = 2.0,
        proteinPerKgMax = 2.5,
        recommendedProteinPerKg = 2.2,
        calorieAdjustmentPercent = -0.20,
        descriptionHu = "Cél: a felesleges testzsír leadása úgy, hogy a meglévő izomtömeg 100%-ban megmaradjon.",
        whyProteinHu = "Kalóriadeficitben a szervezet hajlamos az aminosavakat energiaként elégetni (glükoneogenezis). A megemelt (2.0 - 2.5 g/kg) fehérje megakadályozza az izomvesztést, magas termikus hatása (TEF ~25%) fokozza a kalóriaégetést és tartós teltségérzetet biztosít."
    ),
    BULKING(
        titleHu = "Tömegelés / Izomépítés",
        subtitleHu = "Anabolikus kalóriatöbblet & izomhipertrófia",
        emoji = "📈",
        proteinPerKgMin = 1.6,
        proteinPerKgMax = 2.2,
        recommendedProteinPerKg = 1.9,
        calorieAdjustmentPercent = 0.12,
        descriptionHu = "Cél: minőségi izomtömeg felépítése ellenőrzött kalóriatöbblettel és progresszív túlterheléssel.",
        whyProteinHu = "Kalóriatöbbletben a szénhidrátok és zsírok fehérjekímélő hatása érvényesül. 1.7 - 2.0 g/kg fehérje bőségesen elegendő az izomfehérje-szintézis (MPS) maximalizálásához, míg a többlet energiát minőségi szénhidrátokból érdemes fedezni a nehéz edzésekhez."
    ),
    RECOMPOSITION(
        titleHu = "Mindkettő / Rekompozíció",
        subtitleHu = "Zsírégetés és izomépítés egyidejűleg",
        emoji = "⚡",
        proteinPerKgMin = 2.1,
        proteinPerKgMax = 2.4,
        recommendedProteinPerKg = 2.2,
        calorieAdjustmentPercent = -0.05,
        descriptionHu = "Cél: a testzsírszázalék csökkentése új izomtömeg felépítése mellett. Különösen eredményes kezdőknek, újrakezdőknek és 15-25% testzsír esetén.",
        whyProteinHu = "A rekompozícióhoz szintentartó vagy enyhe (-5%) deficit kalória szükséges, kiemelten magas (2.1 - 2.4 g/kg) fehérjebevitellel és következetes saját testsúlyos (kalisztenika) vagy súlyzós edzéssel."
    ),
    MAINTENANCE(
        titleHu = "Súlytartás & Fittség",
        subtitleHu = "Stabilitás, regeneráció & egészségmegőrzés",
        emoji = "⚖️",
        proteinPerKgMin = 1.4,
        proteinPerKgMax = 1.8,
        recommendedProteinPerKg = 1.6,
        calorieAdjustmentPercent = 0.0,
        descriptionHu = "Cél: az elért ideális testsúly és forma megtartása, maximális napi energiaszinttel.",
        whyProteinHu = "Súlytartás esetén 1.4 - 1.8 g/kg fehérje fedezi a sejtek regenerációját, az immunrendszer működését és fenntartja az izomtónust."
    )
}

enum class TrainingStyle(
    val displayNameHu: String,
    val iconEmoji: String,
    val descriptionHu: String,
    val proteinMultiplierBonus: Double,
    val tdeeMultiplier: Double
) {
    CALISTHENICS(
        displayNameHu = "Kalisztenika & Saját testsúly",
        iconEmoji = "🤸",
        descriptionHu = "Húzódzkodás, fekvőtámaszok, tolódzkodások, core és statika (3-5x/hét)",
        proteinMultiplierBonus = 0.1,
        tdeeMultiplier = 1.55
    ),
    GYM_WEIGHTS(
        displayNameHu = "Konditerem / Súlyzós edzés",
        iconEmoji = "🏋️",
        descriptionHu = "Súlyzós izomépítés, nehéz alapgyakorlatok (3-5x/hét)",
        proteinMultiplierBonus = 0.1,
        tdeeMultiplier = 1.55
    ),
    TACTICAL_MILITARY(
        displayNameHu = "Katonai & Börtön / HIIT",
        iconEmoji = "🪖",
        descriptionHu = "Nagy intenzitású állóképességi és funkcionális köredzések (4-6x/hét)",
        proteinMultiplierBonus = 0.15,
        tdeeMultiplier = 1.72
    ),
    LIGHT_FITNESS(
        displayNameHu = "Könnyű torna & Séta",
        iconEmoji = "🚶",
        descriptionHu = "Rendszeres séta, jóga, könnyű otthoni torna (1-3x/hét)",
        proteinMultiplierBonus = 0.0,
        tdeeMultiplier = 1.375
    ),
    SEDENTARY(
        displayNameHu = "Ülő életmód",
        iconEmoji = "💻",
        descriptionHu = "Irodai munka, minimális heti mozgás",
        proteinMultiplierBonus = -0.1,
        tdeeMultiplier = 1.2
    )
}

data class ProteinFoodExample(
    val foodName: String,
    val portionHu: String,
    val proteinGrams: Double,
    val calories: Int,
    val emoji: String,
    val type: String // "Étel" vagy "Kiegészítő"
)

data class ProteinCalculationResult(
    val weightKg: Double,
    val goal: FitnessGoal,
    val trainingStyle: TrainingStyle,
    val gender: Gender,
    val age: Int,
    val heightCm: Double,
    
    // Protein results
    val proteinGramsRecommended: Int,
    val proteinGramsMin: Int,
    val proteinGramsMax: Int,
    val proteinPerKg: Double,
    
    // Total Calories & Macros
    val bmrCalories: Int,
    val tdeeCalories: Int,
    val targetCalories: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val fiberGrams: Int,
    val waterMl: Int,
    
    // Meals breakdown
    val perMeal3: Int,
    val perMeal4: Int,
    val perMeal5: Int,
    
    // Suggestions
    val foodExamples: List<ProteinFoodExample>,
    val tips: List<String>
)

object ProteinCalculatorEngine {

    val SAMPLE_FOOD_ITEMS = listOf(
        ProteinFoodExample("BioTech 100% Pure Whey", "1 adagolókanál (28g)", 22.0, 108, "🥛", "Kiegészítő"),
        ProteinFoodExample("Scitec 100% Whey Protein", "1 adag (30g)", 22.0, 112, "🥤", "Kiegészítő"),
        ProteinFoodExample("OstroVit WPC80 Tejsavó", "1 adag (30g)", 23.0, 115, "⚡", "Kiegészítő"),
        ProteinFoodExample("Csirkemellfilé (sütve)", "200 g", 46.0, 220, "🍗", "Étel"),
        ProteinFoodExample("Zsírszegény Tehéntúró", "250 g (1 doboz)", 35.0, 185, "🧀", "Étel"),
        ProteinFoodExample("Tonhalkonzerv sós lében", "1 konzerv (130g)", 31.2, 138, "🐟", "Étel"),
        ProteinFoodExample("Egész Tojás (főtt/sült)", "3 db (közepes)", 19.5, 210, "🥚", "Étel"),
        ProteinFoodExample("Sovány Darált Marhahús", "150 g", 33.0, 210, "🥩", "Étel"),
        ProteinFoodExample("Görög Joghurt / Skyr", "200 g", 20.0, 130, "🥣", "Étel"),
        ProteinFoodExample("Zabpehely tejjel / vízzel", "80 g + 200ml", 14.5, 340, "🌾", "Étel"),
        ProteinFoodExample("BioTech Micellar Casein", "1 adag (30g)", 22.0, 105, "🌙", "Kiegészítő"),
        ProteinFoodExample("Földimogyoróvaj (100%)", "35 g (2 ek)", 10.0, 210, "🥜", "Étel")
    )

    fun calculate(
        weightKg: Double,
        goal: FitnessGoal,
        trainingStyle: TrainingStyle,
        gender: Gender = Gender.MALE,
        age: Int = 28,
        heightCm: Double = 178.0
    ): ProteinCalculationResult {
        val safeWeight = weightKg.coerceIn(35.0, 220.0)
        val safeHeight = heightCm.coerceIn(120.0, 230.0)
        val safeAge = age.coerceIn(14, 95)

        // BMR (Mifflin - St Jeor)
        val bmr = if (gender == Gender.MALE) {
            (10.0 * safeWeight) + (6.25 * safeHeight) - (5.0 * safeAge) + 5.0
        } else {
            (10.0 * safeWeight) + (6.25 * safeHeight) - (5.0 * safeAge) - 161.0
        }

        val tdee = bmr * trainingStyle.tdeeMultiplier
        val targetCalRaw = tdee * (1.0 + goal.calorieAdjustmentPercent)
        val minCalorieFloor = if (gender == Gender.MALE) 1500 else 1200
        val targetCalories = targetCalRaw.toInt().coerceAtLeast(minCalorieFloor)

        // Protein calculation
        val effectivePerKg = (goal.recommendedProteinPerKg + trainingStyle.proteinMultiplierBonus).coerceIn(1.2, 2.7)
        val proteinRecommended = (safeWeight * effectivePerKg).toInt().coerceIn(50, 320)
        val proteinMin = (safeWeight * goal.proteinPerKgMin).toInt().coerceIn(45, 280)
        val proteinMax = (safeWeight * (goal.proteinPerKgMax + trainingStyle.proteinMultiplierBonus)).toInt().coerceIn(proteinRecommended, 350)

        // Fat calculation (~0.8-1.0g per kg or 25% of cal)
        val fatGrams = when (goal) {
            FitnessGoal.FAT_LOSS -> (safeWeight * 0.75).toInt().coerceIn(35, 90)
            FitnessGoal.BULKING -> (safeWeight * 1.0).toInt().coerceIn(50, 120)
            FitnessGoal.RECOMPOSITION -> (safeWeight * 0.85).toInt().coerceIn(40, 100)
            FitnessGoal.MAINTENANCE -> (safeWeight * 0.9).toInt().coerceIn(45, 110)
        }

        // Carbs: remaining calories
        val proteinCal = proteinRecommended * 4
        val fatCal = fatGrams * 9
        val remainingCal = (targetCalories - proteinCal - fatCal).coerceAtLeast(200)
        val carbsGrams = (remainingCal / 4.0).toInt().coerceIn(50, 600)

        val fiberGrams = ((targetCalories / 1000.0) * 14.0).toInt().coerceIn(25, 50)
        val waterMl = ((safeWeight * 35) + if (trainingStyle != TrainingStyle.SEDENTARY) 600 else 0).toInt().coerceIn(2000, 4500)

        val perMeal3 = (proteinRecommended / 3.0).toInt()
        val perMeal4 = (proteinRecommended / 4.0).toInt()
        val perMeal5 = (proteinRecommended / 5.0).toInt()

        val tips = when (goal) {
            FitnessGoal.FAT_LOSS -> listOf(
                "🛡️ Izomvédelem kalóriadeficitben: Fogyáskor a szervezet hajlamos izmot lebontani. A napi ${proteinRecommended}g fehérje garantálja a feszes, tónusos izomzat megőrzését.",
                "🔥 Termikus hatás (TEF): A fehérjék emésztése energiát igényel: a bevitt fehérjekalóriák 20-30%-át a szervezeted maga az emésztés során égeti el!",
                "🍽️ Teltségérzet: A magas fehérjetartalmú ételek elnyújtják az emésztést és elnyomják az éhséghormonokat (grelin), így könnyebb tartani a deficitet.",
                "⚡ Időzítés: Edzés után 1 órán belül fogyassz el egy tejsavófehérje turmixot vagy zsírszegény fehérjés ételt."
            )
            FitnessGoal.BULKING -> listOf(
                "🏗️ Maximális Izomfehérje-szintézis: Napi ${proteinRecommended}g (${String.format("%.1f", effectivePerKg)} g/kg) fehérje bőségesen kimaxolja az anabolikus növekedést.",
                "🍚 Szénhidrát mint üzemanyag: Ne vigyél be feleslegesen túlzott fehérjét (pl. 3g/kg felesleges). A többlet kalóriát rizs, zab és burgonya formájában vidd be a maximális edzésteljesítményért.",
                "💪 Leucin-küszöb: Minden főétkezésed tartalmazzon legalább 30-40g komplett fehérjét, hogy folyamatosan aktív maradjon az mTOR jelátvitel.",
                "🌙 Éjszakai védelem: Lefekvés előtt egy adag kazein vagy 200g túró biztosítja az aminosavakat az éjszakai 8 órás növekedéshez."
            )
            FitnessGoal.RECOMPOSITION -> listOf(
                "⚡ Zsírégetés & Izomépítés egyszerre: A fenntartó/enyhe deficit kalóriák melletti magas (${proteinRecommended}g) fehérjebevitel lehetővé teszi, hogy a tested a zsírraktárakból fedezze az izomépítés energiáját.",
                "🏋️ Progresszív túlterhelés elengedhetetlen: Minden héten törekedj több ismétlésre a fekvőtámaszokban, húzódzkodásokban vagy nagyobb súlyokra.",
                "⏱️ 4-5 egyenletes étkezés: Oszd el a fehérjédet ~${perMeal4}g-os adagokra a nap során a stabil anabolikus környezetért."
            )
            FitnessGoal.MAINTENANCE -> listOf(
                "⚖️ Egyensúly és vitalitás: Napi ${proteinRecommended}g fehérje tökéletesen fenntartja a meglévő izomtömeget és vitalitást.",
                "💧 Ne feledkezz meg a hidratációról: A napi ajánlott folyadékbeviteled kb. ${waterMl / 1000.0} liter víz.",
                "🥗 Változatos fehérjeforrások: Kombinálj csirkét, halat, tojást, tejtermékeket és növényi fehérjéket (lencse, zab)."
            )
        }

        return ProteinCalculationResult(
            weightKg = safeWeight,
            goal = goal,
            trainingStyle = trainingStyle,
            gender = gender,
            age = safeAge,
            heightCm = safeHeight,
            proteinGramsRecommended = proteinRecommended,
            proteinGramsMin = proteinMin,
            proteinGramsMax = proteinMax,
            proteinPerKg = effectivePerKg,
            bmrCalories = bmr.toInt(),
            tdeeCalories = tdee.toInt(),
            targetCalories = targetCalories,
            carbsGrams = carbsGrams,
            fatGrams = fatGrams,
            fiberGrams = fiberGrams,
            waterMl = waterMl,
            perMeal3 = perMeal3,
            perMeal4 = perMeal4,
            perMeal5 = perMeal5,
            foodExamples = SAMPLE_FOOD_ITEMS,
            tips = tips
        )
    }
}
