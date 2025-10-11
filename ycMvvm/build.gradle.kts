plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.mavenPublish)
}
group = "com.yc.ycmvvm"
version = "1.0.0"
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = (group.toString())
                artifactId = "ycmvvm-kts"
                version = version
            }
        }
    }
}
android {
    namespace = "com.yc.ycmvvm"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.exifinterface)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /**--retrofit  网络请求--**/
    api(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.retrofit.mock)
    implementation(libs.squareup.converter.scalars)

    /**--okHttp-logging-interceptor 日志拦截器--**/
    api(libs.logging.interceptor)
    /**--权限申请--**/
    implementation(libs.xxpermissions)
    /**-- Lifecycle components 生命周期管理--**/
    api(libs.androidx.lifecycle.lifecycle.viewmodel.ktx4)

    /**--图片加载--**/
    implementation(libs.github.glide)

    /**--json解析--**/
    api(libs.google.gson)

    /**--log日志--**/
    implementation(libs.elvishew.xlog)

    /**--下载--**/
    api(libs.org.xutils.xutils3)

    /**-- 加载更多和刷新 --**/
    api(libs.refresh.layout.kernel)    //核心必须依赖
    implementation(libs.refresh.header.material)   //谷歌刷新头
    implementation(libs.refresh.footer.classics)   //经典加载

}