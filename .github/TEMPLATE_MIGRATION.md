# Template Migration Notes

This document tracks the migration from the original syncthing-android structure to the standardized practices from [min-android-app-template](https://github.com/cmwen/min-android-app-template).

## Migration Date

November 2025

## Changes Applied

### 1. DevContainer Configuration

**Added:**
- `.devcontainer/devcontainer.json` - VS Code Remote Containers configuration
- `.devcontainer/Dockerfile` - Container image with Android SDK, NDK, Go, Java 17

**Benefits:**
- Consistent development environment across machines
- Pre-configured Android SDK (API 34) and NDK (26.1.10909125)
- Includes Go 1.22 for Syncthing native builds
- All dependencies installed automatically

**Usage:**
```bash
# Open in VS Code with Remote-Containers extension
code .
# Click "Reopen in Container" when prompted
```

### 2. Workflow Modernization

**Added:**
- `.github/workflows/ci.yml` - Continuous integration for all branches
- `.github/workflows/release.yml` - Release workflow with improved secret handling
- `.github/workflows/codeql.yml` - Security scanning with CodeQL

**Replaced:**
- `.github/workflows/build-self-signed.yaml` - Now superseded by ci.yml and release.yml

**Kept for compatibility:**
- `.github/workflows/build-app.yaml` - Legacy workflow for release branch
- `.github/workflows/build-builder.yaml` - Docker builder image workflow

**Key improvements:**
- Separated CI and release concerns
- Better error handling and validation
- Automated security scanning
- Improved artifact management

### 3. Secret Naming Standardization

**New naming convention (preferred):**
- `ANDROID_KEYSTORE_BASE64` - Base64-encoded keystore file
- `ANDROID_KEYSTORE_PASSWORD` - Keystore password
- `ANDROID_KEY_ALIAS` - Key alias in keystore
- `ANDROID_KEY_PASSWORD` - Key password

**Old naming (still supported):**
- `SYNCTHING_RELEASE_STORE_FILE` - Path to keystore file
- `SYNCTHING_RELEASE_KEY_ALIAS` - Key alias
- `SIGNING_PASSWORD` - Password

**Implementation:**
- Modified `app/build.gradle.kts` to check new names first, then fall back to old names
- Maintains backward compatibility with existing secrets
- All workflows updated to use new naming

### 4. AI Agent Support

**Added:**
- `AGENTS.md` - Comprehensive AI agent guidance
- Updated `CONTRIBUTING.md` - Clearer contribution guidelines with AI agent section

**Content:**
- Project mission and constraints
- Build system overview
- Testing commands
- Security guidelines
- Source of truth documentation

### 5. Dependency Management

**Updated:**
- `.github/dependabot.yml` - Enhanced with grouping and GitHub Actions updates

**Improvements:**
- Groups related dependencies (dagger, androidx, android)
- Monitors GitHub Actions for updates
- Better commit message formatting
- Scheduled weekly updates

### 6. Documentation

**Updated:**
- `README.md` - Added devcontainer section, secret naming documentation, workflow details
- `ISSUE_TEMPLATE.md` - Updated for personal fork context
- `.gitignore` - Added devcontainer and VS Code entries

## Migration Checklist

- [x] DevContainer configuration created
- [x] CI workflow modernized
- [x] Release workflow created with new secrets
- [x] CodeQL security scanning added
- [x] Build configuration updated for backward compatibility
- [x] AI agent documentation added
- [x] Contributing guidelines updated
- [x] Dependabot enhanced
- [x] README documentation updated
- [x] Issue templates updated
- [x] .gitignore updated
- [ ] User migration guide for secrets (if needed)

## Breaking Changes

None. All changes maintain backward compatibility with existing secrets and workflows.

## Next Steps

1. Test new workflows on a test branch
2. Migrate repository secrets to new naming (optional, old names still work)
3. Remove legacy workflows after new workflows are proven (optional)
4. Consider adding more structured issue templates (YAML format)

## Rollback Plan

If issues arise:
1. Revert to commit before migration
2. Or disable new workflows and re-enable legacy workflows
3. Build configuration will continue to work with old secret names

## References

- Template repository: https://github.com/cmwen/min-android-app-template
- DevContainer documentation: https://code.visualstudio.com/docs/devcontainers/containers
- GitHub Actions documentation: https://docs.github.com/en/actions
- CodeQL documentation: https://codeql.github.com/docs/
