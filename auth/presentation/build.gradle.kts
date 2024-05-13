plugins {
    alias(libs.plugins.runningapp.android.feature.ui)
}

android {
    namespace = "igor.petrov.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}