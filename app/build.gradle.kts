// App module build configuration
// This file defines how the Android application is built and packaged

import org.gradle.configurationcache.extensions.capitalized

plugins {
    // Android Application Plugin - enables Android app development features
    id("com.android.application")
    
    // Gradle Versions Plugin - checks for dependency updates
    id("com.github.ben-manes.versions")
    
    // Google Play Publishing Plugin - for uploading to Play Store (legacy, not used in fork)
    id("com.github.triplet.play") version "3.7.0"
}

// Application dependencies - libraries used by the app
dependencies {
    // SuperUser - root access management (optional, for privileged operations)
    implementation("eu.chainfire:libsuperuser:1.1.1")
    
    // Material Design - Google's design system for Android UI
    implementation("com.google.android.material:material:1.8.0")
    
    // Gson - JSON parsing and serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    // BCrypt - password hashing for secure authentication
    implementation("org.mindrot:jbcrypt:0.4")
    
    // Guava - Google's core libraries (collections, utilities)
    implementation("com.google.guava:guava:32.1.3-android")
    
    // Stream - Java 8 stream API backport for older Android versions
    implementation("com.annimon:stream:1.2.2")
    
    // Volley - HTTP networking library
    implementation("com.android.volley:volley:1.2.1")
    
    // Commons IO - file and I/O utilities
    implementation("commons-io:commons-io:2.11.0")

    // ZXing - QR code scanning (for device ID sharing)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        isTransitive = false  // Don't pull in transitive dependencies
    }
    implementation("com.google.zxing:core:3.4.1")

    // AndroidX ConstraintLayout - flexible layout system
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    
    // Dagger 2 - dependency injection framework
    implementation("com.google.dagger:dagger:2.49")
    annotationProcessor("com.google.dagger:dagger-compiler:2.49")
    
    // Testing libraries
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.annotation:annotation:1.2.0")
}

// Android-specific build configuration
android {
    // Get NDK version from root project (shared across modules)
    val ndkVersionShared = rootProject.extra.get("ndkVersionShared")
    
    // SDK versions - MUST match versions in ../docker/Dockerfile for CI builds
    compileSdk = 34                    // SDK used to compile the app
    buildToolsVersion = "34.0.0"       // Build tools version
    ndkVersion = "${ndkVersionShared}" // Native Development Kit for JNI/C++ code

    // Enable modern Android build features
    buildFeatures {
        dataBinding = true   // XML data binding in layouts
        viewBinding = true   // Type-safe view access
    }

    // App metadata and minimum requirements
    defaultConfig {
        applicationId = "com.nutomic.syncthingandroid"  // Unique app identifier
        minSdk = 21          // Android 5.0 Lollipop (minimum supported version)
        targetSdk = 33       // Android 13 (target features and behavior)
        versionCode = 4395   // Numeric version for Play Store (increment each release)
        versionName = "1.28.1"  // Human-readable version string
        
        // Testing configuration
        testApplicationId = "com.nutomic.syncthingandroid.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Signing configuration for release builds
    // Uses environment variables to avoid storing secrets in code
    // Supports both old (SYNCTHING_*) and new (ANDROID_*) naming conventions for backward compatibility
    signingConfigs {
        create("release") {
            // Path to keystore file
            // Priority: ANDROID_KEYSTORE_PATH > SYNCTHING_RELEASE_STORE_FILE
            storeFile = (System.getenv("ANDROID_KEYSTORE_PATH") 
                ?: System.getenv("SYNCTHING_RELEASE_STORE_FILE"))?.let(::file)
            
            // Keystore password
            // Priority: ANDROID_KEYSTORE_PASSWORD > SIGNING_PASSWORD
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") 
                ?: System.getenv("SIGNING_PASSWORD")
            
            // Key alias in keystore
            // Priority: ANDROID_KEY_ALIAS > SYNCTHING_RELEASE_KEY_ALIAS
            keyAlias = System.getenv("ANDROID_KEY_ALIAS") 
                ?: System.getenv("SYNCTHING_RELEASE_KEY_ALIAS")
            
            // Key password
            // Priority: ANDROID_KEY_PASSWORD > SIGNING_PASSWORD
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD") 
                ?: System.getenv("SIGNING_PASSWORD")
        }
    }

    // Build variants - different versions of the app for different purposes
    buildTypes {
        // Debug build - for development and testing
        getByName("debug") {
            applicationIdSuffix = ".debug"  // Different package name (allows install alongside release)
            isDebuggable = true             // Enable debugging
            isJniDebuggable = true          // Enable native code debugging
            isRenderscriptDebuggable = true // Enable renderscript debugging
            isMinifyEnabled = false         // Don't optimize/shrink code (easier debugging)
        }
        
        // Release build - for distribution
        getByName("release") {
            // Use signing config if available (storeFile must exist)
            // If not available, build will succeed but APK won't be signed
            signingConfig = signingConfigs.runCatching { getByName("release") }
                .getOrNull()
                .takeIf { it?.storeFile != null }
        }
    }

    // Java compilation settings
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11  // Java 11 source code
        targetCompatibility = JavaVersion.VERSION_11  // Java 11 bytecode
    }

    // Packaging options - how to include native libraries in APK
    // IMPORTANT: Legacy packaging is required for libsyncthing.so to work correctly
    // Without this, the native library won't be extracted properly from app bundles
    packagingOptions {
        jniLibs {
            useLegacyPackaging = true  // Use older APK packaging for native libs
        }
    }
}

// Google Play Publishing configuration
// NOTE: This is legacy from upstream and not used in this fork
// Left for reference/compatibility only
play {
    // Service account credentials for Play Store API
    serviceAccountCredentials.set(
        file(System.getenv("SYNCTHING_RELEASE_PLAY_ACCOUNT_CONFIG_FILE") ?: "keys.json")
    )
    track.set("beta")  // Publish to beta track (not production)
}

// Custom task: Delete Play Store translations for unsupported languages
// Some languages in the app are not supported by Google Play Store
// This task removes those listings before publishing
tasks.register<Delete>("deleteUnsupportedPlayTranslations") {
    delete(
        "src/main/play/listings/de_DE/",   // Duplicate of de
        "src/main/play/listings/el-EL/",   // Not supported
        "src/main/play/listings/en/",      // Use en-GB or en-US instead
        "src/main/play/listings/eo/",      // Esperanto not supported
        "src/main/play/listings/eu/",      // Basque not supported
        "src/main/play/listings/nb/",      // Norwegian Bokmål not supported
        "src/main/play/listings/nl_BE/",   // Belgian Dutch not supported
        "src/main/play/listings/nn/",      // Norwegian Nynorsk not supported
        "src/main/play/listings/ta/",      // Tamil not supported
    )
}

// Task dependency configuration
// This ensures native libraries are built before being packaged into APK
project.afterEvaluate {
    // For each build type (debug, release)
    android.buildTypes.forEach {
        // Make the JNI library merging task depend on building native libraries
        tasks.named("merge${it.name.capitalized()}JniLibFolders") {
            dependsOn(":syncthing:buildNative")  // Build libsyncthing.so first
        }
    }
}
