package com.sceproject.zenden.data.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    data class User(val id: String, val firstName: String, val lastName: String, val email: String)

    private val TAG = HomeViewModel::class.simpleName
    private val adminEmail = "mendili@ac.sce.ac.il" // Define the admin email

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isAdmin: MutableLiveData<Boolean> =
        MutableLiveData(false) // New MutableLiveData for admin check
    val emailId: MutableLiveData<String> = MutableLiveData()

    val firstName: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val age: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<String> = MutableLiveData()
    val isDatabaseAlive: MutableLiveData<Boolean> = MutableLiveData()
    val users: MutableLiveData<List<User>> = MutableLiveData()
    val resetPasswordStatus: MutableLiveData<String> = MutableLiveData()

    fun logout() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        firebaseAuth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                Log.d(TAG, "Inside sign out success")
                ZenDenAppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }
    }

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
            getUserData()
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            emailId.value = user.email
            val userId = user.uid

            // Check if the user is an admin based on their email
            if (user.email == adminEmail) {
                isAdmin.value = true
            } else {
                isAdmin.value = false
            }

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        firstName.value = document.getString("firstName")
                        lastName.value = document.getString("lastName")
                        age.value = document.getString("age")
                        gender.value = document.getString("gender")
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    fun sendPasswordResetEmail() {
        resetPasswordStatus.value = ""
        FirebaseAuth.getInstance().currentUser?.email?.let { email ->
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Password reset email sent.")
                        resetPasswordStatus.value = "קישור לאיפוס סיסמה נשלח לאימייל"
                    } else {
                        Log.e(TAG, "שגיאה בשליחת קישור לאימייל")
                        resetPasswordStatus.value =
                            "Error in sending password reset email: ${task.exception?.message}"
                    }
                }
        }
    }

    fun checkDatabaseStatus() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            viewModelScope.launch {
                delay(3000) // Simulate a 3-second delay
                val db = FirebaseFirestore.getInstance()
                db.collection("test").document("status").get()
                    .addOnSuccessListener { document ->
                        isDatabaseAlive.value = true
                        Log.d(TAG, "Database is alive")
                    }
                    .addOnFailureListener { exception ->
                        isDatabaseAlive.value = false
                        Log.d(TAG, "Database is not reachable", exception)
                    }
            }
        } else {
            Log.d(TAG, "User is not authenticated")
            isDatabaseAlive.value = false
        }
    }


    fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").get()
            .addOnSuccessListener { result ->
                val userList = result.map { document ->
                    User(
                        id = document.id,
                        firstName = document.getString("firstName") ?: "",
                        lastName = document.getString("lastName") ?: "",
                        email = document.getString("email") ?: ""
                    )
                }
                users.value = userList
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting users: ", exception)
            }
    }


    fun deleteUser(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "User successfully deleted!")
                fetchUsers() // Refresh the user list after deletion
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error deleting user: ", exception)
            }
    }


}
