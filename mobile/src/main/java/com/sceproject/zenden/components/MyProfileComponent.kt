package com.sceproject.zenden.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.sceproject.zenden.R
import com.sceproject.zenden.data.viewmodels.HomeViewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@Composable
fun MyProfileContent(paddingValues: PaddingValues, homeViewModel: HomeViewModel) {
    val email by homeViewModel.emailId.observeAsState("")
    val firstName by homeViewModel.firstName.observeAsState("")
    val lastName by homeViewModel.lastName.observeAsState("")
    val age by homeViewModel.age.observeAsState("")
    val gender by homeViewModel.gender.observeAsState("")
    val isLoggedIn by homeViewModel.isUserLoggedIn.observeAsState(false)
    val resetPasswordStatus by homeViewModel.resetPasswordStatus.observeAsState("")


    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val showPopup = remember { mutableStateOf(false) }

    if (isLoggedIn) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    "פרופיל המשתמש שלי:",
                    style = MaterialTheme.typography.h6
                )
            }
            item {
                ProfileCard(
                    title = "שם פרטי",
                    value = firstName
                )
            }
            item {
                ProfileCard(
                    title = "שם משפחה",
                    value = lastName
                )
            }
            item {
                ProfileCard(
                    title = "גיל",
                    value = age
                )
            }
            item {
                ProfileCard(
                    title = "מין",
                    value = gender
                )
            }
            item {
                ProfileCard(
                    title = "דוא\"ל",
                    value = email
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            homeViewModel.sendPasswordResetEmail()
                            Toast.makeText(
                                context,
                                R.string.toast_password_reset,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                    ) {
                        Text("אפס סיסמה")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = {
                            showPopup.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("מחק משתמש", color = Color.White)
                    }
                }
            }
            item {
                if (resetPasswordStatus.isNotEmpty()) {
                    Text(
                        text = resetPasswordStatus,
                        style = MaterialTheme.typography.body2,
                        color = if (resetPasswordStatus.contains("Error")) Color.Red else Color.Black
                    )
                }
            }
        }
    } else {
        Text("משתמש לא מחובר", style = MaterialTheme.typography.h6)
    }

    if (showPopup.value) {
        DeleteOptionsPopup(
            onDismiss = {
                showPopup.value = false
            },
            onConfirmDeleteUser = { _ ->
                // Implement your deletion logic here
                homeViewModel.viewModelScope.launch {
                    try {
                        Log.d("Here", "here in myProfile")
                        homeViewModel.deleteUserTest() { success ->
                            if (success) {
                                Toast.makeText(context, "User data deleted", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to delete user data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MyProfileContent", "Error deleting user data", e)
                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            },

            onConfirmDeleteData = { _ ->
                // Implement your deletion logic here
                homeViewModel.viewModelScope.launch {
                    try {
                        Log.d("Here", "here in myProfile")
                        homeViewModel.deleteUserDataTest() { success ->
                            if (success) {
                                Toast.makeText(context, "User data deleted", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to delete user data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MyProfileContent", "Error deleting user data", e)
                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                showPopup.value = false
            }
        )
    }


}

@Composable
fun ProfileCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start
            )
        }
    }
}
