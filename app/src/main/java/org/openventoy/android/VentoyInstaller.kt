package org.openventoy.android

import android.content.Context
import java.io.File
import java.security.MessageDigest

/**
 * Fully offline clean-room Ventoy installer contract.
 *
 * No INTERNET permission. Official Ventoy boot files are bundled under
 * `app/src/main/assets/ventoy/` by the maintainer from
 * https://github.com/ventoy/Ventoy/releases (GPL-3.0, with attribution
 * in `app/src/main/assets/ventoy/PROVENANCE.txt`), then verified by
 * SHA-256 at runtime before any USB write.
 *
 * Nothing here is copied from the analyzed APK's
 * `assets/ventoy/ventoy.disk.img` / `assets/boot/{boot,core}.img`.
 * Actual block writes require an open USB-mass-storage stack plus
 * filesystem formatting. Those steps stay explicit TODOs instead of
 * copying proprietary native code.
 */
class VentoyInstaller(private val context: Context) {

    data class BundledRelease(
        val version: String,
        val expectedSha256: String,
    )

    fun verifyAssetSha256(assetName: String, expectedHex: String): Boolean {
        val digest = MessageDigest.getInstance("SHA-256")
        context.assets.open(assetName).use { input ->
            val buffer = ByteArray(1024 * 1024)
            while (true) {
                val read = input.read(buffer)
                if (read < 0) break
                digest.update(buffer, 0, read)
            }
        }
        val actual = digest.digest().joinToString("") { "%02x".format(it) }
        return actual.equals(expectedHex, ignoreCase = true)
    }

    fun verifyFileSha256(file: File, expectedHex: String): Boolean {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(1024 * 1024)
            while (true) {
                val read = input.read(buffer)
                if (read < 0) break
                digest.update(buffer, 0, read)
            }
        }
        val actual = digest.digest().joinToString("") { "%02x".format(it) }
        return actual.equals(expectedHex, ignoreCase = true)
    }

    /**
     * TODO (needs device + open USB stack):
     * 1. read bundled asset (e.g. `ventoy/boot.img`),
     * 2. verify SHA-256 against `PROVENANCE.txt`,
     * 3. partition/format target USB only after explicit user confirmation
     *    (destructive), 4. write bootloader + data partition.
     * Must never run without explicit user confirmation, and never touch
     * the network (there is no INTERNET permission).
     */
    fun installOffline(release: BundledRelease, targetBlock: String): Nothing =
        throw UnsupportedOperationException(
            "Refusing to guess block writes. Bundle official ${release.version} " +
                "under assets/ventoy/, verify hash, confirm target $targetBlock " +
                "with the user, then implement offline writes here."
        )
}
