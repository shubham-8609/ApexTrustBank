plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.codeleg.apextrustbank"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.codeleg.apextrustbank"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
    android {
        // ... other configurations ...

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11 // Or your desired Java version
            targetCompatibility = JavaVersion.VERSION_11 // Or your desired Java version
        }

        kotlinOptions {
            jvmTarget = "11" // Or the same version as sourceCompatibility
        }
    }

}
val room_version = "2.8.1"
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.airbnb.android:lottie:6.6.6")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.room:room-runtime:2.8.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.google.material)
    kapt("androidx.room:room-compiler:2.8.1")
    implementation("androidx.room:room-ktx:2.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation(libs.core.ktx)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}