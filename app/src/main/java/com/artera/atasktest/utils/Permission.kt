package com.artera.atasktest.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permission {
    companion object{

        const val PERMISSION_REQUEST_CODE = 1001

        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPermission(context: Context?) {
            ActivityCompat.requestPermissions((context as Activity?)!!, PERMISSIONS_REQUIRED, PERMISSION_REQUEST_CODE)
        }
    }
}