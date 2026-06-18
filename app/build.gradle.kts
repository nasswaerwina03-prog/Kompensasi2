plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.kompensasi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.kompensasi"
        minSdk = 26 // Fixed: Required for Apache POI 5.x / Log4j 2
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Added to prevent conflicts with Apache POI resources
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/NOTICE.md"
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation("com.itextpdf:itextg:5.5.10")
    implementation("com.google.zxing:core:3.5.1")
    implementation("commons-io:commons-io:2.15.1")
    
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")

    // Consolidated to Apache POI 5.2.5 (requires minSdk 26)
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("com.github.virtuald:curvesapi:1.07")

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    
    // JavaMail API for Email OTP
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}
