package com.sceproject.zenden.app

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PanicAttackPredictor(private val context: Context) {
    private lateinit var tflite: Interpreter

    private val meanAge = 38.891876f  // Update with actual value from norm_params.pkl
    private val stdAge = 9.962012f  // Update with actual value from norm_params.pkl
    private val meanSdnn = 49.7875f  // Update with actual value from norm_params.pkl
    private val stdSdnn = 14.629944f  // Update with actual value from norm_params.pkl
    private val meanPdssScore = 12.819583f  // Update with actual value from norm_params.pkl
    private val stdPdssScore = 7.780106f  // Update with actual value from norm_params.pkl
    private val meanGender = 0.491875f  // Update with actual value from norm_params.pkl
    private val stdGender = 0.499986f  // Update with actual value from norm_params.pkl

    init {
        val tfliteModel = FileUtil.loadMappedFile(context, "panic_attack_model.tflite")
        tflite = Interpreter(tfliteModel)
    }

    fun predict(age: Float, sdnn: Float, pdssScore: Float, gender: Float): Float {
        val inputBuffer = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder())

        // Normalize the inputs using the mean and std deviation
        val normAge = (age - meanAge) / stdAge
        val normSdnn = (sdnn - meanSdnn) / stdSdnn
        val normPdssScore = (pdssScore - meanPdssScore) / stdPdssScore
        val normGender = (gender - meanGender) / stdGender

        Log.d("PanicAttackPredictor", "Normalized Age: $normAge")
        Log.d("PanicAttackPredictor", "Normalized SDNN: $normSdnn")
        Log.d("PanicAttackPredictor", "Normalized PDSS Score: $normPdssScore")
        Log.d("PanicAttackPredictor", "Normalized Gender: $normGender")

        inputBuffer.putFloat(normAge)
        inputBuffer.putFloat(normSdnn)
        inputBuffer.putFloat(normPdssScore)
        inputBuffer.putFloat(normGender)

        val outputBuffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder())
        tflite.run(inputBuffer, outputBuffer)

        outputBuffer.rewind()
        val prediction = outputBuffer.float

        Log.d("PanicAttackPredictor", "Prediction result: $prediction")

        return prediction
    }
}



