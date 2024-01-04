@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
}

android {
    namespace = "com.wael.firebasecrud"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wael.firebasecrud"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //firebase
    implementation ("com.google.firebase:firebase-bom:32.7.0")
    implementation ("com.google.firebase:firebase-auth:22.3.0")
    implementation ("com.google.firebase:firebase-firestore:24.10.0")
    implementation ("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.firebase:firebase-messaging:23.4.0")
    implementation ("com.google.firebase:firebase-analytics:21.5.0")
    implementation ("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation ("com.google.firebase:firebase-config:21.6.0")
    implementation ("com.google.firebase:firebase-core:21.1.1")

    //jetpack components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.databinding:databinding-runtime:8.2.0")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation ("androidx.work:work-runtime:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
