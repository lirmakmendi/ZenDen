package com.sceproject.zenden.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MeasureNowViewModel : ViewModel() {

    enum class MeasurementStatus {
        Connecting,
        Measuring,
        Received
    }

    private val _heartRate = MutableLiveData<Int>()
    val heartRate: LiveData<Int> = _heartRate

    private val _measurementStatus =
        MutableLiveData<MeasurementStatus>(MeasurementStatus.Connecting)
    val measurementStatus: LiveData<MeasurementStatus> = _measurementStatus

    init {
        simulateHeartRateMeasurement()
    }

    fun simulateHeartRateMeasurement() {
        // Simulate the process of connecting to the watch and receiving data
        viewModelScope.launch {
            _measurementStatus.value = MeasurementStatus.Connecting
            delay(3500) // Simulate connecting delay
            _measurementStatus.value = MeasurementStatus.Measuring
            delay(2000) // Simulate measuring delay
            _heartRate.value = (60..150).random() // Simulate receiving heart rate data
            _measurementStatus.value = MeasurementStatus.Received
        }
    }

    private val _gad7Responses = MutableLiveData<Map<Int, Int>>(emptyMap())
    val gad7Responses: LiveData<Map<Int, Int>> = _gad7Responses

    val gad7Questions = listOf(
        Gad7Question(1, "הרגשתי עצבני, חרד או מתוח מאוד"),
        Gad7Question(2, "לא הייתי מסוגל להפסיק לדאוג או לשלוט בדאגה"),
        Gad7Question(3, "הייתי מודאג ביותר מדי בנוגע לדברים שונים"),
        Gad7Question(4, "התקשיתי להירגע"),
        Gad7Question(5, "הייתי כל כך חסר מנוחה שהיה לי קשה לשבת מבלי לנוע"),
        Gad7Question(6, "הייתי מתעצבן או מתרגז בקלות"),
        Gad7Question(7, "פחדתי כאילו משהו נורא עלול לקרות")
        // Add more questions as needed
    )

    fun setGad7Response(questionId: Int, score: Int) {
        _gad7Responses.value = _gad7Responses.value?.toMutableMap()?.apply {
            put(questionId, score)
        }
    }

    fun calculateTotalAnxiety(heartRate: Int): Int {
        val surveyScore = gad7Responses.value?.values?.sum() ?: 0
        val heartRateModifier = calculateHeartRateScore(heartRate)

        // Apply the heart rate modifier as a percentage adjustment to the survey score
        val adjustedScore = surveyScore * (1 + heartRateModifier)

        // Round the result as we're dealing with a score that should be a whole number
        return adjustedScore.roundToInt()
    }

    fun calculateHeartRateScore(heartRate: Int): Double {
        return when (heartRate) {
            in 51..60 -> -0.1 // Lower than average, but not necessarily concerning for well-trained individuals
            in 61..100 -> 0.0 // Normal range, no modifier
            in 101..110 -> 0.1 // Slightly elevated, slight concern
            else -> 0.2 // Significantly elevated, higher concern
        }
    }

    data class Gad7Question(
        val id: Int,
        val questionText: String,
        val answers: List<String> = listOf(
            "כלל לא",
            "כמה ימים",
            "יותר ממחצית הימים",
            "כמעט כל יום"
        ),
        val scores: List<Int> = listOf(
            0,
            1,
            2,
            3
        )
    )


}
