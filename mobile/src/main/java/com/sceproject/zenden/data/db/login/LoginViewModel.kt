package com.sceproject.zenden.data.db.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sceproject.zenden.data.db.rules.Validator
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName
    private val adminEmail = "mendili@ac.sce.ac.il" // Define the admin email

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var errorMessage = mutableStateOf<String?>(null)

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status
    }

    private fun login() {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null && user.isEmailVerified) {
                        checkUserInFirestore(user.uid)
                    } else {
                        loginInProgress.value = false
                        FirebaseAuth.getInstance().signOut()
                        errorMessage.value = "יש לאמת את האימייל לפני התחברות ראשונה"
                        Log.d(TAG, "Email not verified")
                    }
                } else {
                    loginInProgress.value = false
                    errorMessage.value = "אימייל או סיסמה לא נכונים"
                    Log.d(TAG, "Login failed: ${task.exception?.message}")
                }
            }
            .addOnFailureListener {
                loginInProgress.value = false
                errorMessage.value = "אימייל או סיסמה לא נכונים"
                Log.d(TAG, "Login error: ${it.localizedMessage}")
            }
    }

    private fun checkUserInFirestore(uid: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                loginInProgress.value = false
                if (document.exists()) {
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    if (email == adminEmail) {
                        ZenDenAppRouter.navigateTo(Screen.AdminScreen)
                    } else {
                        ZenDenAppRouter.navigateTo(Screen.HomeScreen)
                    }
                } else {
                    FirebaseAuth.getInstance().signOut()
                    errorMessage.value = "אימייל או סיסמה לא נכונים"
                    Log.d(TAG, "User UID not found in Firestore")
                }
            }
            .addOnFailureListener { exception ->
                loginInProgress.value = false
                FirebaseAuth.getInstance().signOut()
                errorMessage.value = "שגיאה בבדיקת המשתמש במערכת: ${exception.localizedMessage}"
                Log.d(TAG, "Error checking user in Firestore: ", exception)
            }
    }


}
