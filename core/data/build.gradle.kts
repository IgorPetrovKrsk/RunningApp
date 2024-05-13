plugins {
    alias(libs.plugins.runningapp.android.library)
    alias(libs.plugins.runningapp.jvm.ktor)
}

android {
    namespace = "igor.petrov.core.data"
}

dependencies {
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.core.database)
}