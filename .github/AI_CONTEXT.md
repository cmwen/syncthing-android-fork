# AI Context for Syncthing Android Fork

## Project Overview

This is a personal fork of [syncthing-android](https://github.com/syncthing/syncthing-android), created for maintaining a stable version for personal use. The original project was discontinued in December 2024.

### What is This Project?

Syncthing-Android is a wrapper application that brings [Syncthing](https://github.com/syncthing/syncthing) (a continuous file synchronization program) to Android devices. It allows users to synchronize files between their Android device and other devices running Syncthing.

## Technology Stack

- **Language**: Java (Android)
- **Build System**: Gradle 8.5 with Kotlin DSL
- **Native Components**: Go (for Syncthing core), compiled via NDK
- **Minimum Android SDK**: 21 (Android 5.0 Lollipop)
- **Target Android SDK**: 33 (Android 13)
- **Compile Android SDK**: 34 (Android 14)
- **NDK Version**: 25.2.9519653

## Project Structure

```
syncthing-android-fork/
├── .github/
│   ├── workflows/           # GitHub Actions CI/CD workflows
│   └── AI_CONTEXT.md       # This file - AI-friendly context
├── app/                     # Main Android application module
│   ├── src/main/java/      # Java source code
│   ├── src/main/res/       # Android resources (layouts, strings, etc.)
│   └── build.gradle.kts    # App module build configuration
├── syncthing/              # Syncthing native library submodule
├── docker/                 # Docker build environment
│   └── Dockerfile          # Builder container definition
├── scripts/                # Build and utility scripts
├── build.gradle.kts        # Root build configuration
├── settings.gradle.kts     # Gradle settings and modules
└── README.md               # User-facing documentation
```

## Key Components

### 1. Android Application (`app/`)
- **Package**: `com.nutomic.syncthingandroid`
- **Main Components**:
  - Activities and Fragments for UI
  - Services for background synchronization
  - Broadcast Receivers for system events
  - Native library integration with Syncthing Go binary

### 2. Native Syncthing Library (`syncthing/`)
- Git submodule containing the Syncthing Go source code
- Built using NDK and Go compiler
- Produces `libsyncthing.so` for different ABIs (armeabi-v7a, arm64-v8a, x86, x86_64)

### 3. Build System
- **Gradle Tasks**:
  - `buildNative`: Compiles the Go Syncthing library for all architectures
  - `assembleDebug`: Creates debug APK
  - `assembleRelease`: Creates release APK (requires signing)
  - `lint`: Runs Android lint checks

## Build Process

The build process has two main phases:

1. **Native Build**: Compile Syncthing Go code to native libraries
   ```bash
   ./gradlew buildNative
   ```

2. **Android Build**: Package everything into an APK
   ```bash
   ./gradlew assembleDebug   # or assembleRelease
   ```

### Build Requirements

- **Java**: JDK 11 (configured in build files)
- **Android SDK**: Platform 34, Build Tools 34.0.0
- **Android NDK**: Version 25.2.9519653
- **Go**: Version 1.22.7 (for building Syncthing)
- **Python**: Version 3 (for build scripts)

## Signing Configuration

The app uses Android's signing configuration system:

- **Debug builds**: Automatically signed with debug keystore
- **Release builds**: Requires environment variables:
  - `SYNCTHING_RELEASE_STORE_FILE`: Path to keystore file
  - `SYNCTHING_RELEASE_KEY_ALIAS`: Key alias in keystore
  - `SIGNING_PASSWORD`: Password for keystore and key

## GitHub Actions Workflows

### Current Workflows

1. **build-app.yaml**: Builds debug APK on push/PR to main/release branches
2. **release-app.yaml**: Builds signed release APK when tags are pushed
3. **build-builder.yaml**: Builds the Docker builder image
4. **image-builder-template.yaml**: Template for builder image workflow

### Workflow Improvements (Implemented in this Fork)

- Self-signed APK generation without external secrets
- Automatic GitHub Releases with APK artifacts
- Simplified signing process for personal use
- Better artifact naming and organization

## Dependencies

### Android Libraries
- Material Design Components
- Google Gson (JSON parsing)
- Guava (utilities)
- ZXing (QR code scanning)
- AndroidX libraries (ConstraintLayout, etc.)
- Dagger 2 (Dependency Injection)
- Volley (HTTP networking)

### Native Dependencies
- Syncthing core (Go binary)
- libsuperuser (root access, if available)

## Development Workflow

1. **Clone with submodules**:
   ```bash
   git clone --recursive https://github.com/cmwen/syncthing-android-fork.git
   ```

2. **Build native components**:
   ```bash
   ./gradlew buildNative
   ```

3. **Run lint checks**:
   ```bash
   ./gradlew lint
   ```

4. **Build APK**:
   ```bash
   ./gradlew assembleDebug
   ```

5. **Install on device**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## Testing

- **Unit Tests**: Available but minimal
- **Android Instrumentation Tests**: Run with `./gradlew connectedAndroidTest`
- **Manual Testing**: Required for most functionality

## Common Issues and Solutions

### Issue: Native build fails
**Solution**: Ensure Go 1.22.7 is installed and in PATH, NDK is properly configured

### Issue: Gradle build fails with "SDK not found"
**Solution**: Set ANDROID_HOME environment variable to SDK location

### Issue: APK won't install on device
**Solution**: Check if debug certificate is valid, or if a conflicting version is installed

## AI-Friendly Features

This fork includes several improvements for AI understanding and automation:

1. **Comprehensive documentation** in markdown format
2. **Clear project structure** with documented purpose for each directory
3. **Simplified workflows** with inline comments explaining each step
4. **Environment variable documentation** with clear naming
5. **Automated releases** without requiring manual intervention

## Versioning

- **Version Code**: Incremented for each release (currently 4397)
- **Version Name**: Semantic versioning (currently 1.28.3)
- Format: `major.minor.patch` or `major.minor.patch.hotfix`

## License

Mozilla Public License Version 2.0 (MPLv2)

---

*This document is maintained to help AI assistants understand the project structure and make informed decisions when modifying code or documentation.*
