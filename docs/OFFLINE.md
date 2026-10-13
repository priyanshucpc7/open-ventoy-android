# Offline bundling — official Ventoy only, GPL-3.0

This app works fully offline with no `INTERNET` permission.
Ventoy boot files must be bundled at build time by the maintainer:

1. Download an official release from
   `https://github.com/ventoy/Ventoy/releases` on a separate machine.
2. Record version, source URL, and SHA-256 in
   `app/src/main/assets/ventoy/PROVENANCE.txt` (template below).
3. Place the official files under `app/src/main/assets/ventoy/`,
   e.g. `boot.img`, `core.img`, `ventoy.disk.img` (exact names follow
   the official release layout; do NOT copy these from the analyzed
   third-party APK — its `assets/ventoy/*` bytes are proprietary
   packaging, not the upstream source).
4. `VentoyInstaller.verifyAssetSha256()` checks the hash at runtime
   before any USB write. Mismatch = refuse to install.

`PROVENANCE.txt` template:

```
upstream: Ventoy <version>
source_url: https://github.com/ventoy/Ventoy/releases/tag/v<version>
license: GPL-3.0-only
files:
  boot.img sha256=<hex>
  core.img sha256=<hex>
  ventoy.disk.img sha256=<hex>
```

License: Ventoy is GPL-3.0. Bundling its official files keeps this
installer GPL-3.0-only (see `LICENSE`). Keep this file and upstream
attribution in every build. ISO/WIM/IMG payloads copied by the user
to their own USB remain the user's files.
