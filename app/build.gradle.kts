

    plugins {
        alias(libs.plugins.android.application)
    }

    android {
        namespace = "com.example.myapplication"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.example.myapplication"
            minSdk = 16
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            release {
                isMinifyEnabled = false // 对于 release 版本，通常建议设置为 true 以进行代码混淆和优化
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            // 你可以根据需要添加 debug build type 的配置
            // debug {
            //     // ...
            // }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            // 如果你使用 Kotlin, Kotlin 的 JVM target 通常在 kotlinOptions 中设置
        }

        // 如果你使用 Kotlin，通常会在这里配置 kotlinOptions
        // kotlinOptions {
        //     jvmTarget = "11" // 或者 JavaVersion.VERSION_11.toString()
        // }

        buildFeatures {
            viewBinding = true
            // dataBinding = true // 如果你也使用 Data Binding
        }
    }

    dependencies {
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.constraintlayout)
        implementation(libs.navigation.fragment)
        implementation(libs.navigation.ui)
        // 如果你使用了 libs.core.ktx, libs.lifecycle.runtime.ktx 等，也在这里添加
        // implementation(libs.core.ktx)
        // implementation(libs.lifecycle.runtime.ktx)

        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }
