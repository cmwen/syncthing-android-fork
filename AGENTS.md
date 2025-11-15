# AI Agent Guidance

This repository includes machine-readable and human-readable context to help AI coding assistants produce high-quality changes. Share this document with every automation tool or agent that can modify the codebase.

## Mission

- Build and maintain a personal fork of Syncthing Android for personal use
- Keep automation, documentation, and release workflows in sync with the source code
- Protect sensitive information such as signing keys, API credentials, and user data
- Maintain compatibility with upstream Syncthing Android project structure where possible

## Source of Truth

- `.github/AI_CONTEXT.md` — comprehensive project overview for AI assistants
- `.github/PROJECT_STRUCTURE.md` — detailed project structure and organization
- `.github/WORKFLOWS.md` — workflow and CI/CD documentation
- `.github/QUICK_START.md` — quick start guide for development
- `README.md` — user-facing documentation and build instructions
- `.devcontainer/` — development container configuration for VS Code

## Project Specifics

### Build System

This is a dual-phase build process:
1. **Native Build**: Compiles Go-based Syncthing core using NDK (`./gradlew buildNative`)
2. **Android Build**: Assembles the APK using standard Android Gradle build

### Key Technologies

- **Java/Kotlin**: Android app code (primarily Java)
- **Go**: Syncthing core (compiled as native library)
- **Gradle**: Build system with Kotlin DSL
- **Android NDK**: Native code compilation
- **Docker**: Build container for CI/CD

## Expectations for AI Agents

1. **Minimal Changes**: Make the smallest possible changes to achieve the goal
2. **Test Before Commit**: Always run builds and tests before committing
3. **Respect Structure**: Maintain the two-phase build process (native → Android)
4. **Document Changes**: Update relevant documentation alongside code changes
5. **Security First**: Never commit secrets, API keys, or signing keystores
6. **CI Compatibility**: Ensure changes work in both local and CI environments

## Build and Test Commands

```bash
# Full build (native + APK)
./gradlew buildNative assembleDebug

# Lint checks
./gradlew lint

# Clean build
./gradlew clean buildNative assembleDebug

# Release build (requires signing config)
./gradlew buildNative assembleRelease
```

## Workflows

### CI Workflows

- `.github/workflows/build-self-signed.yaml` — Main CI workflow for debug and release builds
- `.github/workflows/build-app.yaml` — Legacy workflow (compatibility)
- `.github/workflows/build-builder.yaml` — Docker builder image workflow

### Secrets Management

This fork uses environment variables for signing configuration:

**Current naming (being updated)**:
- `SYNCTHING_RELEASE_STORE_FILE` — Path to keystore file
- `SYNCTHING_RELEASE_KEY_ALIAS` — Key alias in keystore
- `SIGNING_PASSWORD` — Keystore and key password

**New naming (standardized with template)**:
- `ANDROID_KEYSTORE_BASE64` — Base64-encoded keystore file
- `ANDROID_KEYSTORE_PASSWORD` — Keystore password
- `ANDROID_KEY_ALIAS` — Key alias in keystore
- `ANDROID_KEY_PASSWORD` — Key password

## Safety and Compliance

- Never commit files from `build/`, `*.keystore`, `*.jks`, or other sensitive artifacts
- Never commit files from `syncthing/` submodule changes (unless specifically updating submodule)
- Use `.gitignore` to prevent accidental commits of build artifacts
- When unsure about requirements, prefer asking for clarification

## Development Environment

### Local Development

1. Clone with submodules: `git clone --recursive <repo-url>`
2. Install Java 17, Go 1.22+, Android SDK/NDK
3. Run `./gradlew buildNative assembleDebug`

### DevContainer Development

1. Open in VS Code with Remote-Containers extension
2. VS Code will build and start the devcontainer automatically
3. All dependencies are pre-installed in the container
4. Run `./gradlew buildNative assembleDebug` inside the container

## Upstream Compatibility

This is a fork of the discontinued syncthing-android project. When making changes:
- Maintain compatibility with core Syncthing functionality
- Respect the original code structure where possible
- Document deviations from upstream in commit messages
- Consider upstreaming useful changes if the project is revived
