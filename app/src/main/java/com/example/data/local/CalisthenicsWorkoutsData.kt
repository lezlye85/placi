package com.example.data.local

import com.example.data.model.CalisthenicsExercise
import com.example.data.model.CalisthenicsRoutine
import com.example.data.model.DifficultyLevel
import com.example.data.model.WorkoutCategory

object CalisthenicsWorkoutsData {

    val ROUTINES: List<CalisthenicsRoutine> = listOf(
        // 1. Kezdő Teljes Test Alapozó
        CalisthenicsRoutine(
            id = "routine_beginner_fullbody",
            titleHu = "Kezdő Otthoni Kalisztenika Alapozó",
            subtitleHu = "Teljes testes testsúlyos alapozás 100% eszközök nélkül",
            difficulty = DifficultyLevel.BEGINNER,
            category = WorkoutCategory.FULL_BODY,
            estimatedMinutes = 25,
            totalCaloriesBurn = 220,
            descriptionHu = "Ideális kezdő program a testsúlyos edzés alapjainak elsajátításához. Kíméli az ízületeket, fejleszti a törzs stabilitását és beindítja a zsírégetést.",
            focusAreaHu = "Teljes test • Zsírégetés • Erőnlét",
            recommendedScheduleHu = "Heti 3 alkalom (pl. Hétfő - Szerda - Péntek)",
            requiredEquipmentHu = "100% Otthoni (Nincs szükség eszközre, csak székre vagy ágy szélére)",
            warmUpHu = "3 perc dinamikus karkörzés, csípőkörzés és helyben járás",
            coolDownHu = "2 perc mellkas-, comb- és vádlinyújtás a szőnyegen",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_squat_standard",
                    nameHu = "Saját testsúlyos guggolás (Squat)",
                    targetMuscle = "Négyfejű combizom, Farizom, Vádli",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Állj vállszéles terpeszben, a lábfejek enyhén kifelé nézzenek. Engedd le a csípődet vízszintesig, miközben a hátad egyenes marad.",
                    tipsHu = "A testsúlyt a sarkakra és a talp közepére helyezd, a térdek kövessék a lábfejek vonalát.",
                    breathingTipHu = "Leengedéskor mély beszívás az orron, felálláskor erőteljes kifújás szájon át.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Székre leülés és felállás (Box Squat)",
                    harderAlternativeHu = "1,5-ös guggolás (alul még egy fél pulzálás felállás előtt)",
                    stepByStepStepsHu = listOf(
                        "1. Kiinduló állás: Állj vállszélességű terpeszben, a mellkasodat emeld ki, a hasfalat enyhén feszítsd meg.",
                        "2. Leereszkedés: Told hátra a csípődet mintha egy székre ülnél le, miközben a térdeidet enyhén kifelé tolod.",
                        "3. Mélypont: Engedd le a combjaidat legalább a talajjal párhuzamos szintig úgy, hogy a sarkaid lent maradnak.",
                        "4. Felállás: Nyomd át az erőt a sarkaidon keresztül és feszítsd meg a farizmokat a felső ponton."
                    ),
                    commonMistakesHu = listOf(
                        "A térdek befelé rogynak leengedés közben.",
                        "A sarkak felemelkednek a földről.",
                        "A felsőtest túlságosan előredől és görbül a hát."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_pushup_incline_or_knee",
                    nameHu = "Térdelő vagy Döntött Fekvőtámasz",
                    targetMuscle = "Nagy mellizom, Elülső váll, Tricepsz",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "10-12 ismétlés",
                    caloriesBurnEstimate = 30,
                    instructionsHu = "Tedd a tenyereidet a földre vállszélességben. Térdelj le (vagy támaszkodj kanapé/ágy szélére). Engedd a mellkasod a talajhoz közel.",
                    tipsHu = "Ne hagyd lógni a csípődet, a nyakad legyen a gerinc egyenes folytatása.",
                    breathingTipHu = "Leengedés közben beszívás, kinyomáskor határozott kifújás.",
                    equipmentHu = "Szőnyeg vagy kanapé széle",
                    easierAlternativeHu = "Fali fekvőtámasz (állva, falnak döntve)",
                    harderAlternativeHu = "Lassú 3 másodperces leengedésű klasszikus fekvőtámasz",
                    stepByStepStepsHu = listOf(
                        "1. Beállítás: Helyezd a tenyereket a vállak alá, a törzset tartsd feszesen, a térdeket tedd le kényelmesen a szőnyegre.",
                        "2. Leengedés: Hajlítsd a könyökeidet kb. 45 fokos szögben a törzsedhez képest, engedd a mellkast a talajhoz 2-3 cm-re.",
                        "3. Megállás: Tartsd meg a feszültséget egy pillanatra az alsó holtponton.",
                        "4. Kinyomás: Erőteljesen nyomd vissza magad a kiinduló helyzetbe a lapockák szétfeszítésével."
                    ),
                    commonMistakesHu = listOf(
                        "A könyökök derékszögben oldalra állnak ki (túlterheli a vállat).",
                        "A medence lóg vagy a fenék kiemelkedik."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_table_pullup",
                    nameHu = "Asztal alatti evezés (Inverted Row)",
                    targetMuscle = "Széles hátizom, Lapockazárók, Bicepsz",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "8-10 ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Feküdj egy stabil étkezőasztal alá. Fogd meg a szélét vállszélesen, a sarkaid a földön. Húzd a mellkasodat az asztallaphoz.",
                    tipsHu = "A csúcsponton zárd össze a lapockáidat mintha egy ceruzát fognál össze köztük.",
                    breathingTipHu = "Húzáskor erőteljes kifújás, visszaengedéskor egyenletes beszívás.",
                    equipmentHu = "Stabil étkezőasztal vagy szobaajtó",
                    easierAlternativeHu = "Ajtófélfába kapaszkodva végzett álló evezés",
                    harderAlternativeHu = "Nyújtott lábas asztali evezés felpolcolt sarokkal",
                    stepByStepStepsHu = listOf(
                        "1. Elhelyezkedés: Feküdj az asztal alá háttal, ragadd meg az asztallap peremét stabil felső fogással.",
                        "2. Feszítés: Húzd be a hasad, feszítsd meg a farizmot, a tested egy egyenes deszkát alkosson.",
                        "3. Húzás: Húzd a mellkasodat az asztallap alsó részéhez a hátizmok megfeszítésével.",
                        "4. Visszaengedés: Lassan, kontrolláltan engedd le a tested a karok kinyújtásáig."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő beesik vagy megtörik a test vonala.",
                        "Csak a karból húzás a hátizmok aktiválása nélkül."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_chair_dips",
                    nameHu = "Széken tolódzkodás (Chair Dips)",
                    targetMuscle = "Tricepsz, Elülső deltaizom, Felső mellkas",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "12 ismétlés",
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Ülj egy stabil szék szélére, tenyerek a peremen a csípő mellett. Lépj előre, engedd le a csípődet 90 fokos könyökhajlításig, majd nyomd fel magad.",
                    tipsHu = "A hátadat tartsd végig a szék ülőfelületéhez közel.",
                    breathingTipHu = "Leengedéskor beszívás, felnyomáskor kifújás.",
                    equipmentHu = "Stabil szék vagy dohányzóasztal",
                    easierAlternativeHu = "Közelebb húzott lábakkal, derékszögben hajlított térddel",
                    harderAlternativeHu = "Kinyújtott lábakkal vagy egy másik székre felrakott sarokkal",
                    stepByStepStepsHu = listOf(
                        "1. Kezdőállás: Helyezd a tenyereidet a szék szélére ujjakkal előrefelé, a fenekedet csúsztasd le a székről.",
                        "2. Süllyedés: Hajlítsd be a könyöködet és engedd le a tested addig, amíg a felkar vízszintes nem lesz.",
                        "3. Feszítés: Nyomd fel magad a tricepsz erejével a karok teljes kinyújtásáig.",
                        "4. Ismétlés: Tartsd a vállakat leengedve, ne engedd felhúzódni a fülekhez."
                    ),
                    commonMistakesHu = listOf(
                        "Túl messzire távolodsz a széktől előre, ami megterheli a vállízületet.",
                        "Túlságosan mélyre engedés, ami vállfájdalmat okozhat."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_plank_hold",
                    nameHu = "Alkartámaszos Deszka (Plank)",
                    targetMuscle = "Mély hasizom (Transversus), Ferde hasizmok, Gluteus",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "40 másodperc",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Helyezkedj el alkartámaszban. Feszítsd meg a hasizmot, a feneket és a combokat. A tested alkosson egyenes vonalat.",
                    tipsHu = "Húzd a köldöködet a gerinced felé és billentsd hátra a medencédet.",
                    breathingTipHu = "Folyamatos, ritmikus rekeszizom légzés (ne tartsd vissza a levegőt!).",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelő plank tartás (20-30 mp)",
                    harderAlternativeHu = "Plank egy láb elemelésével vagy fűrész mozgással előre-hátra",
                    stepByStepStepsHu = listOf(
                        "1. Alkartámasz: Tedd le az alkarjaidat a vállak alá, a könyökök 90 fokban álljanak.",
                        "2. Testvonal: Nyújtsd ki a lábakat, támaszkodj a lábujjakon, billentsd a medencét hátra.",
                        "3. Statikus tartás: Feszítsd meg kőkeményre a has- és farizmokat, tekintet a talaj felé nézzen.",
                        "4. Kitartás: Tartsd meg a rezzenéstelen pozíciót az időzítő lejártáig."
                    ),
                    commonMistakesHu = listOf(
                        "A derék beesik, ami gerincfájdalmat eredményezhet.",
                        "A fenék túl magasra emelkedik sátrat formálva."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_glute_bridge",
                    nameHu = "Csípőemelés (Glute Bridge)",
                    targetMuscle = "Nagy farizom, Combhajlító, Ágyéki gerincmerevítők",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Feküdj a hátadra, hajlítsd be a térdeidet. Emeld fel a csípődet a földről a farizom megfeszítésével 2 másodpercig.",
                    tipsHu = "A csúcsponton szorítsd össze a farizmokat és ne a derekadból feszíts.",
                    breathingTipHu = "Felemeléskor kifújás, leengedéskor mély beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Kisebb mozgástartományú csípőemelés talajon",
                    harderAlternativeHu = "Egylábas csípőemelés a másik láb kinyújtásával a levegőben",
                    stepByStepStepsHu = listOf(
                        "1. Hanyattfekvés: Feküdj hanyatt, talpak a talajon vállszélességben, karok a test mellett.",
                        "2. Emelés: Nyomd a sarkaidat a talajba, és emeld fel a medencédet amíg a comb és törzs egy vonalba nem kerül.",
                        "3. Csúcskontrakció: Feszítsd meg a farizmokat 2 teljes másodpercig a felső ponton.",
                        "4. Leengedés: Lassan engedd vissza a medencét a talaj érintéséig."
                    ),
                    commonMistakesHu = listOf(
                        "Túltolás a derékból túlzott homorítással.",
                        "A térdek túlzott szétnyílása vagy összezáródása."
                    )
                )
            )
        ),

        // 2. Zsírégető Kalisztenika HIIT & Kardió
        CalisthenicsRoutine(
            id = "routine_fatburn_hiit",
            titleHu = "Zsírégető Kalisztenika HIIT & Kardió",
            subtitleHu = "Anyagcsere-pörgető intervallum edzés a gyors fogyáshoz",
            difficulty = DifficultyLevel.INTERMEDIATE,
            category = WorkoutCategory.FAT_BURN_HIIT,
            estimatedMinutes = 20,
            totalCaloriesBurn = 260,
            descriptionHu = "Magas intenzitású saját testsúlyos intervallum (HIIT) tréning, ami edzés után órákig tartó utóégető hatást (EPOC) vált ki a maximális zsírégetésért.",
            focusAreaHu = "Kardió • Zsírégetés • Állóképesség",
            recommendedScheduleHu = "Heti 2-3 alkalom regenerációs napokkal",
            requiredEquipmentHu = "100% Otthoni (Nincs szükség semmilyen eszközre)",
            warmUpHu = "3 perc helyben kocogás és dinamikus vállkörzések",
            coolDownHu = "3 perc mélylégzés és teljes testes levezető nyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_jumping_jacks",
                    nameHu = "Terpeszugrás (Jumping Jacks)",
                    targetMuscle = "Teljes test, Vádli, Váll, Szív-érrendszer",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "45 másodperc",
                    isTimer = true,
                    durationSeconds = 45,
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Ugorj terpeszbe, miközben a kezeidet a fejed felett összeérinted, majd ugorj vissza zárt állásba.",
                    tipsHu = "Rugalmasan a talppárnákon rugózz, ne csapd le a sarkadat a földre.",
                    breathingTipHu = "Folyamatos, ritmikus légzés minden ugrásra hangolva.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Step Jacks (ugrás nélküli oldalra lépés és karemelés)",
                    harderAlternativeHu = "Star Jumps (guggolásból robbanó felugrás a levegőben)",
                    stepByStepStepsHu = listOf(
                        "1. Zárt állás: Állj zárt lábakkal, kezek a comb mellett leengedve.",
                        "2. Terpeszugrás: Ugorj vállnál szélesebb terpeszbe, közben lendítsd a karokat fej fölé ívesen.",
                        "3. Érintés: Érintsd össze a tenyereket a fejed felett.",
                        "4. Visszaugrás: Érkezz vissza zárt lábakkal puha talajfogással."
                    ),
                    commonMistakesHu = listOf(
                        "Nyújtott, merev térdekkel való kemény leérkezés.",
                        "Légzésvisszatartás az ugrássorozat alatt."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_mountain_climbers",
                    nameHu = "Hegymászó (Mountain Climbers)",
                    targetMuscle = "Egyenes hasizom, Vállöv, Csípőhajlítók",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "40 másodperc",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Fekvőtámasz pozícióból váltott lábbal lendületes tempóban húzd a térdeidet a mellkasodhoz.",
                    tipsHu = "A vállak maradjanak pontosan a tenyerek felett, a csípőt tartsd alacsonyan.",
                    breathingTipHu = "Két térdhúzásonként egy-egy határozott ki-be fújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Lassú, lépésről lépésre végzett térdhúzás emelt asztallapon",
                    harderAlternativeHu = "Keresztező hegymászó (térd az átlós könyökhöz húzva)",
                    stepByStepStepsHu = listOf(
                        "1. Fekvőtámasz tartás: Helyezkedj el nyújtott karú deszka pozícióban.",
                        "2. Térdhúzás: Húzd a jobb térdedet robbanékonyan a mellkasod irányába a talaj érintése nélkül.",
                        "3. Lábváltás: Ugorj vissza a jobb lábbal, miközben a bal térdedet húzod előre.",
                        "4. Dinamika: Tartsd a folyamatos futó ritmust."
                    ),
                    commonMistakesHu = listOf(
                        "A fenék feltolódik túl magasra.",
                        "A vállak elcsúsznak a tenyerek mögé."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_burpees_no_pushup",
                    nameHu = "Négyütemű fekvőtámasz (Burpee)",
                    targetMuscle = "Teljes test, Combok, Mellkas, Kardió",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "10 ismétlés",
                    caloriesBurnEstimate = 55,
                    instructionsHu = "Állásból guggolj le, ugorj hátra fekvőtámasz tartásba, majd ugorj vissza guggolásba és felugorva tapsolj.",
                    tipsHu = "Tartsd a feszes törzset a hátraugrás pillanatában, ne engedd beesni a medencét.",
                    breathingTipHu = "Leérkezéskor beszívás, felugráskor erőteljes kifújás.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Ugrás nélkül, hátralépéssel végzett négyütemű",
                    harderAlternativeHu = "Burpee teljes mellkasérintéses fekvőtámasszal és magas felugrással",
                    stepByStepStepsHu = listOf(
                        "1. Guggolás: Állásból guggolj le mélyre és tedd le a tenyereidet a talajra a lábak elé.",
                        "2. Hátraugrás: Mindkét lábbal egyszerre ugorj hátra feszes fekvőtámasz helyzetbe.",
                        "3. Előreugrás: Robbanékonyan ugorj vissza a tenyerek mellé talpra.",
                        "4. Felugrás: Rugaszkodj el a földről felfelé és tapsolj a fej felett."
                    ),
                    commonMistakesHu = listOf(
                        "A medence lezuhan hátraugráskor.",
                        "Nem egész talpra, hanem csak lábujjhegyre érkezés előreugráskor."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_skater_jumps",
                    nameHu = "Korcsojázó oldalugrások (Skater Jumps)",
                    targetMuscle = "Farizom külső része, Combok, Egyensúly",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "40 másodperc",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Ugorj oldalra a jobb lábadra enyhén guggolva, a bal lábad keresztezve lendüljön hátra. Majd ugorj át a bal lábadra.",
                    tipsHu = "Használd a karjaidat az egyensúlyozáshoz és a lendületvételhez.",
                    breathingTipHu = "Ritmikus ki-be fújás minden oldalváltásra.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Ugrás helyett széles oldalra lépés és keresztlépés",
                    harderAlternativeHu = "Mélyebb guggolással és a talaj megérintésével az átlós kézzel",
                    stepByStepStepsHu = listOf(
                        "1. Kiindulás: Állj a jobb lábadon, enyhén hajlított térddel.",
                        "2. Oldalugrás: Rugaszkodj el oldalra balra, és érkezz puhán a bal lábadra.",
                        "3. Lendítés: A jobb lábadat lendítsd át keresztben a bal mögé a talaj érintése nélkül.",
                        "4. Visszaugrás: Ismételd meg a mozdulatsort a jobb oldalra dinamikusan."
                    ),
                    commonMistakesHu = listOf(
                        "Merev térdekkel történő oldalra érkezés.",
                        "Túlságosan kis mozgástartomány."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_high_knees",
                    nameHu = "Helybenfutás magas térdemeléssel",
                    targetMuscle = "Négyfejű comb, Csípőhorpasz, Kardió",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "40 másodperc",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Fuss helyben úgy, hogy a térdeidet derékmagasságig felhúzod folyamatos lendületes karkísérettel.",
                    tipsHu = "Húzd ki magad büszkén, ne dőlj hátra a futás közben.",
                    breathingTipHu = "Egyenletes, mély orron be - szájon ki légzés.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Magas térdemeléses menetelés helyben ugrálás nélkül",
                    harderAlternativeHu = "Dupla karhúzással és maximális sprint tempóval",
                    stepByStepStepsHu = listOf(
                        "1. Alapállás: Állj egyenesen, lábak csípőszélességben.",
                        "2. Térdemelés: Lendítsd a jobb térdedet vízszintes magasságig a bal kar előrelendítésével.",
                        "3. Ritmikus váltás: Puha talppárnás leérkezés után azonnal válts át a bal térdre.",
                        "4. Folyamatosság: Tartsd fenn a magas frekvenciát 40 másodpercig."
                    ),
                    commonMistakesHu = listOf(
                        "A törzs túlzott hátrahajlítása a térdemelés kompenzálására.",
                        "Csak félig felemelt térdek."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_bicycle_crunches",
                    nameHu = "Biciklis hasprés (Bicycle Crunches)",
                    targetMuscle = "Külső és belső ferde hasizom, Egyenes hasizom",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "20 váltott ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Hanyattfekvésben érintsd az ellentétes könyöködet az átlós térdedhez, miközben a másik lábadat kinyújtva tartod.",
                    tipsHu = "A vállövet forgasd el, ne a nyakadat rángasd a kezeiddel.",
                    breathingTipHu = "Kifújás az ellentétes oldal érintésekor, beszívás a középponton.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Lábak a talajon tartása és sima átlós felülések",
                    harderAlternativeHu = "2 másodperces megállítás az átlós érintési ponton",
                    stepByStepStepsHu = listOf(
                        "1. Hanyattfekvés: Feküdj hanyatt, ujjak a fül mellett, emeld el a lapockákat a talajtól.",
                        "2. Térdhúzás & Fordulás: Húzd a bal térdedet a mellkashoz, és fordítsd felé a jobb könyöködet.",
                        "3. Lábnyújtás: Ezzel egyidejűleg a jobb lábadat nyújtsd ki 45 fokos szögben előre.",
                        "4. Váltás: Kontrollált, lassú tempóban válts át a másik oldalra."
                    ),
                    commonMistakesHu = listOf(
                        "A fej és nyak előrerángatása a kezekkel.",
                        "Túl gyors, kapkodó végrehajtás valódi izomfeszítés nélkül."
                    )
                )
            )
        ),

        // 3. Felsőtest & Kar Kalisztenika
        CalisthenicsRoutine(
            id = "routine_upper_body_strength",
            titleHu = "Felsőtest Erő & Mellkas-Kar Formálás",
            subtitleHu = "Mellkas, széles vállak, tricepsz és hátizmok fejlesztése",
            difficulty = DifficultyLevel.INTERMEDIATE,
            category = WorkoutCategory.UPPER_BODY,
            estimatedMinutes = 30,
            totalCaloriesBurn = 245,
            descriptionHu = "Célzott saját testsúlyos felsőtest edzésprogram, amely fejleszti a nyomóerőt, formálja a karokat és szálkásítja a mellkast.",
            focusAreaHu = "Mellkas • Váll • Tricepsz • Hát",
            recommendedScheduleHu = "Heti 2-3 alkalom",
            requiredEquipmentHu = "100% Otthoni (Ajtófélfa vagy Szék szükséges)",
            warmUpHu = "3 perc karkörzés, csukló- és vállbemelegítés",
            coolDownHu = "2 perc mellkas és tricepsz nyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_standard_pushup",
                    nameHu = "Klasszikus Fekvőtámasz (Push-up)",
                    targetMuscle = "Nagy mellizom, Tricepsz, Elülső váll",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "12-15 ismétlés",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "A kezeid kissé szélesebbek a vállnál. Engedd le a tested 2 centire a talajtól, majd robbanékonyan nyomd ki.",
                    tipsHu = "A könyökök 45 fokos nyílban álljanak a törzshöz képest, a has és farizom kőkemény.",
                    breathingTipHu = "Leengedéskor orron át beszívás, felnyomáskor erőteljes kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Emelt kéztámaszos fekvőtámasz kanapén vagy széken",
                    harderAlternativeHu = "Lábak megemelve a kanapén (Decline Push-up)",
                    stepByStepStepsHu = listOf(
                        "1. Pozíció: Tenyerek a vállak vonalában, lábujjak a talajon, a test egyenes mint a deszka.",
                        "2. Leengedés: Lassan engedd le a mellkast a könyökök hátrafelé-kifelé hajlításával.",
                        "3. Mélypont: Állj meg 2 centire a padlótól a mellkas feszítésével.",
                        "4. Kinyomás: Robbanékony erővel told el magad a padlótól a karok kinyújtásáig."
                    ),
                    commonMistakesHu = listOf(
                        "Beeső derék és lógó medence.",
                        "Nem teljes mozgástartomány (fél-fekvőtámaszok)."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_pike_pushup",
                    nameHu = "Pike Fekvőtámasz (Vállerősítés)",
                    targetMuscle = "Elülső és oldalsó vállizmok, Felső mellkas",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "8-10 ismétlés",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Kéz- és lábtámasszal emeld fel a csípőd a magasba (fordított V alak). Engedd a fejed a kezek elé lefelé, majd nyomd el magad.",
                    tipsHu = "A fejtetővel a tenyerek előtti pontot célozd meg egy háromszöget bezárva.",
                    breathingTipHu = "Leengedéskor beszívás, felfelé toláskor kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Kisebb csípőemeléssel és hajlított térdekkel végzett Pike",
                    harderAlternativeHu = "Lábak felrakva székre vagy kanapéra (Elevated Pike Push-up)",
                    stepByStepStepsHu = listOf(
                        "1. V alak: Helyezkedj el mellső támaszban, majd lépegess a lábakkal a kezed felé és told fel a csípőt.",
                        "2. Süllyedés: Engedd a fejedet kissé előrefelé le a kezek előtti háromszög csúcsához.",
                        "3. Csúcs: Érintsd meg finoman a talajt a homlokoddal.",
                        "4. Feltolás: Toljad magad vissza a vállaidból a fej hátrahúzásával a karok közé."
                    ),
                    commonMistakesHu = listOf(
                        "A könyökök túl szélesre nyílnak ki oldalra.",
                        "Nem a fej elé, hanem a kezek közé való leengedés."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_diamond_pushup",
                    nameHu = "Gyémánt Fekvőtámasz (Diamond Push-up)",
                    targetMuscle = "Tricepsz belső és oldalsó feje, Belső mellizom",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "8-12 ismétlés",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Érintsd össze a hüvelyk- és mutatóujjaidat egy gyémánt alakot formálva a mellkas alatt. Úgy végezz fekvőtámaszt.",
                    tipsHu = "Tartsd a könyököket a törzsedhez közel a maximális tricepsz terhelésért.",
                    breathingTipHu = "Leengedéskor beszívás, kinyomáskor határozott kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelve végzett gyémánt fekvőtámasz",
                    harderAlternativeHu = "1 másodperces alsó megállással és lassú 4 másodperces leengedéssel",
                    stepByStepStepsHu = listOf(
                        "1. Kézbeállítás: Helyezd a tenyereidet a szegycsontod alá úgy, hogy a hüvelyk- és mutatóujjak érintsék egymást.",
                        "2. Feszítés: Nyújtsd ki a lábakat és zárd össze a sarkakat a stabil törzstartásért.",
                        "3. Leengedés: Engedd le a mellkasodat a kezeidre a könyökök hátrafelé tartásával.",
                        "4. Kinyomás: Feszítsd meg erőteljesen a tricepszeket a mozdulat végén."
                    ),
                    commonMistakesHu = listOf(
                        "A kezek túl messze vannak a mellkastól előre a fej alatt.",
                        "A könyökök széttárása a tricepsz helyett."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_doorframe_row",
                    nameHu = "Ajtófélfás evezés (Doorframe Rows)",
                    targetMuscle = "Széles hátizom, Bicepsz, Rombuszizom",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "12 ismétlés / kar",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Állj az ajtófélfához, fogd meg kézzel, dőlj hátra nyújtott karral és húzd a tested a kerethez a lapockák zárásával.",
                    tipsHu = "Minél közelebb van a lábad az ajtó küszöbéhez, annál nehezebb az ellenállás.",
                    breathingTipHu = "Húzáskor kifújás, visszaengedéskor beszívás.",
                    equipmentHu = "Szobaajtó vagy stabil ajtófélfa",
                    easierAlternativeHu = "Függőlegesebb testhelyzetben, kisebb dőlésszöggel",
                    harderAlternativeHu = "Egykezes ajtófélfás evezés lassú negatív szakasszal",
                    stepByStepStepsHu = listOf(
                        "1. Fogás: Állj az ajtónyílásba, fogd meg az ajtókeretet mellmagasságban stabil kézzel.",
                        "2. Hátradőlés: Tedd a lábfejeket a keret tövéhez, és dőlj hátra nyújtott karral feszes testtel.",
                        "3. Húzás: Húzd oda a mellkasodat a félfához a hátizmok és lapockák erőteljes összezárásával.",
                        "4. Kiengedés: Kontrolláltan engedd vissza magad a kar kinyújtásáig."
                    ),
                    commonMistakesHu = listOf(
                        "Csak a bicepszből való húzás a hátizmok bekapcsolása nélkül.",
                        "A derék megtörése hátradőlés közben."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_plank_shoulder_taps",
                    nameHu = "Vállérintés mellső fekvőtámaszban",
                    targetMuscle = "Vállöv stabilitás, Ferde hasizmok, Mély törzs",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "20 váltott érintés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Fekvőtámasz pozícióból emeld fel a jobb kezedet és érintsd meg a bal válladat úgy, hogy a csípőd ne billegjen.",
                    tipsHu = "A lábaidat nyisd kissé szélesebb terpeszbe az extra medence-stabilitáshoz.",
                    breathingTipHu = "Minden vállérintésnél egy-egy határozott kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelő mellső támaszban végzett vállérintés",
                    harderAlternativeHu = "Szűk lábterpeszben vagy lábemeléses ellenoldali érintéssel",
                    stepByStepStepsHu = listOf(
                        "1. Kezdőállás: Vegyél fel nyújtott karú fekvőtámasz pozíciót, lábak vállszélesnél kicsit szélesebben.",
                        "2. Feszítés: Rögzítsd a medencédet úgy, hogy teljesen mozdulatlan maradjon.",
                        "3. Érintés: Emeld fel a jobb kezed, és finoman érintsd meg a bal válladat.",
                        "4. Visszatétel: Tedd vissza a kezed a talajra, majd azonnal ismételd a másik oldallal."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő folyamatos jobbra-balra forgatása érintés közben.",
                        "A fej lelógatása."
                    )
                )
            )
        ),

        // 4. Lapos Has & Core Tréning
        CalisthenicsRoutine(
            id = "routine_core_abs_mastery",
            titleHu = "Core & Lapos Hasizom Tréning (6-Pack)",
            subtitleHu = "Mély hasizom feszítés, derékvédelem és szálkás hasfal",
            difficulty = DifficultyLevel.INTERMEDIATE,
            category = WorkoutCategory.CORE_ABS,
            estimatedMinutes = 20,
            totalCaloriesBurn = 210,
            descriptionHu = "Átfogó hasizom- és törzsstabilizáló program, amely a has összes rétegét (egyenes, ferde és mély haránt hasizmok) célba veszi a lapos és feszes hasért.",
            focusAreaHu = "Egyenes hasizom • Ferde hasizom • Törzserő",
            recommendedScheduleHu = "Heti 3-4 alkalom",
            requiredEquipmentHu = "100% Otthoni (Csak tornaszőnyeg szükséges)",
            warmUpHu = "2 perc macska-tehén póz és törzsfordítások",
            coolDownHu = "2 perc hasonfekvő kobra hasizom nyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_dead_bug",
                    nameHu = "Döglött bogár (Dead Bug)",
                    targetMuscle = "Mély hasizom, Keresztirányú törzsstabilitás",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "16 váltott ismétlés",
                    caloriesBurnEstimate = 30,
                    instructionsHu = "Hanyattfekvésben emeld a karokat és a 90 fokos térdeket a levegőbe. Nyújtsd ki az ellentétes kart és lábat úgy, hogy a derekad a földön marad.",
                    tipsHu = "Képzeld el, hogy egy 10.000 forintost szorítasz a derekaddal a padlóhoz!",
                    breathingTipHu = "Kinyújtáskor fújd ki a levegőt, visszahúzáskor szívd be mélyen.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Csak a lábak kinyújtása, a karok a talajon maradnak",
                    harderAlternativeHu = "Lassú 3 másodperces kinyújtott tartás és gumiszalag feszítés",
                    stepByStepStepsHu = listOf(
                        "1. Kiinduló póz: Feküdj a hátadra, emeld a karokat függőlegesen a mennyezet felé, a térdeket 90 fokban a csípő fölé.",
                        "2. Derék leszorítás: Nyomd a derekad teljes felületét a talajba résmentesen.",
                        "3. Átlós nyújtás: Lassan engedd le a jobb kart a fej mögé és nyújtsd ki a bal lábat a padló felett 5 cm-re.",
                        "4. Visszahúzás: Térj vissza a kiinduló helyzetbe és ismételd a másik átlóval."
                    ),
                    commonMistakesHu = listOf(
                        "A derék felemelkedik a talajról és boltívet képez.",
                        "Túl gyors kapkodás a kontrollált feszítés helyett."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_hollow_body_hold",
                    nameHu = "Hollow Body Hold (Testfeszítés)",
                    targetMuscle = "Teljes hasfal, Haránt hasizom, Törzs feszítő lánc",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "30 másodperc",
                    isTimer = true,
                    durationSeconds = 30,
                    caloriesBurnEstimate = 30,
                    instructionsHu = "Hanyattfekvésben szorítsd a derekad a földre, emeld el a lapockákat és a nyújtott lábakat 10 centire a padlótól banana alakot formálva.",
                    tipsHu = "A hasizom maximális tónusban dolgozik, a lábujjak előre mutatnak.",
                    breathingTipHu = "Rövid, sekély és kontrollált kilégzések feszített hasfal mellett.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Tuck Hollow Hold (térdek mellkashoz húzva, kezek comb mellett)",
                    harderAlternativeHu = "Hollow Rock (enyhe hintázás a feszes testív megőrzésével)",
                    stepByStepStepsHu = listOf(
                        "1. Fekvés: Feküdj hanyatt teljesen kinyújtózva, karok a fej mellett kinyújtva.",
                        "2. Lapockaemelés: Emeld el a fejedet, nyakadat és a lapockáidat a talajtól a has megfeszítésével.",
                        "3. Lábemelés: Emeld el a nyújtott, összezárt lábakat 10-15 cm-re a földtől.",
                        "4. Statikus ív: Tartsd meg a csónak alakot a megadott másodpercig rezzenéstelenül."
                    ),
                    commonMistakesHu = listOf(
                        "A derék felpattan a talajról.",
                        "A lábak túl magasra emelkednek (csökken a hasizmok terhelése)."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_lying_leg_raises",
                    nameHu = "Fekvő Lábemelés (Leg Raises)",
                    targetMuscle = "Alsó hasizom, Egyenes hasizom alsó rostjai",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "12 ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Hanyattfekvésben nyújtsd ki a lábaidat és emeld fel őket függőlegesig, majd lassan engedd le a talaj fölé.",
                    tipsHu = "Tedd a tenyereidet a combok mellé vagy a fenék alá a derék védelmére.",
                    breathingTipHu = "Emeléskor kifújás, leengedéskor mély beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Hajlított térddel végzett lábemelés (Reverse Crunch)",
                    harderAlternativeHu = "Emelés után a csípő felnyomása a mennyezet felé (Candle Pose)",
                    stepByStepStepsHu = listOf(
                        "1. Hanyattfekvés: Feküdj a szőnyegre, karok a test mellett, lábak teljesen kinyújtva és összezárva.",
                        "2. Emelés: A hasizmok erejével emeld fel a lábakat függőleges 90 fokos helyzetig.",
                        "3. Negatív szakasz: Lassan, 3 másodperc alatt engedd le a lábaidat.",
                        "4. Megállítás: Állj meg 2 cm-re a talaj felett a sarok lerakása nélkül, és indítsd a következő emelést."
                    ),
                    commonMistakesHu = listOf(
                        "Lendületből való felcsapás kontroll nélkül.",
                        "A sarkak lecsapása a padlóra."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_russian_twists",
                    nameHu = "Orosz Csavarás (Russian Twists)",
                    targetMuscle = "Belső és külső ferde hasizmok, Csípőfordítók",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "20 váltott érintés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Ülj a talajra, dőlj hátra 45 fokban, emeld fel a lábfejeket a földről, és forgasd a törzsed jobbra-balra.",
                    tipsHu = "A tekinteted és a mellkasod kövesse a kezeid elfordulását.",
                    breathingTipHu = "Minden oldalsó érintéskor határozott kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Sarkak a talajon tartása hátra dőlt pozícióban",
                    harderAlternativeHu = "Egy 1,5 literes vizes palackot tartva a kezedben súlyként",
                    stepByStepStepsHu = listOf(
                        "1. V-ülés: Ülj le, hajlítsd a térdeket, dőlj hátra a felsőtesttel kb. 45 fokban egyensúlyozva az ülőgumókon.",
                        "2. Lábemelés: Emeld el a sarkaidat a talajtól 10 cm-re.",
                        "3. Csavarás jobbra: Forgasd el a vállövedet és érintsd meg mindkét kézzel a talajt a jobb csípőd mellett.",
                        "4. Csavarás balra: Lendület nélkül, a ferde hasizmokkal fordítsd át a tested a bal oldalra."
                    ),
                    commonMistakesHu = listOf(
                        "Csak a kezek mozgatása a törzs tényleges elfordítása nélkül.",
                        "Görbe háttal való végrehajtás."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_side_plank_hold",
                    nameHu = "Oldalsó Deszka (Side Plank)",
                    targetMuscle = "Ferde hasizom, Quadratus lumborum, Farizom",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "30 mp / oldal",
                    isTimer = true,
                    durationSeconds = 30,
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Feküdj az oldaladra, támaszkodj az alkarodra a váll alatt. Emeld fel a csípődet a földről egyenes vonalat képezve.",
                    tipsHu = "Ne engedd lebillenni az elülső válladat előrefelé.",
                    breathingTipHu = "Egyenletes, lassú és ritmikus légzés a tartás alatt.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Oldalsó deszka hajlított térddel a földön támaszkodva",
                    harderAlternativeHu = "Oldalsó deszkában a felső láb emelése (Star Side Plank)",
                    stepByStepStepsHu = listOf(
                        "1. Alkar elhelyezés: Tedd le az alkarodat merőlegesen a testedre, közvetlenül a váll alá.",
                        "2. Lábak elrendezése: Helyezd a felső lábadat az alsó tetejére vagy egymás mögé a talajra.",
                        "3. Csípőemelés: Emeld fel a medencédet a talajról, a szabad kezedet nyújtsd a magasba vagy tedd csípőre.",
                        "4. Tartás: Tartsd meg a stabil egyenest 30 másodpercig, majd válts oldalt."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő lesüllyed a talaj felé fáradáskor.",
                        "A felső váll előrecsavarodik."
                    )
                )
            )
        ),

        // 5. Alsótest & Comb-Farizom
        CalisthenicsRoutine(
            id = "routine_lower_body_glutes",
            titleHu = "Alsótest & Comb-Farizom Formáló",
            subtitleHu = "Kerek farizom, tónusos combok és robbanékony alsótest",
            difficulty = DifficultyLevel.INTERMEDIATE,
            category = WorkoutCategory.LOWER_BODY,
            estimatedMinutes = 25,
            totalCaloriesBurn = 230,
            descriptionHu = "Kifejezetten az alsótest nagy izomcsoportjaira összpontosító edzésterv, amely hatékonyan feszesíti a combokat és formálja a farizmokat.",
            focusAreaHu = "Farizom • Comb • Vádli • Egyensúly",
            recommendedScheduleHu = "Heti 2-3 alkalom",
            requiredEquipmentHu = "100% Otthoni (Szék vagy Fal szükséges)",
            warmUpHu = "3 perc dinamikus láblendítés és bokaátmozgatás",
            coolDownHu = "3 perc comb- és farizom nyújtás a szőnyegen",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_bulgarian_split_squat",
                    nameHu = "Bolgár Guggolás széken (Split Squat)",
                    targetMuscle = "Farizom, Négyfejű combizom, Combhajlító",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "10-12 ismétlés / láb",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Állj háttal egy széknek, tedd fel az egyik lábfejed a székre. Guggolj le mélyre az elöl lévő lábaddal, majd állj fel.",
                    tipsHu = "Az elülső térded ne menjen túlságosan a lábujjak elé, a törzsed maradjon egyenes.",
                    breathingTipHu = "Leengedéskor beszívás, felnyomáskor erőteljes kifújás.",
                    equipmentHu = "Stabil szék vagy kanapé pereme",
                    easierAlternativeHu = "Hagyományos kitörés hátrafelé a talajon",
                    harderAlternativeHu = "1,5-ös pulzáló bolgár guggolás a mélyponton",
                    stepByStepStepsHu = listOf(
                        "1. Beállás: Lépj kb. 80-90 cm-t előre egy széktől, majd a hátsó lábfejed rüsztjét helyezd a székre.",
                        "2. Süllyedés: Engedd le a csípődet függőlegesen lefelé, amíg az elülső comb vízszintes nem lesz.",
                        "3. Mélypont: A hátsó térded közelítse meg a talajt.",
                        "4. Feltolás: Az elülső láb sarkán keresztül nyomd vissza magad a kiinduló helyzetbe."
                    ),
                    commonMistakesHu = listOf(
                        "Túl rövid terpesz, ami túlzott terhelést ró az elülső térdre.",
                        "A felsőtest előreborulása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_sumo_squat",
                    nameHu = "Szumó Guggolás (Sumo Squat)",
                    targetMuscle = "Belső comb (Adduktorok), Farizom",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Állj széles terpeszben, a lábfejeket fordítsd 45 fokban kifelé. Guggolj le mélyre a belső combok feszítésével.",
                    tipsHu = "Told a térdeidet szétfelé a lábfejek irányába a leereszkedés során.",
                    breathingTipHu = "Leengedéskor beszívás, kinyomáskor kifújás.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Székre támaszkodva végzett széles guggolás",
                    harderAlternativeHu = "Mélyponton 3 másodperces statikus tartással",
                    stepByStepStepsHu = listOf(
                        "1. Széles terpesz: Állj vállszélesnél jóval szélesebb terpeszbe, a lábfejek 45 fokban nézzenek kifelé.",
                        "2. Leereszkedés: Tartsd egyenesen a felsőtestedet, és engedd le a csípődet mintha egy fal mentén csúsznál le.",
                        "3. Térdirány: Figyelj, hogy a térdek folyamatosan a lábujjak vonalát kövessék.",
                        "4. Feszítés: Nyomd fel magad és szorítsd össze a belső combokat és farizmokat."
                    ),
                    commonMistakesHu = listOf(
                        "A térdek befelé rogynak leengedéskor.",
                        "A sarok elemelkedése a padlóról."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_wall_sit",
                    nameHu = "Falnál Ülés tartás (Wall Sit)",
                    targetMuscle = "Négyfejű combizom izometrikus ereje",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "45 másodperc",
                    isTimer = true,
                    durationSeconds = 45,
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Támaszd a hátadat a falnak és csússz le addig, amíg a combjaid derékszöget zárnak be a fallal. Tartsd meg mozdulatlanul.",
                    tipsHu = "A combok legyenek teljesen párhuzamosak a padlóval, a kezeket ne támaszd a térdre!",
                    breathingTipHu = "Nyugodt, egyenletes mélylégzés a combok égető érzése ellenére.",
                    equipmentHu = "Szoba fal",
                    easierAlternativeHu = "Magasabb szögben végzett 30 másodperces falnál ülés",
                    harderAlternativeHu = "Egylábas falnál ülés a másik láb előrenyújtásával",
                    stepByStepStepsHu = listOf(
                        "1. Falhoz állás: Állj háttal a falnak, a hátadat és a fejedet simítsd a falhoz.",
                        "2. Lecsúszás: Csússz lefelé a falon addig, amíg a térdeid és a csípőd pontosan 90 fokos szöget zárnak be.",
                        "3. Karok: Nyújtsd ki a karokat előre vagy pihentesd a mellkasodon (ne a combon!).",
                        "4. Időzítés: Tartsd ki a pozíciót az időzítő lejártáig."
                    ),
                    commonMistakesHu = listOf(
                        "A kézzel a combra támaszkodás a tehermentesítésért.",
                        "Nem elég mélyre csúszás (túl magasan maradás)."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_single_leg_calf_raises",
                    nameHu = "Egylábas Vádliemelés (Calf Raises)",
                    targetMuscle = "Kétfejű lábikra és gázlóizom (Vádli)",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "15 ismétlés / láb",
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Állj egy lábon, kapaszkodj meg enyhén a falban. Emelkedj fel lábujjhegyre a legmagasabb pontig, majd lassan engedd le a sarkad.",
                    tipsHu = "A csúcsponton tartsd meg 1 másodpercig a teljes összehúzódást.",
                    breathingTipHu = "Emelkedéskor kifújás, süllyedéskor beszívás.",
                    equipmentHu = "Fal vagy küszöb",
                    easierAlternativeHu = "Két lábon egyszerre végzett vádliemelés",
                    harderAlternativeHu = "Könyvön vagy lépcsőfokon állva a sarka mélyebbre engedésével",
                    stepByStepStepsHu = listOf(
                        "1. Alapállás: Állj a jobb lábadon, a bal lábadat hajlítsd be hátul, ujjaiddal érintsd a falat egyensúlyért.",
                        "2. Felemelkedés: Toljad fel magad a jobb talppárnán a lehető legmagasabb lábujjhegyre.",
                        "3. Csúcstartás: Feszítsd meg a vádlit 1 másodpercre.",
                        "4. Leengedés: Lassan, kontrolláltan engedd le a sarkadat a föld érintéséig."
                    ),
                    commonMistakesHu = listOf(
                        "Gyors rugózás a csúcskontrakció megtartása nélkül.",
                        "Túl nagy támaszkodás és húzás a kezekkel."
                    )
                )
            )
        ),

        // 6. Hát- & Tartásjavító Kalisztenika
        CalisthenicsRoutine(
            id = "routine_posture_back_health",
            titleHu = "Hát- & Gerinckímélő Tartásjavító",
            subtitleHu = "Ülőmunkát kompenzáló lapockazáró és mélyhát-erősítés",
            difficulty = DifficultyLevel.BEGINNER,
            category = WorkoutCategory.POSTURE_BACK,
            estimatedMinutes = 20,
            totalCaloriesBurn = 180,
            descriptionHu = "Tökéletes program a görnyedt testtartás korrigálására, a lapockaközi izmok és a mély gerincmerevítők megerősítésére fájdalommentesen.",
            focusAreaHu = "Hát • Lapockazárók • Gerinc • Nyakvédelem",
            recommendedScheduleHu = "Heti 3-5 alkalom (akár munkanap végén is)",
            requiredEquipmentHu = "100% Otthoni (Csak tornaszőnyeg)",
            warmUpHu = "2 perc vállkörzés és nyakkörzés",
            coolDownHu = "2 perc gyermekpóz (Child's Pose) mélylégzéssel",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_bird_dog",
                    nameHu = "Madárkutya (Bird-Dog)",
                    targetMuscle = "Multifidus, Lapockazárók, Farizom, Átlós lánc",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "12 váltott ismétlés",
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Négykézláb állásból nyújtsd ki egyszerre a jobb kart előre és a bal lábat hátra egy vonalba a törzzsel.",
                    tipsHu = "Képzeld el, hogy egy pohár víz van a derekadon és nem szabad kilöttyennie.",
                    breathingTipHu = "Kinyújtáskor kifújás, visszatéréskor beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Csak a karok vagy csak a lábak kinyújtása külön-külön",
                    harderAlternativeHu = "Könyök-térd összeérintése a test alatt minden kinyújtás között",
                    stepByStepStepsHu = listOf(
                        "1. Négykézláb póz: Helyezkedj el négykézláb, térdek a csípő alatt, tenyerek a vállak alatt.",
                        "2. Átlós nyújtás: Emeld fel és nyújtsd ki a jobb karodat előre, és a bal lábadat hátra vízszintes magasságig.",
                        "3. Megtartás: Feszítsd meg a lapockádat és a bal farizmodat 2 másodpercig.",
                        "4. Visszatérés: Lassan engedd vissza a végtagokat a talajra és ismételd a másik átlóval."
                    ),
                    commonMistakesHu = listOf(
                        "A derék túlzott behomorítása a láb túl magasra emelésével.",
                        "A medence kibillenése oldalra."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_superman_hold",
                    nameHu = "Superman Törzsemelés (Superman Hold)",
                    targetMuscle = "Hátizmok, Ágyéki gerincmerevítők, Farizom",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "10 ismétlés (3 mp tartással)",
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Hasonfekvésben nyújtsd ki a karokat előre, majd emeld el egyszerre a mellkast és a lábakat a talajtól.",
                    tipsHu = "A tekinteted lefelé nézzen a nyaki gerinc kímélésére.",
                    breathingTipHu = "Emeléskor kifújás, leengedéskor beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Csak a mellkas vagy csak a lábak emelése felváltva",
                    harderAlternativeHu = "Superman tartás közben karhúzás W alakba hátrafelé",
                    stepByStepStepsHu = listOf(
                        "1. Hasonfekvés: Feküdj hasra a szőnyegen, karok kinyújtva a fej előtt, lábak nyújtva.",
                        "2. Együttes emelés: Feszítsd meg a hátadat és farizmodat, emeld el a mellkast és a combokat a talajtól 10 cm-re.",
                        "3. Tartás: Tartsd meg a repülő Superman pózt 3 másodpercig a hát felső szakaszának feszítésével.",
                        "4. Leengedés: Lassan engedd le a tested a szőnyegre."
                    ),
                    commonMistakesHu = listOf(
                        "A nyak hátrahajlítása és felfelé nézés (megterheli a nyaki csigolyákat).",
                        "Légzés visszatartása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prone_w_y_raises",
                    nameHu = "Hasonfekvő W-ről Y-ra karemelés",
                    targetMuscle = "Alsó és középső trapézizom, Rombuszizmok",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "12 ismétlés",
                    caloriesBurnEstimate = 20,
                    instructionsHu = "Hasonfekvésben emeld a karjaidat W alakba behajlítva a lapockák összezárásával, majd told ki Y alakba előre.",
                    tipsHu = "A hüvelykujjak folyamatosan a mennyezet felé mutassanak.",
                    breathingTipHu = "Y toláskor beszívás, W hátrahúzáskor határozott kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Állva, falnak dőlve végzett W-Y karemelés",
                    harderAlternativeHu = "Kis 0,5 literes palackokkal nehezítve",
                    stepByStepStepsHu = listOf(
                        "1. Hasonfekvés: Feküdj hasra, a homlokodat támaszd le a szőnyegre.",
                        "2. W alak: Húzd a könyököket az oldaladhoz W betűt formálva, hüvelykujjak a mennyezet felé nézzenek.",
                        "3. Lapockazárás: Emeld el a karokat a talajtól 10 cm-re a lapockák maximális összezárásával.",
                        "4. Y nyújtás: Told ki a karokat rézsútosan előre Y betűt formálva a levegőben tartva."
                    ),
                    commonMistakesHu = listOf(
                        "A vállak felhúzása a fülekhez a trapéz alsó része helyett.",
                        "A karok leejtése a szőnyegre az ismétlések között."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_scapular_pushups",
                    nameHu = "Lapocka Fekvőtámasz (Scapular Push-ups)",
                    targetMuscle = "Serratus anterior (Elülső fűrészizom), Lapockastabilitás",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "12 ismétlés",
                    caloriesBurnEstimate = 20,
                    instructionsHu = "Nyújtott karú fekvőtámaszban a könyök hajlítása nélkül engedd össze a lapockáidat, majd told szét őket.",
                    tipsHu = "A karok végig teljesen nyújtva maradnak, a mozgás kizárólag a lapockákból ered.",
                    breathingTipHu = "Lapockazáráskor beszívás, szétnyomáskor kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelőtámaszban végzett lapockamozgatás",
                    harderAlternativeHu = "Alkartámaszos deszkában végzett lapockamozgatás",
                    stepByStepStepsHu = listOf(
                        "1. Kiindulás: Vegyél fel nyújtott karú fekvőtámasz pozíciót merev törzzsel.",
                        "2. Lapockazárás: Nyújtott karok mellett engedd le a mellkasodat a lapockák összezárásával.",
                        "3. Lapockanyitás: Told el a talajt a tenyereiddel, domborítsd a hát felső részét és nyisd szét a lapockákat.",
                        "4. Ismétlés: Tartsd a folyamatos, kontrollált ritmust."
                    ),
                    commonMistakesHu = listOf(
                        "A könyökök behajlítása fekvőtámasszá alakítva a gyakorlatot.",
                        "A derék beesése a lapockamozgatás helyett."
                    )
                )
            )
        ),

        // 7. Haladó Saját Testsúlyos Mesterképző
        CalisthenicsRoutine(
            id = "routine_advanced_mastery",
            titleHu = "Haladó Saját Testsúlyos Mesterképző",
            subtitleHu = "Nehéz készségek: Íjász fekvőtámasz, L-Sit, Pistol Squat",
            difficulty = DifficultyLevel.ADVANCED,
            category = WorkoutCategory.UPPER_BODY,
            estimatedMinutes = 35,
            totalCaloriesBurn = 310,
            descriptionHu = "Haladó saját testsúlyos készségek és egyoldalas erőfejlesztő gyakorlatok a maximális relatív erő és testkontroll eléréséhez.",
            focusAreaHu = "Maximális erő • Egyensúly • Ízületi stabilitás",
            recommendedScheduleHu = "Heti 2-3 alkalom",
            requiredEquipmentHu = "Két stabil szék és szoba fal",
            warmUpHu = "5 perc alapos csukló-, váll- és csípőbemelegítés",
            coolDownHu = "3 perc teljes testes levezetés",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_archer_pushup",
                    nameHu = "Íjász Fekvőtámasz (Archer Push-up)",
                    targetMuscle = "Egyoldali mellizom, Vállöv, Karok",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "6-8 ismétlés / oldal",
                    caloriesBurnEstimate = 50,
                    instructionsHu = "Széles terpesztartásban engedd le a tested az egyik kezed felé, miközben a másik kar teljesen kinyújtva marad.",
                    tipsHu = "A kinyújtott kar segíti az egyensúlyt mint egy íjász íja.",
                    breathingTipHu = "Leengedéskor beszívás, felnyomáskor kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelve végzett íjász fekvőtámasz",
                    harderAlternativeHu = "Egykezes fekvőtámasz progresszió",
                    stepByStepStepsHu = listOf(
                        "1. Széles támasz: Tedd le a tenyereidet a vállaknál kétszer szélesebben.",
                        "2. Süllyedés oldalra: Engedd le a mellkasodat a jobb tenyered felé a jobb könyök hajlításával.",
                        "3. Nyújtott kar: A bal karodat tartsd teljesen kinyújtva oldalra a tenyér belső élére támaszkodva.",
                        "4. Feltolás: Nyomd ki magad a jobb karral a középpontig, majd ismételd a bal oldalra."
                    ),
                    commonMistakesHu = listOf(
                        "A támasztó kar könyökének nem kívánt behajlítása.",
                        "A törzs kifordulása oldalra."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_pistol_squat",
                    nameHu = "Egylábas guggolás (Pistol Squat)",
                    targetMuscle = "Négyfejű comb, Farizom, Boka mobilitás",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "5-8 ismétlés / láb",
                    caloriesBurnEstimate = 60,
                    instructionsHu = "Állj egy lábon, a másik lábat nyújtsd előre. Guggolj le mélyre egy lábon a sarok lent tartásával, majd állj fel.",
                    tipsHu = "Kapaszkodj meg egy szék támlájában az egyensúly megőrzéséhez a kezdeti hetekben.",
                    breathingTipHu = "Leengedéskor mély beszívás, felálláskor erőteljes robbanékony kifújás.",
                    equipmentHu = "Szék vagy szobaajtó kapaszkodónak",
                    easierAlternativeHu = "Székre leülős egylábas guggolás (Single-leg Box Squat)",
                    harderAlternativeHu = "Pistol Squat a lentartási ponton 2 másodperces megállással",
                    stepByStepStepsHu = listOf(
                        "1. Egyensúlyozás: Állj a jobb lábadon, a bal lábadat emeld fel kinyújtva magad előtt.",
                        "2. Guggolás: Nyújtsd ki a karokat előre, told hátra a csípőt és lassan guggolj le a jobb sarkadon maradva.",
                        "3. Mélypont: Érd el a comb-vádli érintési pontot úgy, hogy a bal láb nem érinti a földet.",
                        "4. Felállás: Nyomd át a testsúlyodat a sarkadon keresztül és állj fel egyenesbe."
                    ),
                    commonMistakesHu = listOf(
                        "A támasztó láb sarka felemelkedik a talajról.",
                        "A térd befelé roggyanása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_lsit_progression",
                    nameHu = "L-Sit tartás széken (L-Sit Hold)",
                    targetMuscle = "Hasi izomzat, Csípőhajlítók, Tricepsz",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "20 másodperc",
                    isTimer = true,
                    durationSeconds = 20,
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Nyomd fel magad nyújtott karral két stabil szék széléről, és emeld a nyújtott lábaidat vízszintesbe L alakban.",
                    tipsHu = "Told le a vállaidat a fülektől és feszítsd a hasizmot maximálisan.",
                    breathingTipHu = "Rövid, kontrollált kilégzések a hasfal feszültsége mellett.",
                    equipmentHu = "Két stabil egymás mellé tett szék",
                    easierAlternativeHu = "Tuck L-Sit (behúzott térdekkel tartás)",
                    harderAlternativeHu = "L-Sit a talajról felnyomva könyökhajlítás nélkül",
                    stepByStepStepsHu = listOf(
                        "1. Beülés: Helyezz el két széket egymással szemben, ülj közéjük és tedd a tenyereidet az ülőfelületekre.",
                        "2. Feltolás: Nyújtsd ki a karokat teljesen és emeld el a testedet a székek között.",
                        "3. Lábemelés: Emeld fel a nyújtott lábaidat párhuzamosan a padlóval L betűt formálva.",
                        "4. Kitartás: Tartsd meg a pozíciót a hasfal kőkemény szorításával."
                    ),
                    commonMistakesHu = listOf(
                        "A vállak felcsúsznak a fülekhez (nincs meg a depresszió).",
                        "A lábak lelógása vízszintes alá."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_handstand_against_wall",
                    nameHu = "Fal melletti Kézenállás tartás",
                    targetMuscle = "Vállöv, Csuklók, Trapéz, Mély törzsizmok",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 3,
                    repsOrSec = "35 másodperc",
                    isTimer = true,
                    durationSeconds = 35,
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Lendülj kézenállásba falhoz támaszkodva vagy sétálj fel a falon hátrafelé. Told ki erősen a vállaidat a füledhez.",
                    tipsHu = "Feszítsd a hasizmot, a feneket és a combokat, a test egyenes egyvonalú maradjon.",
                    breathingTipHu = "Nyugodt és egyenletes rekeszizom légzés.",
                    equipmentHu = "Szoba fal",
                    easierAlternativeHu = "Falon felmászós kézenállás 45 fokos szögben (Wall Walk)",
                    harderAlternativeHu = "Fal melletti kézenállásos fekvőtámasz (HSPU)",
                    stepByStepStepsHu = listOf(
                        "1. Kézelhelyezés: Tedd a tenyereket a faltól kb. 15-20 cm-re vállszélességben.",
                        "2. Fellendülés: Egyik lábbal lendülj fel a falhoz puha sarokérintéssel a falon.",
                        "3. Vállkitolás: Told el aktívan a padlót magadtól a lapockák és vállak maximális kitolásával.",
                        "4. Testvonal: Szorítsd össze a lábakat, feszítsd a hasat és tartsd a pozíciót."
                    ),
                    commonMistakesHu = listOf(
                        "Túlzott banánhát (homorítás a törzsfeszítés hiánya miatt).",
                        "A vállak passzív beesése."
                    )
                )
            )
        ),

        // 8. 10 Perc Reggeli Anyagcsere Pörgető
        CalisthenicsRoutine(
            id = "routine_express_morning",
            titleHu = "10 Perc Reggeli Anyagcsere Pörgető",
            subtitleHu = "Gyors, frissítő saját testsúlyos ébresztő a zsírégetésért",
            difficulty = DifficultyLevel.BEGINNER,
            category = WorkoutCategory.EXPRESS,
            estimatedMinutes = 10,
            totalCaloriesBurn = 110,
            descriptionHu = "Rövid, de rendkívül hatékony reggeli ébresztő rutin. Felébreszti az izmokat, beindítja az anyagcserét és energiával tölt fel anélkül, hogy kimerítene.",
            focusAreaHu = "Mobilitás • Anyagcsere • Ébredés",
            recommendedScheduleHu = "Minden reggel felkelés után",
            requiredEquipmentHu = "100% Otthoni (Nincs szükség semmire)",
            warmUpHu = "1 perc mélylégzés és nyújtózás az ágy mellett",
            coolDownHu = "1 perc levezető törzsfordítás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_arm_circles_squat_pulse",
                    nameHu = "Karkörzés guggolás pulzálással",
                    targetMuscle = "Comb, Vállak, Szív-érrendszer",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 2,
                    repsOrSec = "40 másodperc",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 30,
                    instructionsHu = "Guggolj le félállásba, tartsd a combjaidat feszítve, és végezz dinamikus nagy karkörzéseket előre-hátra.",
                    tipsHu = "Nyisd a mellkast minden körzésnél.",
                    breathingTipHu = "Mély beszívás a karok emelésekor, kifújás leengedéskor.",
                    equipmentHu = "Eszköz nélküli",
                    easierAlternativeHu = "Állva végzett karkörzés guggolás nélkül",
                    harderAlternativeHu = "Mélyebb guggolás pulzálással",
                    stepByStepStepsHu = listOf(
                        "1. Terpeszállás: Állj vállszéles terpeszben.",
                        "2. Félguggolás: Engedd le a csípőd félig és tartsd meg ezt az aktív pozíciót.",
                        "3. Karkörzés: Végezz széles, dinamikus körzéseket mindkét karral.",
                        "4. Irányváltás: 20 másodperc után fordítsd meg a karkörzés irányát."
                    ),
                    commonMistakesHu = listOf(
                        "A hát meggörbülése.",
                        "Túl kicsi karkörzések."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_inchworms",
                    nameHu = "Araszoló (Inchworm Walkouts)",
                    targetMuscle = "Combhajlító nyújtás, Vállöv, Hasizom",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 2,
                    repsOrSec = "8 ismétlés",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Állásból hajolj le a talajra nyújtott lábbal, lépegess előre a kezeiddel fekvőtámasz tartásba, majd lépegess vissza és állj fel.",
                    tipsHu = "Érezd a combhajlítók kellemes nyúlását a lehajláskor.",
                    breathingTipHu = "Előrelépkedéskor beszívás, visszalépkedéskor és felálláskor kifújás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Enyhén hajlított térdekkel végzett araszolás",
                    harderAlternativeHu = "A fekvőtámasz pozíció végén egy fekvőtámasz beiktatása",
                    stepByStepStepsHu = listOf(
                        "1. Állás: Állj zárt lábakkal egyenesen.",
                        "2. Lehajlás: Érintsd meg a tenyereiddel a talajt a lábfejek előtt.",
                        "3. Előrelépkedés: Lépegess előre kézen járva nyújtott karú deszka helyzetig.",
                        "4. Visszalépkedés: Lépegess vissza a kezeiddel a lábfejekhez és állj fel kihúzva magad."
                    ),
                    commonMistakesHu = listOf(
                        "A derék lógatása a deszka pozíció elérésekor.",
                        "Kapkodó lépkedés."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_bear_crawl_hold",
                    nameHu = "Medveállás tartás (Bear Crawl Hold)",
                    targetMuscle = "Mély hasizmok, Combok, Vállöv",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 2,
                    repsOrSec = "30 másodperc",
                    isTimer = true,
                    durationSeconds = 30,
                    caloriesBurnEstimate = 25,
                    instructionsHu = "Négykézláb helyzetből emeld el a térdeidet 3 centire a talajtól, és tartsd meg merev háttal.",
                    tipsHu = "A térdek maradjanak nagyon közel a padlóhoz (ne emeld magasra).",
                    breathingTipHu = "Egyenletes, kontrollált rekeszizom légzés.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "15 másodperces medvetartás 5 másodperc pihenőkkel",
                    harderAlternativeHu = "Medvejárás előre és hátra kis lépésekkel",
                    stepByStepStepsHu = listOf(
                        "1. Négykézláb: Tenyerek a vállak alatt, térdek a csípő alatt derékszögben.",
                        "2. Lábujjak betámasztása: Támaszkodj a lábujjakon.",
                        "3. Térdemelés: Emeld el a térdeidet 2-3 cm-re a talaj felett.",
                        "4. Tartás: Feszítsd meg a törzsed és tartsd meg a pozíciót 30 másodpercig."
                    ),
                    commonMistakesHu = listOf(
                        "A fenék feltolása a magasba.",
                        "A derék beesése."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_flow_cobra_downward_dog",
                    nameHu = "Kobra - Lefelé néző kutya átmenet",
                    targetMuscle = "Mellkasnyitás, Gerinc mobilitás, Vádli és combnyújtás",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 2,
                    repsOrSec = "8 folyamatos átmenet",
                    caloriesBurnEstimate = 20,
                    instructionsHu = "Fekvőtámaszból engedd le a csípőd kobrapózba nyitva a mellkast, majd told fel a csípőd a magasba lefelé néző kutyapózba a sarkakat a talajhoz nyomva.",
                    tipsHu = "Finom, folyamatos jógaszerű áramlással végezd a mozdulatot.",
                    breathingTipHu = "Kobra pózban mély beszívás, lefelé néző kutyában hosszas kilégzés.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Térdelésből kobrába és gyermekpózba áttérés",
                    harderAlternativeHu = "Mindkét pozíció 3-3 másodpercig tartva mély nyújtással",
                    stepByStepStepsHu = listOf(
                        "1. Kobra póz: Hasonfekvésből nyomd fel a mellkasodat kinyújtott karral, a tekintet előre nézzen.",
                        "2. Átmenet: Billentsd a lábujjakat a talajra, és told fel a medencédet a magasba.",
                        "3. Lefelé néző kutya: Húzd be a fejed a karok közé, told a mellkast a combok felé és nyomd a sarkaidat a talajhoz.",
                        "4. Visszafolyás: Hullámszerű mozdulattal ereszkedj vissza kobrába."
                    ),
                    commonMistakesHu = listOf(
                        "A vállak felhúzása a fülekhez kobra pózban.",
                        "Görbe hát a lefelé néző kutyában (érdemes kicsit hajlítani a térdet ha kötött a combhajlító)."
                    )
                )
            )
        ),

        // =========================================================================
        // ⛓️ BÖRTÖN EDZÉS (CONVICT CONDITIONING / CELL WORKOUTS)
        // =========================================================================

        // 1. Fegyencedzés Mester Hatos
        CalisthenicsRoutine(
            id = "routine_prison_big_six",
            titleHu = "Fegyencedzés: A Mester Hatos (The Big 6)",
            subtitleHu = "Nyers erőépítés 2x2 méteres szűk cellatérben nulla felszereléssel",
            difficulty = DifficultyLevel.ADVANCED,
            category = WorkoutCategory.PRISON,
            estimatedMinutes = 35,
            totalCaloriesBurn = 340,
            descriptionHu = "A legendás fegyencedzés (Convict Conditioning) 6 fundamentális alapgyakorlata. Maximális szilárdságú inak, acélos izomzat és funkcionális nyers erő kizárólag a saját testsúlyoddal.",
            focusAreaHu = "Nyers funkcionális erő • Teljes test • Ízületi stabilitás",
            recommendedScheduleHu = "Heti 3 alkalom (Hétfő - Szerda - Péntek)",
            requiredEquipmentHu = "100% Zéró Eszköz (Padló, Fal, Ajtófélfa/Asztal/Törölköző)",
            warmUpHu = "3 perc karkörzés, csukló- és bokaátmozgatás, 20 lassú törzskörzés",
            coolDownHu = "3 perc híd- és mellkasnyújtás, mély lélegzetvételek",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_prison_diamond_pushups",
                    nameHu = "Gyémánt börtön fekvőtámasz (Diamond Push-up)",
                    targetMuscle = "Tricepsz, Belső mellizom, Elülső delta",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "12-15 ismétlés",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Helyezd a hüvelyk- és mutatóujjaidat egymás mellé úgy, hogy egy háromszöget (gyémántot) formáljanak a mellkasod alatt. Engedd le a szegycsontodat a kezeid középpontjához, majd robbanékonyan told fel magad.",
                    tipsHu = "A könyökök simuljanak a törzsed mellé 45 fokban, a has és farizom kőkeményen megfeszítve.",
                    breathingTipHu = "Leengedéskor beszívás, a feltolás legfelső szakaszában határozott kilégzés.",
                    equipmentHu = "Saját testsúly",
                    easierAlternativeHu = "Térdelő gyémánt fekvőtámasz",
                    harderAlternativeHu = "Lábak székre feltéve (Döntött gyémánt fekvőtámasz)",
                    stepByStepStepsHu = listOf(
                        "1. Alapállás: Helyezd a kezeidet a mellkasod alá, az ujjak alkossanak gyémánt formát.",
                        "2. Törzsfeszítés: Feszítsd meg a combokat, a feneket és a hasfalat egy egyenes vonalba.",
                        "3. Leengedés: Lassan, 2 másodperc alatt engedd le a mellkasodat a kezeid fölé.",
                        "4. Feltolás: Feszítsd ki a karokat és a tricepszet a felső végponton."
                    ),
                    commonMistakesHu = listOf(
                        "A derék beesése vagy túlzott feltolása.",
                        "A könyökök oldalra kicsapódása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_deep_prisoner_squats",
                    nameHu = "Mély börtön guggolás tarkóra tett kézzel (Prisoner Squats)",
                    targetMuscle = "Négyfejű combizom, Combhajlító, Farizom, Hátizmok",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "25 ismétlés",
                    caloriesBurnEstimate = 50,
                    instructionsHu = "Kulcsold össze a kezeidet a tarkódon, húzd hátra a könyökeidet és a lapockáidat. Guggolj le teljesen mélyre a sarok felemelkedése nélkül, megőrizve az egyenes gerincet.",
                    tipsHu = "A tarkóra tett kéz kényszeríti a hátizmokat a felsőtest stabilizálására, így nem tudsz előre görnyedni.",
                    breathingTipHu = "Leülésnél mély hasi belégzés, felálláskor fújd ki a levegőt.",
                    equipmentHu = "Eszköz nélkül",
                    easierAlternativeHu = "Vízszintesig végzett standard guggolás",
                    harderAlternativeHu = "1 lábas pisztoly guggolás (Pistol squat) rásegítéssel",
                    stepByStepStepsHu = listOf(
                        "1. Kiinduló helyzet: Tarkóra tett kéz, kinyitott könyökök, stabil terpesz.",
                        "2. Leereszkedés: Told hátra a medencét, és engedd le a feneked a térdvonal alá.",
                        "3. Megállítás: 1 másodperces szünet a mélyponton izomfeszítéssel.",
                        "4. Dinamikus felállás: Robbanékony tolás a teljes talpfelületen keresztül."
                    ),
                    commonMistakesHu = listOf(
                        "A könyökök előre engedése.",
                        "A sarkak elemelkedése a padlóról."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_doorframe_towel_pulls",
                    nameHu = "Törölközős / Ajtófélfás börtön evezés (Towel Bodyweight Rows)",
                    targetMuscle = "Széles hátizom, Hátulsó delta, Bicepsz, Marokerő",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Tekerj egy masszív törölközőt egy stabil oszlop, ajtókilincs vagy ajtófélfa köré. Dőlj hátra feszes testtel, majd a lapockáid összezárásával és a karok behajlításával húzd a mellkasodat a kezeidhez.",
                    tipsHu = "Minél mélyebbre lépsz a lábaddal a támaszpont alá, annál nehezebb a mozdulat.",
                    breathingTipHu = "Húzáskor fújd ki a levegőt és tartsd meg 1 másodpercig, leengedéskor lélegezz be.",
                    equipmentHu = "Törölköző vagy Ajtófélfa",
                    easierAlternativeHu = "Magasabb állásszög (enyhébb hátradőlés)",
                    harderAlternativeHu = "Egykezes börtön evezés",
                    stepByStepStepsHu = listOf(
                        "1. Rögzítés: Erősen fogd meg a törölköző két végét.",
                        "2. Dőlésszög: Lépj előre a lábaiddal, nyújtsd ki a karokat, a test egyenes faág.",
                        "3. Húzás: Húzd össze a lapockáidat és rántsd a mellkasodat a rögzítéshez.",
                        "4. Csúcsfeszítés: Zárd a hátizmokat a legfelső ponton."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő hátrahagyása (megtört testvonal).",
                        "Csak karból húzás lapockazárás nélkül."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_candle_leg_raises",
                    nameHu = "Fekvő lábemelés gyertyába (Candle Core Raises)",
                    targetMuscle = "Alsó hasizom, Egyenes hasizom, Törzsstabilitás",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Feküdj a hátadra a padlón. Nyújtott lábakkal emeld fel a lábaidat 90 fokig, majd a hasizmaid erejéből told fel a medencédet a magasba gyertyaállásba, majd lassan kontrollálva engedd vissza a föld fölé.",
                    tipsHu = "Ne lendületből dobd fel a csípőd, hanem a hasizmok alsó szakaszának feszítésével!",
                    breathingTipHu = "Feltoláskor préseld ki az összes levegőt a hasadból, leengedéskor beszívás.",
                    equipmentHu = "Szőnyeg / Padló",
                    easierAlternativeHu = "Hajlított térdes lábemelés",
                    harderAlternativeHu = "Lábemelés megállítással a padló felett 5 centire 3 másodpercig",
                    stepByStepStepsHu = listOf(
                        "1. Alaphelyzet: Hanyattfekvés, tenyerek a talajon a csípő mellett.",
                        "2. Lábemelés: Emeld a feszes lábakat függőlegesig.",
                        "3. Gyertya tolás: Nyomd fel a csípődet egyenesen a plafon felé.",
                        "4. Negatív szakasz: Lassan, csigolyáról csigolyára engedd le a törzsed."
                    ),
                    commonMistakesHu = listOf(
                        "A lábak fej mögé dobása a felfelé tolás helyett.",
                        "A derék felcsapódása a talajra leengedéskor."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_bridge_hold",
                    nameHu = "Börtön híd feszítés (Convict Full Bridge)",
                    targetMuscle = "Gerincmerevítők, Farizom, Hátizmok, Váll mobilitás",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 3,
                    repsOrSec = "30 másodperc tartás",
                    isTimer = true,
                    durationSeconds = 30,
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Feküdj hanyatt, húzd fel a sarkaidat a fenekedhez, kezeidet tedd a füleid mellé az ujjakkal a vállak felé nézve. Nyomd fel a csípődet és a mellkasodat teljes hídba, feszítve az egész hátsó láncot.",
                    tipsHu = "A híd a fegyencedzés legfontosabb gerincvédő és tartásjavító gyakorlata, ellensúlyozza az egész napos ülést.",
                    breathingTipHu = "Egyenletes, lassú mellkasi légzés a híd legfelső pontján.",
                    equipmentHu = "Padló / Szőnyeg",
                    easierAlternativeHu = "Csípőemelés talajon fekve (Glute Bridge)",
                    harderAlternativeHu = "Híd séta (kézzel-lábbal lépegetés híd pozícióban)",
                    stepByStepStepsHu = listOf(
                        "1. Beállás: Sarkak a talajon, tenyerek a fülek mellett betámasztva.",
                        "2. Felfelé nyomás: Nyomd ki a karokat és a lábakat egyszerre.",
                        "3. Ív feszítése: Nyisd a mellkast a karok felé, feszítsd meg a farizmokat.",
                        "4. Tartás: Tartsd meg stabilan 30 másodpercig."
                    ),
                    commonMistakesHu = listOf(
                        "A könyökök túlzott széttárása.",
                        "A farizom elernyesztése (a derékra terhelés helyett)."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_pike_wall_pushups",
                    nameHu = "Falhoz támasztott / Pike vállnyomás (Inverted Prison Press)",
                    targetMuscle = "Vállizmok, Felső mellkas, Trapézizom, Tricepsz",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "10-12 ismétlés",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Vedd fel a fordított V (Pike) pozíciót a padlón, vagy tedd a lábaidat a falra/ágyra. Engedd le a fejed tetejét a kezeid elé a talajra, majd nyomd ki magad vállból.",
                    tipsHu = "A fej a kezek elé érkezzen le (egy háromszöget formálva), ne a kezek vonalában!",
                    breathingTipHu = "Leengedéskor beszívás, kinyomáskor határozott kilégzés.",
                    equipmentHu = "Padló vagy Fal",
                    easierAlternativeHu = "Talajon végzett enyhe terpesz Pike fekvőtámasz",
                    harderAlternativeHu = "Falon végzett kézenállásos fekvőtámasz (Handstand push-up)",
                    stepByStepStepsHu = listOf(
                        "1. Pike állás: Emeld a csípődet a magasba merev lábakkal és karokkal.",
                        "2. Dőlés: Helyezd a testsúlyodat a tenyereidre és a vállakra.",
                        "3. Leengedés: Hajlítsd a könyököket és érintsd a homlokod a padlóhoz.",
                        "4. Vállból nyomás: Told fel magad a kiinduló V pozícióba."
                    ),
                    commonMistakesHu = listOf(
                        "A fej behúzása a mellkashoz leengedés helyett.",
                        "A könyökök oldalra engedése ahelyett hogy hátrafelé mozognának."
                    )
                )
            )
        ),

        // 2. Börtönudvari Burpee & Fekvőtámasz Piramis
        CalisthenicsRoutine(
            id = "routine_prison_burpee_pyramid",
            titleHu = "Börtönudvari Burpee & Fekvőtámasz Piramis",
            subtitleHu = "A hírhedt 10-től 1-ig visszaszámláló túlélő kondíció edzés",
            difficulty = DifficultyLevel.ADVANCED,
            category = WorkoutCategory.PRISON,
            estimatedMinutes = 20,
            totalCaloriesBurn = 280,
            descriptionHu = "Kőkemény börtönudvari kondicionáló edzés. 10 burpee + 10 fekvőtámasz, majd 9-9, 8-8 egészen 1-1-ig megállás nélkül. Zsírégetés és tüdőkapacitás a maximumon.",
            focusAreaHu = "Zsírégetés • Tüdőkapacitás • Mentális szívósság",
            recommendedScheduleHu = "Heti 2 alkalommal állóképesség fokozására",
            requiredEquipmentHu = "100% Zéró Eszköz",
            warmUpHu = "2 perc helyben kocogás és 20 db jumping jack",
            coolDownHu = "2 perc légzésnyugtatás és combnyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_prison_burpee_chest_to_floor",
                    nameHu = "Mellkas talajig érő börtön burpee (Prison Yard Burpee)",
                    targetMuscle = "Teljes test, Szív- és érrendszer, Mell, Váll, Comb",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 5,
                    repsOrSec = "10, 8, 6, 4, 2 ismétléses körök",
                    caloriesBurnEstimate = 80,
                    instructionsHu = "Állásból guggolj le, ugorj hátra fekvőtámaszba, érintsd le a mellkasodat a talajra, majd robbanékonyan ugorj vissza a lábaid közé és ugorj fel tapssal a fejed felett.",
                    tipsHu = "Tarts folyamatos ritmust! Ne állj meg a körök között, lélegezz egyenletesen!",
                    breathingTipHu = "Leugráskor belégzés, felugrásnál robbanékony kilégzés.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "Fekvőtámasz nélküli Sprawl (felugrás leengedés nélkül)",
                    harderAlternativeHu = "Tuck jump burpee (felugrásnál térdfelhúzás a mellkashoz)",
                    stepByStepStepsHu = listOf(
                        "1. Guggolás: Tenyerek a talajra a lábak elé.",
                        "2. Hátraugrás: Lábak hátra dobása merev törzzsel.",
                        "3. Mellkasérintés: Engedd le magad teljesen a padlóra.",
                        "4. Visszaugrás és felugrás: Húzd a lábaidat a kezeidhez és ugorj fel."
                    ),
                    commonMistakesHu = listOf(
                        "A derék leejtése leugráskor.",
                        "Félbehagyott felállás."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_explosive_pushups",
                    nameHu = "Robbanékony börtön fekvőtámasz (Explosive Prison Push-up)",
                    targetMuscle = "Nagy mellizom, Tricepsz, Elülső delta",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 5,
                    repsOrSec = "10, 8, 6, 4, 2 ismétléses körök",
                    caloriesBurnEstimate = 60,
                    instructionsHu = "Végezz fekvőtámaszt olyan dinamikus feltolással, hogy a tenyereid 2-3 centire elhagyják a talajt a felső holtponton.",
                    tipsHu = "A leengedés legyen kontrollált, a feltolás viszont mint a puskagolyó!",
                    breathingTipHu = "Feltoláskor azonnali kilégzés.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "Standard feszes fekvőtámasz megállítással",
                    harderAlternativeHu = "Tapsos fekvőtámasz a levegőben",
                    stepByStepStepsHu = listOf(
                        "1. Törzstartás: Feszes plank pozíció.",
                        "2. Leengedés: Mellkas a talaj felett 1 centire.",
                        "3. Robbanás: Maximális erővel lökd el magad a padlótól.",
                        "4. Puha érkezés: Rugalmasan tompíts a könyökökkel."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő beesése.",
                        "Kemény ráesés a csuklókra tompítás nélkül."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_prison_mountain_climber_sprint",
                    nameHu = "Börtön hegymászó sprint (Cell Mountain Climbers)",
                    targetMuscle = "Hasizmok, Csípőhorpasz, Vállak, Kardió",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "45 másodperc sprint",
                    isTimer = true,
                    durationSeconds = 45,
                    caloriesBurnEstimate = 50,
                    instructionsHu = "Fekvőtámasz tartásból váltott lábbal húzd a térdeidet a mellkasodhoz gyors, ritmusos sprintekkel.",
                    tipsHu = "A feneked maradjon lent, a vállak pontosan a tenyerek felett.",
                    breathingTipHu = "Folyamatos, gyors orr-száj ritmikus légzés.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "Lassú, kontrollált térdhúzások 30 másodpercig",
                    harderAlternativeHu = "Keresztezett hegymászó (térd az ellentétes könyökhöz)",
                    stepByStepStepsHu = listOf(
                        "1. Plank pozíció: Tenyerek a talajon stabilan.",
                        "2. Térdhúzás: Rántsd a jobb térded a mellkasodhoz.",
                        "3. Lábváltás: Ugrással válts lábat a levegőben.",
                        "4. Sprint: Tartsd a maximális tempót 45 másodpercig."
                    ),
                    commonMistakesHu = listOf(
                        "A fenék magasra emelése.",
                        "A testsúly hátra tolása a lábakra."
                    )
                )
            )
        ),

        // =========================================================================
        // 🪖 KATONAI & TAKTIKAI EDZÉS (MILITARY & TACTICAL BOOT CAMP)
        // =========================================================================

        // 1. Különleges Erők Taktikai Erőnléti Teszt
        CalisthenicsRoutine(
            id = "routine_military_navy_seal_prep",
            titleHu = "Különleges Erők: Taktikai Erőnléti Teszt",
            subtitleHu = "Navy SEAL és Ranger felmérő szintű brutális köredzés",
            difficulty = DifficultyLevel.ADVANCED,
            category = WorkoutCategory.MILITARY,
            estimatedMinutes = 30,
            totalCaloriesBurn = 310,
            descriptionHu = "A különleges katonai alakulatok fizikai felmérőjén használt szabványosított gyakorlatok. Taktikai fekvőtámaszok kézfelemeléssel, kommandós felülések és robbanékony kitörések.",
            focusAreaHu = "Taktikai erő • Törzsstabilitás • Harcászati állóképesség",
            recommendedScheduleHu = "Heti 3 alkalom (Hétfő - Szerda - Szombat)",
            requiredEquipmentHu = "100% Zéró Eszköz (Katonai testsúlyos edzés)",
            warmUpHu = "3 perc csillagugrás, karkörzés és katonai magas térdemelés",
            coolDownHu = "3 perc alsóhát-, comb- és lapockanyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_military_hand_release_pushups",
                    nameHu = "Taktikai mellkashoz engedett fekvőtámasz (Hand-Release Push-up)",
                    targetMuscle = "Mellizom, Hátizmok, Tricepsz, Törzs",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "15 ismétlés",
                    caloriesBurnEstimate = 45,
                    instructionsHu = "Engedd le a teljes testedet a padlóra. A legmélyebb ponton emeld fel a tenyereidet a levegőbe 1 centire (kikapcsolva a lendületet), majd tedd vissza és feszített törzzsel nyomd ki magad.",
                    tipsHu = "Ez a hivatalos amerikai hadsereg (ACFT) fekvőtámasz szabvány. Kizárja a csalást és teljes mozgástartományt garantál.",
                    breathingTipHu = "Leérkezésnél kifújás, felemelésnél beszívás, kinyomásnál erőteljes kilégzés.",
                    equipmentHu = "Padló / Szőnyeg",
                    easierAlternativeHu = "Térdelő Hand-release fekvőtámasz",
                    harderAlternativeHu = "Hand-release + Superman kar- és lábemelés a talajon",
                    stepByStepStepsHu = listOf(
                        "1. Fekvőtámasz alapállás.",
                        "2. Teljes leereszkedés a talajra.",
                        "3. Kéz felemelése a talajról 1 másodpercre.",
                        "4. Tenyerek vissza és merev törzzsel felnyomás."
                    ),
                    commonMistakesHu = listOf(
                        "Kígyózó mozdulat (mellkas indul előbb mint a csípő).",
                        "A kéz felemelésének elhagyása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_strict_situps",
                    nameHu = "Katonai szabvány felülés (Strict Military Sit-ups)",
                    targetMuscle = "Egyenes hasizom, Mély hasizmok, Csípőhorpasz",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "25 ismétlés",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Hanyattfekvésben hajlítsd be a térdeidet 90 fokban. Kulcsold a kezeidet a mellkasodon (vagy a füleid mellett), és emeld fel a felsőtestedet úgy, hogy a könyökeid elérjék a combjaidat.",
                    tipsHu = "Ne rángasd a nyakad! A mozgást a hasizmok összehúzása irányítsa.",
                    breathingTipHu = "Felfelé emelkedéskor erőteljes fújás, leengedéskor mély beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Hasprés talajon megemelt lábbal",
                    harderAlternativeHu = "Lassú leengedés 3 másodperces negatív szakasszal",
                    stepByStepStepsHu = listOf(
                        "1. Kiinduló helyzet: Hanyattfekvés, térdek behajlítva.",
                        "2. Összehúzódás: Hasizmok préselése.",
                        "3. Felülés: Emelkedj fel a combok érintéséig.",
                        "4. Kontrollált leengedés a lapockák érintéséig."
                    ),
                    commonMistakesHu = listOf(
                        "A fej és nyak rángatása a kezekkel.",
                        "A derék felcsapódása a földre."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_8_count_bodybuilders",
                    nameHu = "8 ütemű katonai fekvőtámasz (8-Count Tactical Bodybuilder)",
                    targetMuscle = "Teljes test, Mell, Váll, Combok, Core, Állóképesség",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 3,
                    repsOrSec = "10 ismétlés",
                    caloriesBurnEstimate = 65,
                    instructionsHu = "A tengerészgyalogság legendás gyakorlata: 1. Guggolás, 2. Láb hátraugrás plankbe, 3. Terpesz ugrás a lábakkal, 4. Zárás, 5. Fekvőtámasz leengedés, 6. Kinyomás, 7. Láb visszaugrás guggolásba, 8. Felállás ugrással.",
                    tipsHu = "Számold a mozdulatokat fejben katonás fegyelemmel 1-től 8-ig!",
                    breathingTipHu = "Minden ütemnél tarts folyamatos, kontrollált ritmust.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "4 ütemű fekvőtámasz (Standard burpee)",
                    harderAlternativeHu = "Dupla fekvőtámaszos 10 ütemű verzió",
                    stepByStepStepsHu = listOf(
                        "1. Guggolás kéztámasszal.",
                        "2. Láb hátraugrás fekvőtámaszba.",
                        "3. Terpesz ugrás lábbal.",
                        "4. Zárás.",
                        "5. Fekvőtámasz le.",
                        "6. Fekvőtámasz fel.",
                        "7. Láb visszaugrás.",
                        "8. Felugrás állásba."
                    ),
                    commonMistakesHu = listOf(
                        "Az ütemek összecsapása és kapkodás.",
                        "A fekvőtámasz félbehagyása."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_marine_jump_lunges",
                    nameHu = "Tengerészgyalogos ugró kitörés (Marine Jump Lunges)",
                    targetMuscle = "Combizmok, Farizom, Robbanékony láberő",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "20 váltott lábas ismétlés",
                    caloriesBurnEstimate = 50,
                    instructionsHu = "Kitörés pozícióból ugorj fel dinamikusan a levegőbe, válts lábat menet közben, és érkezz mély kitörésbe a másik lábbal tompítva a mozgást.",
                    tipsHu = "A hátul lévő térd közelítse meg a talajt, de ne csapódjon hozzá!",
                    breathingTipHu = "Minden felugrásnál fújd ki a levegőt.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "Helyben végzett váltott lábú dinamikus kitörés ugrás nélkül",
                    harderAlternativeHu = "1,5-ös ugró kitörés (leérkezésnél még egy alsó pulzálás)",
                    stepByStepStepsHu = listOf(
                        "1. Kitörés: Jobb láb elöl, bal térd a talaj felett.",
                        "2. Robbanás: Ugorj fel függőlegesen a magasba.",
                        "3. Lábváltás a levegőben.",
                        "4. Lágy érkezés bal lábbal elöl."
                    ),
                    commonMistakesHu = listOf(
                        "A térd befelé billenése érkezéskor.",
                        "A térd kemény padlóra verődése."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_commando_plank",
                    nameHu = "Kommandós plankből-fekvőtámasz (Plank-to-Pushup Combat Switch)",
                    targetMuscle = "Vállöv, Tricepsz, Egyenes és ferde hasizom",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 3,
                    repsOrSec = "16 váltott karú ismétlés",
                    caloriesBurnEstimate = 40,
                    instructionsHu = "Alkartámaszos plankből nyomd fel magad tenyértámaszos fekvőtámaszba először az egyik karoddal, majd ereszkedj vissza könyökre, a következő ismétlést a másik karral indítva.",
                    tipsHu = "A csípőd tartsd a lehető legstabilabban, ne engedd ide-oda billegni!",
                    breathingTipHu = "Felnyomáskor kilégzés, leereszkedéskor beszívás.",
                    equipmentHu = "Szőnyeg / Padló",
                    easierAlternativeHu = "Térdelve végzett kommandós plank",
                    harderAlternativeHu = "Minden felnyomás után egy teljes fekvőtámasz beiktatása",
                    stepByStepStepsHu = listOf(
                        "1. Alkartámasz plank: Merev törzs.",
                        "2. Jobb kéz feltámasztása: Nyomd fel a jobb kart.",
                        "3. Bal kéz feltámasztása: Felső fekvőtámasz pozíció.",
                        "4. Visszaereszkedés: Jobb könyök, majd bal könyök a talajra."
                    ),
                    commonMistakesHu = listOf(
                        "A csípő túlzott tekergőzése felnyomás közben.",
                        "A hasizom kiengedése."
                    )
                )
            )
        ),

        // 2. Katonai Boot Camp Zsírégető Pokol (Combat HIIT)
        CalisthenicsRoutine(
            id = "routine_military_tactical_hiit",
            titleHu = "Katonai Boot Camp: Zsírégető Pokol (Combat HIIT)",
            subtitleHu = "Nagy intenzitású harcászati köredzés zsírégetésre és szálkásításra",
            difficulty = DifficultyLevel.ADVANCED,
            category = WorkoutCategory.MILITARY,
            estimatedMinutes = 22,
            totalCaloriesBurn = 290,
            descriptionHu = "Pörgős, 40 másodperc munka / 20 másodperc pihenő ritmusú taktikai zsírégető tréning. Harctéri mozgáselemek: medvejárás, taktikai elvetődés, csillagugrás és sprint.",
            focusAreaHu = "Zsírégetés • Robbannékonyság • Taktikai állóképesség",
            recommendedScheduleHu = "Heti 2-3 alkalom zsírégetési fázisban",
            requiredEquipmentHu = "100% Zéró Eszköz",
            warmUpHu = "2 perc helyben kocogás karkörzéssel",
            coolDownHu = "2 perc mélylégzés és teljes testes nyújtás",
            exercises = listOf(
                CalisthenicsExercise(
                    id = "ex_military_sprawl",
                    nameHu = "Taktikai elvetődés és felállás (Combat Sprawls)",
                    targetMuscle = "Teljes test, Reakcióidő, Csípő robbanékonyság, Hasizom",
                    difficulty = DifficultyLevel.ADVANCED,
                    sets = 4,
                    repsOrSec = "40 mp munka / 20 mp pihenő",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 65,
                    instructionsHu = "Küzdősportos és katonai elvetődés: Dobj le a csípődet a padlóra terpesz lábakkal elkerülve a támadást, majd pattanj fel azonnal harckész alapállásba.",
                    tipsHu = "A csípőddel érkezz gyorsan a padlóhoz, és robbanékonyan húzd magad alá a lábaidat.",
                    breathingTipHu = "Leérkezésnél fújd ki a levegőt, felpattanásnál mély levegővétel.",
                    equipmentHu = "Padló",
                    easierAlternativeHu = "Lassú láb hátra lépegetés sprawl",
                    harderAlternativeHu = "Sprawl után 2 direkt ütés a levegőbe állásban",
                    stepByStepStepsHu = listOf(
                        "1. Harci állás.",
                        "2. Kezek le, lábak hátra dobása széles terpeszben.",
                        "3. Csípő letolása a talajhoz.",
                        "4. Robbanékony felugrás alapállásba."
                    ),
                    commonMistakesHu = listOf(
                        "Zárt lábbal hátraugrás terpesz helyett.",
                        "Lassú felállás."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_bear_crawl",
                    nameHu = "Taktikai medvejárás (Tactical Bear Crawl)",
                    targetMuscle = "Mély hasizom, Vállak, Combok, Koordináció",
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    sets = 4,
                    repsOrSec = "40 mp munka / 20 mp pihenő",
                    isTimer = true,
                    durationSeconds = 40,
                    caloriesBurnEstimate = 55,
                    instructionsHu = "Négykézláb helyzetben emeld el a térdeidet 3 centire a talajtól. Lépkedj előre 4 lépést ellentétes kéz-láb koordinációval, majd 4 lépést hátra a szobában.",
                    tipsHu = "A hátad legyen teljesen lapos mint egy asztallap, a térdek maradjanak a föld közelében.",
                    breathingTipHu = "Folyamatos, kontrollált légzés.",
                    equipmentHu = "Padló / Szőnyeg",
                    easierAlternativeHu = "Medvetartás helyben elmozdulás nélkül",
                    harderAlternativeHu = "Medvejárás oldalirányba is lépegetve",
                    stepByStepStepsHu = listOf(
                        "1. Kiindulás: Térdek 3 cm-rel a föld felett.",
                        "2. Előrelépés: Jobb kéz és bal láb egyszerre.",
                        "3. Következő lépés: Bal kéz és jobb láb.",
                        "4. Hátralépkedés ugyanilyen koordinációval."
                    ),
                    commonMistakesHu = listOf(
                        "A fenék feltolása a magasba.",
                        "A térdek felemelkedése."
                    )
                ),
                CalisthenicsExercise(
                    id = "ex_military_superman_arch",
                    nameHu = "Katonai szuperhős hátfeszítés (Tactical Superman Arch)",
                    targetMuscle = "Alsó hát, Gerincmerevítők, Farizom, Hátulsó delta",
                    difficulty = DifficultyLevel.BEGINNER,
                    sets = 3,
                    repsOrSec = "15 ismétlés 2 mp tartással",
                    caloriesBurnEstimate = 35,
                    instructionsHu = "Hasonfekvésben nyújtsd ki a karjaidat előre. Egyszerre emeld el a mellkasodat és a combjaidat a talajtól olyan magasra amilyenre tudod, tartsd meg 2 másodpercig, majd lassan engedd vissza.",
                    tipsHu = "A katonai felszerelés és hátizsák cipeléséhez elengedhetetlen a stabil, erős alsó hátizomzat.",
                    breathingTipHu = "Emeléskor fújd ki a levegőt és feszíts, leengedéskor beszívás.",
                    equipmentHu = "Szőnyeg",
                    easierAlternativeHu = "Csak a felsőtest emelése a lábak lent hagyásával",
                    harderAlternativeHu = "Úszó mozdulat (váltott kar- és láblendítés a levegőben)",
                    stepByStepStepsHu = listOf(
                        "1. Hasonfekvés, karok és lábak kinyújtva.",
                        "2. Feszítés: Emeld a karokat és combokat a magasba.",
                        "3. Tartás: 2 másodperces feszítés a csúcsponton.",
                        "4. Leengedés kontrollálva."
                    ),
                    commonMistakesHu = listOf(
                        "Kapkodó mozdulatok megtartás nélkül.",
                        "A fej túlzott hátracsapása."
                    )
                )
            )
        )
    )
}
