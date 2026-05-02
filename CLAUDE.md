# EIS App — Engineering Notes for Claude

This file is the durable instruction set Claude reads on every session.
It encodes the architectural and design contract for the official Android
app of **Effective & Innovative Solutions (EIS) LLC**, an industrial
software and automation company based in Oman.

If a request would conflict with anything below, push back and propose a
compliant alternative.

---

## 1. Stack & Versions

- **Language**: Kotlin only. No Java sources.
- **UI**: Jetpack Compose + Material 3 (`androidx.compose.material3`). No XML
  layouts, no AppCompat views, no Material 2 imports in Compose code.
- **Min SDK** 24, **Target/Compile SDK** 35.
- **DI**: Hilt. Every ViewModel, repository, and data source is injected.
- **Async**: Kotlin Coroutines + Flow. ViewModels expose `StateFlow<UiState>`.
- **Navigation**: Navigation Compose. Routes live in
  `presentation/navigation/Routes.kt` as a sealed class.
- **Persistence**: DataStore (Preferences) for settings; Firestore for remote.
- **Firebase**: Firestore, FCM, Crashlytics, Analytics — wired via
  `di/FirebaseModule.kt`.
- **Version catalog**: `gradle/libs.versions.toml`. Add new deps there,
  not directly in module build files.

## 2. Architecture

Strict three-layer Clean Architecture, dependency direction
`presentation → domain ← data`:

```
presentation/  Compose screens, ViewModels, UI state, navigation, theme
domain/        pure Kotlin: models, repository interfaces, use cases
data/          Firestore/DataStore impls, DTOs, mappers, FCM service
```

- `domain/` must have **zero Android imports** and **zero Firebase imports**.
- ViewModels expose `StateFlow<UiState>` and accept user intents through
  functions; never expose mutable state.
- Use cases are single-responsibility, suspend where appropriate, injected
  into ViewModels.
- Every external surface (Firestore, FCM, Intents, DataStore) is behind an
  interface so it can be faked in tests.

## 3. Bilingual + RTL — first-class

- Languages: **English (default, LTR)** and **Arabic (RTL)**. Persian/Farsi
  is **not** an app locale — it's only the language of human conversation
  with the developer.
- Every user-visible string lives in both
  `res/values/strings.xml` (English) and `res/values-ar/strings.xml` (Arabic).
  No hardcoded user-visible text in Kotlin, Composables, Toasts, or errors.
- Locale switching uses
  `androidx.appcompat.app.AppCompatDelegate.setApplicationLocales` (see
  `core/LocaleManager.kt`) and persists via DataStore. Manifest declares
  `android:localeConfig="@xml/locales_config"`.
- Use start/end paddings, never left/right. Verify chevrons / back arrows
  use `Icons.AutoMirrored.*` so they mirror under RTL.
- Numbers in Arabic locale stay Western digits for technical/industrial
  figures (this matches the heavy-industry expectation).

## 4. Visual language

- Industrial palette: deep steel blue + graphite + off-white, single accent
  in safety amber. Defined as Material 3 color tokens in
  `presentation/theme/Color.kt` and `Theme.kt`. **Never hardcode hex in UI
  code** — use `MaterialTheme.colorScheme.*`.
- Light **and** dark theme are both first-class; **dark is default** for
  the industrial aesthetic.
- Motion: 150–250 ms, `FastOutSlowInEasing`. No bouncy springs in primary
  navigation. Lottie reserved for splash, success, empty states.
- Icons: Material Symbols Outlined / `material-icons-extended`. No filled
  cartoonish icons.

## 5. Direct contact — one-tap, always works

- WhatsApp: `https://wa.me/<E164-no-plus>?text=<urlencoded>` via
  `Intent.ACTION_VIEW`. From a service detail screen, prefill the message
  with the localized service name.
- Phone: `Intent.ACTION_DIAL` with `tel:` URI. **Never** `ACTION_CALL`
  (would require runtime permission and is wrong for a B2B contact).
- Both helpers live in `core/IntentLauncher.kt`, are injected, and fall
  back to a localized error when no handler is available.

## 6. Firebase contract

