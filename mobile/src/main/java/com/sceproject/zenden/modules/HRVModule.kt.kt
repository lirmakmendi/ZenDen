package com.sceproject.zenden.modules


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.sqrt

class HRVModule {
    private val rrIntervals = mutableListOf<Double>()
    private val _hrvValue = MutableStateFlow(0.0)
    val hrvValue: StateFlow<Double> = _hrvValue

    private val SECONDS_FOR_COLLECTION = 60
    private val CONVERSION_INTERVAL = 60000.0

    fun addHeartRate(bpm: Double) {
        val rrInterval = CONVERSION_INTERVAL / bpm // Convert BPM to RR interval in milliseconds
        rrIntervals.add(rrInterval)

        // Keep only the last 60 seconds of data (adjust as needed)
        if (rrIntervals.size > SECONDS_FOR_COLLECTION) {
            rrIntervals.removeAt(0)
        }

        calculateHRV()
    }

    private fun calculateHRV() {
        if (rrIntervals.size < 2) {
            _hrvValue.value = 0.0
            return
        }

        var sumOfSquaredDifferences = 0.0
        for (i in 1 until rrIntervals.size) {
            val diff = rrIntervals[i] - rrIntervals[i-1]
            sumOfSquaredDifferences += diff * diff
        }

        val rmssd = sqrt(sumOfSquaredDifferences / (rrIntervals.size - 1))
        _hrvValue.value = rmssd
    }
}