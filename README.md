# CivicReport GH

An Android app for reporting civic issues (bad roads, water/power faults, sanitation, environmental
hazards) straight to the responsible Ghanaian institution — by photo, with a suggested category, sent
through the user's own email app.

## Start here
- **New to Android Studio?** Read `docs/BEGINNER_GUIDE.md` — full step-by-step from install to deployment.
- **Already comfortable with Android dev?** Read `docs/DEPLOYMENT.md` for the condensed version.
- **Want to understand how it works / edit the institution contacts?** Read `docs/DOCUMENTATION.md`.

## Project structure
```
CivicReportGH/
├── app/                     ← the Android app source code
│   └── src/main/
│       ├── java/.../        MainActivity.kt, ReportActivity.kt, Institution.kt
│       └── res/              layouts, strings, icons
├── docs/
│   ├── BEGINNER_GUIDE.md    ← step-by-step for first-time Android Studio users
│   ├── DEPLOYMENT.md        ← condensed build/deploy steps
│   └── DOCUMENTATION.md     ← architecture, category-to-institution mapping, permissions
├── build.gradle
└── settings.gradle
```
