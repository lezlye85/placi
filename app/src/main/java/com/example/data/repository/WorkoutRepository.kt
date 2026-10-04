package com.example.data.repository

import com.example.data.local.CalisthenicsSetDao
import com.example.data.local.ExercisePlanDao
import com.example.data.local.ExerciseProgressDao
import com.example.data.local.InitialData
import com.example.data.local.WorkoutDao
import com.example.data.model.CalisthenicsRoutine
import com.example.data.model.CalisthenicsSetLog
import com.example.data.model.ExercisePlan
import com.example.data.model.ExerciseProgressEntry
import com.example.data.model.WorkoutCategory
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val exerciseProgressDao: ExerciseProgressDao? = null,
    private val calisthenicsSetDao: CalisthenicsSetDao? = null,
    private val exercisePlanDao: ExercisePlanDao? = null
) {
    val routines: List<CalisthenicsRoutine> = InitialData.CALISTHENICS_ROUTINES

    val allExercisePlans: Flow<List<ExercisePlan>> =
        exercisePlanDao?.getAllExercisePlans() ?: flowOf(emptyList())

    fun getExercisePlansByType(type: String): Flow<List<ExercisePlan>> =
        exercisePlanDao?.getExercisePlansByType(type) ?: flowOf(emptyList())

    fun getExercisePlansByDifficulty(difficulty: String): Flow<List<ExercisePlan>> =
        exercisePlanDao?.getExercisePlansByDifficulty(difficulty) ?: flowOf(emptyList())

    suspend fun getExercisePlanById(id: Long): ExercisePlan? = withContext(Dispatchers.IO) {
        exercisePlanDao?.getExercisePlanByIdDirect(id)
    }

    suspend fun insertExercisePlan(plan: ExercisePlan): Long = withContext(Dispatchers.IO) {
        exercisePlanDao?.insertExercisePlan(plan) ?: 0L
    }

    suspend fun insertAllExercisePlans(plans: List<ExercisePlan>): List<Long> = withContext(Dispatchers.IO) {
        exercisePlanDao?.insertAllExercisePlans(plans) ?: emptyList()
    }

    suspend fun deleteExercisePlan(plan: ExercisePlan) = withContext(Dispatchers.IO) {
        exercisePlanDao?.deleteExercisePlan(plan)
    }

    fun getRoutineById(id: String): CalisthenicsRoutine? {
        return routines.find { it.id == id }
    }

    fun getAllLogs(): Flow<List<WorkoutLog>> = workoutDao.getAllWorkoutLogs()

    fun getLogsForDate(date: String): Flow<List<WorkoutLog>> = workoutDao.getWorkoutsForDate(date)

    fun getWorkoutsBetweenDates(startDate: String, endDate: String): Flow<List<WorkoutLog>> =
        workoutDao.getWorkoutsBetweenDates(startDate, endDate)

    fun getTotalCaloriesBurnedForDate(date: String): Flow<Int?> =
        workoutDao.getTotalCaloriesBurnedForDate(date)

    fun getTotalDurationForDate(date: String): Flow<Int?> =
        workoutDao.getTotalWorkoutDurationForDate(date)

    fun getTotalWorkoutCount(): Flow<Int> = workoutDao.getTotalWorkoutCount()

    suspend fun logWorkout(
        date: String,
        routineId: String,
        routineTitle: String,
        durationMinutes: Int,
        caloriesBurned: Int,
        notes: String = ""
    ): Long = withContext(Dispatchers.IO) {
        workoutDao.insertWorkoutLog(
            WorkoutLog(
                date = date,
                routineId = routineId,
                routineTitle = routineTitle,
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned,
                notes = notes
            )
        )
    }

    suspend fun deleteWorkoutLog(log: WorkoutLog) = withContext(Dispatchers.IO) {
        workoutDao.deleteWorkoutLog(log)
    }

    suspend fun deleteWorkoutLogById(id: Long) = withContext(Dispatchers.IO) {
        workoutDao.deleteWorkoutLogById(id)
    }

    // --- Individual Exercise Progression Tracking ---
    fun getAllExerciseProgress(): Flow<List<ExerciseProgressEntry>>? =
        exerciseProgressDao?.getAllExerciseProgress()

    fun getProgressForExercise(exerciseId: String): Flow<List<ExerciseProgressEntry>>? =
        exerciseProgressDao?.getProgressForExercise(exerciseId)

    fun getRecentProgressForExercise(exerciseId: String): Flow<ExerciseProgressEntry?>? =
        exerciseProgressDao?.getRecentProgressForExercise(exerciseId)

    fun getMaxRepsForExercise(exerciseId: String): Flow<Int?>? =
        exerciseProgressDao?.getMaxRepsForExercise(exerciseId)

    fun getMaxWeightForExercise(exerciseId: String): Flow<Double?>? =
        exerciseProgressDao?.getMaxWeightForExercise(exerciseId)

    fun getPersonalRecords(): Flow<List<ExerciseProgressEntry>>? =
        exerciseProgressDao?.getPersonalRecords()

    suspend fun logExerciseProgress(
        exerciseId: String,
        exerciseName: String,
        date: String,
        setsCompleted: Int,
        repsCompleted: Int,
        weightAddedKg: Double = 0.0,
        durationSeconds: Int = 0,
        caloriesBurned: Int = 0,
        isPersonalRecord: Boolean = false,
        notes: String = ""
    ): Long = withContext(Dispatchers.IO) {
        exerciseProgressDao?.insertExerciseProgress(
            ExerciseProgressEntry(
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                date = date,
                setsCompleted = setsCompleted,
                repsCompleted = repsCompleted,
                weightAddedKg = weightAddedKg,
                durationSeconds = durationSeconds,
                caloriesBurned = caloriesBurned,
                isPersonalRecord = isPersonalRecord,
                notes = notes
            )
        ) ?: 0L
    }

    suspend fun deleteExerciseProgress(entry: ExerciseProgressEntry) = withContext(Dispatchers.IO) {
        exerciseProgressDao?.deleteExerciseProgress(entry)
    }

    // --- Calisthenics Set & Performance History Tracking (Börtön & Katonai) ---
    fun getAllCalisthenicsSets(): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getAllSetLogs() ?: emptyFlow()

    fun getPrisonPerformanceHistory(): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getPrisonPerformanceHistory() ?: emptyFlow()

    fun getMilitaryPerformanceHistory(): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getMilitaryPerformanceHistory() ?: emptyFlow()

    fun getSetsForRoutineAndDate(routineId: String, date: String): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getSetsForRoutineAndDate(routineId, date) ?: emptyFlow()

    fun getSetsForExercise(exerciseId: String): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getSetsForExercise(exerciseId) ?: emptyFlow()

    fun getRecentPerformanceHistory(limit: Int = 50): Flow<List<CalisthenicsSetLog>> =
        calisthenicsSetDao?.getRecentPerformanceHistory(limit) ?: emptyFlow()

    suspend fun logCalisthenicsSet(setLog: CalisthenicsSetLog): Long = withContext(Dispatchers.IO) {
        calisthenicsSetDao?.insertSetLog(setLog) ?: 0L
    }

    suspend fun logCalisthenicsSets(setLogs: List<CalisthenicsSetLog>): List<Long> = withContext(Dispatchers.IO) {
        calisthenicsSetDao?.insertAllSetLogs(setLogs) ?: emptyList()
    }

    suspend fun deleteCalisthenicsSetById(id: Long) = withContext(Dispatchers.IO) {
        calisthenicsSetDao?.deleteSetLogById(id)
    }
}
