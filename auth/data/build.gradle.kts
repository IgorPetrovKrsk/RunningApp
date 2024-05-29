plugins {
    alias(libs.plugins.runningapp.android.library)
    alias(libs.plugins.runningapp.jvm.ktor)
}

android {
    namespace = "igor.petrov.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}