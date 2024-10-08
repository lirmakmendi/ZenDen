package com.sceproject.zenden.data.db.signup

data class RegistrationUIState(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var age: String = "",
    var gender: String = "",
    var privacyPolicyAccepted: Boolean = false,

    var firstNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var ageError: Boolean = false,
    var genderError: Boolean = false,
    var privacyPolicyError: Boolean = false
)
