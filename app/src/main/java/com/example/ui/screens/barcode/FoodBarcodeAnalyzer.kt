package com.example.ui.screens.barcode

import android.os.SystemClock
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * CameraX ImageAnalysis.Analyzer that detects food barcodes using Google ML Kit.
 * Handles frame rotation, resource lifecycle, and debounces rapid repeated scans.
 */
class FoodBarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_QR_CODE
        )
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    private var lastScannedCode: String? = null
    private var lastScannedTimestamp: Long = 0L
    private val throttleIntervalMs = 1500L
    private var isPaused = false

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
        lastScannedCode = null
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isPaused) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (isPaused) return@addOnSuccessListener
                    val now = SystemClock.elapsedRealtime()

                    for (barcode in barcodes) {
                        val rawValue = barcode.rawValue?.trim() ?: continue
                        if (rawValue.isBlank()) continue

                        // Debounce same barcode within cooldown window
                        if (rawValue == lastScannedCode && (now - lastScannedTimestamp) < throttleIntervalMs) {
                            continue
                        }

                        lastScannedCode = rawValue
                        lastScannedTimestamp = now
                        Log.d("FoodBarcodeAnalyzer", "Barcode detected via CameraX: $rawValue")
                        onBarcodeDetected(rawValue)
                        break
                    }
                }
                .addOnFailureListener { exc ->
                    Log.w("FoodBarcodeAnalyzer", "Barcode analysis failed", exc)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
