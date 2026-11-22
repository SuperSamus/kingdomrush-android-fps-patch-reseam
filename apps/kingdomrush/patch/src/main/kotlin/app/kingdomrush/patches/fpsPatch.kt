package app.kingdomrush.patches

import app.reseam.patch.*
import app.reseam.patch.types.*

val kingdomRushFpsPatch =
    patch(KHFPSPatchStrings.fpsPatchName) {
        description(KHFPSPatchStrings.fpsPatchDescription)

        val targetFps =
            intOption(
                key = "target_fps",
                title = KHFPSPatchStrings.fpsPatchTargetTitle,
                description = KHFPSPatchStrings.fpsPatchTargetDescription,
                default = 60,
                required = false
            )

        compatibleWith("com.ironhidegames.android.kingdomrush")
        compatibleWith("com.ironhidegames.android.kingdomrushfrontiers")
        compatibleWith("com.ironhidegames.android.kingdomrushorigins")
        compatibleWith("com.ironhidegames.android.kingdomrush.alliance")
        compatibleWith("com.ironhidegames.android.kingdomrush6.genesis")
        dependsOn(ExternalPatch("reseam-patches", "app.reseam.patches.universal.removePairip"))

        execute {
            val fps = options[targetFps]

            require(fps in 0..0x7FFF) { "Target FPS must be between 0 and 32767" }

            val fpsLow = (fps and 0xFF).toByte()
            val fpsHigh = ((fps shr 8) and 0xFF).toByte()

            val PATHS = arrayOf("assets/gc32/all/main_utils.lua", "assets/gc64/all/main_utils.lua");

            for (componentName in files.components()) {
                for (assetPath in PATHS) {
                    files.component(componentName).read(assetPath)?.let {
                        replacePattern(it, fpsLow, fpsHigh)
                        files.component(componentName).write(assetPath, it)
                    }
                }
            }
        }
    }

/**
 * Searches for all occurrences of the pattern: 29 ?? 3C 00 58 ?? 01 80 29 ?? 1E 00 and replaces the
 * 3C 00 and 1E 00 values with the given low/high bytes.
 *
 * main_utils.lua is the utility for reading the configuration file. While on the PC version you can
 * simply edit the `fps` value in it, on the Android version there are two problems:
 * 1. The configuration file is in an inaccessible location.
 * 2. Even if it was accessible, it's ignored on Android.
 *
 * So if we can't modify the configuration file, we must modify its reader.
 *
 * On most platforms including Android, `fps` is set to a value depending on the device profile: 30
 * if low, 60 if high. The patch modifies both values. (In the first three games the logic is the
 * same, but there's nothing that sets the device profile to high on Android.)
 */
private fun replacePattern(data: ByteArray, fpsLow: Byte, fpsHigh: Byte) {
    val patternLength = 12

    var i = 0
    while (i <= data.size - patternLength) {
        if (matchesPattern(data, i)) {
            // Replace the first 16‑bit value
            data[i + 2] = fpsLow
            data[i + 3] = fpsHigh
            // Replace the second 16‑bit value
            data[i + 10] = fpsLow
            data[i + 11] = fpsHigh
            // Advance past this match to avoid overlapping replacements
            i += patternLength
        } else {
            i++
        }
    }
}

private fun matchesPattern(data: ByteArray, offset: Int): Boolean {
    // Pattern: 29 ?? 3C 00 58 ?? 01 80 29 ?? 1E 00
    if (data[offset] != 0x29.toByte()) return false
    // offset + 1 is wildcard
    if (data[offset + 2] != 0x3C.toByte()) return false
    if (data[offset + 3] != 0x00.toByte()) return false
    if (data[offset + 4] != 0x58.toByte()) return false
    // offset + 5 is wildcard
    if (data[offset + 6] != 0x01.toByte()) return false
    if (data[offset + 7] != 0x80.toByte()) return false
    if (data[offset + 8] != 0x29.toByte()) return false
    // offset + 9 is wildcard
    if (data[offset + 10] != 0x1E.toByte()) return false
    if (data[offset + 11] != 0x00.toByte()) return false
    return true
}
