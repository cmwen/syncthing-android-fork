# GitHub Actions Workflows Guide

This document explains the CI/CD workflows for building and releasing Syncthing Android.

## Overview

This fork uses GitHub Actions to automatically build and release APKs. The workflows are designed for personal use with self-signed APKs, requiring no external secrets or credentials.

## Workflows

### 1. Build Self-Signed APK (`build-self-signed.yaml`)

**Purpose**: Main workflow for this fork - automatically builds and releases self-signed APKs.

**File**: `.github/workflows/build-self-signed.yaml`

#### Triggers

| Event | Condition | Action |
|-------|-----------|--------|
| Push to `main` | Any commit | Build debug APK as artifact |
| Push tag `v*` | Version tags (e.g., `v1.28.2`) | Build release APK + Create GitHub Release |
| Manual dispatch | Via Actions UI | Build debug or release on-demand |

#### Jobs

##### Job 1: `build-debug`

Runs when: Push to main branch (not tags) OR manual dispatch with "debug"

Steps:
1. Checkout code with submodules
2. Configure Git safe directories
3. Build native Syncthing libraries (Go → .so files)
4. Run Android lint checks
5. Assemble debug APK
6. Upload APK and lint reports as artifacts

**Artifacts produced:**
- `syncthing-android-debug-{sha}.apk` - Debug APK
- `lint-reports-{sha}` - Lint analysis reports

##### Job 2: `build-release`

Runs when: Push version tag OR manual dispatch with "release"

Steps:
1. Checkout code with submodules
2. Configure Git safe directories
3. Generate self-signed keystore (on-the-fly)
4. Build native Syncthing libraries
5. Run Android lint checks
6. Build signed release APK
7. Generate SHA256 checksum
8. Upload APK and checksum as artifacts
9. (If tag) Create GitHub Release with APK

**Artifacts produced:**
- `syncthing-android-release-{sha}.apk` - Signed release APK
- `sha256sum.txt` - Checksum file
- `lint-reports-release-{sha}` - Lint reports
- **GitHub Release** (only for tags)

#### Environment Variables

The workflow uses these environment variables:

| Variable | Purpose | Value/Source |
|----------|---------|--------------|
| `BUILD_USER` | Build metadata | `android-builder` |
| `BUILD_HOST` | Build metadata | `github.actions` |
| `SYNCTHING_RELEASE_STORE_FILE` | Path to keystore | Generated in `${{ runner.temp }}` |
| `SYNCTHING_RELEASE_KEY_ALIAS` | Keystore key alias | `android` |
| `SIGNING_PASSWORD` | Keystore password | `android` (for convenience) |

**Note**: The keystore is generated per-build and not persistent. This is intentional for personal use.

#### Usage Examples

**Trigger debug build:**
```bash
# Push to main branch
git checkout main
git push origin main

# Wait for workflow to complete
# Download APK from: Actions → Build Self-Signed APK → Artifacts
```

**Trigger release build:**
```bash
# Update version in app/build.gradle.kts
# versionCode = 4396
# versionName = "1.28.2"

# Commit and tag
git add app/build.gradle.kts
git commit -m "Bump version to 1.28.2"
git tag v1.28.2
git push origin main
git push origin v1.28.2

# Wait for workflow to complete
# Release will appear at: Releases page
```

**Manual trigger:**
1. Go to: Actions → Build Self-Signed APK
2. Click "Run workflow"
3. Select branch (usually `main`)
4. Choose build type (debug or release)
5. Click "Run workflow"

---

### 2. Build App (Legacy) (`build-app.yaml`)

**Purpose**: Original debug build workflow from upstream.

**Status**: Kept for compatibility with `release` branch. For this fork, use `build-self-signed.yaml` instead.

**Triggers**: Push/PR to `release` branch

**What it does**: Builds debug APK using the upstream builder Docker image

---

### 3. Release App (Legacy) (`release-app.yaml`)

**Purpose**: Original release workflow requiring Google Play credentials and signing secrets.

**Status**: Disabled (modified tag pattern to avoid conflicts)

**Original triggers**: Version tags like `1.28.1`, `1.28.1.2`, `1.28.1-rc.1`

**Why disabled**: This fork doesn't have the required secrets and doesn't publish to Google Play. Use `build-self-signed.yaml` with `v*` tags instead.

**Note**: If you want to use this workflow, you'll need to:
1. Generate a permanent keystore
2. Add secrets to GitHub:
   - `SIGNING_KEYSTORE_JKS_BASE64`
   - `SIGNING_PASSWORD`
   - `GNUPG_SIGNING_KEY_BASE64` (optional, for checksums)
3. Change tag pattern back to match version numbers

---

### 4. Builder Image (`build-builder.yaml`)

**Purpose**: Builds and publishes the Docker image used by CI workflows.

**Triggers**: 
- Pull requests that modify `docker/**` files
- Manual dispatch

**What it does**:
- PR: Builds image without pushing (validation only)
- Manual: Builds and pushes to `ghcr.io/syncthing/syncthing-android-builder`

**When to use**: Only when modifying the Docker build environment (rare).

---

## Container Image

All build workflows use the Docker container: `ghcr.io/syncthing/syncthing-android-builder`

