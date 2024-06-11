plugins {
    alias(libs.plugins.runningapp.android.feature.ui)

}

android {
    namespace = "igor.petrov.analytics.presentation"
}

dependencies {

    implementation(projects.analytics.domain)

}