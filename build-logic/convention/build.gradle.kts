plugins {
    `kotlin-dsl`
}
group = "igor.petrov.runningapp.buildlogic"

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin{
    plugins{
        register("androidApplication"){
            id = "runningapp.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose"){
            id = "runningapp.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationWearCompose"){
            id = "runningapp.android.application.wear.compose"
            implementationClass = "AndroidApplicationWearComposeConventionPlugin"
        }
        register("androidLibrary"){
            id = "runningapp.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose"){
            id = "runningapp.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi"){
            id = "runningapp.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom"){
            id = "runningapp.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidDynamicFeature"){
            id = "runningapp.android.dynamic.feature"
            implementationClass = "AndroidDynamicFeatureConventionPlugin"
        }
        register("jvmLibrary"){
            id = "runningapp.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmKtor"){
            id = "runningapp.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
    }
}