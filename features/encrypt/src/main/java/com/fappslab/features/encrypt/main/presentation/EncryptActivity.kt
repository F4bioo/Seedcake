package com.fappslab.features.encrypt.main.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.ActivityEncryptBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.onFragmentResult

const val PROGRESS_KEY = "PROGRESS_KEY"

class EncryptActivity : AppCompatActivity(R.layout.activity_encrypt) {

    private val binding: ActivityEncryptBinding by viewBinding(R.id.encrypted_root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservables()
    }

    private fun setupObservables() {
        onFragmentResult(PROGRESS_KEY) {
            val progress = when {
                containsKey("progress3") -> 2
                containsKey("progress2") -> 1
                else -> 0
            }
            binding.stepProgress.updateProgress(progress)
        }
    }

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, EncryptActivity::class.java)
    }
}
