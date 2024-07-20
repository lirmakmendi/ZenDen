package com.sceproject.zenden.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt
class MeasureNowViewModel : ViewModel() {

    enum class MeasurementStatus {
        Connecting,
        Measuring,
        Received
    }

    private val _heartRate = MutableLiveData<Int>()
    val heartRate: LiveData<Int> = _heartRate

    private val _measurementStatus = MutableLiveData<MeasurementStatus>(MeasurementStatus.Connecting)
    val measurementStatus: LiveData<MeasurementStatus> = _measurementStatus

    private val _lastHeartRateTimestamp = MutableLiveData<String>()
    val lastHeartRateTimestamp: LiveData<String> = _lastHeartRateTimestamp

    private val _lastPdssMeasurement = MutableLiveData<Int>()
    val lastPdssMeasurement: LiveData<Int> = _lastPdssMeasurement

    private val _lastPdssTimestamp = MutableLiveData<String>()
    val lastPdssTimestamp: LiveData<String> = _lastPdssTimestamp

    init {
        getRealHeartRateMeasurement()
        getLastPdssMeasurement()
    }

    private fun getRealHeartRateMeasurement() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        _measurementStatus.value = MeasurementStatus.Connecting

        db.collection("users").document(userId).collection("measurements")
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    _measurementStatus.value = MeasurementStatus.Connecting // or any error status you prefer
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    for (document in snapshots.documents) {
                        val hrValue = document.getDouble("sdnn")?.toInt() ?: 0
                        _heartRate.value = hrValue
                        val timestamp = document.getTimestamp("timestamp")?.toDate()
                        val formattedTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(timestamp)
                        _lastHeartRateTimestamp.value = formattedTimestamp
                        _measurementStatus.value = MeasurementStatus.Received
                    }
                } else {
                    Log.d("Firestore", "No measurements found")
                    _measurementStatus.value = MeasurementStatus.Connecting // or any status you prefer for no data
                }
            }
    }

    private fun getLastPdssMeasurement() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("pdss_measurements")
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    for (document in snapshots.documents) {
                        val pdssValue = document.getLong("pdssScore")?.toInt() ?: 0
                        _lastPdssMeasurement.value = pdssValue
                        val timestamp = document.getTimestamp("timestamp")?.toDate()
                        val formattedTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(timestamp)
                        _lastPdssTimestamp.value = formattedTimestamp
                    }
                } else {
                    Log.d("Firestore", "No PDSS measurements found")
                }
            }
    }

    private val _pdssResponses = MutableLiveData<Map<Int, Int>>(emptyMap())
    val pdssResponses: LiveData<Map<Int, Int>> = _pdssResponses

    val pdssQuestions = listOf(
        PdssQuestion(1, "כמה התקפי פאניקה היו לך בשבוע האחרון?",
            answers = listOf("ללא", "1-2", "3-4", "5-7", "8+"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(2, "כמה מעיקים היו התקפי הפאניקה שחווית בשבוע האחרון?",
            answers = listOf("לא מעיק בכלל", "מצוקה מתונה", "מצוקה בינונית", "מצוקה קשה", "מצוקה חמורה ביותר, כמעט תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(3, "בשבוע האחרון, עד כמה דאגת או חשת חרדה לגבי מתי התקף החרדה הבא שלך יתרחש או לגבי חשש ללקות בהתקף חרדה נוסף?",
            answers = listOf("בכלל לא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "דאגה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(4, "בשבוע האחרון, עד כמה נמנעת ממצבים, מקומות, פעילויות או אנשים בגלל פחדים מהתקף חרדה?",
            answers = listOf("אין הימנעות", "הימנעות מתונה", "הימנעות בינונית", "הימנעות חמורה", "הימנעות מתמדת"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(5, "בשבוע האחרון, עד כמה התקפי החרדה הפריעו לחיי החברה/פעילויות הפנאי שלך?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(6, "בשבוע האחרון, עד כמה התקפי החרדה הפריעו ליכולת שלך לעבוד או לבצע את תחומי האחריות שלך בבית?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        ),
        PdssQuestion(7, "בשבוע האחרון, עד כמה התקפי הפאניקה הפריעו לחיי המשפחה/אחריות הביתית שלך?",
            answers = listOf("ללא", "בצורה מתונה", "בצורה בינונית", "בצורה חמורה", "הפרעה תמידית"),
            scores = listOf(0, 1, 2, 3, 4)
        )
        // Add more questions as needed
    )

    fun setPdssResponse(questionId: Int, score: Int) {
        _pdssResponses.value = _pdssResponses.value?.toMutableMap()?.apply {
            put(questionId, score)
        }
    }

    fun calculateTotalPanicDisorder(heartRate: Int): Int {
        val surveyScore = pdssResponses.value?.values?.sum() ?: 0
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

    data class PdssQuestion(
        val id: Int,
        val questionText: String,
        val answers: List<String>,
        val scores: List<Int>
    )
    // New function to save PDSS score
    fun savePdssScoreToFirestore(pdssScore: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // Get the current timestamp
        val timestamp = System.currentTimeMillis()

        // Format the timestamp
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedTimestamp = sdf.format(timestamp)

        val pdssMeasurement = hashMapOf(
            "pdssScore" to pdssScore,
            "timestamp" to FieldValue.serverTimestamp()
        )

        // Use the formatted timestamp as the document ID
        val documentId = formattedTimestamp.replace(" ", "_").replace(":", "-").replace("/", "-")

        db.collection("users").document(userId).collection("pdss_measurements").document(documentId).set(pdssMeasurement)
            .addOnSuccessListener {
                Log.d("Firestore", "PDSS Measurement successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing PDSS measurement", e)
            }
    }



}
