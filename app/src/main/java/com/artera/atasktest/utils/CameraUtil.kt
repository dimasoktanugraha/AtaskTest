package com.artera.atasktest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class CameraUtil(val context: Context?) {

//        fun setBlink(cameraPreview: PreviewView) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                cameraPreview.postDelayed({
//                    cameraPreview.foreground = ColorDrawable(Color.WHITE)
//                    cameraPreview.postDelayed({ cameraPreview.foreground = null }, 50L)
//                }, 100)
//            }
//        }
//
//        fun startCamera(cameraPreview: PreviewView, fragmentActivity: FragmentActivity, lensFacing: Int) {
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//            cameraProviderFuture.addListener({
//                try {
//                    val cameraProvider = cameraProviderFuture.get()
//                    val preview = Preview.Builder().build()
//                    preview.setSurfaceProvider(cameraPreview.createSurfaceProvider())
//                    val builder = ImageCapture.Builder()
//                    val imageCapture = builder
//                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//                            .setTargetRotation(fragmentActivity.windowManager.defaultDisplay.rotation)
//                            .build()
//                    val cameraSelector = CameraSelector.Builder()
//                            .requireLensFacing(lensFacing)
//                            .build()
//                    try {
//                        cameraProvider.unbindAll()
//                        cameraProvider.bindToLifecycle(fragmentActivity, cameraSelector, preview, imageCapture)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                } catch (e: ExecutionException) {
//                    e.printStackTrace()
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }, ContextCompat.getMainExecutor(fragmentActivity))
//        }
//
//        fun getImageCapture(activity: FragmentActivity): ImageCapture? {
//            val builder = ImageCapture.Builder()
//            return builder
//                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                    .setTargetRotation(activity.windowManager.defaultDisplay.rotation)
//                    .build()
//        }
//
//        fun getCameraSelector(lensFacing: Int): CameraSelector? {
//            return CameraSelector.Builder()
//                    .requireLensFacing(lensFacing)
//                    .build()
//        }
//
//        fun getCameraPreview(cameraPreview: PreviewView): Preview? {
//            val preview = Preview.Builder().build()
//            preview.setSurfaceProvider(cameraPreview.createSurfaceProvider())
//            return preview
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        fun setUpPinchToZoom(camera: Camera, cameraPreview: PreviewView, context: Context?) {
//            val zoomListener: SimpleOnScaleGestureListener = object : SimpleOnScaleGestureListener() {
//                override fun onScale(detector: ScaleGestureDetector): Boolean {
//                    val scale = camera.cameraInfo.zoomState.value!!.zoomRatio * detector.scaleFactor
//                    camera.cameraControl.setZoomRatio(scale)
//                    return true
//                }
//            }
//            val scaleGestureDetector = ScaleGestureDetector(context, zoomListener)
//            cameraPreview.setOnTouchListener { view: View?, event: MotionEvent? ->
//                scaleGestureDetector.onTouchEvent(event)
//                true
//            }
//        }
//
//        fun setUpCenterFocus(cameraPreview: PreviewView, camera: Camera) {
//            val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(cameraPreview.width.toFloat(), cameraPreview.height.toFloat())
//            val centerWidth = cameraPreview.width.toFloat() / 2
//            val centerHeight = cameraPreview.height.toFloat() / 2
//            val autoFocusPoint = factory.createPoint(centerWidth, centerHeight)
//            try {
//                val action = FocusMeteringAction.Builder(autoFocusPoint, FocusMeteringAction.FLAG_AF).setAutoCancelDuration(1, TimeUnit.SECONDS).build()
//                camera.cameraControl.startFocusAndMetering(action)
//            } catch (e: Exception) {
//                Log.d("ERROR", "cannot access camera " + e.message)
//            }
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        fun setUpTapFocus(cameraPreview: PreviewView, camera: Camera) {
//            cameraPreview.setOnTouchListener { view: View?, motionEvent: MotionEvent ->
//                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
//                    return@setOnTouchListener true
//                } else if (motionEvent.action == MotionEvent.ACTION_UP) {
//                    val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(cameraPreview.width.toFloat(), cameraPreview.height.toFloat())
//                    val autoFocusPoint = factory.createPoint(motionEvent.x, motionEvent.y)
//                    try {
//                        val action = FocusMeteringAction.Builder(autoFocusPoint, FocusMeteringAction.FLAG_AF).disableAutoCancel().build()
//                        camera.cameraControl.startFocusAndMetering(action)
//                    } catch (e: Exception) {
//                        Log.d("ERROR", "cannot access camera " + e.message)
//                    }
//                    return@setOnTouchListener true
//                } else {
//                    return@setOnTouchListener false
//                }
//            }
//        }
}