# Open Ventoy for Android — clean-room, free, ad-free

Clean-room open-source replacement inspired by the behavior of
`Ventoy (Unofficial) 12.3.3 (1.1.17)` (APKPure APK,
SHA-256 `1fe302be0a0933a7943c78345162daa1e51c60511dd2f896715d2a275705e725`).

**Not a patch or repack of that APK.** No code, DEX, resources,
native `.so`, or `assets/ventoy/*` bytes were copied. All sources here
are newly written. Ventoy itself is GPL-3.0; this installer is
GPL-3.0 and works fully offline with no `INTERNET` permission —
official Ventoy files are bundled at build time (see `docs/OFFLINE.md`).

## Why a rewrite, not an ad-strip?

REA archive inventory (`ev_a7b22f7ae65acfcf1eb5734b182075a938bb569b0fa7cdb0f32a176ccfa885a7`,
`ev_c59019e4e5a348a331efef14695077487b6cf21f8050f8a8b2483478a57b943e`,
3554 entries) shows the shipped app is heavily monetized:

- AppLovin MAX + ~20 mediation adapters (Bigo, BidMachine, ByteDance/Pangle,
  Chartboost, Fyber, Google AdManager/AdMob, InMobi, ironSource, Mintegral,
  MobileFuse, Moloco, Ogury, PubMatic, Smaato, Unity, Vungle, Yandex, etc.)
- Google Mobile Ads (`AdActivity`, `APPLICATION_ID`), Unity3D Ads,
  Appodeal, StartApp, MBridge, TaurusX/Maticoo SDKs
- `BILLING` permission + Play Billing client, Firebase Measurement,
  AppMetrica, Sentry, Pangle `libapminsight*`, `libpglarmor`
- Ad assets: `assets/iads/`, `assets/ad-viewer/`, `assets/mraid*.js`,
  `assets/omsdk-*.js`, `res/layout/zmaticoo_*`, `res/layout/*applovin*`,
  `res/layout/*admob*`, etc.

Stripping those SDKs and republishing would create an unauthorized
derivative of proprietary code. This repo instead re-implements only
the *functional* surface from observed, non-copyable facts:

| Observed (REA + shell, no JADX) | Clean replacement in this repo |
|---|---|
| Package `com.mixapplications.ventoy_app`, `MainActivity`, USB host, `UsbFileProvider`, `UsbFileKeepAliveService` | `MainActivity.kt`, `UsbDeviceHandler.kt`, `VentoyFileProvider` — new code using `UsbManager` + SAF |
| `blockdevice/usb`, `blockdevice/crypto`, `libmixapplications.so`, `libutils.so`, 7-Zip JBinding | `UsbBlockDevice.kt` (SAF/USB-mass-storage via open lib), no copied native code |
| `filesystems/fs/{fat,ntfs,ext}`, `WimLib`, 7-Zip | `FileSystemTools.kt` delegating to open libs (documented in `docs/`), no bundled proprietary `.so` |
| `assets/ventoy/ventoy.disk.img`, `assets/boot/{boot,core}.img` | Bundled at build time from **official** Ventoy releases only (GPL, `docs/OFFLINE.md` + `PROVENANCE.txt`), SHA-256 verified offline (see `VentoyInstaller.kt`). Nothing copied from the analyzed APK. |
| 13 `classes*.dex`, `lib/arm64-v8a + armeabi-v7a` dual ABI | Single clean `app` module, minSdk 26, no ads ABI baggage |

DEX class/method bodies were **not** recovered (REA Android provider
needs `REA_JADX_MCP_JAR` + JDK, both absent here). All behavior above
`com/mixapplications/*` (90 classes) and ad packages (`com/applovin`,
`com/unity3d`, `com/bytedance`, `com/mbridge`, etc.) is therefore
**inferred from inventory + manifest strings**, not observed execution.
See `docs/ANALYSIS.md` for the finding ledger.

## Features (free, offline, ad-free, tracker-free)

- USB-OTG device request + permission (`android.hardware.usb.host`)
- List USB mass-storage volumes, browse files (SAF + open USB lib)
- Install/upgrade official Ventoy to USB fully offline (bundled files, hash-verified, user-confirmed destructive write)
- Copy ISO/WIM/IMG files to Ventoy USB (plain file copy, no ads gate, no payment, no login)
- No ads SDKs. No `AD_ID`, no Billing, no Firebase, no Sentry, no AppMetrica.
  No network calls at all: no `INTERNET` permission.
- Permissions: USB host, foreground service for long copy, `POST_NOTIFICATIONS` for progress only.
  No `INTERNET`, no `ACCESS_ADSERVICES_*`, no `BILLING`.

## Build (source only in this environment)

No JDK / Android SDK / Gradle is installed in this container, so this
scaffold has **not been compiled**. On a machine with Android Studio:

```bash
# install JDK 17 + Android SDK 34 + Gradle wrapper
./gradlew :app:assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Layout

```
settings.gradle
build.gradle
app/build.gradle
app/src/main/AndroidManifest.xml (no INTERNET)
app/src/main/assets/ventoy/PROVENANCE.txt
app/src/main/java/org/openventoy/android/*.kt
docs/ANALYSIS.md
docs/PRIVACY.md
docs/OFFLINE.md
```

## License

GPL-3.0-only. See `LICENSE`.
Ventoy trademark/releases belong to their owners; this project is
unofficial and bundles official Ventoy files only (see `docs/OFFLINE.md`).
