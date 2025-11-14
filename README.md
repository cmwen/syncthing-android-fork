# Syncthing Android - Personal Fork

[![License: MPLv2](https://img.shields.io/badge/License-MPLv2-blue.svg)](https://opensource.org/licenses/MPL-2.0)
[![Build Status](https://github.com/cmwen/syncthing-android-fork/workflows/Build%20Self-Signed%20APK/badge.svg)](https://github.com/cmwen/syncthing-android-fork/actions)

> **Note**: This is a personal fork of [syncthing-android](https://github.com/syncthing/syncthing-android) maintained for personal use only. The upstream project was discontinued in December 2024.

## About This Fork

This fork exists to maintain a stable version of Syncthing Android for personal use. It includes several improvements:

- ✅ **AI-Friendly Structure**: Enhanced documentation and code organization for better AI assistant understanding
- ✅ **Self-Signed Builds**: Automated GitHub Actions workflow for creating self-signed APKs
- ✅ **GitHub Releases**: Automatic release creation with APK artifacts when version tags are pushed
- ✅ **Simplified Workflow**: No Google Play Store dependencies or signing secrets required
- ✅ **Clear Documentation**: Comprehensive guides for building, understanding, and modifying the code

This fork is **not intended for distribution** - it's for personal use and learning purposes.

## Upstream Notice

The original syncthing-android project was discontinued in December 2024 due to Google Play Store publishing difficulties and lack of active maintenance. All credit goes to the original contributors who built this excellent application. This fork preserves that work for personal use.

# Overview

[![License: MPLv2](https://img.shields.io/badge/License-MPLv2-blue.svg)](https://opensource.org/licenses/MPL-2.0)

A wrapper of [Syncthing](https://github.com/syncthing/syncthing) for Android.

<img src="app/src/main/play/listings/en-GB/graphics/phone-screenshots/screenshot_phone_1.png" alt="screenshot 1" width="200" /> <img src="app/src/main/play/listings/en-GB/graphics/phone-screenshots/screenshot_phone_2.png" alt="screenshot 2" width="200" /> <img src="app/src/main/play/listings/en-GB/graphics/phone-screenshots/screenshot_phone_3.png" alt="screenshot 3" width="200" />

# Translations

The project is translated on [Hosted Weblate](https://hosted.weblate.org/projects/syncthing/android/).

## Dev

Language codes are usually mapped correctly by Weblate itself.  The supported
set is different between [Google Play][1] and Android apps.  The latter can be
deduced by what the [Android core framework itself supports][2].  New languages
need to be added in the repository first, then appear automatically in Weblate.

[1]: https://support.google.com/googleplay/android-developer/table/4419860
[2]: https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/res/res/

# Quick Start

## Download Pre-Built APKs

The easiest way to use this fork is to download pre-built APKs from the [Releases](https://github.com/cmwen/syncthing-android-fork/releases) page.

1. Download the latest `app-release.apk` from [Releases](https://github.com/cmwen/syncthing-android-fork/releases)
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK
4. (Optional) Verify SHA256 checksum against `sha256sum.txt`

## Building from Source

### Quick Build (Using Docker)

The simplest way to build is using the provided Docker container:

```bash
# Clone with submodules
git clone --recursive https://github.com/cmwen/syncthing-android-fork.git
cd syncthing-android-fork

# Build using Docker (recommended)
docker build -f docker/Dockerfile -t syncthing-builder .
docker run --rm -v $(pwd):/project syncthing-builder ./gradlew buildNative assembleDebug

# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

### Manual Build (Local Environment)

# Building Locally

These dependencies and instructions are necessary for building from the command
line. If you build using Docker (recommended above), you don't need to set up these separately.

## Dependencies

1. Android SDK and NDK
    1. Download SDK command line tools from https://developer.android.com/studio#command-line-tools-only.
    2. Unpack the downloaded archive to an empty folder. This path is going
       to become your `ANDROID_HOME` folder.
    3. Inside the unpacked `cmdline-tools` folder, create yet another folder
       called `latest`, then move everything else inside it, so that the final
       folder hierarchy looks as follows.
       ```
       cmdline-tools/latest/bin
       cmdline-tools/latest/lib
       cmdline-tools/latest/source.properties
       cmdline-tools/latest/NOTICE.txt
       ```
    4. Navigate inside `cmdline-tools/latest/bin`, then execute
       ```
       ./sdkmanager "platform-tools" "build-tools;<version>" "platforms;android-<version>" "extras;android;m2repository" "ndk;<version>"
       ```
       The required tools and NDK will be downloaded automatically.

        **NOTE:** You should check [Dockerfile](docker/Dockerfile) for the
        specific version numbers to insert in the command above.
2. Go (see https://docs.syncthing.net/dev/building#prerequisites for the
   required version)
3. Java version 11 (if not present in ``$PATH``, you might need to set
   ``$JAVA_HOME`` accordingly)
4. Python version 3

## Build instructions

1. Clone the project with submodules:
   ```bash
   git clone --recursive https://github.com/cmwen/syncthing-android-fork.git
   cd syncthing-android-fork
   ```
   
   Or if already cloned, initialize submodules:
   ```bash
   git submodule init && git submodule update
   ```

2. Set up environment:
   ```bash
   # Set Android SDK path
   export ANDROID_HOME=/path/to/android-sdk
   
   # Verify Java version (should be 11)
   java -version
   
   # Verify Go version (should be 1.22.7)
   go version
   ```

3. Build native Syncthing libraries (required first):
   ```bash
   ./gradlew buildNative
   ```
   
   This compiles the Go-based Syncthing core for all Android architectures (ARM, x86).

4. Build the APK:
   ```bash
   # Debug build (for development/testing)
   ./gradlew assembleDebug
   
   # Release build (self-signed, for personal use)
   ./gradlew assembleRelease
   ```

5. Find your APK:
   - Debug: `app/build/outputs/apk/debug/app-debug.apk`
   - Release: `app/build/outputs/apk/release/app-release.apk`

6. Install on device:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Platform-Specific Notes

**Windows**: Use Command Prompt (not PowerShell). Replace `/` with `\` in paths.

**Linux/Mac**: Ensure `ANDROID_HOME` is set and tools are in `PATH`.

### Troubleshooting

- **Build fails**: Check that all dependencies are installed and environment variables are set
- **Native build fails**: Ensure Go 1.22.7+ is installed and accessible
- **Gradle errors**: Try `./gradlew clean` then rebuild
- **APK won't install**: Uninstall any existing version first

# Automated Builds with GitHub Actions

This fork includes automated CI/CD workflows for building and releasing APKs.

## Workflows

### 1. Build Self-Signed APK (`.github/workflows/build-self-signed.yaml`)

**Primary workflow for this fork** - builds self-signed APKs automatically.

**Triggers:**
- **Push to main branch**: Creates debug APK as artifact
- **Version tags** (e.g., `v1.28.2`): Creates release APK and GitHub Release
- **Manual dispatch**: Build debug or release on-demand

**What it does:**
1. Checks out code with submodules
2. Builds native Syncthing libraries using Go and NDK
3. Runs lint checks
4. Assembles APK (debug or release)
5. Generates SHA256 checksum
6. Uploads artifacts to GitHub Actions
7. For tags: Creates GitHub Release with APK

**Usage:**
```bash
# Trigger debug build: Push to main
git push origin main

# Trigger release build: Push version tag
git tag v1.28.2
git push origin v1.28.2

# Manual trigger: Use GitHub Actions UI -> Run workflow
```

### 2. Legacy Workflows

- `build-app.yaml`: Original debug build workflow (kept for `release` branch)
- `release-app.yaml`: Original release workflow (disabled, requires secrets)
- `build-builder.yaml`: Docker builder image workflow

## Creating a Release

To create a new release with automatic APK builds:

1. Update version in `app/build.gradle.kts`:
   ```kotlin
   versionCode = 4396  // Increment
   versionName = "1.28.2"  // Update
   ```

2. Commit changes:
   ```bash
   git add app/build.gradle.kts
   git commit -m "Bump version to 1.28.2"
   git push origin main
   ```

3. Create and push version tag:
   ```bash
   git tag v1.28.2
   git push origin v1.28.2
   ```

4. GitHub Actions will automatically:
   - Build self-signed release APK
   - Generate checksums
   - Create GitHub Release with artifacts

5. Download from: `https://github.com/cmwen/syncthing-android-fork/releases`

## Self-Signed APK Security

This fork uses **self-signed** APKs generated on-the-fly by GitHub Actions:

- **Keystore**: Generated per-build using `keytool` with standard parameters
- **Validity**: 10,000 days (approx. 27 years)
- **Password**: Standard password (for convenience in personal use)
- **Not for distribution**: These APKs are for personal use only

For production use, you should:
1. Generate a permanent keystore
2. Store it securely in GitHub Secrets
3. Modify workflow to use your keystore

# AI-Friendly Development

This fork is optimized for AI assistant collaboration:

## Documentation Structure

- **`.github/AI_CONTEXT.md`**: Comprehensive project overview for AI assistants
- **Inline Comments**: Key files include explanatory comments
- **Clear Naming**: Variables and files use descriptive names
- **Modular Structure**: Code organized logically by feature

## For AI Assistants

When working with this codebase:

1. **Read** `.github/AI_CONTEXT.md` first for project understanding
2. **Build Process**: Two-phase (native build → APK assembly)
3. **Key Files**:
   - `app/build.gradle.kts`: Android app configuration
   - `build.gradle.kts`: Root build configuration
   - `.github/workflows/build-self-signed.yaml`: CI/CD workflow
4. **Testing**: Run `./gradlew buildNative assembleDebug` to validate changes
5. **Workflow Testing**: Push to test branch or use manual dispatch

## Project Structure for AI

```
syncthing-android-fork/
├── .github/
│   ├── AI_CONTEXT.md              # AI-friendly overview
│   └── workflows/
│       └── build-self-signed.yaml # Main build workflow
├── app/                           # Android application
│   ├── build.gradle.kts          # App build config
│   └── src/                      # Java source code
├── syncthing/                     # Native library (submodule)
├── build.gradle.kts              # Root build config
└── README.md                     # This file
```

## Contributing (AI or Human)

This is a personal fork, but improvements are welcome:

1. **Documentation**: Enhance clarity and completeness
2. **Build System**: Simplify or optimize build process
3. **Workflows**: Improve CI/CD automation
4. **Code Quality**: Better organization or comments

When making changes:
- Keep changes minimal and focused
- Document why, not just what
- Test build process after changes
- Update AI_CONTEXT.md if structure changes

# License

The project is licensed under the [MPLv2](LICENSE).

## Credits

- **Original Project**: [syncthing-android](https://github.com/syncthing/syncthing-android) by the Syncthing community
- **Syncthing Core**: [Syncthing](https://github.com/syncthing/syncthing)
- **This Fork**: Personal modifications for AI-friendly structure and self-signed builds

Thanks to all original contributors who built this excellent application!
