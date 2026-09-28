# CivicReport GH — Beginner's Step-by-Step Guide

This assumes you have never used Android Studio before. Follow the steps in order — don't skip ahead.

---

## PART 1: Install Android Studio

1. Go to **https://developer.android.com/studio** in your web browser.
2. Click the big **Download Android Studio** button. Accept the terms when asked.
3. Once it's downloaded, run the installer:
   - **Windows:** double-click the `.exe` file, click "Next" through the setup wizard, keep the
     default options, click "Finish."
   - **Mac:** double-click the `.dmg` file, drag Android Studio into the Applications folder, then
     open it from Applications.
   - **Linux:** unzip the downloaded file, open a terminal in the extracted `android-studio/bin`
     folder, and run `./studio.sh`.
4. The first time it opens, a **Setup Wizard** appears. Choose **Standard** installation and click
   Next through the remaining screens (it will download some extra components — this can take
   5–15 minutes depending on your internet speed). Click **Finish** when it's done.
5. You should now see the **Android Studio Welcome screen** with options like "New Project," "Open,"
   etc. Leave it open — you'll use it in Part 3.

---

## PART 2: Get the project files onto your computer

1. You should have received a file called **`CivicReportGH.zip`** (the project I built for you).
2. Save it somewhere you'll remember — e.g. `Documents` or `Desktop`.
3. **Unzip it:**
   - **Windows:** right-click the zip file → "Extract All..." → choose a location → "Extract."
   - **Mac:** double-click the zip file — it extracts automatically next to it.
   - **Linux:** right-click → "Extract Here," or run `unzip CivicReportGH.zip` in a terminal.
4. After unzipping, you'll have a folder named `CivicReportGH` containing files like `settings.gradle`,
   a `docs` folder, and an `app` folder. **Do not rename or move files inside this folder.**

---

## PART 3: Import the project into Android Studio

1. Open Android Studio (if it's not already open).
2. On the Welcome screen, click **Open** (if Android Studio already has another project open instead
   of the Welcome screen, go to the top menu: **File → Open**).
3. In the file browser that appears, navigate to and select the **`CivicReportGH`** folder itself
   (the one containing `settings.gradle`) — select the folder, not a file inside it — then click
   **OK / Open**.
4. Android Studio will open the project and start "Gradle sync" automatically. You'll see a progress
   bar at the bottom of the window saying things like "Downloading," "Configuring," "Building
   project." **This is normal and can take several minutes the first time** — it's downloading the
   Android build tools and the app's small set of libraries. Make sure you're connected to the
   internet.
5. If a yellow/blue banner appears asking to **"create a Gradle wrapper"** or similar — click the
   suggested action to accept it.
6. If a banner appears saying an **SDK Platform (e.g. "Android 14 / API 34") is missing**, click
   the **"Install missing platform(s) and sync project"** link in that banner, accept the license
   when prompted, and let it install.
7. Wait until the bottom status bar is idle and says something like "Gradle build finished" or
   simply stops showing a progress bar. **Do not proceed until sync finishes without errors.**
   - If you see red error text at the bottom in a "Build" or "Problems" tab, re-check step 3 — the
     most common mistake is opening the wrong folder (e.g. opening `app` instead of `CivicReportGH`).

---

## PART 4: Set up something to run the app on

You need either a **virtual phone (emulator)** on your computer, or a **real Android phone**.

### Option A — Emulator (no physical phone needed)
1. In Android Studio, go to **Tools → Device Manager** (or click the phone icon in the top-right
   toolbar).
2. Click **Create Device** (or the **+** button).
3. Pick a phone, e.g. **Pixel 6**, click **Next**.
4. Pick a system image — choose one with a **release name and API level 30 or higher** (e.g.
   "API 34"), e.g. click **Download** next to it if it's not downloaded yet, wait for it to finish,
   then click **Next**.
5. Click **Finish**. Your new virtual device now appears in the Device Manager list.

> Note: the emulator's built-in camera only produces a test pattern, not a real photo. That's fine
> for checking the app works — for testing with a real photo, use Option B instead.

### Option B — Real Android phone (recommended, lets you use the real camera)
1. On your phone: go to **Settings → About phone**, find **"Build number,"** and tap it **7 times
   in a row**. You'll see a message like "You are now a developer."
2. Go back to **Settings**, find the new **Developer options** menu, open it, and turn on
   **USB debugging**.
3. Connect your phone to your computer with a USB cable.
4. Your phone may show a popup: **"Allow USB debugging?"** — tap **Allow**.
5. Back in Android Studio, your phone's name should now appear in the device dropdown at the top of
   the window (near the Run button).

---

## PART 5: Run the app

1. At the top of the Android Studio window, make sure the dropdown next to the green ▶ (Run) button
   shows either your emulator or your connected phone.
2. Click the green ▶ **Run** button (or press **Shift + F10** on Windows/Linux, **Ctrl + R** on Mac).
3. Android Studio will build the app (progress bar at the bottom) and then automatically install and
   open it on your chosen device/emulator. This first build can take a minute or two.
4. The app opens showing **"CivicReport GH"** with two buttons: "Take a Photo" and "Choose from
   Gallery."
5. **Try it out:**
   - Tap **Take a Photo** → allow the camera permission when asked → take a picture.
   - You'll land on the report screen with the photo shown, a category already guessed for you,
     and a description box.
   - Change the category if needed, type a short description, optionally tap **Add My GPS Location**
     (allow the location permission if asked).
   - Tap **Send Report** → your phone will show a list of your installed email apps — pick one, and
     it opens with the report and photo already filled in, ready for you to review and hit send.

If the app crashes or won't open, check the **Logcat** tab at the bottom of Android Studio (it shows
red error text) — this tells you exactly what went wrong, most often a missing permission grant.

