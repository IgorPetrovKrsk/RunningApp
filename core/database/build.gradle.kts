plugins {
    alias(libs.plugins.runningapp.android.library)
    alias(libs.plugins.runningapp.android.room)
}

android {
    namespace = "igor.petrov.core.database"

}

dependencies {
    implementation(libs.org.mongodb.bson)

    implementation(projects.core.domain)
}