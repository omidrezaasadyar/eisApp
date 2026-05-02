# EIS App

Official Android app for **Effective & Innovative Solutions (EIS) LLC** —
a heavy-industry software and automation company based in Oman.

The app showcases EIS's nine service lines, captures service requests
into Firestore, and provides one-tap WhatsApp / phone contact. It supports
**English (LTR)** and **Arabic (RTL)** with full visual parity.

## Stack

Kotlin · Jetpack Compose · Material 3 · Hilt · Coroutines + Flow ·
Navigation Compose · DataStore · Firebase (Firestore / FCM /
Crashlytics / Analytics) · Coil · Lottie

## Project layout

See [`CLAUDE.md`](./CLAUDE.md) for the architectural contract and the
canonical directory layout. Three-layer Clean Architecture
(`presentation` → `domain` ← `data`) with strict layer boundaries.

## Building

```bash
./gradlew assembleDebug
```

### Firebase setup

1. Create a Firebase project at <https://console.firebase.google.com>.
2. Add an Android app with package name `com.eis.oman` (and
   `com.eis.oman.debug` for debug builds).
3. Download `google-services.json` and place it at `app/google-services.json`.
   This file is **gitignored**.
4. Enable **Firestore**, **Cloud Messaging**, **Crashlytics**, **Analytics**
   in the console.

### Contact endpoints

Phone, WhatsApp, and email are exposed via `BuildConfig`. Override the
defaults by creating `secrets.properties` at the repo root:

```properties
EIS_PHONE_E164=+968XXXXXXXX
EIS_WHATSAPP_E164=+968XXXXXXXX
EIS_EMAIL=info@eis.om
```

`secrets.properties` is gitignored.

## Localization

- English: `app/src/main/res/values/strings.xml`
- Arabic:  `app/src/main/res/values-ar/strings.xml`

Both files are updated together for every new user-visible string. The
in-app language switcher uses `AppCompatDelegate.setApplicationLocales`
and persists the choice in DataStore.
