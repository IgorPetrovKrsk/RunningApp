plugins {
    alias(libs.plugins.runningapp.jvm.library)
    alias(libs.plugins.runningapp.jvm.junit5)
}

dependencies{
    implementation(projects.core.domain)
}