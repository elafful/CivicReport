# CivicReport GH — Documentation

## 1. Overview

CivicReport GH is a lightweight Android app that lets a citizen:

1. Take or select a photo of a civic issue (bad road, water leak, power fault, illegal dumping, etc.)
2. Get a suggested issue **category**, based on on-device photo analysis
3. Confirm the category, add a short description, and optionally attach GPS coordinates
4. Send the report — photo included — by email, to the Ghanaian institution responsible for that category

There is deliberately **no backend server**. The app hands the finished email off to whichever email
app is already installed on the phone (Gmail, Outlook, etc.) via Android's standard "share" mechanism.
This keeps the app simple, free to run, and fully under the user's own email account — nothing is sent
on the user's behalf without them seeing and tapping "send" in their own mail client.

## 2. Architecture

```
MainActivity  →  capture/pick photo → run on-device image labeling → ReportActivity
ReportActivity → confirm category → write description → (optional) attach GPS → build email → hand off to email app
```

Two screens, two Kotlin files for logic, one data file for the institution mapping. No database,
no networking layer beyond what the two library dependencies need internally.

| Component | File | Responsibility |
|---|---|---|
| Capture screen | `MainActivity.kt` / `activity_main.xml` | Camera/gallery access, runs ML Kit labeling |
| Report screen | `ReportActivity.kt` / `activity_report.xml` | Category confirmation, description, location, sends email |
| Category → institution mapping | `Institution.kt` | Single source of truth for routing |

### Why on-device ML Kit labeling, not a custom-trained model
ML Kit's image-labeling model is free, runs fully offline after the first install, and needs no
training data of our own. It only returns generic labels (e.g. "Road", "Water", "Pole"), which is
enough to **suggest** a starting category — the app never sends a report without the user confirming
the category themselves. This avoids the complexity/cost of training and hosting a custom classifier,
which would be overkill for what is fundamentally a routing decision a human makes in two seconds.

### Why email via Intent, not a mail-sending backend
Sending mail directly from the app (SMTP, a cloud function, etc.) would require API keys, a server to
maintain, and a way to prove the report really came from a real person. Handing off to the phone's own
email app avoids all of that: it uses the user's real email identity, works with any provider, costs
nothing to run, and needs no backend at all.

## 3. Issue categories and institution routing

| Category | Institution | Contact used | Verified? |
|---|---|---|---|
| Road / pothole | Ghana Highway Authority | `info@gha.gov.gh` | ⚠️ **Not verified** — placeholder, confirm before use |
| Water supply | Ghana Water Limited (GWCL) | `info@gwcl.com.gh` | ✅ Confirmed against gwcl.com.gh |
| Electricity / streetlight | Electricity Company of Ghana (ECG) | `help@ecggh.com` | ✅ Sourced from ECG's public contact channels |
| Waste / sanitation | Your local MMDA (Metropolitan/Municipal/District Assembly) | *(blank — fill in)* | No single national address exists |
| Environmental hazard | Environmental Protection Agency (EPA) | `info@epa.gov.gh` | ✅ Confirmed against epa.gov.gh |
| Public safety | Ghana Police Service | `info@police.gov.gh` | ⚠️ **Not verified** — placeholder, confirm before use |
| Other | Your local MMDA | *(blank — fill in)* | — |

**Before you rely on this app for real reports:**
- Confirm the Road and Police addresses directly with those institutions (they were not independently
  verifiable at build time) and update them in `Institution.kt`.
- Sanitation and "Other" route to your own district assembly because there is no single national
  inbox for this — look up your MMDA's contact (e.g. Kumasi Metropolitan Assembly, Accra Metropolitan
  Assembly) and fill in the `email` field for those two entries.
- All contacts should be re-checked periodically — government contact addresses do change.

To change any mapping, edit the `mapping` map inside `InstitutionRepository` in `Institution.kt` —
nothing else in the app needs to change.

## 4. Permissions used and why

| Permission | Why |
|---|---|
| `CAMERA` | To take a photo of the issue |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Only used if the user taps "Add My GPS Location" — entirely optional |
| `INTERNET` | Needed once, to download the small ML Kit labeling model after install; the model then runs offline |

No contacts, storage-wide, SMS, or background permissions are requested — deliberately, to keep the
app's footprint and privacy surface as small as the task needs.

## 5. Known limitations (by design)

- The photo-based category suggestion is a convenience, not a verdict — it can be wrong, and the user
  always confirms/changes it.
- There's no report history, tracking, or status screen. If you want to know whether an institution
  acted on a report, that happens through your own email thread with them, same as reporting by email
  normally would.
- No login system — the app doesn't need to know who the user is; the report is sent from their own
  email account.

If you outgrow these limitations later, they're easy follow-on features — but they're intentionally
left out of this build to keep it lean and match what a single citizen actually needs.
