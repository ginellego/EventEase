plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android") version "2.50"
}

android {
    namespace = "comp3350.a15.eventease"
    compileSdk = 34

    defaultConfig {
        applicationId = "comp3350.a15.eventease"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "3.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.dagger:hilt-android:2.51")
    //noinspection GradleDependency
    implementation("org.hsqldb:hsqldb:2.4.1") //don't update this version from 2.4.1!!!
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("com.google.dagger:hilt-android-testing:2.51")
    testImplementation(project(":app"))
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("org.mockito:mockito-android:5.11.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    annotationProcessor("com.google.dagger:hilt-android-compiler:2.51")
    annotationProcessor("com.google.dagger:hilt-compiler:2.51")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.51")
    testAnnotationProcessor("com.google.dagger:hilt-compiler:2.51")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
}