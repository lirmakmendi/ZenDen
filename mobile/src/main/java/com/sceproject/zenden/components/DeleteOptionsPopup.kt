package com.sceproject.zenden.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun DeleteOptionsPopup(
    onDismiss: () -> Unit,
    onConfirmDeleteUser: (String) -> Unit,
    onConfirmDeleteData: (String) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var confirmDialogMessage by remember { mutableStateOf("") }
    var confirmDialogAction by remember { mutableStateOf<() -> Unit>({}) }

    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(focusable = true),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = {
                confirmDialogMessage = "האם אתה בטוח שברצונך למחוק את המשתמש וכל המידע?"
                confirmDialogAction = {
                    onConfirmDeleteUser("Delete User")
                }
                showConfirmDialog = true
            }) {
                Text("מחק משתמש", fontSize = 16.sp, color = Color.Red)
            }
            TextButton(onClick = {
                confirmDialogMessage = "האם אתה בטוח שברצונך למחוק את כל המידע?"
                confirmDialogAction = {
                    onConfirmDeleteData("Delete Data")
                }
                showConfirmDialog = true
            }) {
                Text("מחק מידע", fontSize = 16.sp, color = Color.Red)
            }
            TextButton(onClick = onDismiss) {
                Text("ביטול")
            }

            if (showConfirmDialog) {
                ConfirmDialog(
                    title = "שים לב!",
                    message = confirmDialogMessage,
                    confirmButtonText = "אני מאשר",
                    onConfirm = {
                        confirmDialogAction()
                        showConfirmDialog = false
                    },
                    onDismiss = {
                        showConfirmDialog = false
                    }
                )
            }
        }
    }
}
