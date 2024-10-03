import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.google.services)
}
fun getProps(propName: String): String? {
    val propsFile = rootProject.file("local.properties")
    return if (propsFile.exists()) {
        val props = Properties()
        props.load(FileInputStream(propsFile))
        props.getProperty(propName)
    } else {
        throw NullPointerException("local.properties file not found.")
    }
}

android {

    namespace = "com.sandeveloper.jsscolab"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sandeveloper.jsscolab"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildTypes{
        debug {
            buildConfigField("String", "BASE_URL", "\"${getProps("DEBUG_BASE_URL")}\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"${getProps("RELEASE_BASE_URL")}\"")
        }
    }
}


dependencies {
    //Easy Permission
    implementation ("pub.devrel:easypermissions:3.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.play.services.pal)
    testImplementation(libs.junit)
    implementation(libs.androidx.recyclerview)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //navigation
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    //room

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.ktx)
    //ViewModel
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.lifecycle.viewmodel.ktx)

    //Dagger Hilt
    implementation (libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp (libs.dagger.hilt.android.compiler)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation (libs.adapter.rxjava2)
    implementation (libs.logging.interceptor)

    implementation (libs.androidx.hilt.common)

    //Timber
    implementation (libs.timber)
    //glide
    implementation (libs.glide)
    implementation (libs.okhttp3.integration)

    implementation (libs.github.loupe)
    //play app updater
    implementation (libs.app.update)
    implementation (libs.app.update.ktx)

    //swipe to refresh
    implementation (libs.androidx.swiperefreshlayout)

    //ViewPager 2
    implementation (libs.androidx.viewpager2)

    //Lottie Animations
    implementation (libs.lottie)

    //Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //EventBus
    implementation ("org.greenrobot:eventbus:3.1.1")

    //Bottom Navigation
    implementation (libs.androidx.legacy.support.v4)

    //Overscroll
    implementation ("io.github.everythingme:overscroll-decor-android:1.1.1")

    implementation("com.airbnb.android:lottie:6.5.0")

    implementation (libs.ui)
    implementation (libs.androidx.compose.ui.ui.graphics)
    implementation (libs.ui.tooling.preview)
    implementation (libs.androidx.compose.material3.material3)
    androidTestImplementation (platform(libs.androidx.compose.bom))
    androidTestImplementation (libs.ui.test.junit4)
    debugImplementation (libs.ui.tooling)
    debugImplementation (libs.ui.test.manifest)

    implementation (libs.socket.io.client)
    implementation(libs.engine.io.client)

    //firebase
    implementation (libs.firebase.core)
    implementation(platform(libs.firebase.bom.v3320))
    implementation(libs.google.firebase.messaging.ktx)
//    implementation(libs.firebase.analytics)
    implementation("io.socket:socket.io-client:2.0.0") {
        exclude(group = "org.json", module = "json")
    }
}