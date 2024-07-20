package com.sceproject.zenden.data.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sceproject.zenden.app.PanicAttackPredictor
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import kotlinx.coroutines.launch

class PredictionViewModel(context: Context) : ViewModel() {
    private val predictor = PanicAttackPredictor(context)
    private val TAG = "PredictionViewModel"

    val predictionResult: MutableLiveData<Float> = MutableLiveData()
    val lastPdssMeasurement: MutableLiveData<Int> = MutableLiveData()
    val lastSdnnMeasurement: MutableLiveData<Float> = MutableLiveData()
    val age: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<String> = MutableLiveData()

    init {
        // For initial testing, you can set these values directly
//        age.value = "35"
//        gender.value = "זכר"
//        lastPdssMeasurement.value = 27
//        lastSdnnMeasurement.value = 100f

        // Uncomment these lines to fetch real data from Firebase
        fetchUserData()
        fetchLastMeasurements()
    }

    private fun fetchUserData() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val userId = user.uid
            val db = FirebaseFirestore.getInstance()

            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        age.value = document.getString("age")
                        gender.value = document.getString("gender")
                        Log.d(TAG, "User data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    private fun fetchLastMeasurements() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("pdss_measurements")
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    for (document in snapshots.documents) {
                        val pdssValue = document.getLong("pdssScore")?.toInt() ?: 0
                        lastPdssMeasurement.value = pdssValue
                    }
                }
            }

        db.collection("users").document(userId).collection("measurements")
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    for (document in snapshots.documents) {
                        val sdnnValue = document.getDouble("sdnn")?.toFloat() ?: 0f
                        lastSdnnMeasurement.value = sdnnValue
                    }
                }
            }
    }

    fun runPrediction() {
        val ageValue = age.value?.toFloatOrNull()
        val genderValue = gender.value?.let { if (it == "זכר") 0f else 1f }
        val pdssValue = lastPdssMeasurement.value?.toFloat()
        val sdnnValue = lastSdnnMeasurement.value

        if (ageValue != null && genderValue != null && pdssValue != null && sdnnValue != null) {
            val prediction = predictor.predict(ageValue, sdnnValue, pdssValue, genderValue)
            predictionResult.value = prediction
            handlePredictionResult(prediction)
        } else {
            Log.d(TAG, "Incomplete data for prediction")
        }
    }

    private fun handlePredictionResult(prediction: Float) {
        // Prediction results are handled within the UI now
    }
}
