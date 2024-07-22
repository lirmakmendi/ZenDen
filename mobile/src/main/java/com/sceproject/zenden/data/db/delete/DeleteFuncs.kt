import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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

