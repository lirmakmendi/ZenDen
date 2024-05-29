package com.sceproject.zenden.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.data.viewmodels.HomeViewModel

@Composable
fun ManageUsersContent(paddingValues: PaddingValues, viewModel: HomeViewModel) {
    val users by viewModel.users.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<HomeViewModel.User?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.fetchUsers()
    }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${user.firstName} ${user.lastName}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = user.email, style = MaterialTheme.typography.body2)
                        }
                        IconButton(onClick = {
                            selectedUser = user
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete User",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog && selectedUser != null) {
        DeleteConfirmationDialog(
            user = selectedUser!!,
            onConfirm = {
                viewModel.deleteUser(selectedUser!!.id)
                selectedUser = null
            },
            onDismiss = {
                showDialog = false
                selectedUser = null
            }
        )
    }
}


@Composable
fun DeleteConfirmationDialog(
    user: HomeViewModel.User,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "אישור מחיקה") },
        text = { Text(text = "האם אתה בטוח שאתה רוצה למחוק את ${user.firstName} ${user.lastName}?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("מחיקה")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("ביטול")
            }
        }
    )
}
