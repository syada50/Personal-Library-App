import com.android.build.api.dsl.DataBinding

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}



android {
    buildFeatures {
        viewBinding =true
        dataBinding= true
    }
    namespace = "com.example.personallibrary"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.personallibrary"
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
                getDefaultProguardFile("proguard-android-optimize.txt") ,
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    dependencies {
        val room_version = "2.6.1"

        implementation("androidx.room:room-runtime:$room_version")
        annotationProcessor("androidx.room:room-compiler:$room_version")


        ksp("androidx.room:room-compiler:$room_version")


        implementation("androidx.room:room-ktx:$room_version")
// ViewModel and LiveData
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
        implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
        implementation ("com.google.android.material:material:1.4.0")
    }}