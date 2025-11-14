# Project Structure

Detailed breakdown of the Syncthing Android fork repository structure.

## Directory Tree

```
syncthing-android-fork/
│
├── .git/                           # Git repository data
├── .github/                        # GitHub-specific files
│   ├── workflows/                  # CI/CD workflow definitions
│   │   ├── build-self-signed.yaml  # ⭐ Main build workflow (self-signed APKs)
│   │   ├── build-app.yaml          # Legacy debug build workflow
│   │   ├── release-app.yaml        # Legacy release workflow (disabled)
│   │   ├── build-builder.yaml      # Docker image builder workflow
│   │   └── image-builder-template.yaml  # Builder workflow template
│   ├── AI_CONTEXT.md               # ⭐ AI assistant overview
│   ├── WORKFLOWS.md                # ⭐ Workflow documentation
│   ├── QUICK_START.md              # ⭐ Quick reference guide
│   └── PROJECT_STRUCTURE.md        # This file
│
├── app/                            # 📱 Main Android application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/nutomic/syncthingandroid/  # Java source code
│   │   │   │   ├── activities/     # UI screens (Activities)
│   │   │   │   ├── fragments/      # UI components (Fragments)
│   │   │   │   ├── service/        # Background services
│   │   │   │   ├── model/          # Data models
│   │   │   │   ├── util/           # Utility classes
│   │   │   │   └── ...
│   │   │   ├── res/                # Android resources
│   │   │   │   ├── layout/         # XML UI layouts
│   │   │   │   ├── values/         # Strings, colors, dimensions
│   │   │   │   ├── values-*/       # Translated strings (i18n)
│   │   │   │   ├── drawable/       # Images and icons
│   │   │   │   └── xml/            # Configuration files
│   │   │   ├── jniLibs/            # Native libraries (.so files)
│   │   │   │   ├── armeabi-v7a/    # 32-bit ARM
│   │   │   │   ├── arm64-v8a/      # 64-bit ARM
│   │   │   │   ├── x86/            # 32-bit x86
│   │   │   │   └── x86_64/         # 64-bit x86
│   │   │   ├── play/               # Google Play Store metadata
│   │   │   │   ├── listings/       # Store listings per language
│   │   │   │   └── release-notes/  # Release notes
│   │   │   └── AndroidManifest.xml # App manifest (permissions, components)
│   │   ├── androidTest/            # Android instrumentation tests
│   │   └── test/                   # Unit tests
│   ├── build.gradle.kts            # ⭐ App build configuration (Kotlin DSL)
│   ├── lint.xml                    # Lint configuration
│   └── .gitignore                  # App-specific git ignore rules
│
├── syncthing/                      # 🔧 Native Syncthing library (Git submodule)
│   ├── lib/                        # Go source code for Syncthing core
│   ├── build.gradle.kts            # Native build configuration
│   ├── make-all.bash               # Build script for all architectures
│   └── ...                         # Other Syncthing core files
│
├── docker/                         # 🐳 Docker build environment
│   ├── Dockerfile                  # Builder container definition
│   ├── prebuild.sh                 # Pre-build setup script
│   └── README.md                   # Docker usage instructions
│
├── gradle/                         # Gradle wrapper files
│   └── wrapper/
│       ├── gradle-wrapper.jar      # Gradle wrapper executable
│       └── gradle-wrapper.properties  # Wrapper configuration
│
├── graphics/                       # 🎨 App graphics and assets
│   ├── ic_launcher-web.png         # App icon
│   └── ...                         # Other graphics
│
├── scripts/                        # 🔨 Build and utility scripts
│   └── ...
│
├── build.gradle.kts                # ⭐ Root build configuration
├── settings.gradle.kts             # ⭐ Gradle settings (modules, repositories)
├── gradle.properties               # Gradle properties
├── gradlew                         # Gradle wrapper script (Unix)
├── gradlew.bat                     # Gradle wrapper script (Windows)
│
├── .gitignore                      # Git ignore rules
├── .gitmodules                     # Git submodules configuration
│
├── README.md                       # ⭐ Main documentation
├── LICENSE                         # MPLv2 license
├── CONTRIBUTING.md                 # Contribution guidelines
└── ISSUE_TEMPLATE.md               # GitHub issue template
```

