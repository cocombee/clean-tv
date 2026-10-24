# Troubleshooting

Things that bit us or contributors, so they don't bite you. Commands use `tools/tv.py`; see the
[README](../README.md#doing-it-by-hand) for setting it up.

## Connecting

- **macOS: `adb connect` says "No route to host"** although the port is open. Recent macOS treats
  the Google-signed `adb` as its own app under Local Network privacy and silently blocks it.
  `tools/tv.py` avoids the binary entirely.
- **No "wireless debugging" toggle** on some TVs, Xiaomi among them. USB debugging alone opens
  network ADB on port 5555.
- **USB debugging never opens port 5555** on some Google TV sets (a Skyworth-built Philips among
  them). Pair once through Wireless debugging with the stock `adb` (`adb pair <ip>:<port> <code>`,
  then `adb connect`), run `adb tcpip 5555`, and `tools/tv.py` works from then on.
- **USB devices go dead while USB debugging is on**, seen on the Xiaomi Mi Box S: its firmware
  switches the USB port to ADB device mode, which cuts the power to a USB speaker or drive. Switch
  USB debugging off when you're done; the launcher doesn't need it.
- **Windows PowerShell 5.1 adds a byte-order mark** to text piped into a program, which turned the
  first batch command into gibberish. `tv.py` now strips it, and the `.ps1` scripts pipe clean UTF-8.
- **"Allow USB debugging?" comes back** although `adb devices` already works: `tv.py` used its own
  key next to the one platform-tools had registered. It now reuses `~/.android/adbkey` when that
  exists. Every attempt with an unaccepted key queues one more prompt on the TV, so dismiss them all
  (Back) before testing with key presses.
- **PowerShell says "adb is not recognized"** although you are in the platform-tools folder: PowerShell
  doesn't run a program from the current folder by its bare name. Type `.\adb connect <ip>:5555`, with the
  TV's IP address from its network settings (four numbers, like 192.168.1.20), not the TV's device name.
- **Git Bash rewrites paths** like `/data/local/tmp` into Windows paths. The scripts set
  `MSYS_NO_PATHCONV=1` for you. Set `JAVA_HOME` and `ANDROID_HOME` before building there.

## If Glass TV does not work on a TV

The install script checks Glass TV twice: after starting it, before the TV's own launcher is touched, and
after pressing Home. If either check fails, or the connection drops part-way, it puts the TV back by itself:
Glass TV removed, the original launcher enabled and made Home again. It says so in the output. If it could
not reach the TV to undo it, it prints the restore command to run once the TV is back on the network:
`TV_HOST=<ip> tools/restore.sh <your debloat list>` (Windows: `tools\restore.ps1`). To see what it would do
without a real failure, run the script with `SAFEGUARD_TEST=start` or `SAFEGUARD_TEST=home`: that check
fails on purpose and the rollback is printed instead of run.

## Working on the TV

- **Chained `pm` commands** (`pm disable a; pm disable b`) may silently stop after the first. Run
  one per shell call; `--batch` does exactly that.
- **Large pushes hang** through the Python client. Use `tools/b64push.py` for APKs.
- **`adb exec-out screencap -p` gives a broken PNG** on some MediaTek Xiaomi sets: the binary prints
  `Init wrapper sys mutex successful` on stdout first. Save on the TV and pull the file instead:
  `screencap -p /data/local/tmp/s.png`.
- **Play Protect blocked the launcher** ("unsafe app blocked, built for an older version of Android")
  and `pm install` hung, then failed with `INSTALL_FAILED_VERIFICATION_FAILURE`, while it targeted
  SDK 29. It targets 34 now; if you built an old checkout, rebuild.
- **Screenshots come out empty** while Live TV, an HDMI input or Netflix is on screen, seen on TCL
  and a [Sony KDL-49W800G](https://github.com/Gathin23/cleanyourtv/issues/92). Press Home before
  `screencap`. An empty capture means nothing about the ADB connection or playback; check those
  separately.
- **Home goes back to the TV's own home screen**, most often after Glass TV was installed by hand (an APK
  sideloaded, then picked as the default in Android's "Select a Home app" dialog). That only sets a
  preference, and Android drops it whenever the list of home apps changes: a system update, an update of
  the TV's own launcher, a reboot on some firmware. What keeps Glass TV as Home is disabling the stock
  launcher, which is what `launcher/build-and-install.sh` does: it starts Glass TV first, checks it runs,
  then disables the stock launcher with `pm disable-user` (reversible, never uninstalled) and puts
  everything back if anything fails. If Home has gone back:
  - Run the install script again; it finds the stock launcher and disables it.
  - On Google TV, also disable `com.google.android.tungsten.setupwraith` (below), or it takes Home.
  - A firmware update can re-enable the stock launcher; the script fixes that too.
  - If Home opens the TV's app list instead (some Xiaomi sticks send ALL_APPS for Home), install with
    `HOME_KEY_SERVICE=1`.
  - To go back to the TV's own home screen on purpose: `tools/restore.sh <your debloat list>`.
- **Disable the stock launcher only after the new one is installed**, or the Home button lands on
  a factory test app. Launch the replacement by hand first. `set-home-activity` can report
  `Success` while HOME still resolves to the stock launcher (priority 2) until that one is disabled.
  And check which packages actually handle HOME before disabling anything by name: on the Sony
  above, `com.google.android.leanbacklauncher` has a boot receiver but no HOME activity.
- **The new launcher still doesn't own Home** after the stock one is disabled, seen on Google TV
  (Xiaomi MiTV-MOOR2). Another system app answers HOME: `com.google.android.tungsten.setupwraith`,
  with `.MainActivity` at intent-filter priority 4 and `.RecoveryActivity` at priority 1. You
  cannot outrank it, and the obvious workarounds don't help: Android ignores `android:priority` on
  HOME filters for apps that aren't on the system image, `cmd package set-home-activity` prints
  `Success` without changing anything (not even after a reboot), and `pm disable-user` on one of
  setupwraith's components is refused with a SecurityException. Disabling the whole
  `com.google.android.tungsten.setupwraith` package is what frees the slot. It is the first-run
  setup wizard, so everyday use is unaffected, but re-enable it before a factory reset - list it in
  your `debloat.txt` and `tools/restore.sh` does that for you.
- **The TV drops off Wi-Fi every minute right after the install**, seen on Google TV 14 (Xiaomi
  MiTV-MZTU0): the same setupwraith `.RecoveryActivity` took Home and system_server crash-looped
  on it. The install script now starts the new launcher before disabling the stock one, sets the
  home preference last (Android drops it whenever the set of HOME apps changes), then checks where
  Home resolves and re-enables the stock launcher if it is not the new one. Disable setupwraith as
  above. To confirm it is this bug: `logcat -b crash` shows `FATAL EXCEPTION IN SYSTEM PROCESS:
  android.display`, and the main log shows `setupwraith/.RecoveryActivity` being started every
  200 ms or so. If you're already in the loop:
  `tools/tv.py 'pm enable --user 0 com.google.android.apps.tv.launcherx'`.
- **No on-screen keyboard** after switching launchers, seen on Xiaomi: the default input method
  pointed at a Baidu keyboard that isn't installed. Fix:
  `tools/tv.py 'ime set com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME'`
- **Third-party launchers** often charge for basics like a custom wallpaper. That's why this repo
  has its own.
- **Wrong time zone, clock off by 30 or 60 minutes**, seen on Xiaomi in Iran: the TV picked a
  neighbouring zone, and older firmware ships time zone data from before Iran dropped daylight
  saving in 2022 (on Android 10 and newer, check with
  `tools/tv.py 'cat /apex/com.android.tzdata/etc/tz/tz_version'`). Set the zone yourself:
  `tools/tv.py 'settings put global auto_time_zone 0; cmd alarm set-timezone Asia/Tehran'`.
  If the clock then jumps an hour ahead in spring, a fixed offset keeps it right:
  `tools/tv.py 'cmd alarm set-timezone GMT+03:30'`. The same applies to any country whose rules
  changed after the firmware was built.
- **The network pill says "Wi-Fi" instead of the network name** after `pm clear com.gathin.tvhome`
  or a reinstall from scratch: clearing the launcher's data also drops the location permission
  Android 10 needs to read the SSID. `pm grant com.gathin.tvhome android.permission.ACCESS_FINE_LOCATION`
  (the install script does this).
- **`pm disable-user` answers "Warning! This command is illegal!"**, and the installed launcher disappears a few
  seconds later, seen on a TiVo Stream 4K (reported on Threads, October 2026): the firmware refuses to disable its
  packages and its package service removes the new launcher. The same owner had run the original launcher on that
  stick before, set up with a different AI agent, so it is not a hard block; what differed is not known yet. Stop
  at the refusal: nothing is changed and the stick stays as it was. A report with the commands that worked is welcome.
- **The TV shows its logo and restarts over and over**, the morning after an install that had worked, reported once
  (October 2026, model not known yet). Unplug the TV for a minute. If it reaches Home even briefly, run
  `tools/restore.sh <your debloat list>` (Windows: `tools\restore.ps1`) to enable everything again. The cause is not
  found yet; please open an issue with the model and the `debloat.txt` used if it happens to you.
- **The wallpaper is gone after the TV was switched off** and comes back after a force stop of Glass TV, reported
  once (October 2026). Not reproduced yet; an issue with the model and theme helps.
- **Home is "Select a Home app"** after `tools/restore.sh` followed by re-applying a list: disabling a
  package that also claims HOME (PatchWall on Xiaomi) makes Android drop the default. The install
  script now picks the known stock launcher among the candidates by itself; otherwise choose one on
  the TV or pass `STOCK_LAUNCHER=<package>`.

## Accessibility helper enabled but the remote button does nothing

- **The helper is switched off again after every reboot**, seen on a Toshiba Google TV made by Hisense
  (`songshan`, Android 11, #4): at boot the firmware logs `Ignoring non-encryption-aware service` for
  `InputKeyService` and `HomeKeyService` and clears `enabled_accessibility_services`. On that TV the remote's
  Input button worked without the helper, so nothing was needed. Where you do need it, run the install
  script again with the flag after a reboot.

The optional `HomeKeyService` and `InputKeyService` only filter remote keys once Android has bound to
them. The installer's `enabled` message means the setting was written, not that the service is bound.
Read the state without changing anything:

```sh
tools/tv.py 'am get-current-user'
tools/tv.py 'settings --user current get secure enabled_accessibility_services'
tools/tv.py 'dumpsys accessibility'
```

| Helper | Component in the enabled-services list | Key it filters |
| --- | --- | --- |
| Home button | `com.gathin.tvhome/.HomeKeyService` | `KEYCODE_ALL_APPS` (not an ordinary `KEYCODE_HOME`) |
| Input button | `com.gathin.tvhome/.InputKeyService` | `KEYCODE_TV` |

- **Enabled** means the component is in that user's colon-separated list (the class may be written in
  full, `com.gathin.tvhome/com.gathin.tvhome.HomeKeyService`). Match the whole component.
- **Bound** means it appears under that user's `Bound services` section of the dump (`services` on older
  builds). Give it a few seconds after enabling and read again. Labels in the dump can be translated or
  shared with other services; a line containing "Home" is not proof.
- **Working** means the physical button does the right thing from inside another app. An injected key
  over ADB proves navigation, not that the remote sends the code this helper filters.

Enabled but never bound points at a firmware restriction on auto-starting sideloaded services (some TCL
builds log `OP_AUTO_START` denials). Look at `logcat` for the binding failure before changing settings,
and do not rewrite the enabled-services list or grant app-ops as a blanket fix. When reporting, give the
model, build, current user and the relevant redacted lines, not the raw dump.
