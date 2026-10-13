package org.openventoy.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.openventoy.android.databinding.ActivityTutorialBinding

/** OTG tutorial: cable, power, file copy. No ads-gated steps. */
class OtgTutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.steps.text = getString(R.string.otg_steps)
    }
}
