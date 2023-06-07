/**
 * Created by Hieu Luu (neo) on 05/06/2023
 */
private const val kotlinVersion = "1.3.31"
private const val kotlinBomVersion = "1.8.0"
private const val androidGradleVersion = "3.4.0"
private const val androidxCoreVersion = "1.8.0"
private const val androidxLifecycleVersion = "2.3.1"
private const val activityComposeVersion = "1.7.2"
private const val composeBomVersion = "2022.10.00"

//support libs
private const val appcompatVersion = "1.0.0"
private const val constraintLayoutVersion = "1.1.3"

//test libs
private const val junitVersion = "4.13.2"
private const val extJunitVersion = "1.1.5"
private const val runnerVersion = "1.1.0"
private const val espressoVersion = "3.5.1"
private const val composeBomTestVersion = "2022.10.00"

object Deps {
    object Android {
        const val minSdkVersion = 21
        const val targetSdkVersion = 33
        const val compileSdkVersion = 33
        const val versionCode = 1
        const val versionName = "0.1"

        const val core_ktx = "androidx.core:core-ktx:$androidxCoreVersion"
        const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycleVersion"
    }
    object Kotlin{
        const val jvmTarget = "1.8"
        const val kotlinCompilerExtensionVersion = "1.4.7"

        const val kotlin_bom = "org.jetbrains.kotlin:kotlin-bom:$kotlinBomVersion"
        const val kotlin_std = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    }

    object BuildPlugins {
        const val androidGradle = "com.android.tools.build:gradle:$androidGradleVersion"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }
    object SupportLibs{
        val appcompat = "androidx.appcompat:appcompat:$appcompatVersion"
        val constraint_layout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    }
    object TestLibs{
        val junit = "junit:junit:$junitVersion"
        val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
        val runner = "androidx.test:runner:$runnerVersion"
    }
}