package org.openventoy.android

import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.openventoy.android.databinding.ActivityMainBinding

/**
 * Clean-room entry point. No ads, no tracking, no billing.
 * Functional labels (USB request, browse, install Ventoy, copy ISO)
 * are re-implemented from scratch; no code copied from the analyzed APK.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usb: UsbDeviceHandler
    private lateinit var installer: VentoyInstaller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usbManager = getSystemService(UsbManager::class.java)
        usb = UsbDeviceHandler(this, usbManager)
        installer = VentoyInstaller(this)

        binding.btnRequestUsb.setOnClickListener {
            usb.requestFirstMassStoragePermission()
        }
        binding.btnBrowse.setOnClickListener {
            startActivity(Intent(this, BrowserActivity::class.java))
        }
        binding.btnTutorial.setOnClickListener {
            startActivity(Intent(this, OtgTutorialActivity::class.java))
        }
        binding.btnInstallVentoy.setOnClickListener {
            val device = usb.firstMassStorageDevice()
            if (device == null) {
                binding.status.text = getString(R.string.no_usb)
                return@setOnClickListener
            }
            // Fully offline: maintainer bundles official Ventoy files under
            // assets/ventoy/ (see docs/OFFLINE.md). Verify SHA-256, then write.
            binding.status.text = getString(R.string.pick_bundled_release)
        }
    }
}
