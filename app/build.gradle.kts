plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // added kotlin annotation plugin
    id ("kotlin-kapt")

    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("D:\\NotesApp_Internshala\\testing.jks")
            storePassword = "123456"
            keyAlias = "testing"
            keyPassword = "123456"
        }
    }
    namespace = "com.example.notesapp_internshala"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesapp_internshala"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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

    //start
    // Room and Lifecycle dependencies
    implementation ("androidx.room:room-runtime:2.6.0-alpha02")
    implementation("com.google.firebase:firebase-auth:21.1.0")
    kapt("androidx.room:room-compiler:2.6.1")
    //kotlin extensions for coroutine support with room
    implementation("androidx.room:room-ktx:2.6.1")

    //kotlin extension for coroutine support with activities
    implementation ("androidx.activity:activity-ktx:1.8.2")
//end

    implementation (platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    // Firebase Cloud FireStore Dependency
    implementation ("com.google.firebase:firebase-firestore:24.10.0")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}