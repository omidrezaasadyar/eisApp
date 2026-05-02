# EIS App — Reference Files

Each `*.md` in this directory encodes a contract that Claude reads before
generating code. Keep them in sync with reality. The skill expects:

| File | Purpose |
|---|---|
| `services.md` | Canonical copy for the nine EIS service lines, in English and Arabic. Source of truth for any service-related screen. |
| `architecture.md` | Layer boundaries, DI conventions, ViewModel + UiState pattern, navigation, testing. |
| `ui-theme.md` | Color tokens, typography, shapes, motion, component conventions, Lottie usage. |
| `localization.md` | Translation workflow, language switcher, RTL rules, font handling. |
| `integrations.md` | Firestore schema, FCM topics, WhatsApp + phone intent helpers. |
| `project-skeleton.md` | Gradle setup, version catalog, manifest, application/MainActivity templates. |

Files that are intentionally still empty are listed in
[`PENDING.md`](./PENDING.md). When asked to populate one, prefer reading
the others to keep tone and conventions consistent.
