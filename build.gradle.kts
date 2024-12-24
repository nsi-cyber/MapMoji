
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.daggerHilt) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.androidLibrary) apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false

}