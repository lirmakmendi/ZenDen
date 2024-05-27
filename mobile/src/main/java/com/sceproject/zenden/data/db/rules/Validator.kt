package com.sceproject.zenden.data.db.rules

object Validator {

    fun validateFirstName(fName: String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >= 2)
        )
    }

    fun validateLastName(lName: String): ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >= 2)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        )
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 4)
        )
    }

    fun validateAge(age: String): ValidationResult {
        val ageInt = age.toIntOrNull()
        return ValidationResult(
            ageInt != null && ageInt in 0..120
        )
    }

    fun validateGender(gender: String): ValidationResult {
        val validGenders = listOf("זכר", "נקבה", "אחר")
        return ValidationResult(
            gender in validGenders
        )
    }

    fun validatePrivacyPolicyAcceptance(statusValue: Boolean): ValidationResult {
        return ValidationResult(
            statusValue
        )
    }

}

data class ValidationResult(
    val status: Boolean = false
)
