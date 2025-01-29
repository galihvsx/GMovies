plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    // parcelize
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.gtools.gmovies.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        abortOnError = false
        ignoreWarnings = true
        warningsAsErrors = true
    }
}

dependencies {

    // Retrofit
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Shared
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.activity)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)

    // Room
    //noinspection UseTomlInstead
    implementation("androidx.room:room-runtime:2.6.1")
    //noinspection UseTomlInstead
    ksp("androidx.room:room-compiler:2.6.1")
    //noinspection UseTomlInstead
    implementation("androidx.room:room-ktx:2.6.1")

    // Lifecycle
    //noinspection UseTomlInstead
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    //noinspection UseTomlInstead
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    //noinspection UseTomlInstead
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Koin
    api(libs.koin.android)
    api(libs.koin.core)
    api(libs.koin.androidx.navigation)

    // Glide
    api(libs.glide)
    annotationProcessor(libs.glide.compiler)
    //noinspection UseTomlInstead
    ksp("com.github.bumptech.glide:ksp:4.14.2")

    // Other
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Leak Canary
    debugImplementation(libs.leakcanary.android)

    // SqlCipher
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)
}