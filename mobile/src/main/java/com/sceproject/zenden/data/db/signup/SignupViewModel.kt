package com.sceproject.zenden.data.db.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.sceproject.zenden.data.db.rules.Validator
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import com.google.firebase.firestore.FirebaseFirestore

class SignupViewModel : ViewModel() {

    private val TAG = SignupViewModel::class.simpleName

    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    fun onEvent(event: SignupUIEvent) {
        when (event) {
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(firstName = event.firstName)
                printState()
            }

            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(lastName = event.lastName)
                printState()
            }

            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(email = event.email)
                printState()
            }

            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(password = event.password)
                printState()
            }

            is SignupUIEvent.AgeChanged -> {
                registrationUIState.value = registrationUIState.value.copy(age = event.age)
                printState()
            }

            is SignupUIEvent.GenderChanged -> {
                registrationUIState.value = registrationUIState.value.copy(gender = event.gender)
                printState()
            }

            is SignupUIEvent.PrivacyPolicyCheckBoxClicked -> {
                registrationUIState.value = registrationUIState.value.copy(privacyPolicyAccepted = event.status)
            }

            is SignupUIEvent.RegisterButtonClicked -> {
                signUp()
            }
        }
        validateDataWithRules()
    }

    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(fName = registrationUIState.value.firstName)
        val lNameResult = Validator.validateLastName(lName = registrationUIState.value.lastName)
        val emailResult = Validator.validateEmail(email = registrationUIState.value.email)
        val passwordResult = Validator.validatePassword(password = registrationUIState.value.password)
        val ageResult = Validator.validateAge(age = registrationUIState.value.age)
        val genderResult = Validator.validateGender(gender = registrationUIState.value.gender)
        val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(statusValue = registrationUIState.value.privacyPolicyAccepted)

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")
        Log.d(TAG, "ageResult= $ageResult")
        Log.d(TAG, "genderResult= $genderResult")
        Log.d(TAG, "privacyPolicyResult= $privacyPolicyResult")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            ageError = ageResult.status,
            genderError = genderResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )

        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status &&
                ageResult.status && genderResult.status &&
                privacyPolicyResult.status
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.value.toString())
    }

    private fun createUserInFirebase(email: String, password: String) {
        signUpInProgress.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                signUpInProgress.value = false
                if (task.isSuccessful) {
                    Log.d(TAG, "User creation successful")
                    val user = task.result?.user
                    user?.let {
                        sendVerificationEmail(it)
                        saveUserToFirestore(
                            userId = it.uid,
                            firstName = registrationUIState.value.firstName,
                            lastName = registrationUIState.value.lastName,
                            email = email,
                            age = registrationUIState.value.age,
                            gender = registrationUIState.value.gender
                        )
                    }
                } else {
                    Log.e(TAG, "User creation failed: ${task.exception?.message}")
                }
            }
            .addOnFailureListener { exception ->
                signUpInProgress.value = false
                Log.e(TAG, "User creation error: ${exception.message}")
            }
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Verification email sent to ${user.email}")
                } else {
                    Log.e(TAG, "Failed to send verification email: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserToFirestore(userId: String, firstName: String, lastName: String, email: String, age: String, gender: String) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "age" to age,
            "gender" to gender
        )

        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User successfully written to Firestore")
                createInitialMeasurementDocument(userId)
                ZenDenAppRouter.navigateTo(Screen.LoginScreen)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error writing document to Firestore: ${e.message}")
            }
    }

    private fun createInitialMeasurementDocument(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val measurement = hashMapOf(
            "hr" to 0,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("users").document(userId).collection("measurements").document("measurement").set(measurement)
            .addOnSuccessListener {
                Log.d(TAG, "Initial measurement document successfully written")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error writing measurement document: ${e.message}")
            }
    }
}
