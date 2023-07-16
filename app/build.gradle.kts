plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Configuration.compileSdk)

    defaultConfig {
        applicationId = Configuration.artifactGroup
        minSdkVersion(Configuration.minSdk)
        targetSdkVersion(Configuration.targetSdk)
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        dataBinding = true
    }
}

dependencies {
    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.lifecycleLiveData)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUI)
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.espressoCore)

    // Add Koin dependencies
    implementation(Dependencies.koin)

    // Add Retrofit dependencies
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)

    // okhttp
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttpLoggingInterceptor)

    // Glide
    implementation(Dependencies.glide)
    kapt(Dependencies.glideCompiler)
}
