# Which TVs work

Glass TV runs on **Android TV and Google TV, Android 8 or newer**: on the TV itself, or on a box or stick
plugged into any TV. This page says how far each device has been tested. "Tested" means a person ran it on
that exact model; nothing here is guessed from a spec sheet without saying so.

## 1. Glass TV tested end to end

Every feature of this version (debloat, Glass TV as Home, the glass and gliding selection, places, moving apps,
the Inputs menu, the clock screensaver, self-cleaning caches, the install safeguard), on the real TV, over weeks
of daily use and stress tests:

| Device | System | Example list |
|---|---|---|
| Xiaomi Mi TV (MiTV-MOOQ0, croods) | Android TV 10 | [`xiaomi-mitv-mooq0`](../examples/xiaomi-mitv-mooq0/) |

Also run with Glass TV by others, for the parts they checked:

| Device | System | Checked | Report |
|---|---|---|---|
| Toshiba TV with MIC (Hisense-made, `songshan`, MediaTek) | Google TV, Android 11 | Installed from Windows, became Home, Input button; the accessibility helpers are cleared on every boot on this firmware | [#4](https://github.com/cocombee/glass-tv/issues/4) |
| Sharp 4T-C65FJ1X 4K Google TV (MTC, `expo`, MediaTek MSD9602PA), Malaysia | Google TV, Android 14 | Installed from Windows, became Home after setupwraith was disabled; dock, Input button, HDMI, mic and Cast checked on the day | [`sharp-4t-c65fj1x`](../examples/sharp-4t-c65fj1x/) |

## 2. Tested with the original launcher

Contributors debloated these and made the original [Clean Your TV](https://github.com/Gathin23/cleanyourtv)
launcher their Home. Glass TV is that launcher with more on top and the same Android 8 minimum, so these are
expected to run it too, but each has been checked with the original, not with Glass TV itself. If you run Glass
TV on one, add a line to its `debloat.txt` and it moves to section 1.

| Device | System | Example list |
|---|---|---|
| BenQ E55-735 (Changhong AI PONT, DVB96BQ) | Google TV 14 | [`benq-e55-735`](../examples/benq-e55-735/) |
| CHiQ AI PONT (Changhong GTVU65, dangni, MediaTek mt5867) | Google TV 14 | [`chiq-gtvu65`](../examples/chiq-gtvu65/) |
| CHiQ AI PONT (DVB93CQ) | Android TV 11 | [`chiq-ai-pont`](../examples/chiq-ai-pont/) |
| Chromecast with Google TV 4K (sabrina) | Google TV 14 | [`google-chromecast-gtv-4k`](../examples/google-chromecast-gtv-4k/) |
| Hisense HiSmartTV A4 (xiaoyushan, MStar m7322), India | Android TV 9 | [`hisense-hismarttv-a4`](../examples/hisense-hismarttv-a4/) |
| Hisense SmartTV 4K FFM (huangshan, MediaTek) | Android TV 10 | [`hisense-smarttv-4k-ffm`](../examples/hisense-smarttv-4k-ffm/) |
| Hitachi 65QLEDSM20 (MTC, bangbae, MediaTek m7322), Australia | Android TV 11 | [`hitachi-65qledsm20`](../examples/hitachi-65qledsm20/) |
| NVIDIA SHIELD TV Pro 2019 (P2897, mdarcy) | Android TV 11 | [`nvidia-shield-tv-pro-2019`](../examples/nvidia-shield-tv-pro-2019/) |
| OnePlus TV (D00, Dubai) | Google TV 14 | [`oneplus-tv-dubai`](../examples/oneplus-tv-dubai/) |
| Panasonic 4KTV-JUP (Jupiter_PA, MediaTek) | Android TV 11 | [`panasonic-4ktv-jup`](../examples/panasonic-4ktv-jup/) |
| Philips 50PUS8517/12 (TP Vision, PH1M_EA_9970A, MediaTek MT5895), Europe | Android TV 11 | [`philips-50pus8517`](../examples/philips-50pus8517/) |
| Philips Smart 4K TV (Skyworth, eleonas, MediaTek), India | Google TV 14 | [`philips-smart-4k-google-tv`](../examples/philips-smart-4k-google-tv/) |
| RCA AI PONT SA (Changhong, redwood, ISDB92RC; also sold as realme 40-inch) | Android 14 | [`rca-ai-pont`](../examples/rca-ai-pont/) |
| SANYO ATV R2 (SCBC, Realtek RTD285O) | Android TV 9 | [`sanyo-atv-r2`](../examples/sanyo-atv-r2/) |
| Skyworth SWTV-21AE (sunnyvale, MediaTek) | Android TV 14 | [`skyworth-swtv-21ae`](../examples/skyworth-swtv-21ae/) |
| Sony BRAVIA 4K AE2 (BRAVIA_AE2, MediaTek MT5897), North America | Google TV 14 | [`sony-bravia-ae2`](../examples/sony-bravia-ae2/) |
| Sony BRAVIA 4K GB (BRAVIA_ATV2_EU, BRAVIA_ATV2) | Android TV 9 | [`sony-bravia-4k-gb`](../examples/sony-bravia-4k-gb/) |
| Sony BRAVIA 4K UR2 (BRAVIA_UR2_4K, MediaTek) | Android TV 10 | [`sony-bravia-4k-ur2`](../examples/sony-bravia-4k-ur2/) |
| Sony BRAVIA 4K VH2 (KD-55X80J, BRAVIA_VH2, MediaTek), EU | Google TV 12 | [`sony-bravia-4k-vh2`](../examples/sony-bravia-4k-vh2/) |
| Sony BRAVIA 9 II (K-65XR90M2, BRAVIA_AE3, MediaTek MT5838) | Google TV 14 | [`sony-bravia-9-ii`](../examples/sony-bravia-9-ii/) |
| Sony BRAVIA BF1 (BRAVIA 2 II class, BRAVIA_BF1_4K_A, MediaTek) | Google TV 12 | [`sony-bravia-bf1`](../examples/sony-bravia-bf1/) |
| Sony BRAVIA KD-65X80J (2021, BRAVIA_VH2, MediaTek), India | Google TV 10 | [`sony-kd-65x80j`](../examples/sony-kd-65x80j/) |
| Sony BRAVIA XBR-65X900E (2017, BRAVIA_ATV2) | Android TV 9 | [`sony-xbr-x900e`](../examples/sony-xbr-x900e/) |
| Stellamore T950S projector (Droidlogic be30a3, Amlogic txhd2) | Android 13 | [`stellamore-t950s`](../examples/stellamore-t950s/) |
| TCL 40S5400A (C06, C06_2K_GB, MediaTek MT9221, 1 GB) | Android TV 11 | [`tcl-40s5400a`](../examples/tcl-40s5400a/) |
| TCL 50EP680 (Europe, BeyondTV, Realtek rtd285o) | Android TV 11 | [`tcl-50ep680`](../examples/tcl-50ep680/) |
| TCL 55" 4K Google TV, 2023 (G10, G10_4K_US_NF) | Google TV 12 | [`tcl-g10-google-tv`](../examples/tcl-g10-google-tv/) |
| TCL 65C725 (Smart TV Pro, BeyondTV4) | Android TV 11 | [`tcl-65c725`](../examples/tcl-65c725/) |
| TCL BeyondTV (India) | Android TV 9 | [`tcl-beyondtv`](../examples/tcl-beyondtv/) |
| TCL BeyondTV, EU/Nordic (Realtek RTD2851, V8-R851T02-LF1V606) | Android TV 9 | [`tcl-beyondtv-rt2851`](../examples/tcl-beyondtv-rt2851/) |
| TCL BeyondTV2 (Latin America / Brazil, Realtek RTD2851) | Android TV 9 | [`tcl-beyondtv2-latin-america`](../examples/tcl-beyondtv2-latin-america/) |
| TCL QM6K Pro, 2025 (G08, MediaTek tcl9618) | Google TV 14 | [`tcl-qm6k-google-tv`](../examples/tcl-qm6k-google-tv/) |
| TCL Smart TV Pro (G05, G05_4K_US, MediaTek MT5889) | Google TV 11 | [`tcl-g05-google-tv`](../examples/tcl-g05-google-tv/) |
| Thomson 32HE5800 (TCL-built UnionTV, Realtek RT2841), Spain | Android TV 9 | [`thomson-32he5800`](../examples/thomson-32he5800/) |
| TiVo Stream 4K, streaming stick | Android TV | none yet: an owner ran the original launcher after setting it up with one AI agent; a later Glass TV attempt with another agent was blocked, see [Troubleshooting](TROUBLESHOOTING.md#working-on-the-tv) |
| Vu 4K (HVCL, lushan, MediaTek), India | Google TV 12 | [`vu-4k-lushan`](../examples/vu-4k-lushan/) |
| XGIMI Horizon S Pro projector (XK03H, xgimigalileo, MediaTek) | Android TV 11 | [`xgimi-horizon-s-pro`](../examples/xgimi-horizon-s-pro/) |
| Xiaomi Mi Box S (MIBOX4, oneday), streaming box | Android TV 9 | [`xiaomi-mi-box-s`](../examples/xiaomi-mi-box-s/) |
| Xiaomi Mi Box S 2nd gen (MiTV-AFKR0, jaws), streaming box | Google TV 11 | [`xiaomi-mi-box-s-2nd-gen`](../examples/xiaomi-mi-box-s-2nd-gen/) |
| Xiaomi Mi TV (MiTV-MOOQ0, croods) | Android TV 10 | [`xiaomi-mitv-mooq0`](../examples/xiaomi-mitv-mooq0/) |
| Xiaomi Mi TV 4A (MiTV-MOOQ1) | Android TV 10 | [`xiaomi-mi-tv-4a`](../examples/xiaomi-mi-tv-4a/) |
| Xiaomi MiTV-AESP0 stick (aquaman, 1 GB) | Android TV 9 | [`xiaomi-mitv-aesp0`](../examples/xiaomi-mitv-aesp0/) |
| Xiaomi MiTV-ASTP1 (maverick), China firmware 2.40.641.0514.cn, 1 GB RAM, 720p | Android TV 9 (CN build) | [`xiaomi-mitv-astp1`](../examples/xiaomi-mitv-astp1/) |
| Xiaomi MiTV-AXSO0 (dangal; Mi TV 4C Pro 2019 / 4A Pro) | Android TV 9 | [`xiaomi-mitv-axso0`](../examples/xiaomi-mitv-axso0/) |
| Xiaomi MiTV-MOOR2 (venom, MediaTek) | Google TV 11 | [`xiaomi-mitv-moor2`](../examples/xiaomi-mitv-moor2/) |
| Xiaomi MiTV-MOSR4 (irobot, MediaTek) | Google TV 11 | [`xiaomi-mitv-mosr4`](../examples/xiaomi-mitv-mosr4/) |
| Xiaomi MiTV-MSSP3 (machuca, MediaTek) | Android TV 11 | [`xiaomi-mitv-mssp3`](../examples/xiaomi-mitv-mssp3/) |
| Xiaomi TV A Pro 75" 2026 (MiTV-MZTU0, river, MediaTek) | Google TV 14 | [`xiaomi-tv-a-pro-2026`](../examples/xiaomi-tv-a-pro-2026/) |
| Xiaomi TV Box S 3rd gen (MiTV-AFMU0, twilight, Amlogic), streaming box | Google TV 14 | [`xiaomi-mitv-afmu0`](../examples/xiaomi-mitv-afmu0/) |

## 3. Debloat only

The clean-up works; no other launcher can be Home on these.

| Device | System | Why not the launcher | Example list |
|---|---|---|---|
| Amazon Fire TV Stick 4K, 1st gen 2018 (AFTMM, mantis) | Fire OS 6.7.1.1 / Android 7.1 | Fire OS takes Home back | [`amazon-fire-tv-stick-4k-2018`](../examples/amazon-fire-tv-stick-4k-2018/) |
| Insignia 55" F30 4K Fire TV (NS-55F301NA25, AFTDCT31, duckie) | Fire OS 7 / Android 9 | Fire OS takes Home back | [`insignia-ns55f301na25`](../examples/insignia-ns55f301na25/) |
| Redmi TV (MiTV-ANTL0, starwars) | Android 5.1.1 (not Android TV) | Android 5.1, below the launcher's Android 8 | [`redmi-mitv-antl0`](../examples/redmi-mitv-antl0/) |
| TCL 55Q10G Pro (China, tcl_mt5879_cn, MediaTek MT9952) | Android TV 11 | firmware refuses any other launcher | [`tcl-q10g-pro`](../examples/tcl-q10g-pro/) |
| TCL ak30a5 (China, Amlogic) | Android TV 9 | firmware refuses any other launcher | [`tcl-ak30a5`](../examples/tcl-ak30a5/) |
| Toshiba Fire TV (AFTHA004, hazel) | Fire OS 7 / Android 9 | Fire OS takes Home back | [`toshiba-fire-tv-aftha004`](../examples/toshiba-fire-tv-aftha004/) |

## 4. Expected to work, not tested yet

Android TV or Google TV 8 or newer, so nothing stands in the way, but no one has reported running it:

| Device | System | Notes |
|---|---|---|
| Google TV Streamer, Xiaomi TV Stick 4K, onn. and other Google TV sticks and boxes | Google TV 10+ | Google's setup app can take Home after the install; the install script handles it |
| unifi Plus Box (TM, Malaysia; Skyworth HP40A3) | Android TV 9 | TM may lock developer options; keep TM's unifi TV apps or live channels stop |
| Other Sony, TCL, Philips, Hisense (Android models), Sharp, Toshiba (Android models) TVs | Android TV / Google TV 8+ | Use the nearest example list as a starting point, never as is |

## 5. Not supported

| Runs | Why | Instead |
|---|---|---|
| Samsung (Tizen) | No Android apps, and no app can replace its Home or Home button | A Google TV or Android TV box in an HDMI port |
| LG (webOS) | Same | A box; or the webOS app you open yourself, a preview in [`webos/`](../webos/README.md) |
| Hisense / Toshiba VIDAA, Roku, Vizio SmartCast, Panasonic My Home Screen (the GX655 series among them), Philips Saphi (Philips' own system on its non-Android TVs), Apple TV | Same | A box |
| Older unifi IPTV set-top boxes (before the Plus Box) | Not Android TV | A box |

With a box, the TV becomes the screen and Glass TV is the whole experience. Turn on HDMI-CEC on the TV (Anynet+
on Samsung, SIMPLINK on LG, "CEC Control" on Hisense) so the TV's remote drives the box.

Before setting up any device, look up where developer settings are on that exact model: see
[AGENTS.md](../AGENTS.md), step 1.
