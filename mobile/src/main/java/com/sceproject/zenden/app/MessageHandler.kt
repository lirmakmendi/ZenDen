package com.sceproject.zenden.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sceproject.zenden.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.exp

class MessageHandler(private val context: Context) : MessageClient.OnMessageReceivedListener {

    private val _message = MutableStateFlow("No message")
    val message = _message.asStateFlow()

    init {
        Wearable.getMessageClient(context).addListener(this)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/last_hr_measurement") {
            val receivedMessage = String(messageEvent.data)
            (context as ComponentActivity).lifecycleScope.launch {
                _message.emit(receivedMessage)
                showNotification(receivedMessage)
                saveMeasurementToFirestore(receivedMessage)
            }
        }
    }

    fun onDestroy() {
        Wearable.getMessageClient(context).removeListener(this)
    }

    private fun showNotification(message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android O and above
        val channelId = "message_channel"
        val channel = NotificationChannel(channelId, "Message Notifications", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.message) // Replace with your message icon
            .setContentTitle("New HRV Measurement") // Set the title of the notification
            .setContentText(message) // Set the text of the notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the priority of the notification

        // Show the notification
        notificationManager.notify(1, notificationBuilder.build())

    }

    private fun saveMeasurementToFirestore(hr: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val bpm: Double
        try {
            bpm = hr.toDouble()
            Log.e("Firestore", bpm.toString())
        } catch (e: NumberFormatException) {
            Log.e("Firestore", "Error parsing HR value: $hr", e)
            return
        }

        // Estimate SDNN based on BPM
        val sdnnValue = estimateSDNN(bpm)

        // Get the current timestamp
        val timestamp = System.currentTimeMillis()

        // Format the timestamp
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedTimestamp = sdf.format(timestamp)

        val measurement = hashMapOf(
            "sdnn" to sdnnValue,
            "timestamp" to FieldValue.serverTimestamp()
        )

        // Use the formatted timestamp as the document ID
        val documentId = formattedTimestamp.replace(" ", "_").replace(":", "-").replace("/", "-")

        db.collection("users").document(userId).collection("measurements").document(documentId).set(measurement)
            .addOnSuccessListener {
                Log.d("Firestore", "Measurement successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing measurement", e)
            }
    }

    private fun estimateSDNN(bpm: Double): Double {
        // Dynamic model to estimate SDNN based on BPM
        // Example: SDNN decreases exponentially as BPM increases
        val maxSDNN = 100.0 // Maximum SDNN at very low BPM
        val minSDNN = 20.0 // Minimum SDNN at very high BPM
        val normalBPM = 70.0 // Typical BPM where SDNN is around middle value
        val scalingFactor = 0.1 // Scaling factor to adjust the rate of decrease

        val sdnn = maxSDNN * exp(-scalingFactor * (bpm - normalBPM)) + minSDNN
        return sdnn.coerceIn(minSDNN, maxSDNN)
    }
}
