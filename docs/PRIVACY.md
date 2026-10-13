# Privacy — fully offline, no payments

This clean-room app contains:

- No ads SDKs (no AppLovin, AdMob, Unity Ads, ironSource, Mintegral,
  Vungle, InMobi, Chartboost, Pangle, Moloco, Smaato, Fyber, Bigo,
  Yandex, Appodeal, StartApp, MBridge, TaurusX/Maticoo, or adapters).
- No `AD_ID` / `ACCESS_ADSERVICES_*` permissions.
- No Play Billing / `BILLING` permission. All features are free.
  No login, no VIP gate, no server-side entitlement check.
- No Firebase Measurement, no Sentry, no AppMetrica.
- No analytics, no attribution (`REFERRAL_PROVIDER` absent),
  no cross-app tracking.

Network use: none.

- No `INTERNET` permission (see `app/src/main/AndroidManifest.xml`).
- Ventoy boot files are bundled under `app/src/main/assets/ventoy/`
  by the maintainer from official Ventoy releases (GPL-3.0, see
  `docs/OFFLINE.md` + `PROVENANCE.txt`), verified by SHA-256 at
  runtime before any USB write.
- No crash reporting. No ads prefetch. No update check.

Permissions:

- USB host (`android.hardware.usb.host`, `USB_PERMISSION`) — required.
- `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_DATA_SYNC` — long copy/install.
- `POST_NOTIFICATIONS` — only for install/copy progress on Android 13+.
  No marketing notifications.
