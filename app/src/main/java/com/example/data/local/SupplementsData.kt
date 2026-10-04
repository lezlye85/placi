package com.example.data.local

import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.SupplementCategory
import com.example.data.model.SupplementGoal
import com.example.data.model.SupplementItem
import com.example.data.model.SupplementTiming
import com.example.data.model.UserDailySupplement

object SupplementsData {

    val SUPPLEMENTS: List<SupplementItem> = listOf(
        // =========================================================================
        // 1. FEHÉRJÉK (PROTEINS)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_pure_whey",
            name = "100% Pure Whey",
            brand = "BioTechUSA",
            barcode = "5999076228300",
            category = SupplementCategory.PROTEIN,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.FAT_LOSS, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.POST_WORKOUT,
            timingDescriptionHu = "Edzés után 20-30 percen belül a gyors izomregenerációért, vagy reggeli zabkásához/nasi helyett fehérje dúsításra.",
            recommendedDosageHu = "1 adagolókanál (28g) felrázva 250 ml hideg vízben vagy sovány tejben. Napi 1-2 adag.",
            activeIngredientsHu = "78% Tejsavófehérje komplex (koncentrátum + izolátum), hozzáadott BCAA (L-leucin, L-izoleucin, L-valin), L-glutamin és L-arginin. Gluténmentes.",
            whyTakeItHu = "A tejsavófehérje a legmagasabb biológiai értékű fehérjeforrás. Gyorsan felszívódik, megállítja az edzés utáni izomleépülést (katabolizmust), és elősegíti a száraz izomtömeg épülését és a regenerációt.",
            stackingTipsHu = "Keverj hozzá 5g kreatin-monohidrátot és 30-40g gyors szénhidrátot (pl. dextróz vagy banán) edzés után a glikogénraktárak maximális visszatöltéséhez.",
            caloriesPerServing = 104.0,
            proteinPerServing = 21.0,
            carbsPerServing = 1.4,
            fatPerServing = 1.1,
            servingGrams = 28.0,
            servingUnitHu = "1 adagolókanál (28g)",
            badgeTagHu = "⭐ Bestseller"
        ),
        SupplementItem(
            id = "supp_scitec_whey_pro",
            name = "100% Whey Protein Professional",
            brand = "Scitec Nutrition",
            barcode = "5996655100018",
            category = SupplementCategory.PROTEIN,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.FAT_LOSS, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.POST_WORKOUT,
            timingDescriptionHu = "Közvetlenül edzés után azonnal, illetve pihenőnapokon délelőtt vagy délutáni étkezés kiegészítéseként.",
            recommendedDosageHu = "1 adagolókanál (30g) elkeverve 250-300 ml vízben. Napi 1-2 alkalommal.",
            activeIngredientsHu = "Tejsavófehérje koncentrátum + izolátum, Extra aminosav mátrix (Taurin, L-glutamin, L-leucin), Papain és Bromelain emésztőenzimek a tökéletes gyomoremésztésért.",
            whyTakeItHu = "Ikonikus, kiváló ízvilágú fehérje formula. A hozzáadott emésztőenzimek megszüntetik a puffadást és maximalizálják az aminosavak véráramba jutását.",
            stackingTipsHu = "Reggel zabpehellyel keverve komplett anabolikus reggeli, edzés után pedig 5g Scitec Glutaminnal kombinálva a legjobb izomvédő.",
            caloriesPerServing = 112.0,
            proteinPerServing = 22.0,
            carbsPerServing = 1.4,
            fatPerServing = 2.0,
            servingGrams = 30.0,
            servingUnitHu = "1 adagolókanál (30g)",
            badgeTagHu = "🏆 Legnépszerűbb"
        ),
        SupplementItem(
            id = "supp_biotech_iso_whey_zero",
            name = "Iso Whey Zero (Cukor- és Laktózmentes)",
            brand = "BioTechUSA",
            barcode = "5999076211234",
            category = SupplementCategory.PROTEIN,
            targetGoals = listOf(SupplementGoal.FAT_LOSS, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.POST_WORKOUT,
            timingDescriptionHu = "Edzés után azonnal vagy szigorú szálkásító diéta alatt étkezések közötti fehérjepótlásként.",
            recommendedDosageHu = "1 adagolókanál (25g) 200 ml hideg vízben elkeverve shékerben.",
            activeIngredientsHu = "Prémium Native Whey Isolate (Cross-Flow mikroszűrt tejsavó izolátum), zéró cukor, zéró laktóz, gluténmentes.",
            whyTakeItHu = "A legtisztább tejsavó-izolátum forma. Érzékeny gyomrúaknak és laktózérzékenyeknek a legjobb választás. Nem vizesít, ultra alacsony kalóriatartalmú.",
            stackingTipsHu = "Diétában kombináld L-Karnitinnel vagy Super Burnerrel a zsírégetés maximalizálásához.",
            caloriesPerServing = 93.0,
            proteinPerServing = 21.0,
            carbsPerServing = 0.72,
            fatPerServing = 0.65,
            servingGrams = 25.0,
            servingUnitHu = "1 adagolókanál (25g)",
            badgeTagHu = "🔬 Prémium Tisztaság"
        ),
        SupplementItem(
            id = "supp_ostrovit_whey",
            name = "OstroVit 100% Whey Protein",
            brand = "OstroVit",
            barcode = "5902232617894",
            category = SupplementCategory.PROTEIN,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.POST_WORKOUT,
            timingDescriptionHu = "Edzés után 30 percen belül vagy fehérjedús étkezések kiegészítésére.",
            recommendedDosageHu = "30g (kb. 2.5 adagolókanál) 200 ml vízben vagy tejben.",
            activeIngredientsHu = "100% Whey Protein Concentrate (WPC), gazdag természetes BCAA-ban.",
            whyTakeItHu = "Piacvezető ár/érték arányú, megbízható európai laboratóriumban bevizsgált fehérje felesleges adalékok nélkül.",
            stackingTipsHu = "Kitűnő alap fehérjepor turmixokba, zabkásába és fehérjés palacsintába.",
            caloriesPerServing = 111.0,
            proteinPerServing = 21.0,
            carbsPerServing = 1.7,
            fatPerServing = 2.0,
            servingGrams = 30.0,
            servingUnitHu = "1 adag (30g)",
            badgeTagHu = "💰 Legjobb Ár/Érték"
        ),
        SupplementItem(
            id = "supp_biotech_micellar_casein",
            name = "Micellar Casein (Éjszakai Fehérje)",
            brand = "BioTechUSA",
            barcode = "5999076239999",
            category = SupplementCategory.PROTEIN,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.SLEEP_STRESS),
            primaryTiming = SupplementTiming.BEFORE_BED,
            timingDescriptionHu = "Közvetlenül lefekvés előtt 30-45 perccel, vagy hosszú étkezésmentes időszakok előtt.",
            recommendedDosageHu = "1 adagolókanál (30g) sűrűre kikeverve 300 ml vízben vagy mandulatejben (pudingos állagú).",
            activeIngredientsHu = "100% Micelláris Kazein micelláris szerkezettel, lassan bomló fehérjemolekulák, kalcium.",
            whyTakeItHu = "A kazein 6-8 órán keresztül folyamatosan bocsátja ki az aminosavakat a véráramba, így megelőzi az éjszakai izomleépülést alvás közben.",
            stackingTipsHu = "Kombináld ZMB (Cink-Magnézium-B6) formulával és Ashwagandhával a mély alvásért és az éjszakai tesztoszteron termelésért.",
            caloriesPerServing = 105.0,
            proteinPerServing = 22.0,
            carbsPerServing = 1.5,
            fatPerServing = 0.9,
            servingGrams = 30.0,
            servingUnitHu = "1 adag (30g)",
            badgeTagHu = "🌙 Éjszakai Regeneráció"
        ),

