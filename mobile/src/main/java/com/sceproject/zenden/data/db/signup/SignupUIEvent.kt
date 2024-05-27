package com.sceproject.zenden.data.db.signup

sealed class SignupUIEvent {
    data class FirstNameChanged(val firstName: String) : SignupUIEvent()
    data class LastNameChanged(val lastName: String) : SignupUIEvent()
    data class EmailChanged(val email: String) : SignupUIEvent()
    data class PasswordChanged(val password: String) : SignupUIEvent()
    data class AgeChanged(val age: String) : SignupUIEvent()
    data class GenderChanged(val gender: String) : SignupUIEvent()
    data class PrivacyPolicyCheckBoxClicked(val status: Boolean) : SignupUIEvent()
    object RegisterButtonClicked : SignupUIEvent()
}
