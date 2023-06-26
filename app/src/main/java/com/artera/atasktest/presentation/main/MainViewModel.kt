package com.artera.atasktest.presentation.main

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artera.atasktest.utils.CalculatorAction
import com.artera.atasktest.utils.CalculatorOperation
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import java.text.DecimalFormat

class MainViewModel: ViewModel() {

    var num1 = ""
    var num2 = ""
    var operation: CalculatorOperation? = null
    var result = ""

    private var displayOperation: MutableLiveData<String> = MutableLiveData("0")
    private var display: MutableLiveData<String> = MutableLiveData("")

    init {
        setDisplay()
        displayOperation.postValue("")
    }

    fun getDisplayText(): LiveData<String?> {
        return display
    }

    fun getDisplayOperationText(): LiveData<String?> {
        return displayOperation
    }

    fun onAction(action: CalculatorAction) {
        if (result.isNotEmpty()) clearAll()
        when(action) {
            is CalculatorAction.Number -> addNumber(action.number)
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Clear -> clearAll()
            is CalculatorAction.Operation -> addOperation(action.operation)
            is CalculatorAction.Decimal -> addDecimal()
            is CalculatorAction.Calculate -> calculate()
        }
    }

    private fun addOperation(operation: CalculatorOperation) {
        if(num1.isNotBlank()) {
            this.operation = operation
        }
        setDisplay()
    }


    private fun calculate() {
        val number1 = num1.toDoubleOrNull()
        val number2 = num2.toDoubleOrNull()
        if(number1 != null && number2 != null) {
            val operate = when(operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return
            }
            val format = DecimalFormat("0.#")
            result = format.format(operate).toString()
        }
        setDisplayResult()
    }

    private fun delete() {
        when {
            num2.isNotBlank() -> num2.dropLast(1)
            operation != null -> operation = null
            num1.isNotBlank() -> num1.dropLast(1)
        }
        setDisplay()
    }

    private fun clearAll(){
        num1 = ""
        num2 = ""
        operation = null
        result = ""

        displayOperation.postValue("")
        setDisplay()
    }

    private fun addDecimal() {
        if(operation == null && !num1.contains(".") && num1.isNotBlank()) {
            num1 += "."
            setDisplay()
        } else if(!num2.contains(".") && num2.isNotBlank()) {
            num2 += "."
            setDisplay()
        }
    }

    private fun addNumber(number: String) {
        if(operation == null) {
            num1 += number
            setDisplay()
            return
        }
        num2 += number
        setDisplay()
    }

    private fun setDisplay(){
        display.postValue(dataOperation())
    }

    private fun setDisplayResult() {
        displayOperation.postValue(dataOperation())
        display.postValue(result)
    }

    private fun dataOperation(): String {
        var text = "0"
        if (num1.isNotEmpty()) text = num1
        if (operation != null) text += " ${operation?.operation}"
        if (num2.isNotEmpty()) text += " $num2"
        return text
    }

    fun recognizeOcr(it: Uri?, contentResolver: ContentResolver, textRecognizer: TextRecognizer) {
        clearAll()
        try {
            val bitmap = getBitmap(contentResolver, it)
            val frameImage = Frame.Builder().setBitmap(bitmap).build()
            val parseArray = textRecognizer.detect(frameImage)
            var ocrResult = ""
            for (i in 0 until parseArray.size()) {
                val textBlock = parseArray.get(parseArray.keyAt(i))
                ocrResult += textBlock.value
            }
            ocrResult.replace(" ", "")
            Log.d("MAIN ACTIVITY", "OCR: $ocrResult")

            calculateOcr(ocrResult)
        }catch (e: Exception){
            Log.d("MAIN ACTIVITY", e.message.toString())
        }
    }

    private fun calculateOcr(ocr: String) {
        for (char in ocr){
            when {
                char.isDigit() -> {
                    addNumber(char.toString())
                }
                char.isDecimal() -> {
                    addDecimal()
                }
                char.isOperator() -> {
                    if(num2.isNotEmpty()) break
                    when(char.toString()){
                        "x", "X", "*" -> addOperation(CalculatorOperation.Multiply)
                        "/" -> addOperation(CalculatorOperation.Divide)
                        "+" -> addOperation(CalculatorOperation.Add)
                        "-" -> addOperation(CalculatorOperation.Subtract)
                    }
                }
            }
        }
        calculate()
    }

    private fun Char.isDecimal(): Boolean {
        return this.toString() == "."
    }

    private fun Char.isOperator(): Boolean {
        val regex = Regex("[\\+\\-\\*\\/x]", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(this.toString())
    }

    private fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri!!))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
            }
        } catch (e: Exception){
            null
        }
    }
}