        // =========================================================================
        // 2. KREATIN (CREATINE)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_creatine_mono",
            name = "100% Micronized Creatine Monohydrate",
            brand = "BioTechUSA",
            barcode = "5999076214567",
            category = SupplementCategory.CREATINE,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.DAILY_ANYTIME,
            timingDescriptionHu = "Napi 1 alkalommal bármikor (pl. reggel éhgyomorra vagy edzés utáni fehérjeturmixba keverve). A rendszeres napi bevitel a legfontosabb!",
            recommendedDosageHu = "1 adagolókanál / csapott teáskanál (3.4g - 5g) 300-500 ml folyadékkal bevéve.",
            activeIngredientsHu = "100% tiszta, mikronizált (200 mesh) gyógyszerészeti tisztaságú kreatin-monohidrát.",
            whyTakeItHu = "A legtöbbet kutatott, bizonyítottan hatékony legális teljesítményfokozó. Növeli az izomsejtek ATP (azonnali energia) szintjét, 10-15%-kal növeli az erőszintet és a robbanékonyságot, sejtvolumenizáló hatású.",
            stackingTipsHu = "Edzés után keverd bele a fehérjeturmixodba és fogyassz mellé elegendő vizet (+5 dl plusz folyadék naponta).",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 5.0,
            servingUnitHu = "1 adag (5g / 1 teáskanál)",
            badgeTagHu = "⚡ Bajnok Erőfokozó"
        ),
        SupplementItem(
            id = "supp_scitec_creatine_mono",
            name = "100% Creatine Monohydrate",
            brand = "Scitec Nutrition",
            barcode = "5996655100100",
            category = SupplementCategory.CREATINE,
            targetGoals = listOf(SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.DAILY_ANYTIME,
            timingDescriptionHu = "Edzésnapokon edzés után közvetlenül a shake-be, pihenőnapokon reggel éhgyomorra egy pohár vízzel.",
            recommendedDosageHu = "3.4g - 5g naponta, folyamatos szedéssel (feltöltési fázis nem kötelező).",
            activeIngredientsHu = "Ultra tiszta kreatin-monohidrát mikronizált formában.",
            whyTakeItHu = "Fokozza a fizikai teljesítményt a rövid, sorozatos, nagy intenzitású gyakorlatok során (fekvőtámasz, guggolás, robbanékony ugrások).",
            stackingTipsHu = "Kombináld EAA aminosavakkal és Béta-alaninnal az állóképesség maximalizálásához.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 5.0,
            servingUnitHu = "1 adag (5g)",
            badgeTagHu = "🏆 Klasszikus Erő"
        ),
        SupplementItem(
            id = "supp_ostrovit_creatine",
            name = "OstroVit Creatine Monohydrate",
            brand = "OstroVit",
            barcode = "5902232610116",
            category = SupplementCategory.CREATINE,
            targetGoals = listOf(SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.DAILY_ANYTIME,
            timingDescriptionHu = "Napi 1 alkalommal tetszőleges időpontban, bőséges folyadék kíséretében.",
            recommendedDosageHu = "3-5g naponta (kb. egy csapott mérőkanál).",
            activeIngredientsHu = "100% tiszta mikronizált kreatin monohidrát, ízesítetlen és gyümölcsös változatban.",
            whyTakeItHu = "Prémium tisztaság és kiváló oldódás verhetetlen áron. Növeli az izomsejtek hidratáltságát és az anaerob erőt.",
            stackingTipsHu = "Napi 3-4 liter víz fogyasztása ajánlott kreatin szedése mellett a legjobb izomhidratációért.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 5.0,
            servingUnitHu = "1 adag (5g)",
            badgeTagHu = "💰 Legjobb Ár"
        ),

        // =========================================================================
        // 3. VITAMINOK & ÁSVÁNYI ANYAGOK (VITAMINS & MINERALS)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_one_a_day",
            name = "One-A-Day Multivitamin",
            brand = "BioTechUSA",
            barcode = "5999076200001",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.MUSCLE_BUILDING, SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.WITH_BREAKFAST,
            timingDescriptionHu = "Reggel, a reggeli étkezés közben vagy közvetlenül utána, bő folyadékkal.",
            recommendedDosageHu = "Napi 1 tabletta reggeli étkezéssel.",
            activeIngredientsHu = "12-féle vitamin (A, C, D3, E, B1, B2, B3, B5, B6, B12, Folsav, Biotin) + 10-féle létfontosságú ásványi anyag (Cink, Magnézium, Vas, Szelén, Mangán, Jód, Réz, Króm).",
            whyTakeItHu = "A sportolók és aktív életet élők szervezete fokozottan használja a mikrotápanyagokat. Fedezni kell a napi szükségletet az immunrendszer, anyagcsere és hormonális egyensúly fenntartásához.",
            stackingTipsHu = "Egészítsd ki extra D3+K2 vitaminnal és Omega-3 halolajjal a teljes mikrotápanyag védelemért.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 1.5,
            servingUnitHu = "1 tabletta",
            badgeTagHu = "🛡️ Napi Alap Multivitamin"
        ),
        SupplementItem(
            id = "supp_ostrovit_d3_k2",
            name = "OstroVit Vitamin D3 4000 IU + K2 (MK-7)",
            brand = "OstroVit",
            barcode = "5902232619003",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.JOINTS_MOBILITY, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.WITH_BREAKFAST,
            timingDescriptionHu = "Reggeli zsírtartalmú étkezés közben (pl. tojásos reggeli vagy olajos magvak mellett a zsírban oldódás miatt).",
            recommendedDosageHu = "1 tabletta naponta (vagy 2 naponta 1 tabletta 2000 IU átlaghoz) étkezés közben.",
            activeIngredientsHu = "4000 NE természetes D3-vitamin (kolekalciferol) + 100 mcg K2-vitamin (MK-7 natto eredetű, legjobban felszívódó forma).",
            whyTakeItHu = "A D-vitamin elengedhetetlen a tesztoszteronszint, az izomerő és az immunrendszer működéséhez. A K2-vitamin gondoskodik róla, hogy a kalcium a csontokba és fogakba épüljön be, és ne az erek falában rakódjon le.",
            stackingTipsHu = "Mindig étkezéssel vedd be! Zsírsavak (Omega-3) jelenlétében sokszorosára nő a felszívódása.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 0.5,
            servingUnitHu = "1 tabletta",
            badgeTagHu = "⭐ Elengedhetetlen Alap"
        ),
        SupplementItem(
            id = "supp_biotech_c1000",
            name = "Vitamin C 1000 mg + Rose Hips",
            brand = "BioTechUSA",
            barcode = "5999076200555",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.JOINTS_MOBILITY),
            primaryTiming = SupplementTiming.WITH_BREAKFAST,
            timingDescriptionHu = "Reggeli vagy déli étkezés után egy pohár vízzel.",
            recommendedDosageHu = "Napi 1 tabletta.",
            activeIngredientsHu = "1000 mg L-aszkorbinsav + Természetes csipkebogyó por és bioflavonoidok a nyújtott felszívódásért.",
            whyTakeItHu = "Erős antioxidáns, serkenti a szervezet saját kollagéntermelését (ami elengedhetetlen az ízületek és inak épségéhez), csökkenti a fáradtságot és a kortizolszintet.",
            stackingTipsHu = "Szedd együtt kollagénnel az ízületek és porcok maximális megújulásához.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 1.2,
            servingUnitHu = "1 tabletta",
            badgeTagHu = "🍊 Immunerő"
        ),
        SupplementItem(
            id = "supp_biotech_magnesium_bisglycinate",
            name = "Magnesium Bisglycinate (Szerves Magnézium)",
            brand = "BioTechUSA",
            barcode = "5999076229991",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.SLEEP_STRESS, SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.HEALTH_IMMUNITY),
            primaryTiming = SupplementTiming.BEFORE_BED,
            timingDescriptionHu = "Este, lefekvés előtt 30-60 perccel vagy nehéz láb-/hátedzés után az izomgörcsök megelőzésére.",
            recommendedDosageHu = "Napi 1-2 kapszula egy pohár vízzel.",
            activeIngredientsHu = "100% Kelátkötésű Magnézium-biszglicinát (a legmagasabb biohasznosulású, gyomorkímélő szerves forma).",
            whyTakeItHu = "A szervetlen magnézium-oxidokkal szemben a biszglicinát nem hajtja meg a hasat, közvetlenül az idegrendszerbe és izmokba jut. Ellazítja az izmokat, megszünteti a görcsöket és mélyíti az alvási fázisokat.",
            stackingTipsHu = "Tökéletes esti párosítás Cinkkel és B6-vitaminnal (ZMB formula).",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 1.0,
            servingUnitHu = "1-2 kapszula",
            badgeTagHu = "🧠 Pihentető Alvás"
        ),
        SupplementItem(
            id = "supp_scitec_omega3",
            name = "Omega 3 Halolaj (EPA + DHA)",
            brand = "Scitec Nutrition",
            barcode = "5996655100333",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.JOINTS_MOBILITY, SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.WITH_MEAL,
            timingDescriptionHu = "Ebéd vagy vacsora közben, étkezéssel együtt bevéve.",
            recommendedDosageHu = "Napi 2 lágyzselatin kapszula bő folyadékkal.",
            activeIngredientsHu = "1000 mg tiszta tengeri halolaj koncentrátum kapszulánként: EPA (eikozapentaénsav) + DHA (dokozahexaénsav).",
            whyTakeItHu = "Csökkenti a szervezetben lévő gyulladásokat, védi a szív- és érrendszert, javítja az inzulinérzékenységet (elősegíti a zsírégetést és tápanyag-hasznosulást) és keni az ízületeket.",
            stackingTipsHu = "Szedd D3+K2 vitaminnal együtt étkezéskor a maximális szinergia érdekében.",
            caloriesPerServing = 18.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 2.0,
            servingGrams = 2.0,
            servingUnitHu = "2 kapszula",
            badgeTagHu = "🐟 Szív & Ízületvédelem"
        ),
        SupplementItem(
            id = "supp_ostrovit_zinc",
            name = "OstroVit Zinc Picolinate 15mg",
            brand = "OstroVit",
            barcode = "5902232614442",
            category = SupplementCategory.VITAMINS_MINERALS,
            targetGoals = listOf(SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.BEFORE_BED,
            timingDescriptionHu = "Este étkezés után vagy lefekvés előtt.",
            recommendedDosageHu = "Napi 1 tabletta.",
            activeIngredientsHu = "15 mg szerves Cink-pikolinát (kiváló felszívódású kelát).",
            whyTakeItHu = "A cink kulcsszerepet játszik a normál tesztoszteronszint fenntartásában, a fehérjeszintézisben és a bőr/haj egészségében.",
            stackingTipsHu = "Ne szedd egyszerre nagy dózisú kalciummal, mert gátolhatják egymás felszívódását.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 0.3,
            servingUnitHu = "1 tabletta",
            badgeTagHu = "🛡️ Férfierő & Immunitás"
        ),

        // =========================================================================
        // 4. ÍZÜLETVÉDŐK & KOLLAGÉN (JOINT HEALTH)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_arthro_guard",
            name = "Arthro Guard (17 Hatóanyagos Ízületvédő)",
            brand = "BioTechUSA",
            barcode = "5999076208888",
            category = SupplementCategory.JOINT_HEALTH,
            targetGoals = listOf(SupplementGoal.JOINTS_MOBILITY, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.WITH_MEAL,
            timingDescriptionHu = "Főétkezés közben (pl. ebéd) bő vízzel bevéve.",
            recommendedDosageHu = "Napi 3 tabletta főétkezés közben.",
            activeIngredientsHu = "Glükozamin-szulfát (800mg), Metil-szulfonil-metán (MSM 400mg), Kondroitin-szulfát (300mg), Hidrolizált kollagén (160mg), Boswellia serrata (tömjénfa kivonat), Kurkuma kivonat, C-vitamin, Kalcium, Mangán.",
            whyTakeItHu = "Kalisztenika és intenzív edzés mellett a könyök, csukló, váll és térd inai fokozott terhelésnek vannak kitéve. Az Arthro Guard serkenti az ízületi folyadék termelését, csökkenti a gyulladást és regenerálja a porcokat.",
            stackingTipsHu = "Kombináld C-vitaminnal és Omega-3-mal a gyulladáscsökkentő hatás felerősítésére.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 3.5,
            servingUnitHu = "3 tabletta",
            badgeTagHu = "🦴 Professzionális Ízületvédelem"
        ),
        SupplementItem(
            id = "supp_scitec_joint_x",
            name = "Joint-X Ízületvédő Komplex",
            brand = "Scitec Nutrition",
            barcode = "5996655100444",
            category = SupplementCategory.JOINT_HEALTH,
            targetGoals = listOf(SupplementGoal.JOINTS_MOBILITY),
            primaryTiming = SupplementTiming.WITH_MEAL,
            timingDescriptionHu = "Napi étkezésekkel elosztva (pl. 2 reggel, 2 este).",
            recommendedDosageHu = "Napi 4 kapszula étkezésekkel elosztva.",
            activeIngredientsHu = "Glükozamin-szulfát, Kondroitin-szulfát, MSM (Metil-szulfonil-metán), Zselatin.",
            whyTakeItHu = "Klasszikus 4 komponensű ízületi mátrix az ízületi porcok mechanikai védelmére és a fájdalommentes mozgástartomány megőrzésére.",
            stackingTipsHu = "Nehéz guggolások és fekvőtámasz programok alatt folyamatos 2-3 hónapos kúrában javasolt szedni.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 2.8,
            servingUnitHu = "4 kapszula",
            badgeTagHu = "🦴 Megbízható Ízületvédelem"
        ),
        SupplementItem(
            id = "supp_ostrovit_collagen",
            name = "OstroVit Collagen + Vitamin C",
            brand = "OstroVit",
            barcode = "5902232615555",
            category = SupplementCategory.JOINT_HEALTH,
            targetGoals = listOf(SupplementGoal.JOINTS_MOBILITY, SupplementGoal.HEALTH_IMMUNITY),
            primaryTiming = SupplementTiming.MORNING_FASTED,
            timingDescriptionHu = "Reggel éhgyomorra vagy edzés előtt 45 perccel vízben feloldva.",
            recommendedDosageHu = "10g (kb. 2 adagolókanál) feloldva 200 ml vízben vagy gyümölcslében.",
            activeIngredientsHu = "Hidrolizált marhakollagén peptidek (8766 mg) + 500 mg C-vitamin a kollagén beépüléséhez.",
            whyTakeItHu = "A kollagén peptidek közvetlen építőkövei az inaknak, szalagoknak, ízületi porcoknak és a bőrnek. A hozzáadott C-vitamin nélkül a kollagén nem képes beépülni.",
            stackingTipsHu = "Finom gyümölcsös ízekben elérhető, reggeli rituáléként vagy edzés előtti italként is kitűnő.",
            caloriesPerServing = 35.0,
            proteinPerServing = 8.8,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 10.0,
            servingUnitHu = "1 adag (10g por)",
            badgeTagHu = "💧 Tiszta Kollagén"
        ),

        // =========================================================================
        // 5. EDZÉS ELŐTTI PÖRGETŐK (PRE-WORKOUT)
        // =========================================================================
        SupplementItem(
            id = "supp_scitec_hot_blood",
            name = "Hot Blood Hardcore",
            brand = "Scitec Nutrition",
            barcode = "5996655100777",
            category = SupplementCategory.PRE_WORKOUT,
            targetGoals = listOf(SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.PRE_WORKOUT,
            timingDescriptionHu = "Edzés előtt pontosan 25-35 perccel, lehetőleg nem teljesen teli hassal.",
            recommendedDosageHu = "1 adagolókanál (12.5g - 25g) 300-400 ml hideg vízben feloldva.",
            activeIngredientsHu = "300 mg Koffein mátrix, L-Citrullin-malát, Béta-alanin (hangyamászás érzés), Kreatin mátrix, L-arginin, Nootropikumok (L-tirozin, Ginkgo biloba), Elektrolitok.",
            whyTakeItHu = "Brutális bedurranást (nitrogén-monoxid fokozás), lézerfókuszt, robbanékony erőt és fáradtságkitolást biztosít a legkeményebb börtön vagy katonai edzésekhez.",
            stackingTipsHu = "Késő este (lefekvés előtt 5-6 órán belül) ne fogyaszd a magas koffeintartalom miatt! Bőséges vízfogyasztás javasolt mellé.",
            caloriesPerServing = 30.0,
            proteinPerServing = 0.0,
            carbsPerServing = 3.0,
            fatPerServing = 0.0,
            servingGrams = 25.0,
            servingUnitHu = "1 adag (25g)",
            badgeTagHu = "🔥 Extrém Fókusz & Erő"
        ),
        SupplementItem(
            id = "supp_biotech_black_blood",
            name = "Black Blood NOX+",
            brand = "BioTechUSA",
            barcode = "5999076206666",
            category = SupplementCategory.PRE_WORKOUT,
            targetGoals = listOf(SupplementGoal.STRENGTH_ENDURANCE, SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.PRE_WORKOUT,
            timingDescriptionHu = "Edzés előtt 20 perccel meginni hideg vízzel rázva.",
            recommendedDosageHu = "1 adagolókanál (19g) 300 ml hideg vízben.",
            activeIngredientsHu = "10.500 mg NOX Komplex (L-Citrullin malát, AAKG, Béta-alanin), 300 mg Vízmentes Koffein, Kreatin komplex, Feketebors kivonat (BioPerine). Zéró cukor!",
            whyTakeItHu = "Erőteljes bedurranást ad az izmoknak, tágítja az ereket és maximális oxigénellátást biztosít a dolgozó izomcsoportoknak.",
            stackingTipsHu = "Kezdőknek fél adaggal érdemes kezdeni a tolerancia felmérésére.",
            caloriesPerServing = 22.0,
            proteinPerServing = 0.0,
            carbsPerServing = 1.0,
            fatPerServing = 0.0,
            servingGrams = 19.0,
            servingUnitHu = "1 adag (19g)",
            badgeTagHu = "⚡ Brutális Bedurranás"
        ),

        // =========================================================================
        // 6. AMINOSAVAK & REGENERÁCIÓ (AMINO ACIDS)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_bcaa_glutamine",
            name = "BCAA + Glutamine Zero",
            brand = "BioTechUSA",
            barcode = "5999076207777",
            category = SupplementCategory.AMINO_ACIDS,
            targetGoals = listOf(SupplementGoal.FAT_LOSS, SupplementGoal.MUSCLE_BUILDING, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.INTRA_WORKOUT,
            timingDescriptionHu = "Edzés közben a kulacsba keverve folyamatosan kortyolgatva, vagy reggel éhgyomros kardió előtt.",
            recommendedDosageHu = "1 adagolókanál (12g) 400-600 ml hideg vízben feloldva.",
            activeIngredientsHu = "5000 mg BCAA (2:1:1 L-leucin, L-izoleucin, L-valin) + 5000 mg tiszta L-Glutamin. 0g cukor, 0g szénhidrát.",
            whyTakeItHu = "Megakadályozza, hogy a szervezet az izomszövetet bontsa le energiáért a kemény, hosszan tartó edzések során. Csökkenti az izomlázat és gyorsítja a regenerációt.",
            stackingTipsHu = "Hosszú köredzéseknél tegyél mellé egy csipet sót vagy elektrolit port a görcsök ellen.",
            caloriesPerServing = 35.0,
            proteinPerServing = 8.5,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 12.0,
            servingUnitHu = "1 adag (12g)",
            badgeTagHu = "🥤 Edzés Közbeni Frissítő"
        ),
        SupplementItem(
            id = "supp_scitec_bcaa_xpress",
            name = "BCAA Xpress 100% Free Form",
            brand = "Scitec Nutrition",
            barcode = "5996655100888",
            category = SupplementCategory.AMINO_ACIDS,
            targetGoals = listOf(SupplementGoal.MUSCLE_BUILDING, SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.INTRA_WORKOUT,
            timingDescriptionHu = "Edzés közben vagy étkezések között folyadékpótlásként.",
            recommendedDosageHu = "7g por 350 ml vízben elkeverve.",
            activeIngredientsHu = "5000 mg tiszta szabad formájú BCAA aminosav 2:1:1 arányban (L-leucin, L-izoleucin, L-valin).",
            whyTakeItHu = "Közvetlenül az izmokban metabolizálódik (nem a májban), azonnali védőpajzsot biztosít az izomrostoknak.",
            stackingTipsHu = "Edzés után azonnal tegyél 5g-ot a tejsavó fehérjédbe az aminosav profil felturbózására.",
            caloriesPerServing = 20.0,
            proteinPerServing = 5.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 7.0,
            servingUnitHu = "1 adag (7g)",
            badgeTagHu = "🧬 Tiszta Izomvédelem"
        ),

        // =========================================================================
        // 7. ZSÍRÉGETŐK & ANYAGCSERE (FAT BURNERS)
        // =========================================================================
        SupplementItem(
            id = "supp_biotech_super_burner",
            name = "Super Burner (Komplex Zsírégető)",
            brand = "BioTechUSA",
            barcode = "5999076201111",
            category = SupplementCategory.FAT_BURNERS,
            targetGoals = listOf(SupplementGoal.FAT_LOSS),
            primaryTiming = SupplementTiming.PRE_WORKOUT,
            timingDescriptionHu = "Edzésnapokon edzés előtt 30 perccel, pihenőnapokon 2 tabletta reggel és 2 tabletta délután étkezés előtt.",
            recommendedDosageHu = "Napi 4 tabletta (2 délelőtt, 2 délután / edzés előtt).",
            activeIngredientsHu = "13 aktív hatóanyag: L-Karnitin, Króm (normalizálja a vércukorszintet és megszünteti a cukor utáni vágyat), CLA, Garcinia Cambogia (HCA), Zöld tea kivonat, Cink, B6-vitamin. Koffeinmentes!",
            whyTakeItHu = "Lipotróp és anyagcsere-serkentő formula koffein nélkül, így este edzők és vérnyomás-érzékenyek is biztonsággal szedhetik. Támogatja a zsírsavak mitokondriumba történő szállítását.",
            stackingTipsHu = "Kombináld Iso Whey Zero fehérjével és kalóriadeficittel a látványos szálkásodásért.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 4.0,
            servingUnitHu = "4 tabletta",
            badgeTagHu = "🔥 Koffeinmentes Zsírégető"
        ),
        SupplementItem(
            id = "supp_biotech_lcarnitine_liquid",
            name = "L-Carnitine 100.000 Liquid",
            brand = "BioTechUSA",
            barcode = "5999076202222",
            category = SupplementCategory.FAT_BURNERS,
            targetGoals = listOf(SupplementGoal.FAT_LOSS, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.PRE_WORKOUT,
            timingDescriptionHu = "Edzés (különösen kardió, HIIT vagy saját testsúlyos köredzés) előtt 30 perccel meginni egy adagot.",
            recommendedDosageHu = "1 adag (10ml kupak) hígítatlanul vagy 200 ml vízben feloldva.",
            activeIngredientsHu = "2000 mg tiszta L-karnitin bázis adagonként + Zöld tea kivonat, Inulin és B-vitamin komplex.",
            whyTakeItHu = "Az L-karnitin a szervezet zsírszállító molekulája: a raktározott zsírsavakat közvetlenül a sejt erőműveibe (mitokondriumokba) szállítja, ahol energiává égnek el.",
            stackingTipsHu = "A leghatékonyabb aerob és HIIT pulzustartományban végzett edzések előtt fogyasztva.",
            caloriesPerServing = 8.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.2,
            fatPerServing = 0.0,
            servingGrams = 10.0,
            servingUnitHu = "1 kupak (10ml)",
            badgeTagHu = "⚡ Folyékony Zsírégetés"
        ),

        // =========================================================================
        // 8. ALVÁS & STRESSZKEZELÉS (RECOVERY & SLEEP)
        // =========================================================================
        SupplementItem(
            id = "supp_ostrovit_ashwagandha",
            name = "OstroVit Ashwagandha KSM-66",
            brand = "OstroVit",
            barcode = "5902232618888",
            category = SupplementCategory.RECOVERY_SLEEP,
            targetGoals = listOf(SupplementGoal.SLEEP_STRESS, SupplementGoal.HEALTH_IMMUNITY, SupplementGoal.MUSCLE_BUILDING),
            primaryTiming = SupplementTiming.BEFORE_BED,
            timingDescriptionHu = "Lefekvés előtt 45 perccel vagy a nap legstresszesebb időszakában.",
            recommendedDosageHu = "1 tabletta naponta egy pohár vízzel.",
            activeIngredientsHu = "200 mg KSM-66 Ashwagandha gyökérkivonat (standardizált 5% withanolid tartalommal - a világ legjobban kutatott adaptogénje).",
            whyTakeItHu = "Csökkenti a kortizol (stresszhormon) szintet, mérsékli a szorongást, optimalizálja a természetes tesztoszteronszintet és javítja a mélyalvás (REM/Deep) minőségét.",
            stackingTipsHu = "Kombináld Magnézium-biszglicináttal és Cinkkel az igazi mély, pihentető regenerációért.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 0.4,
            servingUnitHu = "1 tabletta",
            badgeTagHu = "🧘 Prémium Adaptogén"
        ),
        SupplementItem(
            id = "supp_scitec_zmb6",
            name = "ZMB6 (Cink + Magnézium + B6)",
            brand = "Scitec Nutrition",
            barcode = "5996655100999",
            category = SupplementCategory.RECOVERY_SLEEP,
            targetGoals = listOf(SupplementGoal.SLEEP_STRESS, SupplementGoal.MUSCLE_BUILDING, SupplementGoal.STRENGTH_ENDURANCE),
            primaryTiming = SupplementTiming.BEFORE_BED,
            timingDescriptionHu = "Este, lefekvés előtt 30 perccel, éhgyomorra (ne tejtermékkel!).",
            recommendedDosageHu = "Napi 2 kapszula lefekvés előtt vízzel.",
            activeIngredientsHu = "Cink-aszpartát (15mg), Magnézium-oxid (240mg), B6-vitamin (piridoxin 5.6mg).",
            whyTakeItHu = "A ZMA formula természetes módon serkenti az éjszakai növekedési hormon (GH) és tesztoszteron kibocsátást, megakadályozza az éjszakai izomgörcsöket.",
            stackingTipsHu = "Ne fogyassz mellé magas kalciumtartalmú ételt vagy tejet, mert gátolja a cink felszívódását.",
            caloriesPerServing = 0.0,
            proteinPerServing = 0.0,
            carbsPerServing = 0.0,
            fatPerServing = 0.0,
            servingGrams = 1.2,
            servingUnitHu = "2 kapszula",
            badgeTagHu = "🌙 Éjszakai Anabolizmus"
        )
    )

    // Pre-configured default user stacks based on goals
    fun getRecommendedStack(goal: SupplementGoal): List<SupplementItem> {
        return when (goal) {
            SupplementGoal.MUSCLE_BUILDING -> SUPPLEMENTS.filter {
                it.id in listOf("supp_biotech_pure_whey", "supp_biotech_creatine_mono", "supp_biotech_one_a_day", "supp_ostrovit_d3_k2", "supp_biotech_magnesium_bisglycinate")
            }
            SupplementGoal.FAT_LOSS -> SUPPLEMENTS.filter {
                it.id in listOf("supp_biotech_iso_whey_zero", "supp_biotech_super_burner", "supp_biotech_lcarnitine_liquid", "supp_scitec_omega3", "supp_biotech_one_a_day")
            }
            SupplementGoal.STRENGTH_ENDURANCE -> SUPPLEMENTS.filter {
                it.id in listOf("supp_scitec_creatine_mono", "supp_scitec_hot_blood", "supp_biotech_bcaa_glutamine", "supp_biotech_pure_whey", "supp_biotech_magnesium_bisglycinate")
            }
            SupplementGoal.JOINTS_MOBILITY -> SUPPLEMENTS.filter {
                it.id in listOf("supp_biotech_arthro_guard", "supp_ostrovit_collagen", "supp_biotech_c1000", "supp_scitec_omega3", "supp_ostrovit_d3_k2")
            }
            SupplementGoal.HEALTH_IMMUNITY -> SUPPLEMENTS.filter {
                it.id in listOf("supp_biotech_one_a_day", "supp_ostrovit_d3_k2", "supp_biotech_c1000", "supp_scitec_omega3", "supp_biotech_magnesium_bisglycinate")
            }
            SupplementGoal.SLEEP_STRESS -> SUPPLEMENTS.filter {
                it.id in listOf("supp_biotech_magnesium_bisglycinate", "supp_ostrovit_ashwagandha", "supp_scitec_zmb6", "supp_biotech_micellar_casein")
            }
            SupplementGoal.ALL -> SUPPLEMENTS.take(6)
        }
    }

    // Default daily trackable stack for the user
    val DEFAULT_USER_DAILY_STACK: List<UserDailySupplement> = listOf(
        UserDailySupplement(
            id = "user_stack_1",
            supplementId = "supp_biotech_one_a_day",
            supplementName = "One-A-Day Multivitamin",
            brand = "BioTechUSA",
            timing = SupplementTiming.WITH_BREAKFAST,
            dosageText = "1 tabletta reggeli étkezéssel",
            calories = 0.0,
            proteinGrams = 0.0,
            isTakenToday = true,
            takenTimeHu = "08:15"
        ),
        UserDailySupplement(
            id = "user_stack_2",
            supplementId = "supp_ostrovit_d3_k2",
            supplementName = "Vitamin D3 4000 IU + K2",
            brand = "OstroVit",
            timing = SupplementTiming.WITH_BREAKFAST,
            dosageText = "1 tabletta zsírosabb reggelivel",
            calories = 0.0,
            proteinGrams = 0.0,
            isTakenToday = true,
            takenTimeHu = "08:15"
        ),
        UserDailySupplement(
            id = "user_stack_3",
            supplementId = "supp_biotech_pure_whey",
            supplementName = "100% Pure Whey fehérje",
            brand = "BioTechUSA",
            timing = SupplementTiming.POST_WORKOUT,
            dosageText = "1 adagolókanál (28g) 250ml vízben",
            calories = 104.0,
            proteinGrams = 21.0,
            isTakenToday = false,
            takenTimeHu = null
        ),
        UserDailySupplement(
            id = "user_stack_4",
            supplementId = "supp_biotech_creatine_mono",
            supplementName = "100% Kreatin Monohidrát",
            brand = "BioTechUSA",
            timing = SupplementTiming.POST_WORKOUT,
            dosageText = "5g (1 teáskanál) a fehérjeturmixba",
            calories = 0.0,
            proteinGrams = 0.0,
            isTakenToday = false,
            takenTimeHu = null
        ),
        UserDailySupplement(
            id = "user_stack_5",
            supplementId = "supp_biotech_magnesium_bisglycinate",
            supplementName = "Magnesium Bisglycinate",
            brand = "BioTechUSA",
            timing = SupplementTiming.BEFORE_BED,
            dosageText = "2 kapszula lefekvés előtt 30 perccel",
            calories = 0.0,
            proteinGrams = 0.0,
            isTakenToday = false,
            takenTimeHu = null
        )
    )

    // Convert supplement to FoodItem for database searches & meal diary logging
    fun toFoodItem(item: SupplementItem): FoodItem {
        val calories100g = if (item.servingGrams > 0) (item.caloriesPerServing / item.servingGrams) * 100.0 else 0.0
        val protein100g = if (item.servingGrams > 0) (item.proteinPerServing / item.servingGrams) * 100.0 else 0.0
        val carbs100g = if (item.servingGrams > 0) (item.carbsPerServing / item.servingGrams) * 100.0 else 0.0
        val fat100g = if (item.servingGrams > 0) (item.fatPerServing / item.servingGrams) * 100.0 else 0.0

        return FoodItem(
            name = item.name,
            brand = item.brand,
            barcode = item.barcode,
            category = FoodCategory.SUPPLEMENTS.name,
            caloriesPer100g = calories100g,
            proteinPer100g = protein100g,
            carbsPer100g = carbs100g,
            fatPer100g = fat100g,
            fiberPer100g = 0.0,
            defaultServingUnit = item.servingUnitHu,
            defaultServingGrams = item.servingGrams,
            isCustom = false
        )
    }

    val SUPPLEMENT_FOOD_ITEMS: List<FoodItem> = SUPPLEMENTS.map { toFoodItem(it) }
}
