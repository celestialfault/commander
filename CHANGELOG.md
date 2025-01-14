# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 2.0.0 - 2025-01-14

### Changed

- **[BREAKING]** `Commander` is now a class that must be instantiated to avoid type handlers from multiple mods conflicting with each other
- **[BREAKING]** `Commander.register(KClass, ArgumentHandler)` has been renamed to `Commander#addHandler(KClass, ArgumentHandler)`
- **[BREAKING]** `@Suggestions` has been renamed to `@Suggests` to avoid name clashes with the Brigadier class
- **[BREAKING]** The built-in primitive argument handlers are now classes instead of objects

## 1.0.1 - 2024-12-31

### Fixed

- Annotations on types not being properly discovered
