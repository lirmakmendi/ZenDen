package com.sceproject.zenden.data.db.contacts

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

data class Contact(
    val id: String = "",  // Firebase ID
    val name: String = "",
    val phoneNumber: String = ""
)


fun saveContactToFirebase(userId: String, contact: Contact, onComplete: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val contactRef = db.collection("users").document(userId).collection("contacts")

    val newContact = contact.copy(id = contactRef.document().id)
    contactRef.document(newContact.id).set(newContact)
        .addOnSuccessListener {
            onComplete()
        }
        .addOnFailureListener { e ->
            Log.e("FirebaseError", "Error saving contact: $e")
        }
}


fun getContactsFromFirebase(userId: String, onResult: (List<Contact>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId).collection("contacts")
        .get()
        .addOnSuccessListener { snapshot ->
            val contacts = snapshot.documents.map { document ->
                document.toObject(Contact::class.java)!!
            }
            onResult(contacts)
        }
        .addOnFailureListener { e ->
            // Handle failure
        }
}

fun deleteContactFromFirebase(userId: String, contactId: String, onSuccess: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .document(userId)
        .collection("contacts")
        .document(contactId)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error deleting document", e)
        }
}

