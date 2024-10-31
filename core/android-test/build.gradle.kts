plugins {
    alias(libs.plugins.runningapp.android.library)
    alias(libs.plugins.runningapp.android.junit5)
}

android {
    namespace = "igor.petrov.core.android_test"

}

dependencies {
    implementation(projects.auth.data)
    implementation(projects.core.domain)
    api(projects.core.test)

    implementation(libs.ktor.client.mock)
    implementation(libs.bundles.ktor)
    implementation(libs.coroutines.test)
}