**Contents:**
- Eclipse Temurin JDK 11
- Go 1.22.7
- Android SDK (platform-tools, build-tools 34.0.0, platform 34)
- Android NDK 25.2.9519653
- Python 3
- Build essentials (gcc, make, etc.)

**Dockerfile**: `docker/Dockerfile`

---

## Self-Signed Keystore Details

The `build-self-signed.yaml` workflow generates a keystore on-the-fly:

```bash
keytool -genkeypair \
  -keystore release-key.jks \
  -alias android \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass android \
  -keypass android \
  -dname "CN=Personal Fork, OU=Personal, O=Personal, L=Personal, ST=Personal, C=US"
```

**Properties:**
- **Algorithm**: RSA 2048-bit
- **Validity**: 10,000 days (~27 years)
- **Password**: `android` (stored in plain text in workflow)
- **Distinguished Name**: Generic personal fork info

**Security Considerations:**

✅ **Acceptable for personal use:**
- Keystore is temporary (not reused)
- Password exposure is not critical (personal fork)
- APK signing ensures integrity (prevents tampering)
- Good enough for non-distributed apps

❌ **Not suitable for:**
- Public distribution
- Play Store publishing (requires persistent key)
- Multi-user scenarios
- High-security requirements

**To use a persistent keystore:**

1. Generate keystore locally:
   ```bash
   keytool -genkeypair -keystore my-release-key.jks -alias my-key \
     -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Base64 encode it:
   ```bash
   cat my-release-key.jks | base64 > keystore.b64
   ```

3. Add to GitHub Secrets:
   - Name: `SIGNING_KEYSTORE_JKS_BASE64`
   - Value: Contents of `keystore.b64`
   - Also add: `SIGNING_PASSWORD`, `SYNCTHING_RELEASE_KEY_ALIAS`

4. Modify workflow to use secrets:
   ```yaml
   - name: Setup Keystore
     run: |
       echo '${{ secrets.SIGNING_KEYSTORE_JKS_BASE64 }}' | base64 -d > ${{ runner.temp }}/release-key.jks
   
   - name: Build Release APK
     env:
       SYNCTHING_RELEASE_STORE_FILE: ${{ runner.temp }}/release-key.jks
       SYNCTHING_RELEASE_KEY_ALIAS: ${{ secrets.SYNCTHING_RELEASE_KEY_ALIAS }}
       SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
     run: ./gradlew assembleRelease
   ```

---

## Workflow Best Practices

### For AI Assistants

When modifying workflows:

1. **Test incrementally**: Use manual dispatch to test changes
2. **Check container compatibility**: Ensure tools/versions match Dockerfile
3. **Validate paths**: APK location is `app/build/outputs/apk/{buildType}/app-{buildType}.apk`
4. **Review artifacts**: Always check what files are uploaded
5. **Security**: Never commit secrets, use GitHub Secrets instead

### For Human Developers

1. **Use draft releases**: Set `draft: true` when testing release workflow
2. **Verify checksums**: Always compare SHA256 before installing APK
3. **Tag conventions**: Use `v*` for releases, avoid `upstream-*` tags
4. **Clean builds**: If build fails, try deleting `build/` directories first

---

## Troubleshooting

### Build fails in CI but works locally

**Possible causes:**
- Missing submodules: Check `.gitmodules` is included
- Environment differences: Check Dockerfile vs local setup
- Cache issues: Clear workflow cache (Settings → Actions → Caches)

**Solution:**
```bash
# Test in Docker locally
docker build -f docker/Dockerfile -t test-builder .
docker run --rm -v $(pwd):/project test-builder ./gradlew clean buildNative assembleDebug
```

### APK not appearing in release

**Possible causes:**
- Build failed before release step
- Artifact path incorrect
- Tag pattern doesn't match

**Solution:**
- Check workflow logs: Actions → Failed workflow → View logs
- Verify APK exists: Look for "Upload Release APK" step
- Confirm tag pattern: Must start with `v` (e.g., `v1.28.2`)

### Keystore generation fails

**Possible causes:**
- JDK not available in container
- Permissions issue in `runner.temp`

**Solution:**
```yaml
- name: Debug keytool
  run: |
    which keytool
    keytool -help
    ls -la ${{ runner.temp }}
```

### Workflow doesn't trigger

**Possible causes:**
- Branch/tag pattern doesn't match
- Workflow file has syntax errors
- GitHub Actions disabled

**Solution:**
- Validate YAML: Use online YAML validator
- Check Actions settings: Settings → Actions → General
- Test with manual dispatch first

---

## Future Improvements

Potential enhancements for this workflow:

- [ ] Add workflow to automatically update dependencies
- [ ] Implement automatic changelog generation
- [ ] Add code coverage reporting
- [ ] Create nightly builds from main branch
- [ ] Add APK size tracking over time
- [ ] Implement automatic security scanning
- [ ] Add workflow to check for upstream updates

---

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android Gradle Plugin](https://developer.android.com/build)
- [keytool Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
- [APK Signing](https://developer.android.com/studio/publish/app-signing)

---

*Last updated: 2024-11 - This document should be updated when workflows change significantly.*
