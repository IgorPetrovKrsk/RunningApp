package igor.petrov.wear.run.presentation

import igor.petrov.core.presentation.ui.UiText
import igor.petrov.wear.run.domain.ExerciseError

fun ExerciseError.toUiText(): UiText?{
    return when(this) {
        ExerciseError.ONGOWING_OWN_EXERCISE,
        ExerciseError.ONGOWING_OTHER_EXERCISE -> UiText.StringResource(R.string.error_ongoing_exercise)
        ExerciseError.EXERCISE_ALREADY_ENDED -> UiText.StringResource(R.string.error_exercise_already_ended)
        ExerciseError.UNKNOWN -> UiText.StringResource(igor.petrov.core.presentation.ui.R.string.error_unknown)
        ExerciseError.TRACKING_NOT_SUPPORTED -> null
    }
}