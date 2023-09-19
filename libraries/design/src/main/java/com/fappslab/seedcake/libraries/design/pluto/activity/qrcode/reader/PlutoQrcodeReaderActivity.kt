package com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.reader

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoQrcodeReaderActivityBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.setTint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlutoQrcodeReaderActivity : AppCompatActivity(R.layout.pluto_qrcode_reader_activity) {

    private val binding: PlutoQrcodeReaderActivityBinding by viewBinding(R.id.qrcode_root)
    private val cameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this) }
    private var cameraControl: CameraControl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemUI()
        setupListeners()
        showScanner()
    }

    private fun showScanner() {
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            Analyzer(::setCameraResult)
        )

        val selector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        runCatching {
            val camera = cameraProviderFuture.get()
                .bindToLifecycle(this, selector, preview, imageAnalysis)
            cameraControl = camera.cameraControl
        }
    }

    private fun setCameraResult(@ColorRes color: Int, result: String?) {
        binding.cameraFrameFocus.setTint(color)
        result?.let { encryptedSeeds ->
            lifecycleScope.launch {
                onResultAction(encryptedSeeds)
            }
        }
    }

    private suspend fun onResultAction(encryptedSeeds: String) {
        val data = Intent()
        data.putExtra(EXTRA_QRCODE_RESULT, encryptedSeeds)
        setResult(RESULT_OK, data)
        delay(timeMillis = 500)
        finish()
    }

    private fun setupSystemUI() = window.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(false)
            insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun setupListeners() = binding.run {
        checkFlash.setOnCheckedChangeListener { _, isChecked ->
            cameraControl?.enableTorch(isChecked)
        }
        buttonClose.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_QRCODE_RESULT = "EXTRA_QRCODE_RESULT"
        fun createIntent(context: Context): Intent =
            Intent(context, PlutoQrcodeReaderActivity::class.java)
    }
}
