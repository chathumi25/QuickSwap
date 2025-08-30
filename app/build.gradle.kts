plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Firebase plugin
    id("com.google.firebase.crashlytics") // Crashlytics plugin
    id("com.google.firebase.firebase-perf") // Firebase Performance plugin
    id("com.google.firebase.appdistribution") // Firebase App Distribution plugin
}

android {
    namespace = "com.example.quickswap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quickswap"
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
}

dependencies {
    // AndroidX + UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase libraries
    implementation("com.google.firebase:firebase-auth:24.0.1")
    implementation("com.google.firebase:firebase-database:22.0.0")
    implementation("com.google.firebase:firebase-firestore:26.0.0")
    implementation("com.google.firebase:firebase-ai:17.2.0")
    implementation("com.google.firebase:firebase-storage:22.0.0")
    implementation("com.google.firebase:firebase-functions:22.0.0")
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.firebase:firebase-crashlytics:20.0.1") // Crashlytics
    implementation("com.google.firebase:firebase-perf:22.0.1") // Performance Monitoring
    implementation("com.google.firebase:firebase-appdistribution:5.1.1") // App Distribution
    implementation("com.google.firebase:firebase-messaging:25.0.0") // Cloud Messaging
    implementation("com.google.firebase:firebase-inappmessaging-display:22.0.0") // In-App Messaging Display
    implementation("com.google.firebase:firebase-config:23.0.0") // Remote Config
    implementation ("com.google.firebase:firebase-analytics:23.0.0")

    // Google Identity Services (Sign-in with Google)
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Google Mobile Ads
    implementation("com.google.android.gms:play-services-ads:24.5.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

