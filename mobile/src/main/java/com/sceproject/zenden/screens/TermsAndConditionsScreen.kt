package com.sceproject.zenden.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.R
import com.sceproject.zenden.components.HeadingTextComponent
import com.sceproject.zenden.components.TermsContent
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.SystemBackButtonHandler
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun TermsAndConditionsScreen() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
        .padding(16.dp)) {

        Column {
            HeadingTextComponent(value = stringResource(id = R.string.terms_and_conditions_header))
            Spacer(modifier = Modifier.height(16.dp))
            TermsContent()
        }
    }

    SystemBackButtonHandler {
        ZenDenAppRouter.navigateTo(Screen.SignUpScreen)
    }
}