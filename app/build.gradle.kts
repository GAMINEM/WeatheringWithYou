@file:Suppress("UNUSED_EXPRESSION")

import org.bouncycastle.LICENSE
import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled
import javax.sound.midi.MetaMessage.META

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.example.weatheringwithyou"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatheringwithyou"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/LICENSE-notice.md")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.inappmessaging)
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("junit:junit:4.12")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.android.support:appcompat-v7:28.0.0-alpha1")
    implementation ("com.airbnb.android:lottie:6.4.0")//lottie animation

    implementation("com.android.volley:volley:1.2.1")//volley api
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") //GSON CONVERTER
    implementation("com.squareup.retrofit2:retrofit:2.9.0")



}