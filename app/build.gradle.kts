import java.util.Properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.ibtehazrafid.weatherroute"

    buildFeatures {
        buildConfig = true
    }

    compileSdk = 36

    defaultConfig {
        applicationId = "com.ibtehazrafid.weatherroute"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mapsApiKey = localProperties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = mapsApiKey
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Places API
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    // Play Services Maps
    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    // Places for Address Autocomplete
    implementation("com.google.android.libraries.places:places:3.5.0")
    // Routes API + Weather API (Retrofit for HTTP calls)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Room (local database)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    // Glide (image loading for weather icons)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // ViewModel + LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.0")
    // WorkManager (background weather refresh)
    implementation("androidx.work:work-runtime:2.9.0")
}