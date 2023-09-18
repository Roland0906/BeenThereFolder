import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")
    id ("com.google.gms.google-services")
    id ("kotlin-android")
    id ("kotlin-kapt")

    // newly add
    id ("com.google.dagger.hilt.android")
}

//kotlin {
//    jvmToolchain(11)
//}


android {
    namespace = "com.example.beenthere"
    compileSdk = 33

    defaultConfig {

        val properties = Properties()
//        properties.load(rootProject.file("gradle.properties").inputStream())
        properties.load(rootProject.file("local.properties").inputStream())

        buildConfigField("String", "OPEN_AI_KEY", "\"${properties.getProperty("OPEN_AI_KEY")}\"")
        buildConfigField("String", "BOOK_API_KEY", "\"${properties.getProperty("BOOK_API_KEY")}\"")


        applicationId = "com.example.beenthere"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // newly add
//    packagingOptions {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            excludes += "META-INF/INDEX.LIST"
//            excludes += "META-INF/DEPENDENCIES"
//        }
//    }
    //

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

//    implementation("com.google.cloud:gapic-google-cloud-ai-generativelanguage-v1beta2-java:0.0.0-SNAPSHOT")
//    implementation("io.grpc:grpc-okhttp:1.53.0")

    implementation ("com.google.mlkit:text-recognition:16.0.0")

//    implementation ("com.google.mlkit:text-recognition-chinese:16.0.0")

//    implementation ("androidx.camera:camera-camera2:1.2.1-SNAPSHOT")
//    implementation ("androidx.camera:camera-lifecycle:1.2.1-SNAPSHOT")
//    implementation ("androidx.camera:camera-view:1.2.1-SNAPSHOT")



    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.camera:camera-core:1.2.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.squareup.moshi:moshi:1.13.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.13.0")

    implementation ("com.github.bumptech.glide:glide:4.15.0")

    kapt ("com.github.bumptech.glide:compiler:4.15.0")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")

    implementation ("com.airbnb.android:lottie:6.1.0")

    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

//    implementation ("fileTree(include: ['*.jar', '*.aar'], dir: 'libs')")

    implementation ("com.facebook.android:facebook-login:latest.release")

    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")


    implementation("com.loopj.android:android-async-http:1.4.11")


    // Gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil
    implementation ("io.coil-kt:coil:1.1.1")


    // newly added
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

}

kapt {
    correctErrorTypes = true
}
