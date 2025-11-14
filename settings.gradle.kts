// Gradle settings - configures project structure and repositories

// Plugin management - where to find Gradle plugins
pluginManagement {
    repositories {
        gradlePluginPortal()  // Official Gradle plugin repository
        google()              // Google/Android plugins
        mavenCentral()        // Maven Central plugins
    }
}

// Dependency resolution - where to find libraries
dependencyResolutionManagement {
    // Enforce centralized repository configuration (no per-project repos)
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    
    repositories {
        google()              // Android libraries (AndroidX, Material, etc.)
        mavenCentral()        // Standard Java/Kotlin libraries
        maven { url = java.net.URI("https://jitpack.io") }  // Third-party libraries
    }
}

// Include modules in the build
include(
    ":app",        // Main Android application module
    ":syncthing"   // Native Syncthing library module (Go → .so files)
)