- Collection `service_requests` — schema follows
  `data/dto/ServiceRequestDto.kt` and `data/repository/ServiceRequestRepositoryImpl.kt`.
  Every write is `@ServerTimestamp`-stamped and tagged with the user's
  current locale.
- FCM topics: `news_en`, `news_ar`, `requests_<requestId>`.
- All Firebase calls go through repositories under `data/`. ViewModels
  never touch Firebase types.

## 7. The nine EIS services (fixed, ordered)

The catalogue is hard-coded in `data/repository/ServiceRepositoryImpl.kt`
via `domain/model/ServiceId`. **Do not invent, merge, split, or paraphrase
these.** Localized copy is keyed by `ServiceId.key` and resolved through
string resources.

1. Industrial Software & Digital Solutions — `industrial_software`
2. Automation & Process Optimization — `automation`
3. AI & Data Intelligence for Industry — `ai_data`
4. Industrial Integration & Middleware Solutions — `integration_middleware`
5. Engineering Consulting & Technical Services — `engineering_consulting`
6. Monitoring, KPI & Decision Support Systems — `monitoring_kpi`
7. Industrial Equipment Trading & Supply — `equipment_trading`
8. Installation, Commissioning & Support — `installation_commissioning`
9. Smart Factory & Industry 4.0 Solutions — `smart_factory`

## 8. Production-readiness baseline

- Every screen has loading / empty / error / content states modelled as a
  sealed `UiState`.
- Every async op runs in `viewModelScope` and is cancellation-safe.
- Crashlytics + Analytics are gated off in debug builds.
- Contact endpoints come from `BuildConfig` (`EIS_PHONE_E164`,
  `EIS_WHATSAPP_E164`, `EIS_EMAIL`) and can be overridden via
  `secrets.properties` at the repo root (gitignored).

## 9. Project layout

```
app/src/main/
├── AndroidManifest.xml
├── java/com/eis/oman/
│   ├── EISApplication.kt
│   ├── MainActivity.kt
│   ├── di/                  Hilt modules
│   ├── core/                Result, IntentLauncher, LocaleManager, dispatchers
│   ├── data/
│   │   ├── remote/firebase/ FCM service
│   │   ├── dto/             Firestore DTOs
│   │   ├── mapper/          DTO ↔ domain
│   │   └── repository/      interface impls
│   ├── domain/
│   │   ├── model/           Service, ServiceRequest, ContactInfo, AppLanguage
│   │   ├── repository/      interfaces
│   │   └── usecase/         use cases
│   └── presentation/
│       ├── theme/           Color, Type, Shape, Theme
│       ├── components/      reusable Composables
│       ├── navigation/      NavGraph, Routes
│       ├── MainViewModel.kt
│       └── screens/
│           ├── splash/
│           ├── home/
│           ├── services/
│           ├── service_detail/
│           ├── request/
│           ├── about/
│           ├── contact/
│           └── settings/
└── res/
    ├── values/strings.xml          English (default)
    ├── values-ar/strings.xml       Arabic
    ├── values/colors.xml
    ├── values/themes.xml
    ├── values-night/themes.xml
    ├── xml/locales_config.xml
    └── drawable/ …
```

## 10. Workflow rules for Claude

When asked to add a feature:

1. Place code in the correct layer; never leak Android/Firebase types into
   `domain/`.
2. Add new strings to **both** `values/strings.xml` and
   `values-ar/strings.xml` in the same change.
3. New deps go through `gradle/libs.versions.toml`.
4. Generate complete, compilable code — no `// TODO` placeholders inside
   production logic. If a value is unknown (e.g. brand asset), expose it as
   `BuildConfig` or a constant and call it out in the response.
5. End non-trivial changes with the list of files created/modified.
6. Self-check before delivering: Compose preview-friendly? Light + Dark
   variants? RTL-safe? Hilt-injectable? StateFlow-driven? No hardcoded
   strings? No layer leaks?

## 11. Build & test

```bash
./gradlew assembleDebug          # build the debug APK
./gradlew lint                   # Android lint
./gradlew testDebugUnitTest      # unit tests
./gradlew connectedDebugAndroidTest  # instrumentation (requires device/emulator)
```

`google-services.json` is required at `app/google-services.json` for
Firebase to initialize. It is **gitignored** — fetch it from the Firebase
console or ask the maintainer.
