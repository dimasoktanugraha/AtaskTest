package com.artera.atasktest.presentation.camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel: ViewModel() {

    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun getRequiredPermission() = mutableListOf(
        android.Manifest.permission.CAMERA
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()

    fun hasPermission(context: Context) = getRequiredPermission().all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun getOutputOptionCamera(contentResolver: ContentResolver): ImageCapture.OutputFileOptions {
        // Create name and MediaStore
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Atask")
            }
        }

        // Create output option and metadata
        return ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
    }
}