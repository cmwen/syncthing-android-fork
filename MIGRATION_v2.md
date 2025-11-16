# Migration to Syncthing 2.0.11

This document describes the migration from Syncthing 1.x to 2.0.11 in this fork.

## Overview

This fork has been updated from **Syncthing v1.28.1** to **v2.0.11**. Syncthing 2.0 includes significant architectural changes and performance improvements.

## Critical Information

### ⚠️ Protocol Incompatibility

**Syncthing 2.x devices CANNOT sync with 1.x devices.**

You must upgrade **all devices** in your Syncthing network together:
1. Upgrade all Android devices running this app
2. Upgrade desktop clients (Windows, Mac, Linux)
3. Upgrade any servers or NAS devices
4. Upgrade any other devices in your sync network

**Do not upgrade only some devices** - they will not be able to sync with devices still on 1.x.

### Database Migration

On first launch after upgrading to 2.0:
- The app will automatically migrate your database from LevelDB to SQLite
- This can take **several minutes** for large setups with many files/folders
- The app may appear frozen during migration - this is normal
- **Do not force-close the app** during migration
- Your data and configuration will be preserved

## Major Changes in Syncthing 2.0

### 1. Database Backend (SQLite)
- **Old**: LevelDB
- **New**: SQLite
- **Impact**: More reliable, easier to debug, automatic migration on first launch
- **Action**: None required - migration is automatic

### 2. Performance Improvements
- **Removed**: Rolling hash detection (rarely useful in practice)
- **Added**: Multiple connections by default (3 connections per device pair)
- **Result**: Faster scanning and syncing

### 3. Deleted Item Retention
- **Old**: Deleted items tracked forever
- **New**: Deleted items expire after 15 months
- **Impact**: Reduces database size over time
- **Action**: None required for most users

### 4. Structured Logging
- New structured log format with key-value pairs
- Better diagnostic information
- No impact on Android app usage

### 5. Command Line Changes
- Modernized CLI options (single-dash long options removed)
- No impact on Android app (uses native library, not CLI)

## Build System Changes

### Go Version Requirement
- **Old**: Go 1.22.7
- **New**: Go 1.23.12+ (minimum 1.23.0)
- **Files Updated**:
  - `docker/Dockerfile`: Updated Go version
  - `.devcontainer/Dockerfile`: Updated Go package

### Version Information
- **App Version Code**: 4397 → 4398
- **App Version Name**: 1.28.3 → 2.0.11
- **Syncthing Submodule**: Updated to v2.0.11 commit

## Upgrade Instructions

### For Users

1. **Backup** (recommended but optional):
   - Export your Syncthing configuration if possible
   - Note your folder IDs and device IDs

2. **Coordinate Upgrade**:
   - Plan to upgrade all devices in your network at the same time
   - Inform other users in shared folders about the upgrade

3. **Install Update**:
   - Download the new APK from [Releases](https://github.com/cmwen/syncthing-android-fork/releases)
   - Install over existing version (will preserve data)
   - Grant any requested permissions

4. **First Launch**:
   - Launch the app
   - Wait for database migration (can take several minutes)
   - Do not force-close during migration
   - Check that all folders and devices are visible

5. **Verify Operation**:
   - Check that folders are syncing
   - Verify connections to other devices
   - Monitor for any errors or issues

### For Developers

1. **Update Go Version**:
   ```bash
   # Ensure Go 1.23+ is installed
   go version
   ```

2. **Update Submodule**:
   ```bash
   cd syncthing/src/github.com/syncthing/syncthing
   git fetch --tags
   git checkout v2.0.11
   cd ../../../../..
   git add syncthing/src/github.com/syncthing/syncthing
   ```

3. **Build Native Library**:
   ```bash
   ./gradlew buildNative
   ```

4. **Build APK**:
   ```bash
   ./gradlew assembleDebug
   ```

## Rollback Instructions

If you need to rollback to Syncthing 1.x:

1. **Uninstall** the 2.0.11 version
2. **Download** version 1.28.3 from [previous releases](https://github.com/cmwen/syncthing-android-fork/releases/tag/v1.28.3)
3. **Install** the 1.28.3 APK

⚠️ **Warning**: The database migration from 1.x to 2.0 is **one-way**. If you rollback:
- You may need to reconfigure folders and devices
- Syncthing will recreate its database from scratch
- Your sync history will be lost (but files are safe)

## Known Issues

### Database Migration Time
- Large setups may take 10+ minutes to migrate
- Progress is not shown during migration
- App may appear unresponsive - this is normal

### First Sync After Migration
- Initial scan may take longer than usual
- Large folders may trigger re-indexing
- This is normal and should only happen once

## Testing

Before deploying to production:
1. Test migration on a non-critical device first
2. Verify sync operation with a small test folder
3. Monitor logs for any errors or warnings
4. Wait for full sync before upgrading other devices

## Support

This is a personal fork maintained for personal use. For issues:
1. Check [Syncthing 2.0 Release Notes](https://github.com/syncthing/syncthing/releases/tag/v2.0.0)
2. Review [Syncthing Documentation](https://docs.syncthing.net/)
3. Open an issue in this repository for fork-specific problems

## References

- [Syncthing 2.0.0 Release Notes](https://github.com/syncthing/syncthing/releases/tag/v2.0.0)
- [Syncthing 2.0.11 Release Notes](https://github.com/syncthing/syncthing/releases/tag/v2.0.11)
- [Syncthing Documentation](https://docs.syncthing.net/)
- [Original syncthing-android Repository](https://github.com/syncthing/syncthing-android)

---

**Last Updated**: November 2025  
**Fork Maintainer**: cmwen  
**Syncthing Version**: 2.0.11
