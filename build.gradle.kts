// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {

//    repositories {
//
//        mavenCentral()
//        mavenLocal()
//        google()
//
//        //        google()
//    }

    dependencies {
        classpath("androidx.navigation.safeargs:androidx.navigation.safeargs.gradle.plugin:2.7.2")
        classpath("com.google.gms:google-services:4.3.15")

        // attention pls
        classpath("com.android.tools.build:gradle:8.1.0")
//        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")


    }
}

//allprojects {
//    repositories {
//        google()
//        //        google()
//        //        mavenCentral()
////        mavenLocal()
////        google()
//    }
//}


plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false

    // newly add
    id("com.google.dagger.hilt.android") version "2.44" apply false

}