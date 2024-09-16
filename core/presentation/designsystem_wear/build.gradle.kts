plugins {
    alias(libs.plugins.runningapp.android.library.compose)
}

android {
    namespace = "igor.petrov.core.presentation.designsystem_wear"

    defaultConfig{
        minSdk = 30
    }
}

dependencies {
    api(projects.core.presentation.designsystem)

    implementation(libs.androidx.wear.compose.material)
}