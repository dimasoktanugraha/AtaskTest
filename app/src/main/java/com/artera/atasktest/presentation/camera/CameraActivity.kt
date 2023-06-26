package com.artera.atasktest.presentation.camera

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.artera.atasktest.presentation.main.MainActivity
import com.artera.atasktest.databinding.ActivityCameraBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private val viewModel: CameraViewModel by viewModel()

    private lateinit var cameraController: LifecycleCameraController
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA

    companion object{
        private const val TAG = "Camera Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!viewModel.hasPermission(baseContext)){
            activityResultLauncher.launch(viewModel.getRequiredPermission())
        }else{
            startCamera()
        }

        binding.btnCapture.setOnClickListener { takePhoto() }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permission ->
            var permissionGranted = true
            permission.entries.forEach{
                if (it.key in viewModel.getRequiredPermission() && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted){
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
            }else{
                startCamera()
            }
        }

    private fun startCamera() {
        val previewCamera = binding.previewCamera
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = lensFacing
        previewCamera.controller = cameraController
    }

    private fun takePhoto() {
        cameraController.takePicture(
            viewModel.getOutputOptionCamera(contentResolver),
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(baseContext, "Succeed: ${outputFileResults.savedUri}", Toast.LENGTH_SHORT).show()

                    binding.rlProgress.visibility = View.GONE
                    val intent = Intent()
                    intent.putExtra(MainActivity.IMAGE_URI, outputFileResults.savedUri.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    binding.rlProgress.visibility = View.GONE
                    Toast.makeText(baseContext, "Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        setBlink()
    }

    private fun setBlink() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.previewCamera.run {
                postDelayed({
                    foreground = ColorDrawable(Color.WHITE)
                    postDelayed({
                        foreground = null
                        binding.rlProgress.visibility = View.VISIBLE
                    }, 50L)
                }, 100)
            }
        }
    }
}