package com.sceproject.zenden.data.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter



class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.simpleName

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val emailId: MutableLiveData<String> = MutableLiveData()

    val firstName: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val age: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<String> = MutableLiveData()

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
                        resetPasswordStatus.value = "Error in sending password reset email: ${task.exception?.message}"
                    }
                }
        }
    }
}
