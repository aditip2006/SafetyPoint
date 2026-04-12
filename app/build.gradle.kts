plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.safetypoint"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.safetypoint"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}