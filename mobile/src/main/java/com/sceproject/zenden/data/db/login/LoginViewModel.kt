package com.sceproject.zenden.data.db.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sceproject.zenden.data.db.rules.Validator
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

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
                        loginInProgress.value = false
                        ZenDenAppRouter.navigateTo(Screen.HomeScreen)
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
}
