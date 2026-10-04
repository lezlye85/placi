package com.example.data.local

import com.example.data.model.CostLevel
import com.example.data.model.DayMealPlan
import com.example.data.model.FitnessChallenge
import com.example.data.model.LeaderboardUser
import com.example.data.model.MacroTrendItem
import com.example.data.model.MealType
import com.example.data.model.MonthlyStatsSummary
import com.example.data.model.PostCategory
import com.example.data.model.PostComment
import com.example.data.model.CommunityPost
import com.example.data.model.Recipe
import com.example.data.model.RecipeDietaryTag
import com.example.data.model.RecipeIngredient
import com.example.data.model.ShoppingItem
import com.example.data.model.VirtualBadge
import com.example.data.model.WeeklyMealPlan
import com.example.data.model.WorkoutFrequencyItem

object RecipeAndCommunityData {

    // ==========================================
    // RECEPT ADATBÁZIS (Kiemelten olcsó & magas fehérjetartalmú)
    // ==========================================
    val RECIPES: List<Recipe> = listOf(
        // REGELIK
        Recipe(
            id = "rec_oatmeal_protein",
            titleHu = "Fehérjés Zabkása Almával & Fahéjjal",
            subtitleHu = "Ultra laktató, olcsó és 5 perc alatt kész",
            mealType = MealType.BREAKFAST,
            prepTimeMinutes = 5,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 280,
            calories = 385.0,
            protein = 28.0,
            carbs = 54.0,
            fat = 6.0,
            fiber = 8.5,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.FIBER_RICH),
            ingredients = listOf(
                RecipeIngredient("Zabpehely", "60g", 60, 225.0, 8.0, 39.0, 4.0),
                RecipeIngredient("Tejsavó fehérjepor (vagy túró)", "25g", 140, 95.0, 19.0, 1.5, 1.0),
                RecipeIngredient("Alma (reszelve)", "1 db közepes", 70, 60.0, 0.5, 14.0, 0.2),
                RecipeIngredient("Fahéj & víz", "ízlés szerint", 10, 5.0, 0.5, 0.5, 0.1)
            ),
            instructionsHu = listOf(
                "A zabpelyhet forró vízzel vagy tejjel leöntjük és 2 percig állni hagyjuk.",
                "Hozzákeverjük a fehérjeport (vagy zsírszegény túrót) és a reszelt almát.",
                "Fahéjjal megszórva melegen fogyasztjuk."
            ),
            emoji = "🥣",
            budgetTip = "A zabpehely kilója kb. 500-600 Ft, a legolcsóbb komplex szénhidrátforrás."
        ),
        Recipe(
            id = "rec_scramble_turo",
            titleHu = "Túrós-Tojásos Rántotta Teljes Kiőrlésű Kenyérrel",
            subtitleHu = "Dupla fehérje bomba minimális költségből",
            mealType = MealType.BREAKFAST,
            prepTimeMinutes = 8,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 390,
            calories = 440.0,
            protein = 36.0,
            carbs = 26.0,
            fat = 18.0,
            fiber = 4.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.QUICK_MEAL),
            ingredients = listOf(
                RecipeIngredient("Tojás (M-es)", "2 db", 160, 140.0, 13.0, 1.0, 10.0),
                RecipeIngredient("Zsírszegény tehéntúró", "100g", 140, 85.0, 16.0, 3.5, 0.5),
                RecipeIngredient("Teljes kiőrlésű kenyér", "1 szelet (50g)", 60, 120.0, 4.5, 21.0, 1.5),
                RecipeIngredient("Újhagyma vagy paprika", "fél db", 30, 15.0, 0.5, 3.0, 0.2)
            ),
            instructionsHu = listOf(
                "A tojásokat villával felverjük a túróval és fűszerekkel (só, bors).",
                "Kevés olajon serpenyőben krémesre sütjük.",
                "Pirított teljes kiőrlésű kenyérrel és friss zöldséggel tálaljuk."
            ),
            emoji = "🍳",
            budgetTip = "A zsírszegény túró tojással keverve hihetetlenül krémessé teszi a rántottát, miközben 36g tiszta fehérjét ad alig 400 Ft-ból."
        ),
        Recipe(
            id = "rec_cottage_pancake",
            titleHu = "3 Hozzávalós Fitnesz Túrópalacsinta",
            subtitleHu = "Cukormentes, lisztmentes diétás finomság",
            mealType = MealType.BREAKFAST,
            prepTimeMinutes = 10,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 350,
            calories = 360.0,
            protein = 32.0,
            carbs = 30.0,
            fat = 9.0,
            fiber = 4.5,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Zsírszegény túró", "150g", 210, 128.0, 24.0, 5.0, 1.0),
                RecipeIngredient("Tojás", "1 db", 80, 70.0, 6.5, 0.5, 5.0),
                RecipeIngredient("Zabpehely (darált)", "40g", 40, 150.0, 5.5, 26.0, 2.5),
                RecipeIngredient("Édesítő & Fahéj", "ízlés szerint", 20, 5.0, 0.1, 0.5, 0.0)
            ),
            instructionsHu = listOf(
                "Minden hozzávalót villával vagy botmixerrel simára keverünk.",
                "Tapadásmentes serpenyőben kis adagokban mindkét oldalát 2-2 percig sütjük.",
                "Friss citromhéjjal vagy fahéjjal ízesítjük."
            ),
            emoji = "🥞",
            budgetTip = "Liszt helyett simán használj aprószemű zabpelyhet, sokkal tovább laktat."
        ),

        // EBÉDEK
        Recipe(
            id = "rec_budget_chicken_rice",
            titleHu = "Költségkímélő Fűszeres Csirke Rizses Zöldségágyon",
            subtitleHu = "A klasszikus testépítő & kalisztenika alap ebéd",
            mealType = MealType.LUNCH,
            prepTimeMinutes = 20,
            servings = 1,
            costLevel = CostLevel.BUDGET,
            estimatedCostHuf = 580,
            calories = 520.0,
            protein = 48.0,
            carbs = 58.0,
            fat = 9.0,
            fiber = 6.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER),
            ingredients = listOf(
                RecipeIngredient("Csirkemell filé", "170g", 380, 195.0, 42.0, 0.0, 2.5),
                RecipeIngredient("Jázmin vagy barna rizs", "70g (száraz)", 90, 250.0, 5.0, 55.0, 1.0),
                RecipeIngredient("Fagyasztott vegyes zöldség", "120g", 90, 60.0, 2.5, 11.0, 0.5),
                RecipeIngredient("Olívaolaj / fűszerek", "1 tk", 20, 40.0, 0.0, 0.0, 4.5)
            ),
            instructionsHu = listOf(
                "A rizst kétszeres mennyiségű enyhén sós vízben puhára főzzük.",
                "A csirkemellet csíkokra vágjuk, magyaros vagy zöldfűszeres pácban serpenyőben pirítjuk.",
                "Hozzáadjuk a zöldségeket és 5 perc alatt összeforgatjuk."
            ),
            emoji = "🍗",
            budgetTip = "Vegyél egész csirkemellet vagy akciós védőgázas kiszerelést, a fagyasztott zöldség pedig egész évben a legolcsóbb vitaminforrás."
        ),
        Recipe(
            id = "rec_lentil_egg_stew",
            titleHu = "Fehérjedús Magyaros Lencsefőzelék Főtt Tojással",
            subtitleHu = "Rendkívül gazdaságos, rostbomba növényi fehérjével",
            mealType = MealType.LUNCH,
            prepTimeMinutes = 25,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 320,
            calories = 490.0,
            protein = 32.0,
            carbs = 62.0,
            fat = 11.0,
            fiber = 14.0,
            tags = listOf(RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.FIBER_RICH, RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Száraz lencse", "80g", 120, 280.0, 20.0, 48.0, 1.0),
                RecipeIngredient("Tojás", "2 db", 160, 140.0, 13.0, 1.0, 10.0),
                RecipeIngredient("Babérlevél, fokhagyma, mustár", "-", 30, 25.0, 1.0, 4.0, 0.5),
                RecipeIngredient("Kevés zsírszegény tejföl", "1 ek", 30, 30.0, 0.8, 1.2, 2.5)
            ),
            instructionsHu = listOf(
                "A lencsét babérlevéllel és fokhagymával puhára főzzük (kb. 20 perc).",
                "Mustárral, ecettel és kevés zsírszegény tejföllel sűrítjük saját magával turmixolva (lisztmentes).",
                "Két db lágy vagy kemény főtt tojással tálaljuk."
            ),
            emoji = "🍲",
            budgetTip = "A száraz lencse 100g-ja alig 120-150 Ft, de 25g fehérjét és rengeteg rostot tartalmaz!"
        ),
        Recipe(
            id = "rec_tuna_pasta",
            titleHu = "Mediterrán Tonhalas Paradicsomos Tészta",
            subtitleHu = "12 perces gyors ebéd zsírszegény szafttal",
            mealType = MealType.LUNCH,
            prepTimeMinutes = 12,
            servings = 1,
            costLevel = CostLevel.BUDGET,
            estimatedCostHuf = 540,
            calories = 510.0,
            protein = 38.0,
            carbs = 68.0,
            fat = 6.5,
            fiber = 5.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.BUDGET_SAVER),
            ingredients = listOf(
                RecipeIngredient("Durum tészta", "80g", 70, 280.0, 10.0, 58.0, 1.5),
                RecipeIngredient("Tonhalkonzerv sós lében", "1 doboz (112g)", 340, 115.0, 26.0, 0.0, 1.0),
                RecipeIngredient("Passzírozott paradicsom (Passata)", "150g", 90, 45.0, 2.0, 9.0, 0.2),
                RecipeIngredient("Oregánó, bazsalikom, fokhagyma", "-", 20, 15.0, 0.5, 3.0, 0.1)
            ),
            instructionsHu = listOf(
                "A durum tésztát sós forrásban lévő vízben al dente-re főzzük.",
                "A paradicsompürét fokhagymával és zöldfűszerekkel 3 percig forraljuk, hozzáadjuk a lecsöpögtetett tonhalat.",
                "A tésztát a mártással összeforgatjuk."
            ),
            emoji = "🍝",
            budgetTip = "A saját márkás sós levestalpú tonhalkonzervek kiválóak és feleannyiba kerülnek mint a márkásak."
        ),

        // VACSORÁK
        Recipe(
            id = "rec_cottage_veggie_bowl",
            titleHu = "Fűszeres Körözöttes Túrótál Friss Zöldségekkel",
            subtitleHu = "Könnyű, alacsony szénhidráttartalmú esti fehérje",
            mealType = MealType.DINNER,
            prepTimeMinutes = 6,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 340,
            calories = 310.0,
            protein = 34.0,
            carbs = 18.0,
            fat = 7.0,
            fiber = 5.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.LOW_CARB, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Zsírszegény tehéntúró", "200g", 240, 170.0, 32.0, 7.0, 1.0),
                RecipeIngredient("Fűszerpaprika, kömény, lilahagyma", "-", 40, 30.0, 1.0, 5.0, 0.3),
                RecipeIngredient("Kígyóuborka & Kaliforniai paprika", "150g", 80, 40.0, 1.5, 8.0, 0.4),
                RecipeIngredient("Puffasztott rizs", "2 szelet", 20, 60.0, 1.2, 13.0, 0.5)
            ),
            instructionsHu = listOf(
                "A túrót fűszerpaprikával, őrölt köménnyel és apróra vágott lilahagymával villával összekeverjük.",
                "Uborka- és paprikahasábokkal, valamint 2 szelet puffasztott rizzsel mártogatva tálaljuk."
            ),
            emoji = "🥗",
            budgetTip = "A túró kazein fehérjét tartalmaz, ami lassan szívódik fel az éjszaka folyamán, védve az izomtömeget."
        ),
        Recipe(
            id = "rec_turkey_sweet_potato",
            titleHu = "Darált Pulykás Zöldségragu Sült Burgonyával",
            subtitleHu = "Kiadós, ízletes és pénztárcakímélő vacsora",
            mealType = MealType.DINNER,
            prepTimeMinutes = 20,
            servings = 1,
            costLevel = CostLevel.BUDGET,
            estimatedCostHuf = 560,
            calories = 460.0,
            protein = 38.0,
            carbs = 44.0,
            fat = 12.0,
            fiber = 6.5,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER),
            ingredients = listOf(
                RecipeIngredient("Sovány darált pulykacomb vagy csirkemell", "150g", 350, 180.0, 32.0, 0.0, 6.0),
                RecipeIngredient("Burgonya", "150g", 60, 115.0, 3.0, 26.0, 0.2),
                RecipeIngredient("Cukkini & sárgarépa", "120g", 90, 45.0, 2.0, 9.0, 0.3),
                RecipeIngredient("Fűszerek & 1 tk olaj", "-", 30, 45.0, 0.2, 1.0, 4.5)
            ),
            instructionsHu = listOf(
                "A burgonyát kockákra vágva sütőben vagy airfryerben ropogósra sütjük.",
                "A darált húst serpenyőben lepirítjuk a zöldségekkel és fokhagymás fűszerezéssel.",
                "Összeforgatva frissen fogyasztjuk."
            ),
            emoji = "🥔",
            budgetTip = "A sima burgonya kiváló laktató indexű, káliumban gazdag és jóval olcsóbb az édesburgonyánál."
        ),
        Recipe(
            id = "rec_egg_baked_beans",
            titleHu = "Serpenyős Angol Bab Tükörtojással",
            subtitleHu = "Fehérjedús, rostos gyors melegvacsora",
            mealType = MealType.DINNER,
            prepTimeMinutes = 10,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 360,
            calories = 410.0,
            protein = 26.0,
            carbs = 48.0,
            fat = 11.0,
            fiber = 11.0,
            tags = listOf(RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.FIBER_RICH, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Fehérbab paradicsomos szószban (konzerv)", "200g", 180, 180.0, 9.5, 32.0, 1.0),
                RecipeIngredient("Tojás", "2 db", 160, 140.0, 13.0, 1.0, 10.0),
                RecipeIngredient("Teljes kiőrlésű pirítós", "1 szelet", 40, 85.0, 3.5, 15.0, 1.0)
            ),
            instructionsHu = listOf(
                "A paradicsomos babot serpenyőbe öntjük és felmelegítjük.",
                "Két mélyedést készítünk és beleütjük a tojásokat, fedő alatt 4-5 percig pároljuk.",
                "Pirítóssal tálaljuk."
            ),
            emoji = "🫘",
            budgetTip = "A babkonzervek hihetetlenül gazdagok rostban és növényi fehérjében, kiváló laktató hatásúak."
        ),

        // NASIK / UZSONNÁK
        Recipe(
            id = "rec_snack_greek_yogurt_berries",
            titleHu = "Görög Joghurtos Mogyoróvajas Krémtál",
            subtitleHu = "15 másodperces fitnesz desszert",
            mealType = MealType.SNACK,
            prepTimeMinutes = 2,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 220,
            calories = 210.0,
            protein = 18.0,
            carbs = 14.0,
            fat = 7.5,
            fiber = 2.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Zsírszegény görög joghurt", "150g", 140, 90.0, 15.0, 6.0, 0.5),
                RecipeIngredient("Mogyoróvaj (100%-os)", "1 teáskanál (12g)", 60, 75.0, 3.0, 2.0, 6.5),
                RecipeIngredient("Fahéj / Édesítő", "-", 20, 5.0, 0.0, 1.0, 0.0)
            ),
            instructionsHu = listOf(
                "A görög joghurtot simára keverjük egy kanál mogyoróvajjal és édesítővel.",
                "Hidegen kanalazzuk."
            ),
            emoji = "🍦",
            budgetTip = "A görög joghurt krémes állagú, magas fehérjetartalmú és remek nassolási vágy ellen."
        ),
        Recipe(
            id = "rec_snack_boiled_eggs_apple",
            titleHu = "Főtt Tojás Ropogós Almával & Dióval",
            subtitleHu = "Praktikus, dobozolható edzés előtti/utáni snack",
            mealType = MealType.SNACK,
            prepTimeMinutes = 10,
            servings = 1,
            costLevel = CostLevel.ULTRA_CHEAP,
            estimatedCostHuf = 190,
            calories = 190.0,
            protein = 14.0,
            carbs = 16.0,
            fat = 8.0,
            fiber = 3.0,
            tags = listOf(RecipeDietaryTag.HIGH_PROTEIN, RecipeDietaryTag.BUDGET_SAVER, RecipeDietaryTag.QUICK_MEAL, RecipeDietaryTag.VEGETARIAN),
            ingredients = listOf(
                RecipeIngredient("Főtt tojás", "2 db", 140, 140.0, 13.0, 1.0, 10.0),
                RecipeIngredient("Alma", "1 db", 60, 50.0, 0.5, 12.0, 0.2)
            ),
            instructionsHu = listOf(
                "A tojásokat keményre főzzük, megpucoljuk és felszeletelt almával csomagoljuk."
            ),
            emoji = "🥚",
            budgetTip = "Főzz meg előre 6-8 db tojást a hűtőbe, napokig eláll megbízható snackként."
        )
    )

    // ==========================================
    // SZEMÉLYRE SZABOTT 1 HETES OLCSÓ ÉTLAP GENERÁTOR
    // ==========================================
    fun generateWeeklyMealPlan(targetDailyCalories: Int, goalTag: String = "Költségkímélő Zsírégetés"): WeeklyMealPlan {
        val daysList = listOf(
            DayMealPlan(
                dayNameHu = "Hétfő",
                dayNumber = 1,
                breakfast = RECIPES[0], // Fehérjés Zabkása Almával (385 kcal, 280 Ft)
                lunch = RECIPES[3],     // Csirke Rizses Zöldségágyon (520 kcal, 580 Ft)
                dinner = RECIPES[6],    // Körözöttes Túrótál (310 kcal, 340 Ft)
                snack = RECIPES[9]      // Görög Joghurtos Mogyoróvaj (210 kcal, 220 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Kedd",
                dayNumber = 2,
                breakfast = RECIPES[1], // Túrós Rántotta (440 kcal, 390 Ft)
                lunch = RECIPES[4],     // Lencsefőzelék Főtt Tojással (490 kcal, 320 Ft)
                dinner = RECIPES[7],    // Darált Pulykás Sült Burgonya (460 kcal, 560 Ft)
                snack = RECIPES[10]     // Főtt Tojás Almával (190 kcal, 190 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Szerda",
                dayNumber = 3,
                breakfast = RECIPES[2], // Fitnesz Túrópalacsinta (360 kcal, 350 Ft)
                lunch = RECIPES[5],     // Mediterrán Tonhalas Tészta (510 kcal, 540 Ft)
                dinner = RECIPES[8],    // Serpenyős Angol Bab (410 kcal, 360 Ft)
                snack = RECIPES[9]      // Görög Joghurtos Mogyoróvaj (210 kcal, 220 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Csütörtök",
                dayNumber = 4,
                breakfast = RECIPES[0], // Fehérjés Zabkása (385 kcal, 280 Ft)
                lunch = RECIPES[3],     // Csirke Rizses Zöldségágyon (520 kcal, 580 Ft)
                dinner = RECIPES[6],    // Körözöttes Túrótál (310 kcal, 340 Ft)
                snack = RECIPES[10]     // Főtt Tojás Almával (190 kcal, 190 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Péntek",
                dayNumber = 5,
                breakfast = RECIPES[1], // Túrós Rántotta (440 kcal, 390 Ft)
                lunch = RECIPES[4],     // Lencsefőzelék Főtt Tojással (490 kcal, 320 Ft)
                dinner = RECIPES[7],    // Darált Pulykás Sült Burgonya (460 kcal, 560 Ft)
                snack = RECIPES[9]      // Görög Joghurtos Mogyoróvaj (210 kcal, 220 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Szombat",
                dayNumber = 6,
                breakfast = RECIPES[2], // Túrópalacsinta (360 kcal, 350 Ft)
                lunch = RECIPES[5],     // Tonhalas Tészta (510 kcal, 540 Ft)
                dinner = RECIPES[8],    // Serpenyős Bab (410 kcal, 360 Ft)
                snack = RECIPES[10]     // Főtt Tojás Almával (190 kcal, 190 Ft)
            ),
            DayMealPlan(
                dayNameHu = "Vasárnap",
                dayNumber = 7,
                breakfast = RECIPES[0], // Fehérjés Zabkása (385 kcal, 280 Ft)
                lunch = RECIPES[3],     // Csirke Rizses Zöldségágyon (520 kcal, 580 Ft)
                dinner = RECIPES[6],    // Körözöttes Túrótál (310 kcal, 340 Ft)
                snack = RECIPES[9]      // Görög Joghurtos Mogyoróvaj (210 kcal, 220 Ft)
            )
        )

        val totalWeeklyCost = daysList.sumOf { it.totalCostHuf }
        val dailyAvgCost = totalWeeklyCost / 7
        val dailyAvgCal = daysList.map { it.totalCalories.toInt() }.average().toInt()

        return WeeklyMealPlan(
            titleHu = "1 Hetes Ultra-Olcsó Diák & Sportoló Étlap",
            goalTagHu = goalTag,
            dailyAvgCalories = dailyAvgCal,
            dailyAvgCostHuf = dailyAvgCost,
            totalWeeklyCostHuf = totalWeeklyCost,
            days = daysList
        )
    }

    val INITIAL_SHOPPING_LIST: List<ShoppingItem> = listOf(
        ShoppingItem("shop_1", "Zabpehely (1 kg)", "1 csomag", "Gabonák", 580),
        ShoppingItem("shop_2", "Zsírszegény tehéntúró (500g)", "2 doboz", "Tejtermék", 1180),
        ShoppingItem("shop_3", "Friss tojás (10 db-os)", "2 doboz", "Tojás", 1580),
        ShoppingItem("shop_4", "Csirkemell filé (1 kg)", "1 kg", "Hús", 2290),
        ShoppingItem("shop_5", "Tonhalkonzerv sós lében", "2 db", "Konzerv", 680),
        ShoppingItem("shop_6", "Száraz lencse (500g)", "1 csomag", "Hüvelyesek", 450),
        ShoppingItem("shop_7", "Fehérbab paradicsomszószban", "2 konzerv", "Konzerv", 560),
        ShoppingItem("shop_8", "Jázmin rizs & Durum tészta", "1-1 csomag", "Gabonák", 890),
        ShoppingItem("shop_9", "Fagyasztott vegyes zöldség (1 kg)", "1 csomag", "Fagyasztott", 790),
        ShoppingItem("shop_10", "Burgonya & Alma & Zöldségek", "1-1 kg", "Zöldség/Gyümölcs", 1150)
    )

    // ==========================================
    // KÖZÖSSÉGI KEZDŐ POSZTOK & INTERAKCIÓK
    // ==========================================
    val INITIAL_COMMUNITY_POSTS: List<CommunityPost> = listOf(
        CommunityPost(
            id = "post_1",
            authorName = "Kovács Balázs",
            authorBadge = "Kalisztenika Bajnok",
            authorAvatarEmoji = "🏋️‍♂️",
            timeAgoHu = "2 órája",
            category = PostCategory.WORKOUT_WIN,
            title = "Megvan az 5x10 tiszta húzódzkodás!",
            content = "3 hónapja 2 darabot se tudtam szabályosan megcsinálni. A napi rutinok és a kalóriadeficit miatt lement 6 kg, most sokkal könnyebb a saját testsúlyos edzés. Ne adjátok fel srácok!",
            statsTag = "💪 40 perc Saját testsúlyos edzés (-340 kcal)",
            likesCount = 24,
            isLikedByMe = false,
            commentsCount = 4,
            cheersCount = 12,
            comments = listOf(
                PostComment("c1", "Szabó Tamás", "🔥", "Hatalmas fejlődés, gratulálok testvér!", "1 órája"),
                PostComment("c2", "Horváth Anna", "👏", "Nagyon inspiráló, nekem a fekvőtámasz még nehezen megy de nyomom!", "45 perce")
            )
        ),
        CommunityPost(
            id = "post_2",
            authorName = "Nagy Dániel",
            authorBadge = "Budget Séf",
            authorAvatarEmoji = "👨‍🍳",
            timeAgoHu = "5 órája",
            category = PostCategory.MEAL_SHARE,
            title = "Napi 1500 Ft-os menü 140g fehérjével kipróbálva!",
            content = "A recept fülön lévő lencsefőzeléket és a túrós rántottát dobozoltam mára. Hihetetlenül finom, laktató és fillérekbe kerül. Mellé 3 liter víz megvolt.",
            statsTag = "🥗 1850 kcal • F: 142g • Sz: 180g • Zs: 55g",
            likesCount = 38,
            isLikedByMe = true,
            commentsCount = 6,
            cheersCount = 19,
            comments = listOf(
                PostComment("c3", "Varga Gábor", "🥗", "A túrós rántotta nálam is sláger lett, imádom.", "3 órája")
            )
        ),
        CommunityPost(
            id = "post_3",
            authorName = "Tóth Máté",
            authorBadge = "Transzformáció",
            authorAvatarEmoji = "⭐",
            timeAgoHu = "1 napja",
            category = PostCategory.TRANSFORMATION,
            title = "-8.5 kg az elmúlt 10 hétben! 📉",
            content = "88 kg-ról indultam, ma reggel 79.5 kg-ot mértem a mérlegen. Napi kalória kontroll + 4 heti kalisztenika edzés a szobámban. Semmi kondibérlet, csak kitartás!",
            statsTag = "📉 88.0 kg ➔ 79.5 kg (-8.5 kg)",
            likesCount = 64,
            isLikedByMe = false,
            commentsCount = 9,
            cheersCount = 31,
            comments = listOf(
                PostComment("c4", "Molnár Zoltán", "👑", "Brutális eredmény! Csak így tovább!", "18 órája")
            )
        ),
        CommunityPost(
            id = "post_4",
            authorName = "Kiss Petra",
            authorBadge = "Víz Harcos",
            authorAvatarEmoji = "💧",
            timeAgoHu = "2 napja",
            category = PostCategory.TIPS_MOTIVATION,
            title = "A vízivás a legnagyobb csodafegyver a sóvárgás ellen!",
            content = "Régen azt hittem éhes vagyok, pedig csak szomjas voltam. Amióta minden étkezés előtt megiszom fél liter hideg vizet, eltűnt a délutáni nasizási kényszer.",
            statsTag = "💧 3.0 L Víz / nap (12 pohár)",
            likesCount = 19,
            isLikedByMe = false,
            commentsCount = 2,
            cheersCount = 8,
            comments = emptyList()
        )
    )

    // ==========================================
    // HAVI FITNESZ KIHÍVÁS
    // ==========================================
    val CURRENT_CHALLENGE = FitnessChallenge(
        id = "chal_aug_2026",
        titleHu = "30 Napos Kalisztenika & Kalória Fegyelem",
        monthHu = "2026. Augusztus - Szeptember",
        descriptionHu = "Tartsuk a kalóriadeficitet, igyunk naponta legalább 2.5 liter vizet, és teljesítsünk heti 4 saját testsúlyos edzést a havi bajnoki díjért!",
        targetDays = 30,
        completedDays = 18,
        rewardBadgeName = "30 Napos Bajnok",
        rewardBadgeEmoji = "🏆",
        dailyTasksHu = listOf(
            "Napi kalóriacél betartása (±50 kcal)",
            "Legalább 2.5 L tiszta víz fogyasztása",
            "1 kalisztenika rutin vagy 50 fekvőtámasz",
            "Minimum 100g fehérjebevitel"
        ),
        pointsReward = 500,
        isJoined = true
    )

    // ==========================================
    // HETI RANGLISTA
    // ==========================================
    val LEADERBOARD_USERS: List<LeaderboardUser> = listOf(
        LeaderboardUser(1, "Kovács Balázs", "🥇", "Kalisztenika Mester", 1480, 24, false, "Mai edzés: 45p Haladó húzódzkodás"),
        LeaderboardUser(2, "Nagy Dániel", "🥈", "Diéta Bajnok", 1320, 19, false, "Naplózva: 4 étkezés (100% célban)"),
        LeaderboardUser(3, "Tóth Máté", "🥉", "Szálkásító Harcos", 1190, 15, false, "Mai edzés: 30p HIIT & Plank"),
        LeaderboardUser(4, "Felhasználó (Te)", "💪", "Elkötelezett Sportoló", 1050, 12, true, "Napi célok 100%-ban teljesítve!"),
        LeaderboardUser(5, "Szabó Tamás", "⚡", "Kezdő Bajnok", 920, 8, false, "Vízbevitel: 3.0 L teljesítve"),
        LeaderboardUser(6, "Kiss Petra", "💧", "Víz Királynő", 840, 7, false, "Recept elkészítve: Zabkása"),
        LeaderboardUser(7, "Horváth Anna", "🥗", "Egészség Újonc", 760, 5, false, "Mai mérés rögzítve: -0.4 kg")
    )

    // ==========================================
    // VIRTUÁLIS DÍJAK & TRÓFEÁK
    // ==========================================
    val VIRTUAL_BADGES: List<VirtualBadge> = listOf(
        VirtualBadge(
            id = "badge_streak_7",
            titleHu = "7 Napos Fegyelem",
            descriptionHu = "7 egymást követő napon át rögzítetted az összes étkezésedet.",
            emoji = "🔥",
            isUnlocked = true,
            unlockedDateHu = "2026. augusztus 18.",
            progressCurrent = 7,
            progressMax = 7,
            rarityHu = "Közönséges"
        ),
        VirtualBadge(
            id = "badge_calisthenics_warrior",
            titleHu = "Kalisztenika Harcos",
            descriptionHu = "Teljesíts legalább 10 saját testsúlyos edzéssorozatot.",
            emoji = "💪",
            isUnlocked = true,
            unlockedDateHu = "2026. augusztus 22.",
            progressCurrent = 10,
            progressMax = 10,
            rarityHu = "Ritka"
        ),
        VirtualBadge(
            id = "badge_water_master",
            titleHu = "Vízbajnok",
            descriptionHu = "Érd el a napi 2.5 literes vízcélodat 10 különböző napon.",
            emoji = "💧",
            isUnlocked = true,
            unlockedDateHu = "2026. augusztus 24.",
            progressCurrent = 10,
            progressMax = 10,
            rarityHu = "Közönséges"
        ),
        VirtualBadge(
            id = "badge_budget_chef",
            titleHu = "Költséghatékony Séf",
            descriptionHu = "Készíts el és naplózz 5 olcsó fitnesz receptet a menüből.",
            emoji = "🍳",
            isUnlocked = true,
            unlockedDateHu = "2026. augusztus 25.",
            progressCurrent = 5,
            progressMax = 5,
            rarityHu = "Ritka"
        ),
        VirtualBadge(
            id = "badge_macro_sniper",
            titleHu = "Makró Célmester",
            descriptionHu = "Tartsd a napi fehérje- és kalóriacélodat 14 napon át.",
            emoji = "🎯",
            isUnlocked = false,
            unlockedDateHu = null,
            progressCurrent = 11,
            progressMax = 14,
            rarityHu = "Epikus"
        ),
        VirtualBadge(
            id = "badge_podium_finish",
            titleHu = "Ranglista Dobogós",
            descriptionHu = "Kerülj be a heti ranglista legjobb 3 helyezettje közé.",
            emoji = "👑",
            isUnlocked = false,
            unlockedDateHu = null,
            progressCurrent = 4,
            progressMax = 3,
            rarityHu = "Epikus"
        ),
        VirtualBadge(
            id = "badge_challenge_champion",
            titleHu = "30 Napos Bajnok",
            descriptionHu = "Fejezd be sikeresen a havi 30 napos fitnesz kihívást.",
            emoji = "🏆",
            isUnlocked = false,
            unlockedDateHu = null,
            progressCurrent = 18,
            progressMax = 30,
            rarityHu = "Legendás"
        )
    )

    // ==========================================
    // MULTI-MONTH STATISZTIKAI ADATOK (Június, Július, Augusztus 2026)
    // ==========================================
    val MONTHLY_SUMMARIES: List<MonthlyStatsSummary> = listOf(
        MonthlyStatsSummary(
            monthYearHu = "2026. Augusztus (Jelenlegi)",
            totalCaloriesBurned = 7850,
            avgDailyConsumedCalories = 1920,
            avgDailyDeficitCalories = 480,
            totalWorkoutsCount = 18,
            totalWorkoutMinutes = 620,
            avgDailyProteinGrams = 148.5,
            avgDailyWaterMl = 2750,
            estimatedFatLostKg = 2.1,
            startingWeightKg = 84.1,
            endingWeightKg = 82.0,
            bestStreakDays = 12,
            consistencyScorePercent = 92
        ),
        MonthlyStatsSummary(
            monthYearHu = "2026. Július",
            totalCaloriesBurned = 8400,
            avgDailyConsumedCalories = 1960,
            avgDailyDeficitCalories = 450,
            totalWorkoutsCount = 19,
            totalWorkoutMinutes = 660,
            avgDailyProteinGrams = 142.0,
            avgDailyWaterMl = 2600,
            estimatedFatLostKg = 1.9,
            startingWeightKg = 86.0,
            endingWeightKg = 84.1,
            bestStreakDays = 14,
            consistencyScorePercent = 88
        ),
        MonthlyStatsSummary(
            monthYearHu = "2026. Június",
            totalCaloriesBurned = 6900,
            avgDailyConsumedCalories = 2050,
            avgDailyDeficitCalories = 380,
            totalWorkoutsCount = 15,
            totalWorkoutMinutes = 520,
            avgDailyProteinGrams = 135.0,
            avgDailyWaterMl = 2400,
            estimatedFatLostKg = 1.6,
            startingWeightKg = 87.6,
            endingWeightKg = 86.0,
            bestStreakDays = 9,
            consistencyScorePercent = 81
        )
    )

    val MACRO_TRENDS: List<MacroTrendItem> = listOf(
        MacroTrendItem("Június", avgProteinGrams = 135.0, avgCarbsGrams = 220.0, avgFatGrams = 65.0, avgTotalCalories = 2050.0),
        MacroTrendItem("Július", avgProteinGrams = 142.0, avgCarbsGrams = 205.0, avgFatGrams = 62.0, avgTotalCalories = 1960.0),
        MacroTrendItem("Augusztus", avgProteinGrams = 148.5, avgCarbsGrams = 190.0, avgFatGrams = 58.0, avgTotalCalories = 1920.0)
    )

    val WORKOUT_FREQUENCY_TRENDS: List<WorkoutFrequencyItem> = listOf(
        WorkoutFrequencyItem("Május", sessionCount = 11, totalMinutes = 380, targetSessions = 16),
        WorkoutFrequencyItem("Június", sessionCount = 15, totalMinutes = 520, targetSessions = 16),
        WorkoutFrequencyItem("Július", sessionCount = 19, totalMinutes = 660, targetSessions = 16),
        WorkoutFrequencyItem("Augusztus", sessionCount = 18, totalMinutes = 620, targetSessions = 16)
    )
}
