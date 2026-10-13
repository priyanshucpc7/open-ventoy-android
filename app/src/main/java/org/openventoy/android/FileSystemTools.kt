package org.openventoy.android

import java.io.File

/**
 * Filesystem facade. The analyzed app shipped FAT/NTFS/ext readers,
 * WimLib, and 7-Zip JBinding. This clean version delegates to
 * caller-added open libraries instead of copying that code.
 *
 * Suggested open components (add in Gradle, review licenses):
 * - libaums (Apache-2.0) for USB mass storage
 * - exFAT/NTFS via device formatting intents or open fs tools
 * - Apache Commons Compress (Apache-2.0) instead of proprietary 7-Zip JNI
 * - WIM handling via open wimlib (GPL) command-line or port
 */
object FileSystemTools {
    fun probeLabel(root: File): String =
        throw UnsupportedOperationException(
            "Wire an open filesystem stack; refusing to copy proprietary fs code."
        )
}
