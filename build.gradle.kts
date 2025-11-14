// Root build configuration for Syncthing Android
// This file configures the build system for all sub-projects (app and syncthing modules)

buildscript {
    extra.apply {
        // NDK version shared across all modules
        // IMPORTANT: This version must match the version in ./docker/Dockerfile
        // Cannot be called "ndkVersion" as that leads to naming collision with Android plugin
        set("ndkVersionShared", "25.2.9519653")
    }

    // Repository configuration for build dependencies
    repositories {
        gradlePluginPortal()  // Gradle plugins
        google()              // Android and Google libraries
        mavenCentral()        // General Java/Kotlin libraries
    }
    
    // Build script dependencies (plugins used in build process)
    dependencies {
        // Android Gradle Plugin - manages Android-specific build tasks
        classpath("com.android.tools.build:gradle:7.3.1")
        
        // Gradle Versions Plugin - helps check for dependency updates
        classpath("com.github.ben-manes:gradle-versions-plugin:0.36.0")

        // NOTE: Application dependencies (libraries used by the app) belong
        // in the individual module build.gradle files (e.g., app/build.gradle.kts)
    }
}

// Clean task - removes all build outputs
// Usage: ./gradlew clean
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)  // Deletes the build/ directory
}
