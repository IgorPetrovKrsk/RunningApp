plugins {
    alias(libs.plugins.runningapp.jvm.library)
}

dependencies{
    implementation(projects.core.domain)
}