package org.openventoy.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.openventoy.android.databinding.ActivityBrowserBinding

/** Clean-room USB file browser placeholder (SAF + open USB lib). */
class BrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrowserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.hint.text = getString(R.string.browser_hint)
    }
}