⭐ = Key files for understanding the project

## Key Files Explained

### Build Configuration

#### `build.gradle.kts` (Root)
- **Purpose**: Top-level build configuration for all modules
- **Contains**: 
  - NDK version (shared across modules)
  - Build script dependencies (Android Gradle Plugin)
  - Clean task definition
- **Used by**: Gradle build system
- **Modified when**: Updating Gradle plugins, changing NDK version

#### `settings.gradle.kts`
- **Purpose**: Defines project structure and dependency repositories
- **Contains**:
  - Module declarations (`:app`, `:syncthing`)
  - Repository configuration (Google, Maven Central, JitPack)
  - Plugin management
- **Used by**: Gradle build system
- **Modified when**: Adding/removing modules, changing repositories

#### `app/build.gradle.kts`
- **Purpose**: Android app-specific build configuration
- **Contains**:
  - App metadata (package name, version)
  - SDK versions (min, target, compile)
  - Dependencies (libraries)
  - Build types (debug, release)
  - Signing configuration
- **Used by**: Android Gradle Plugin
- **Modified when**: 
  - Updating version for release
  - Adding new dependencies
  - Changing build configuration

### Documentation Files

#### `.github/AI_CONTEXT.md`
- **Purpose**: Comprehensive project overview for AI assistants
- **Contains**:
  - Technology stack
  - Project structure overview
  - Build process explanation
  - Common issues and solutions
- **Audience**: AI assistants, new contributors
- **Update when**: Major structural changes

#### `.github/WORKFLOWS.md`
- **Purpose**: Detailed CI/CD workflow documentation
- **Contains**:
  - Workflow triggers and behavior
  - Environment variables
  - Keystore generation details
  - Troubleshooting guide
- **Audience**: Developers, CI/CD maintainers
- **Update when**: Workflow changes

#### `.github/QUICK_START.md`
- **Purpose**: Fast reference for common tasks
- **Contains**:
  - Quick build commands
  - Common troubleshooting
  - File locations
  - Useful commands
- **Audience**: All users
- **Update when**: Adding new common tasks

#### `README.md`
- **Purpose**: Main project documentation
- **Contains**:
  - Project overview
  - Installation instructions
  - Build instructions
  - License and credits
- **Audience**: General public, contributors
- **Update when**: Major project changes

### Workflow Files

#### `.github/workflows/build-self-signed.yaml`
- **Purpose**: Main CI/CD workflow for this fork
- **Triggers**:
  - Push to main (debug build)
  - Version tags (release build + GitHub Release)
  - Manual dispatch
- **Outputs**: Self-signed APKs, checksums, GitHub Releases

#### `.github/workflows/build-app.yaml` (Legacy)
- **Purpose**: Original debug build workflow
- **Status**: Kept for compatibility
- **Triggers**: Push to `release` branch

#### `.github/workflows/release-app.yaml` (Legacy)
- **Purpose**: Original release workflow (requires secrets)
- **Status**: Disabled (uses non-standard tags)
- **Note**: For Play Store publishing

### Source Code Structure

#### `app/src/main/java/com/nutomic/syncthingandroid/`

```
activities/
├── MainActivity.java            # Main app screen
├── SettingsActivity.java        # Settings screen
├── FolderActivity.java          # Folder configuration
└── ...

fragments/
├── DrawerFragment.java          # Navigation drawer
└── ...

service/
├── SyncthingService.java        # Main background service
├── RestApi.java                 # Syncthing REST API wrapper
└── ...

model/
├── Device.java                  # Device data model
├── Folder.java                  # Folder data model
└── ...

util/
├── Util.java                    # General utilities
├── ConfigXml.java               # Config file parser
└── ...
```

### Resource Structure

#### `app/src/main/res/`