---

## PART 6: Build an installable app file (APK)

This creates the actual app file you can share or install without Android Studio.

1. Open a terminal (**Terminal** tab at the bottom of Android Studio, or your system terminal) inside
   the `CivicReportGH` folder, and generate a signing key — this is like a permanent digital signature
   for your app. Run:
   ```
   keytool -genkey -v -keystore civicreport-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias civicreport
   ```
   - It will ask you to create a **keystore password** and a few identity questions (name,
     organization, city, country code — e.g. `GH`). Answer them; press Enter to accept defaults where
     unsure.
   - **Save the resulting `civicreport-release.jks` file and your passwords somewhere safe and
     permanent.** You will need this exact file for every future update of the app — if you lose it,
     you can never update this app again under the same identity.
2. In Android Studio's top menu: **Build → Generate Signed Bundle / APK**.
3. Choose **APK**, click **Next**.
4. Click the **Choose existing...** button next to "Key store path," browse to and select your
   `civicreport-release.jks` file.
5. Enter your keystore password, key alias (`civicreport`), and key password (same as keystore
   password unless you set a different one), then click **Next**.
6. Choose the **release** build variant, tick both **V1** and **V2** signature versions if shown,
   click **Finish**.
7. Wait for the build to finish — a notification pops up in the bottom-right saying **"APK(s)
   generated successfully"** with a **locate** link. Click it to find your file — it will be inside
   `CivicReportGH/app/release/app-release.apk`.

---

## PART 7: Install/deploy the app

Choose whichever fits what you need:

### A — Install directly on a phone (simplest, no store needed)
1. Copy `app-release.apk` to your phone (via USB cable, WhatsApp to yourself, email, or Google Drive).
2. On the phone, open the file from your **Files** app or **Downloads**.
3. Android will warn about installing from an unknown source — tap **Settings**, allow installs from
   that source, then go back and tap **Install**.
4. Open the app from your app drawer once installed — done.

### B — Publish to the Google Play Store (for public distribution)
1. Go to **https://play.google.com/console** and sign up as a developer (there's a one-time
   registration fee).
2. Click **Create app**, fill in the name ("CivicReport GH"), language, and category.
3. In Android Studio, repeat Part 6 but choose **Android App Bundle** instead of APK in step 3 (Play
   Store requires this format).
4. In the Play Console, go to your app's **Production** (or **Internal testing**, to try it with a
   small group first) release section, and upload the `.aab` file.
5. Fill in the required store listing details: short/full description, screenshots, an app icon, and
   a **privacy policy URL** (required because the app uses camera and location — explain in one page
   that photos and location are only used to build the report the user sends themselves, and nothing
   is collected by you).
6. Complete the **Data safety** and **Content rating** questionnaires honestly, based on what's in
   `docs/DOCUMENTATION.md` (Section 4: Permissions used and why).
7. Click **Submit for review**. Google typically reviews new apps within a few days.

### C — Share with a small group only (e.g. one office or NGO)
- In the Play Console, use the **Internal testing** or **Closed testing** track instead of
  Production — you add specific testers by email, and it never becomes publicly visible.

---

## Quick troubleshooting

| Problem | Likely fix |
|---|---|
| Gradle sync never finishes / fails | Check your internet connection; try **File → Sync Project with Gradle Files** again |
| "SDK not found" error | **Tools → SDK Manager**, make sure an Android SDK Platform is installed and ticked |
| Run button is greyed out | Wait for Gradle sync to finish first (check the bottom status bar) |
| App installs but crashes immediately | Open the **Logcat** tab at the bottom, read the red text, it names the exact error |
| Camera button does nothing on emulator | Expected — emulators show a test pattern; use a real device for real photos |
| "App not installed" when installing the APK | Uninstall any older test version of the app first, then reinstall |

---

Before you distribute this app to real people, don't forget **Part B of `DEPLOYMENT.md`**: update the
two placeholder institution emails (Road, Police) and fill in your local MMDA's email for Sanitation
and Other — otherwise those categories won't have anywhere real to send to.
