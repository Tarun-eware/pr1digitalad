//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    alias(libs.plugins.androidLibrary) apply false
//}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.3.0" apply false
}

buildscript {

    dependencies {
        classpath("com.android.tools.build:gradle:8.3.0")
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}