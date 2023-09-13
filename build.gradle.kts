// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("androidx.navigation.safeargs:androidx.navigation.safeargs.gradle.plugin:2.7.2")
        classpath("com.google.gms:google-services:4.3.15")
    }
}



plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}