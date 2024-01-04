buildscript {

    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath ("com.google.gms:google-services:4.4.0")

    }
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id ("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.8" apply false
}
true // Needed to make the Suppress annotation work for the plugins block