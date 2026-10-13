package org.openventoy.android

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build

/**
 * Minimal USB-host helper using only the Android framework.
 * Replaces the observed proprietary blockdevice/usb surface with a
 * clean implementation. Bulk transfer / SCSI / filesystem work belongs
 * in [UsbBlockDevice] on top of an open USB-mass-storage library.
 */
class UsbDeviceHandler(
    private val context: Context,
    private val usbManager: UsbManager,
) {
    companion object {
        const val ACTION_USB_PERMISSION = "org.openventoy.android.USB_PERMISSION"
    }

    fun massStorageDevices(): List<UsbDevice> =
        usbManager.deviceList.values.filter { device ->
            device.deviceClass == 0 && device.interfaceCount > 0 &&
                (0 until device.interfaceCount).any { i ->
                    device.getInterface(i).interfaceClass == 8 // mass storage
                }
        }

    fun firstMassStorageDevice(): UsbDevice? = massStorageDevices().firstOrNull()

    fun hasPermission(device: UsbDevice): Boolean =
        usbManager.hasPermission(device)

    fun requestFirstMassStoragePermission(): Boolean {
        val device = firstMassStorageDevice() ?: return false
        if (hasPermission(device)) return true
        val flags = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val intent = PendingIntent.getBroadcast(
            context, 0, Intent(ACTION_USB_PERMISSION), flags
        )
        usbManager.requestPermission(device, intent)
        return false
    }
}
