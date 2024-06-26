plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.example.to_do_list.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.to_do_list.android"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}



dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    //implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.tflite.acceleration.service)
    debugImplementation(libs.compose.ui.tooling)
    implementation ("androidx.activity:activity-ktx:1.6.1")
    implementation ("androidx.activity:activity-compose:1.6.1")

    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")
    implementation ("nl.dionsegijn:konfetti-compose:2.0.4")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
}




