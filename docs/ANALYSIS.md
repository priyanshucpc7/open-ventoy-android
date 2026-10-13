# REA finding ledger — Ventoy (Unofficial) APK

Target: `Ventoy+(Unofficial)_12.3.3+(1.1.17)_APKPure.apk`
SHA-256: `1fe302be0a0933a7943c78345162daa1e51c60511dd2f896715d2a275705e725`
Size: 78,530,274 bytes. REA `open_binary` format `apk`, kind `archive`.

## REA Evidence (retained, exported)

- `ev_a7b22f7ae65acfcf1eb5734b182075a938bb569b0fa7cdb0f32a176ccfa885a7`
  (`inspect_artifact`, `authority: shipped-artifact`, `confidence: observed`)
- `ev_c59019e4e5a348a331efef14695077487b6cf21f8050f8a8b2483478a57b943e`
  (`inventory_artifact`, 3337 nodes / 3555 occurrences / 3554 edges)
- Exported bundle: `/tmp/opencode/rea-bundle-final.json` (7,974,134 bytes, 2 records)

Limitations (from REA): static inventory only; ZIP central-directory
entries only; bytes after central directory (APK Signing Block / ZIP
comment) are not occurrences; no execution; no format-specific semantics.

## Answered (observed)

1. **Package identity** — binary `AndroidManifest.xml` UTF-16LE strings
   (ordinary shell parse, not REA): package `com.mixapplications.ventoy_app`,
   `MainActivity`, `UsbFileProvider`, `UsbFileKeepAliveService`,
   USB host feature, `USB_PERMISSION` intent. [shell-observed]
2. **Archive inventory** — 3554 entries: `res/` 3048, `META-INF/` 286,
   `assets/` 79, `lib/` 46, 13× `classes*.dex` (~100 MB total DEX),
   `resources.arsc`, dual ABI `arm64-v8a` + `armeabi-v7a`. [REA Evidence above]
3. **Ad/monetization footprint** — filenames + DEX class-prefix counts
   (shell `zipfile` + regex on shipped bytes, not behavior):
   `com/applovin` 2189, `com/unity3d` 3898, `com/bytedance` 3887,
   `com/mbridge` 4091, `com/google` 22153 (includes GMS/ads),
   plus `ironsource`, `inmobi`, `fyber`, `chartboost`, `smaato`,
   `vungle`, `ogury`, `pubmatic`, `moloco`, `mobilefuse`, `mytarget`,
   `yandex` 10408, `appodeal`, `startapp`, `zmaticoo`, `taurusx`, `maticoo`,
   `bigoads`, `vkads`, `mintegral`, `tapjoy`, `digitalturbine`.
   Manifest activities confirm: AppLovin fullscreen/debugger ×12,
   ByteDance TT* ×15, Chartboost, Fyber/Inneractive, Google `AdActivity`,
   Amazon Aps, Appodeal `TestActivity`, MobileFuse splash, Bigo splash.
   Permissions include `ACCESS_ADSERVICES_AD_ID/ATTRIBUTION/TOPICS`,
   `AD_ID`, `BILLING`. [shell-observed on shipped bytes]
4. **Functional surface** — 90 `com/mixapplications/*` DEX classes:
   `blockdevice/{BlockDevice,crypto/NativeUsbCrypt,usb/UsbNativeCommunication}`,
   `commons/{BrowserActivity,MyApplication,MyMainActivity,OtgTutorialActivity}`,
   `filesystems/{fs/{fat,ntfs,ext},windows/WimLib*,sevenzipjbinding/*}`,
   `ventoy/Ventoy`, `data/Loader`. Native: `libmixapplications.so`
   (5.1 MB arm64), `libutils.so`, `lib7-Zip-JBinding.so`, `libfast-lzma2`,
   `libzstd`, `libbrotli*`, `libnms`, Pangle `libapminsight*/libpglarmor`,
   AppLovin crash reporter. Assets: `assets/ventoy/ventoy.disk.img`,
   `assets/boot/{boot,core}.img`, `assets/Linux-arm/`, 7-Zip properties.
   [shell-observed]

## Partially answered / inferred (not observed)

- Exact Ventoy install flow (partition, write boot/core/disk images,
  exFAT/NTFS formatting): inferred from asset names + class names only.
  No pseudocode (JADX provider missing).
- Exact ad call sites / billing flows: file/manifest presence only;
  no method bodies, no traces, no runtime observation.

## Unresolved (needs JADX + JDK + device, out of scope here)

- `inspect_android_package`, `search_android_classes`,
  `inspect_android_class/method`, `decode_android_resources`
  all blocked: `provider_missing — Set REA_JADX_MCP_JAR...`
  (`inspect_android_package` attempt recorded).
- `inspect_artifact` hypotheses / next probes: none retained
  (`hypotheses_retained: 0`); no `project_android_application_graph`
  projection was materialized (would need inline Evidence plumbing).
- Runtime behavior, server endpoints, live ad delivery: never executed.

## Clean-room boundary

All new code in `app/` is written from the functional labels above
(USB-OTG + file browse + Ventoy install + ISO copy) without copying
DEX source, smali, resources, native code, or image bytes.
