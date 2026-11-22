<h1 align="center">Kingdom Rush Android FPS patch</h1>

A [Reseam](https://reseam.app) patch for the Android versions of the Kingdom Rush games that use LÖVE.

The patch allows the game to run at the desired frame rate, up from 30 FPS for the original trilogy, or 30/60 FPS depending on the device for Kingdom Rush 5 and 6.
<super><sub>Vengeance runs on a different engine, so the patch doesn't apply to it.</sub></super>

This does the equivalent of editing the `fps` value in the configuration file `settings.lua` on the PC version.

Notes:
* Applying the patch resigns the APK, so Google Play Games won't work on the patched version.
* Only the original game can be automatically downloaded by the manager as it's the only free game in the series. To patch the other games you need to own them, and in the manager set the APK to "Installed version".
* (Relevant only for >60 FPS) If the game is running below the target FPS, the gameplay will be in slow motion, so don't choose a target FPS higher than the display Hz. Also, note that battery optimizations may reduce the display Hz, and that since Android 15 the display is set to 60Hz by default when running a game. The patch doesn't request the OS for a high display refresh rate, you'll have to force it yourself.
* The map screen always runs at 60 FPS regardless of the patch.

## Patches

<!-- patches:start -->

### `com.ironhidegames.android.kingdomrush`, `com.ironhidegames.android.kingdomrushfrontiers`, `com.ironhidegames.android.kingdomrushorigins`, `com.ironhidegames.android.kingdomrush.alliance`, `com.ironhidegames.android.kingdomrush6.genesis`

| Patch | What it does |
|---|---|
| Target FPS | Changes the target frame rate to the chosen value, up from 30 FPS (KR1, KR2, KR3) or 30/60 FPS depending on device (KR5, KR6). |

<!-- patches:end -->

## License

AGPL-3.0-or-later.
