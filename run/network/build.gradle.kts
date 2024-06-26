plugins {
    alias(libs.plugins.runningapp.android.library)
    alias(libs.plugins.runningapp.jvm.ktor)
}

android {
    namespace = "igor.petrov.run.network"

}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(projects.core.data)
}