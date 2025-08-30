// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Google Services Gradle plugin for Firebase
        classpath("com.google.gms:google-services:4.4.3")

        // Firebase Crashlytics Gradle plugin
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.6")

        // Firebase Performance Monitoring Gradle plugin
        classpath("com.google.firebase:perf-plugin:2.0.1")

        // Firebase App Distribution Gradle plugin
        classpath("com.google.firebase:firebase-appdistribution-gradle:5.1.1")
    }
}
