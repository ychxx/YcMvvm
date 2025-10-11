plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.yc.ycmvvm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yc.ycmvvm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("releaseSignConfig") {
            keyAlias = "1qaz2wsx"
            keyPassword = "1qaz2wsx"
            storeFile = file("1qaz2wsx.jks")
            storePassword = "1qaz2wsx"
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("releaseSignConfig")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "src/libs", "include" to listOf("*.jar","*.aar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":ycMvvm"))
    implementation(libs.androidx.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.camera.camera.core)
    implementation(libs.androidx.camera.camera.camera2)
    implementation(libs.androidx.camera.camera.lifecycle)
    implementation(libs.androidx.camera.camera.video)
    implementation(libs.androidx.camera.camera.view)
    implementation(libs.androidx.camera.camera.extensions)


    val media3_version = "1.5.1"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3_version")
    // Common Kotlin-specific functionality
    implementation("androidx.media3:media3-common-ktx:$media3_version")
    // Common functionality for reading and writing media containers
    implementation("androidx.media3:media3-container:$media3_version")

    implementation("com.github.ychxx:Android-PickerView:1.9")

    implementation("com.google.android.flexbox:flexbox:3.0.0")
    //测试sse链接
    implementation("com.squareup.okhttp3:okhttp-sse:4.12.0")

    // 图片压缩
    implementation(libs.com.github.skynet2017.luban2)

}