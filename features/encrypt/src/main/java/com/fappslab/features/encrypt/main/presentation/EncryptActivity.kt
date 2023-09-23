package com.fappslab.features.encrypt.main.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress1
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress2
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress3
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptActivityBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.onFragmentResult

const val REQUEST_KEY_PROGRESS = "REQUEST_KEY_PROGRESS"

class EncryptActivity : AppCompatActivity(R.layout.encrypt_activity) {

    private val binding: EncryptActivityBinding by viewBinding(R.id.encrypted_root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservables()
    }

    private fun setupObservables() {
        onFragmentResult(REQUEST_KEY_PROGRESS) {
            val progress = when {
                containsKey(Progress3.name) -> Progress3.ordinal
                containsKey(Progress2.name) -> Progress2.ordinal
                else -> Progress1.ordinal
            }
            binding.stepProgress.updateProgress(progress)
        }
    }

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, EncryptActivity::class.java)
    }
}
