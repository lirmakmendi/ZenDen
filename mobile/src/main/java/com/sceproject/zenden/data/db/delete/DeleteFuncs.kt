import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import kotlinx.coroutines.tasks.await





suspend fun deleteAllDocumentsInCollection(collectionRef: CollectionReference): Boolean {
    return try {
        val querySnapshot = collectionRef.get().await()
        if (querySnapshot.isEmpty) {
            println("No documents to delete in collection ${collectionRef.path}.")
            return false // No documents to delete
        }
        for (document in querySnapshot.documents) {
            document.reference.delete().await()
        }
        true
    } catch (e: Exception) {
        println("Failed to delete documents in collection ${collectionRef.path}: ${e.message}")
        e.printStackTrace()
        false
    }
}

suspend fun deleteUserAndSubcollections(userId: String) {
    val TAG = "testing"
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("users").document(userId)

    // Define known subcollections
    val subCollections = listOf("measurements", "pdss_measurements", "predictions")

    try {
        var anySubcollectionsDeleted = false

        for (subCollection in subCollections) {
            val collectionRef = userDocRef.collection(subCollection)
            val deleted = deleteAllDocumentsInCollection(collectionRef)
            if (deleted) {
                anySubcollectionsDeleted = true
                Log.d(TAG, "Successfully deleted documents in subcollection $subCollection.")
            } else {
                Log.d(TAG, "Subcollection $subCollection might not exist or is empty.")
            }
        }

        // Finally, delete the user document if any subcollections were deleted
        if (anySubcollectionsDeleted) {
            userDocRef.delete().await()
            Log.d(TAG, "User and all subcollections successfully deleted.")
        } else {
            Log.d(TAG, "No documents were deleted from subcollections. User document not deleted.")
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error deleting user and subcollections: ${e.message}", e)
    }
}

suspend fun deleteUserDataAndInitData(userId: String) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val user = firebaseAuth.currentUser ?: run {
        Log.e("testing", "No authenticated user found.")
        return
    }

    Log.d("testing", "user id: $userId")

    try {
        val document = db.collection("users").document(userId).get().await()

        if (document.exists()) {
            val userHashMap = hashMapOf(
                "firstName" to (document.getString("firstName") ?: ""),
                "lastName" to (document.getString("lastName") ?: ""),
                "email" to (document.getString("email") ?: ""),
                "age" to (document.getString("age") ?: ""),
                "gender" to (document.getString("gender") ?: "")
            )

            db.collection("users").document(userId).set(userHashMap).await()
            Log.d("testing", "User successfully written to Firestore")

            createInitialMeasurementDocument(userId)
            // ZenDenAppRouter.navigateTo(Screen.LoginScreen)
        } else {
            Log.d("testing", "No such document")
        }
    } catch (e: Exception) {
        Log.e("testing", "Error fetching or writing document: ${e.message}", e)
    }
}
suspend fun deleteUserFromEverything(userId: String) {
    val TAG = "testing"
    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser ?: run {
        Log.e(TAG, "No authenticated user found.")
        return
    }

    try {
        // Delete user from Firestore first
        deleteUserAndSubcollections(userId)

        // Delete user from Firebase Authentication
        user.delete().await()
        Log.d(TAG, "User successfully deleted from Firebase Authentication.")
    } catch (e: Exception) {
        Log.e(TAG, "Error deleting user: ${e.message}", e)
        // Handle error, possibly restore user data if needed
    }
}


//i know  its not SOLID
fun createInitialMeasurementDocument(userId: String) {
    val db = FirebaseFirestore.getInstance()
    val measurement = hashMapOf(
        "hr" to 0,
        "timestamp" to FieldValue.serverTimestamp()
    )

    db.collection("users").document(userId).collection("measurements").document("measurement").set(measurement)
        .addOnSuccessListener {
            Log.d("testing", "Initial measurement document successfully written")
        }
        .addOnFailureListener { e ->
            Log.e("testing", "Error writing measurement document: ${e.message}")
        }
}
