# Quick Start Guide

Fast reference for common tasks in this Syncthing Android fork.

## For Users

### Install Pre-Built APK

1. Go to [Releases](https://github.com/cmwen/syncthing-android-fork/releases)
2. Download latest `app-release.apk`
3. Enable "Unknown Sources" on Android device
4. Install APK
5. (Optional) Verify SHA256: Compare `sha256sum.txt` with computed hash

### Update to New Version

1. Download new APK from Releases
2. Android will prompt to update (or uninstall old version first)
3. Install new APK
4. Your data/settings are preserved

---

## For Developers

### Initial Setup

```bash
# Clone with submodules
git clone --recursive https://github.com/cmwen/syncthing-android-fork.git
cd syncthing-android-fork

# Verify submodules
git submodule status

# Set environment
export ANDROID_HOME=/path/to/android-sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
```

### Quick Build

```bash
# Build native libraries (required first time)
./gradlew buildNative

# Build debug APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device

```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or copy APK to device and install manually
```

### Clean Build

```bash
# Clean everything
./gradlew clean

# Also clean native libraries
rm -rf syncthing/gobuild syncthing/pkg

# Rebuild from scratch
./gradlew buildNative assembleDebug
```

---

## For CI/CD

### Trigger Debug Build

Push to main branch:
```bash
git add .
git commit -m "Your changes"
git push origin main
```

Download from: Actions → Build Self-Signed APK → Artifacts

### Create Release

1. **Update version** in `app/build.gradle.kts`:
   ```kotlin
   versionCode = 4396  // Increment by 1
   versionName = "1.28.2"  // Update
   ```

2. **Commit and tag**:
   ```bash
   git add app/build.gradle.kts
   git commit -m "Bump version to 1.28.2"
   git push origin main
   
   git tag v1.28.2
   git push origin v1.28.2
   ```

3. **Wait for workflow**: Check Actions tab

4. **Download**: Releases page will have new release

### Manual Workflow Trigger

1. Go to Actions → Build Self-Signed APK
2. Click "Run workflow"
3. Select branch (main)
4. Choose type (debug/release)
5. Click "Run workflow" button

---

## Common Tasks

### Update Dependencies

```bash
# Check for updates
./gradlew dependencyUpdates

# Update in build files manually
# Then test:
./gradlew clean buildNative assembleDebug
```

### Run Lint

```bash
./gradlew lint

# View results
open app/build/reports/lint-results.html
```

### View Build Configuration

```bash
# List all tasks
./gradlew tasks

# List properties
./gradlew properties

# Show project structure
./gradlew projects
```

### Sync with Upstream (if needed)

```bash
# Add upstream remote (one time)
git remote add upstream https://github.com/syncthing/syncthing-android.git

# Fetch upstream changes
git fetch upstream

# View differences
git log HEAD..upstream/main

# Merge (if desired)
git merge upstream/main

# Update submodules
git submodule update --remote
```

---

## Troubleshooting

### Build Fails: "SDK not found"

```bash
# Set ANDROID_HOME
export ANDROID_HOME=/path/to/android-sdk

# Verify
echo $ANDROID_HOME
ls $ANDROID_HOME/platforms
```

### Build Fails: "Go not found"

```bash
# Install Go 1.22.7+
# https://go.dev/dl/

# Verify
go version
```

### Build Fails: "NDK not found"

```bash
# Install NDK via SDK Manager
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "ndk;25.2.9519653"

# Or set ANDROID_NDK_HOME
export ANDROID_NDK_HOME=$ANDROID_HOME/ndk/25.2.9519653
```

### Native Build Fails

```bash
# Check Go version
go version  # Should be 1.22.7+

# Check NDK
ls $ANDROID_HOME/ndk/

# Clean and rebuild
./gradlew clean
rm -rf syncthing/gobuild syncthing/pkg
./gradlew buildNative
```

### APK Won't Install

```bash
# Uninstall existing version
adb uninstall com.nutomic.syncthingandroid
# or
adb uninstall com.nutomic.syncthingandroid.debug

# Then reinstall
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Workflow Not Triggering

1. **Check Actions settings**: Settings → Actions → General
2. **Verify workflow file**: No YAML syntax errors
3. **Check branch protection**: May block pushes
4. **Try manual dispatch**: Actions → Run workflow

### Release Not Created

1. **Check tag format**: Must be `v*` (e.g., `v1.28.2`)
2. **View workflow logs**: Actions → Failed run → Logs
3. **Verify APK built**: Check for "Build Release APK" step
4. **Check permissions**: Repository settings → Actions → General → Workflow permissions

---

## Environment Variables Reference

### Build Configuration

| Variable | Purpose | Example |
|----------|---------|---------|
| `ANDROID_HOME` | Android SDK path | `/home/user/Android/Sdk` |
| `JAVA_HOME` | Java JDK path | `/usr/lib/jvm/java-11-openjdk` |
| `ANDROID_NDK_HOME` | NDK path (optional) | `$ANDROID_HOME/ndk/25.2.9519653` |

### Signing Configuration (for manual release builds)

| Variable | Purpose | Example |
|----------|---------|---------|
| `SYNCTHING_RELEASE_STORE_FILE` | Keystore path | `~/.android/release-key.jks` |
| `SYNCTHING_RELEASE_KEY_ALIAS` | Key alias | `android` |
| `SIGNING_PASSWORD` | Keystore password | `your-password` |

### Build Metadata (optional)

| Variable | Purpose | Default |
|----------|---------|---------|
| `BUILD_USER` | Builder name | `android-builder` |
| `BUILD_HOST` | Build host | `localhost` |

---

## File Locations

### Build Outputs

```
app/build/outputs/
├── apk/
│   ├── debug/
│   │   └── app-debug.apk
│   └── release/
│       └── app-release.apk
├── bundle/
│   └── release/
│       └── app-release.aab  (Android App Bundle)
└── logs/
    └── manifest-merger-*.txt
```

### Reports

```
app/build/reports/
├── lint-results.html       # Lint analysis
├── lint-results.xml
└── tests/                  # Test results
```

### Native Libraries

```
app/src/main/jniLibs/
├── armeabi-v7a/
│   └── libsyncthing.so
├── arm64-v8a/
│   └── libsyncthing.so
├── x86/
│   └── libsyncthing.so
└── x86_64/
    └── libsyncthing.so
```

---

## Useful Commands

```bash
# Show Gradle version
./gradlew --version

# List all build variants
./gradlew tasks --all | grep assemble

# Build specific variant
./gradlew assembleDebug
./gradlew assembleRelease

# Install and run
./gradlew installDebug
adb shell am start -n com.nutomic.syncthingandroid.debug/.activity.MainActivity

# Uninstall
./gradlew uninstallDebug

# View connected devices
adb devices

# View device logs
adb logcat | grep Syncthing

# Push APK to device
adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/
```

---

## Links

- **Repository**: https://github.com/cmwen/syncthing-android-fork
- **Releases**: https://github.com/cmwen/syncthing-android-fork/releases
- **Actions**: https://github.com/cmwen/syncthing-android-fork/actions
- **Issues**: https://github.com/cmwen/syncthing-android-fork/issues
- **Original Project**: https://github.com/syncthing/syncthing-android
- **Syncthing Core**: https://github.com/syncthing/syncthing

---

*For detailed information, see [README.md](../README.md) and [WORKFLOWS.md](WORKFLOWS.md)*