```
layout/                          # UI layouts
├── activity_main.xml            # Main screen layout
├── fragment_drawer.xml          # Drawer layout
└── ...

values/                          # Default values
├── strings.xml                  # English strings
├── colors.xml                   # Color definitions
├── dimens.xml                   # Dimensions
└── styles.xml                   # UI styles

values-de/                       # German translations
├── strings.xml
└── ...

values-es/                       # Spanish translations
values-fr/                       # French translations
...

drawable/                        # Images and icons
├── ic_launcher.xml              # App icon
└── ...

xml/                            # Configuration files
└── preferences.xml              # Settings structure
```

## Module Dependencies

```
Root Project
├── :app (Android Application)
│   ├── Depends on: :syncthing
│   ├── Libraries: Material, Gson, Dagger, etc.
│   └── Produces: APK files
│
└── :syncthing (Native Library)
    ├── Depends on: Go compiler, NDK
    ├── Source: Git submodule (Syncthing core)
    └── Produces: libsyncthing.so (all ABIs)
```

## Build Artifacts

### Build Directory Structure

```
app/build/
├── outputs/
│   ├── apk/
│   │   ├── debug/
│   │   │   └── app-debug.apk         # Debug APK
│   │   └── release/
│   │       └── app-release.apk       # Release APK (signed)
│   ├── bundle/
│   │   └── release/
│   │       └── app-release.aab       # Android App Bundle
│   └── logs/
│       └── manifest-merger-*.txt     # Build logs
│
├── reports/
│   ├── lint-results.html             # Lint report
│   └── tests/                        # Test results
│
├── intermediates/                    # Intermediate build files
└── tmp/                              # Temporary files
```

## Data Flow

### Build Process

```
1. Root build.gradle.kts
   ↓
2. settings.gradle.kts (defines modules)
   ↓
3. app/build.gradle.kts (app configuration)
   ↓
4. Resolve dependencies
   ↓
5. Build native libraries (:syncthing:buildNative)
   ↓
6. Compile Java code
   ↓
7. Process resources
   ↓
8. Merge JNI libs (libsyncthing.so)
   ↓
9. Package APK
   ↓
10. Sign APK (if configured)
    ↓
11. Output: app-{debug|release}.apk
```

### CI/CD Flow (build-self-signed.yaml)

```
1. GitHub trigger (push/tag)
   ↓
2. Checkout code + submodules
   ↓
3. Run in Docker container
   ↓
4. Generate keystore (for release)
   ↓
5. Build native libraries
   ↓
6. Run lint
   ↓
7. Assemble APK
   ↓
8. Generate checksum
   ↓
9. Upload artifacts
   ↓
10. Create GitHub Release (if tag)
```

## Important Paths

### For Development
- **Source code**: `app/src/main/java/`
- **Resources**: `app/src/main/res/`
- **Manifest**: `app/src/main/AndroidManifest.xml`
- **Build config**: `app/build.gradle.kts`

### For Building
- **Native build**: `./gradlew buildNative`
- **APK output**: `app/build/outputs/apk/`
- **Lint report**: `app/build/reports/lint-results.html`

### For CI/CD
- **Workflows**: `.github/workflows/`
- **Documentation**: `.github/*.md`
- **Docker**: `docker/Dockerfile`

## File Size Reference

Typical build artifact sizes:

- **Debug APK**: ~45-50 MB
- **Release APK**: ~40-45 MB (optimized)
- **Native libraries**: ~10-15 MB each ABI
- **Full build directory**: ~500 MB - 1 GB

## Git Submodules

### `syncthing/`
- **URL**: https://github.com/syncthing/syncthing
- **Purpose**: Syncthing core (Go code)
- **Update**: `git submodule update --remote syncthing`
- **Note**: Specific commit is pinned in `.gitmodules`

## Configuration Files

| File | Purpose | Format |
|------|---------|--------|
| `gradle.properties` | Gradle settings (memory, parallel builds) | Properties |
| `local.properties` | Local SDK paths (not committed) | Properties |
| `app/lint.xml` | Lint rule configuration | XML |
| `.gitignore` | Files to ignore in Git | Text |
| `.gitmodules` | Submodule definitions | Git config |

---

*This structure is current as of the fork creation. For updates, see git history.*
