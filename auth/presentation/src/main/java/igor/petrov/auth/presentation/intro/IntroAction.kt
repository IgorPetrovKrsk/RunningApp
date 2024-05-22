package igor.petrov.auth.presentation.intro

sealed interface IntroAction {
    data object OnSingInClick: IntroAction
    data object OnSighUpClick: IntroAction
}