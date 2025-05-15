plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Aplico Safe Args aquí:
    id("androidx.navigation.safeargs.kotlin")

    id("kotlin-kapt") // O id 'com.google.devtools.ksp' si KSP
}

android {
    namespace = "com.example.dogapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dogapp"
        minSdk = 24
        targetSdk = 35
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx.v277)
    implementation(libs.androidx.navigation.ui.ktx.v277)

    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Lottie for animations
    implementation(libs.lottie)

    // Biometric authentication
    implementation(libs.androidx.biometric.ktx)

    // Coil (para cargar imágenes - opcional para HU 2.0, pero útil para después)
    implementation(libs.coil) // Usa la versión estable más reciente

    implementation(libs.material.v190) // o la última versión


    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)


    // Room Persistence Library (¡ASEGÚRATE QUE ESTÉN ACTIVAS!)
    implementation (libs.androidx.room.runtime)
    //kapt(libs.androidx.room.compiler) // O usa 'ksp "androidx.room:room-compiler:$room_version"'
    implementation (libs.androidx.room.ktx) // Para coroutines y Flow/LiveData support

    // Lifecycle (ya deberían estar)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v270)
    implementation (libs.androidx.lifecycle.livedata.ktx.v270)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Coroutines (ya deberían estar)
    implementation (libs.kotlinx.coroutines.android)



    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}