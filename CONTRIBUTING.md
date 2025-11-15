# Contributing to Syncthing Android Fork

Thank you for your interest in contributing! This is a personal fork maintained for personal use, but improvements are welcome.

## Reporting Issues

Please file issues in the [GitHub Issue Tracker](https://github.com/cmwen/syncthing-android-fork/issues).

Include at least the following in your issue report:
- Clear description of what happened
- What you expected to happen instead
- Version information (Android version, app version)
- Screenshots if the issue is UI-related
- Log entries from Settings → Open Log → Android Log

## Contributing Code

### Before You Start

1. Check existing issues and PRs to avoid duplicate work
2. For major changes, open an issue first to discuss the approach
3. Keep changes minimal and focused
4. Follow existing code style and patterns

### Development Setup

#### Option 1: Using DevContainer (Recommended)

1. Install VS Code with Remote-Containers extension
2. Open the repository in VS Code
3. Click "Reopen in Container" when prompted
4. All dependencies are pre-configured

#### Option 2: Local Development

1. Install Java 17, Go 1.22+, Android SDK/NDK
2. Clone with submodules: `git clone --recursive <repo-url>`
3. Set `ANDROID_HOME` environment variable
4. Run `./gradlew buildNative assembleDebug`

### Code Style

- Follow [Android Code Style Guidelines](https://source.android.com/source/code-style.html)
- Use existing formatting (AS default settings)
- Add comments for complex logic or non-obvious decisions
- Keep functions small and focused

### Testing Your Changes

```bash
# Build native libraries and APK
./gradlew buildNative assembleDebug

# Run lint checks
./gradlew lint

# Clean build
./gradlew clean buildNative assembleDebug
```

### Submitting Changes

1. Fork the repository
2. Create a feature branch from `main`
3. Make your changes with clear commit messages
4. Test thoroughly (build, lint, manual testing)
5. Update documentation if needed
6. Submit a pull request

### What to Contribute

Welcome contributions include:
- Bug fixes
- Build system improvements
- Documentation enhancements
- CI/CD workflow improvements
- Code quality improvements
- Performance optimizations

### What Not to Contribute

Please avoid:
- Major feature additions (this is a personal fork)
- Breaking changes to core functionality
- Changes that significantly deviate from upstream
- Dependencies on external services

## Development Guidelines for AI Agents

If you're an AI agent working on this codebase:
1. Read `AGENTS.md` for comprehensive guidance
2. Follow the two-phase build process (native → Android)
3. Test changes before committing
4. Update relevant documentation
5. Never commit secrets or build artifacts

## License

By contributing, you agree that your contributions will be licensed under the MPLv2 license.
