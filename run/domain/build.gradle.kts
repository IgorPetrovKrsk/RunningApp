plugins {
    alias(libs.plugins.runningapp.jvm.library)
}

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}