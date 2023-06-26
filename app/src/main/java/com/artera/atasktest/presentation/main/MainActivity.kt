package com.artera.atasktest.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.artera.atasktest.*
import com.artera.atasktest.databinding.ActivityMainBinding
import com.artera.atasktest.presentation.camera.CameraActivity
import com.artera.atasktest.utils.CalculatorAction
import com.artera.atasktest.utils.CalculatorOperation
import com.google.android.gms.vision.text.TextRecognizer
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private lateinit var textRecognizer: TextRecognizer

    companion object{
        private const val TAG = "Main Activity"
        const val IMAGE_URI = "IMAGE_URI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textRecognizer = TextRecognizer.Builder(this).build()

        binding.opOcr.icon = ContextCompat.getDrawable(this, if (BuildConfig.FLAVOR_input == "camera") R.drawable.ic_camera else R.drawable.ic_image)

        onClickHandle()
        subscribe()
    }

    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.recognizeOcr(it, contentResolver, textRecognizer)
        binding.ivGallery.setImageURI(it)
    }

    private var launchActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val intent = result.data
        if (intent != null) {
            if (intent.hasExtra(IMAGE_URI)) {
                val uri = Uri.parse(intent.getStringExtra(IMAGE_URI))
                viewModel.recognizeOcr(uri, contentResolver, textRecognizer)
                binding.ivGallery.setImageURI(uri)
            }
            return@registerForActivityResult
        }
    }

    fun onDigitClick(view: View){
        binding.ivGallery.setImageURI(null)
        viewModel.onAction(CalculatorAction.Number((view as Button).text.toString()))
    }

    private fun onClickHandle() {
        binding.opAdd.setOnClickListener {
            viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
        }
        binding.opSubtract.setOnClickListener {
            viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Subtract))
        }
        binding.opMultiply.setOnClickListener {
            viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Multiply))
        }
        binding.opDivide.setOnClickListener {
            viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Divide))
        }

        binding.opClear.setOnClickListener { viewModel.onAction(CalculatorAction.Clear) }
        binding.opBackspace.setOnClickListener { viewModel.onAction(CalculatorAction.Delete) }
        binding.opResult.setOnClickListener { viewModel.onAction(CalculatorAction.Calculate) }
        binding.opPoint.setOnClickListener { viewModel.onAction(CalculatorAction.Decimal) }

        binding.opOcr.setOnClickListener {
            when(BuildConfig.FLAVOR_input){
                "camera" -> launchActivity.launch(Intent(this, CameraActivity::class.java))
                "filesystem" -> gallery.launch("image/*")
            }
        }
    }

    private fun subscribe() {
        viewModel.getDisplayText().observe(this, Observer { display ->
            binding.tvResult.text = display
        })
        viewModel.getDisplayOperationText().observe(this, Observer { display ->
            binding.tvCalculate.visibility = if (display.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.tvCalculate.text = display
        })
    }
}