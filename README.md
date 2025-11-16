# Syncthing Android - Personal Fork

[![License: MPLv2](https://img.shields.io/badge/License-MPLv2-blue.svg)](https://opensource.org/licenses/MPL-2.0)
[![Build Status](https://github.com/cmwen/syncthing-android-fork/workflows/Build%20Self-Signed%20APK/badge.svg)](https://github.com/cmwen/syncthing-android-fork/actions)
[![Syncthing Version](https://img.shields.io/badge/Syncthing-v2.0.11-brightgreen.svg)](https://github.com/syncthing/syncthing/releases/tag/v2.0.11)

> **Note**: This is a personal fork of [syncthing-android](https://github.com/syncthing/syncthing-android) maintained for personal use only. The upstream project was discontinued in December 2024.

> **⚠️ Important**: This fork now uses **Syncthing 2.0.11**. Syncthing 2.x cannot sync with 1.x devices. All devices in your network must be upgraded together for compatibility.

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

## ⚠️ Upgrading from 1.x to 2.0

**Important Changes in Syncthing 2.0:**

1. **Protocol Incompatibility**: Syncthing 2.x devices **cannot sync** with 1.x devices. You must upgrade **all devices** in your network together.

2. **Database Migration**: On first launch, Syncthing 2.0 will migrate your database from LevelDB to SQLite. This can take several minutes for large setups. The app may appear frozen - this is normal.

3. **Key Improvements**:
   - Faster scanning and syncing (removed rolling hash detection)
   - Multiple connections by default (improved performance)
   - Better database reliability (SQLite vs LevelDB)
   - Deleted items expire after 15 months (instead of forever)

4. **Upgrade Steps**:
   - Backup your existing Syncthing configuration (optional but recommended)
   - Upgrade **all** Android devices first
   - Then upgrade other devices (desktop, servers, etc.)
   - Wait for database migration to complete on first launch
   - Verify all devices can connect and sync

**If you cannot upgrade all devices immediately**, stay on version 1.28.3 from [previous releases](https://github.com/cmwen/syncthing-android-fork/releases/tag/v1.28.3).

## Download Pre-Built APKs

The easiest way to use this fork is to download pre-built APKs from the [Releases](https://github.com/cmwen/syncthing-android-fork/releases) page.

1. Download the latest `app-release.apk` from [Releases](https://github.com/cmwen/syncthing-android-fork/releases)
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK (will upgrade from 1.x if already installed)
4. Wait for database migration on first launch (can take several minutes)
5. (Optional) Verify SHA256 checksum against `sha256sum.txt`

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

### DevContainer Build (Recommended for Development)

The easiest way to develop is using VS Code with the Remote-Containers extension. All dependencies are pre-configured:

```bash
# Clone with submodules
git clone --recursive https://github.com/cmwen/syncthing-android-fork.git
cd syncthing-android-fork

# Open in VS Code
code .

# VS Code will prompt to "Reopen in Container" - click it
# Once the container is ready, build:
./gradlew buildNative assembleDebug

# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

The devcontainer includes:
- Android SDK (API 34)
- Android NDK (26.1.10909125)
- Java 17
- Go 1.22
- All required build tools

### Manual Build (Local Environment)

# Building Locally

These dependencies and instructions are necessary for building from the command
line. If you build using Docker or DevContainer (recommended above), you don't need to set up these separately.

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
2. Go 1.23+ (required for Syncthing 2.0; see https://docs.syncthing.net/dev/building#prerequisites)
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

### 1. Android CI (`.github/workflows/ci.yml`)

**Primary CI workflow** - runs on every push and pull request.

**Triggers:**
- Push to `main` or `release` branches
- Pull requests to `main` or `release` branches

**What it does:**
1. Checks out code with submodules
2. Builds native Syncthing libraries
3. Runs lint checks
4. Builds debug APK
5. Uploads artifacts and reports

### 2. Android Release (`.github/workflows/release.yml`)

**Release workflow** - creates signed release builds and GitHub releases.

**Triggers:**
- Push to version tags (e.g., `v1.28.2`)
- Manual workflow dispatch with version parameters

**What it does:**
1. Builds native libraries and release APK
2. Signs APK (uses secrets if available, generates self-signed otherwise)
3. Generates SHA256 checksums
4. Creates GitHub Release with APK and checksums

**Usage:**
```bash
# Option 1: Push a version tag (automatic release)
git tag v1.28.2
git push origin v1.28.2

# Option 2: Manual dispatch via GitHub Actions UI
# Go to Actions → Android Release → Run workflow
# Enter version name (e.g., 1.28.2) and version code (e.g., 4396)
```

### 3. CodeQL Security Scan (`.github/workflows/codeql.yml`)

**Security scanning workflow** - analyzes code for vulnerabilities.

**Triggers:**
- Push to `main` branch
- Pull requests to `main` branch
- Weekly schedule (Mondays)

**What it does:**
1. Analyzes Java code for security issues
2. Reports findings in Security tab

### 4. Legacy Workflows (Compatibility)

- `build-self-signed.yaml`: Previous main workflow (replaced by ci.yml and release.yml)
- `build-app.yaml`: Original debug build workflow (kept for `release` branch)
- `release-app.yaml`: Original release workflow (disabled)
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

## Signing Configuration

### Self-Signed APKs (Default)

By default, this fork uses **self-signed** APKs generated automatically by GitHub Actions:

- **Keystore**: Generated per-build using `keytool` with standard parameters
- **Validity**: 10,000 days (approx. 27 years)
- **Password**: Standard password (for convenience in personal use)
- **Not for distribution**: These APKs are for personal use only

### Using Your Own Keystore (Recommended for Personal Use)

For consistent signatures across builds (allows seamless updates), configure GitHub Secrets:

1. Generate a permanent keystore locally:
   ```bash
   keytool -genkeypair \
     -keystore release.keystore \
     -alias android \
     -keyalg RSA \
     -keysize 2048 \
     -validity 10000 \
     -storepass YOUR_STORE_PASSWORD \
     -keypass YOUR_KEY_PASSWORD \
     -dname "CN=Your Name, OU=Personal, O=Personal, L=City, ST=State, C=US"
   ```

2. Base64-encode the keystore:
   ```bash
   base64 -i release.keystore -o release.keystore.base64
   ```

3. Add the following secrets to your GitHub repository (Settings → Secrets → Actions):
   - `ANDROID_KEYSTORE_BASE64`: Contents of `release.keystore.base64`
   - `ANDROID_KEYSTORE_PASSWORD`: Your store password
   - `ANDROID_KEY_ALIAS`: `android` (or your chosen alias)
   - `ANDROID_KEY_PASSWORD`: Your key password

4. The release workflow will automatically use these secrets when available

### Secret Naming Convention

This fork now uses standardized secret names for consistency:

**New names (recommended)**:
- `ANDROID_KEYSTORE_BASE64` — Base64-encoded keystore file
- `ANDROID_KEYSTORE_PASSWORD` — Keystore password
- `ANDROID_KEY_ALIAS` — Key alias
- `ANDROID_KEY_PASSWORD` — Key password

**Old names (still supported for backward compatibility)**:
- `SYNCTHING_RELEASE_STORE_FILE` — Path to keystore file
- `SYNCTHING_RELEASE_KEY_ALIAS` — Key alias
- `SIGNING_PASSWORD` — Password for both keystore and key

The build system checks for new names first, then falls back to old names.

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
