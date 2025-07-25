plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tselfor.wellnesstrackercapstone"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tselfor.wellnesstrackercapstone"
        minSdk = 26
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Unit Test Dependencies
    testImplementation(libs.junit)
    testImplementation(libs.room.testing)
    testImplementation(libs.arch.core.testing)

    // Instrumented Test Dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}