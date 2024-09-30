package igor.petrov.wear.run.domain

import igor.petrov.core.domain.util.EmptyResult
import igor.petrov.core.domain.util.Error
import kotlinx.coroutines.flow.Flow

interface ExerciseTracker {
    val heartRate: Flow<Int>
    suspend fun isHeartRateTrackingSupported(): Boolean
    suspend fun prepareExercise(): EmptyResult<ExerciseError>
    suspend fun startExercise(): EmptyResult<ExerciseError>
    suspend fun resumeExercise(): EmptyResult<ExerciseError>
    suspend fun pauseExercise(): EmptyResult<ExerciseError>
    suspend fun stopExercise(): EmptyResult<ExerciseError>
}

enum class ExerciseError: Error {
    TRACKING_NOT_SUPPORTED,
    ONGOWING_OWN_EXERCISE,
    ONGOWING_OTHER_EXERCISE,
    EXERCISE_ALREADY_ENDED,
    UNKNOWN

}