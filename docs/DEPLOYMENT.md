# CivicReport GH — Step-by-Step: Build → Run → Deploy

## Part A — Open and run the project in Android Studio

1. **Install Android Studio** (Hedgehog/2023.1 or newer) from developer.android.com if you don't
   already have it, and open it.
2. **Open the project**: `File → Open`, then select the `CivicReportGH` folder (the one containing
   `settings.gradle`).
3. **Let Gradle sync.** Android Studio will notice there's no `gradlew` wrapper jar yet and offer to
   generate one — accept that. (If it doesn't prompt automatically, go to
   `File → Sync Project with Gradle Files`.) This will download Gradle 8.4 and the dependencies
   listed in `app/build.gradle` — needs an internet connection the first time.
4. **Fix the SDK version if prompted.** If Android Studio asks to install SDK Platform 34 or a
   matching build tools version, click "Install" — this is normal on a fresh Android Studio setup.
5. **Create/select a device to run on:**
   - Emulator: `Tools → Device Manager → Create Device`, pick any modern phone profile (e.g. Pixel 6),
     API 30+ system image.
   - Or plug in a real Android phone with USB debugging enabled (`Settings → About phone → tap
     "Build number" 7 times → Developer options → USB debugging`).
6. **Run it**: click the green ▶ Run button (or Shift+F10). The app installs and opens on your chosen
   device/emulator.
7. **Try the flow**: tap "Take a Photo" (grant camera permission), take a picture, confirm/change the
   suggested category, add a one-line description, then tap "Send Report" — your phone's email app
   picker opens with the report pre-filled and the photo attached.

> **Note on the emulator and camera:** the Android emulator's default camera can only produce a test
> pattern image, not a real photo. To test with a real photo, either use a physical device, or in the
> emulator's camera settings switch the back camera to "Webcam0" if your computer has one.

## Part B — Before you use it for real reports

1. Open `app/src/main/java/com/civicreportgh/app/Institution.kt`.
2. Replace the two placeholder emails (Road, Police) with the correct, current addresses — call or
   check the institution's official website first, don't guess.
3. Fill in the `email` field for **Sanitation** and **Other** with your own MMDA's contact address.
4. Re-build and re-run to confirm the "Will be sent to" text on the report screen now shows the
   correct institution and no longer shows the "⚠ verify this contact" warning.

## Part C — Preparing a release build (the installable APK)

1. **Generate a signing key** (one-time, keep this file and its passwords safe forever — you'll need
   the same key for every future update):
   ```
   keytool -genkey -v -keystore civicreport-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias civicreport
   ```
   Run this from a terminal with Java installed (Android Studio bundles one, or use the one on your
   machine). It will ask for a keystore password and some identity details.
2. In Android Studio: `Build → Generate Signed Bundle / APK`.
3. Choose **APK** (simplest for direct install/sharing) or **Android App Bundle** (required if you
   plan to publish on Google Play).
4. Point it at the `civicreport-release.jks` file you just created, enter the passwords and alias.
5. Choose the **release** build variant, click **Finish**.
6. Android Studio will build the signed APK/AAB and show a notification with a "locate" link — the
   file lands in `app/release/`.

## Part D — Deployment options

Pick whichever matches how you actually want to distribute it:

- **Direct install (fastest, no store needed):** copy the signed APK to a phone (via USB, WhatsApp,
  Google Drive, etc.) and open it — Android will prompt to allow installs from that source, then
  install it. Good for personal use, pilots, or sharing with a small group.
- **Google Play Store:**
  1. Create a Google Play Console developer account (one-time signup fee applies).
  2. Create a new app entry, fill in the store listing (description, screenshots, privacy policy URL
     — required even for a free app that requests camera/location permissions).
  3. Upload the **Android App Bundle** (`.aab`) from Part C.
  4. Complete the "Data safety" and "Content rating" questionnaires — be accurate about the camera
     and location permissions and why they're used (Section 4 of `DOCUMENTATION.md` covers this).
  5. Submit for review. Google's review typically takes a few days for a new app.
- **Internal/organization distribution:** if this is for a specific office or NGO rather than the
  public, Google Play's "Internal testing" or "Closed testing" tracks let you share it with a fixed
  list of testers without a public listing.

## Part E — Updating the app later

1. Make your code changes.
2. Bump `versionCode` (integer, must increase every release) and `versionName` (human-readable, e.g.
   "1.1") in `app/build.gradle`.
3. Repeat Part C using the **same** keystore file — Android will reject an update signed with a
   different key.
