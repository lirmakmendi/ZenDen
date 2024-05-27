package com.sceproject.zenden.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.ButtonComponent
import com.sceproject.zenden.components.CheckboxComponent
import com.sceproject.zenden.components.ClickableLoginTextComponent
import com.sceproject.zenden.components.DividerTextComponent
import com.sceproject.zenden.components.HeadingTextComponent
import com.sceproject.zenden.components.MyTextFieldComponent
import com.sceproject.zenden.components.NormalTextComponent
import com.sceproject.zenden.components.PasswordTextFieldComponent
import com.sceproject.zenden.data.db.signup.SignupUIEvent
import com.sceproject.zenden.data.db.signup.SignupViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun SignUpScreen(signupViewModel: SignupViewModel = viewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                NormalTextComponent(value = stringResource(id = R.string.ZenDen))
                HeadingTextComponent(value = stringResource(id = R.string.create_account))
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id = R.drawable.profile),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource = painterResource(id = R.drawable.profile),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.lastNameError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.emailError
                )

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource = painterResource(id = R.drawable.ic_lock),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.passwordError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.age),
                    painterResource = painterResource(id = R.drawable.ic_age),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.AgeChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.ageError
                )

                GenderDropdown(
                    onGenderSelected = {
                        signupViewModel.onEvent(SignupUIEvent.GenderChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.genderError
                )

                CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                    onTextSelected = {
                        ZenDenAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
                    },
                    onCheckedChange = {
                        signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it))
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = stringResource(id = R.string.register),
                    onButtonClicked = {
                        signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                    ZenDenAppRouter.navigateTo(Screen.LoginScreen)
                })
            }
        }

        if (signupViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun GenderDropdown(onGenderSelected: (String) -> Unit, errorStatus: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    val genders = listOf("זכר", "נקבה", "אחר")

    Box {
        TextField(
            value = selectedGender,
            onValueChange = {},
            label = { Text("מגדר") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = true })
            },
            isError = errorStatus,
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(onClick = {
                    selectedGender = gender
                    expanded = false
                    onGenderSelected(gender)
                }) {
                    Text(text = gender)
                }
            }
        }
    }
}
